/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.viglet.shiohara.provider.storage;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.filter.PathFilter;

/**
 * @author Alexandre Oliveira
 * @since 0.3.6
 */
public class ShGitProvider {
	static final Logger logger = LogManager.getLogger(ShGitProvider.class.getName());
	
	String rootShObjectFolder = "/root";

	private Git git;

	public ShGitProvider() {
		super();
	}

	public void init() throws IOException {

		logger.info("Opening a git repo at '{}'", this.rootShObjectFolder);
		Repository localRepo = new FileRepository(Joiner.on(File.separator).join(this.rootShObjectFolder, ".git"));
		if (!localRepo.getDirectory().exists()) {
			logger.info("Git repo {} does not exist, creating a new one", localRepo.getDirectory());
			localRepo.create();
		}
		git = new Git(localRepo);
	}

	public void move(String shObjectId, String shObjectPath, String newShObjectPath)
			throws IOException {
		String shObjectFileName = "<<DEFINE>";
		String newShObjectFileName = "<<DEFINE>";
		git.rm().addFilepattern(shObjectFileName);
		git.add().addFilepattern(newShObjectFileName);
		try {
			git.commit().setMessage("Move shObject " + shObjectId + " from " + shObjectFileName + " to " + newShObjectFileName).call();
		} catch (GitAPIException e) {
			throw new IOException(e);
		}
	}

	public void move(String folderPath, String newFolderPath) throws IOException {
		git.rm().addFilepattern(folderPath.substring(1));
		git.add().addFilepattern(newFolderPath.substring(1));
		try {
			git.commit().setMessage("Move folder " + folderPath + " to " + newFolderPath).call();
		} catch (GitAPIException e) {
			throw new IOException(e);
		}
	}

	@SuppressWarnings("unused")
	public String checkpoint(String shObjectId, String shObjectPath, String commitMessage)
			throws IOException {
		String shObjectFileName = "<<DEFINE>>";
		try {
			List<DiffEntry> gitDiff = git.diff().call();
			boolean modified = gitDiff.parallelStream()
					.anyMatch(diffEntry -> diffEntry.getNewPath().equals(shObjectFileName));
			if (modified) {
				logger.debug("Changes found for pattern '{}': {}", shObjectFileName, gitDiff);
				DirCache added = git.add().addFilepattern(shObjectFileName).call();
				logger.debug("{} changes are about to be commited", added.getEntryCount());
				RevCommit commit = git.commit().setMessage(commitMessage).call();
			} else {
				logger.debug("No changes found {}", shObjectFileName);
			}
		} catch (GitAPIException e) {
			logger.error("Failed to add+commit {} to Git", shObjectFileName, e);
		}
		return null;
	}

	public synchronized String get(String shObjectId, String shObjectPath, String revId)
			throws IOException {
		RevCommit stash = null;
		String shObjectFileName = "<<DEFINE>>";
		try {
			List<DiffEntry> gitDiff = git.diff().setPathFilter(PathFilter.create(shObjectFileName)).call();
			boolean modified = !gitDiff.isEmpty();
			if (modified) {			
				stash = git.stashCreate().call();
				Collection<RevCommit> stashes = git.stashList().call();
				logger.debug("Created stash : {}, stash size : {}", stash, stashes.size());
			}
			ObjectId head = git.getRepository().resolve(Constants.HEAD);
			git.checkout().setStartPoint(revId).addPath(shObjectFileName).call();
			git.checkout().setStartPoint(head.getName()).addPath(shObjectFileName).call();
			if (modified && stash != null) {
				ObjectId applied = git.stashApply().setStashRef(stash.getName()).call();
				ObjectId dropped = git.stashDrop().setStashRef(0).call();
				Collection<RevCommit> stashes = git.stashList().call();
				logger.debug("Stash applied as : {}, and dropped : {}, stash size: {}", applied, dropped,
						stashes.size());
			}
		} catch (GitAPIException e) {
			logger.error("Failed to return shObject from revision \"{}\"", revId, e);
		}
		return null;
	}

	public List<String> revisionHistory(String shObjectId, String shObjectPath)
			throws IOException {
		List<String> history = Lists.newArrayList();
		String shObjectFileName = "<<DEFINE>>";
		logger.debug("Listing history for {}:", shObjectFileName);
		try {
			Iterable<RevCommit> logs = git.log().addPath(shObjectFileName).call();
			for (RevCommit log : logs) {
				history.add("History Item");
				logger.debug(" - ({},{},{})", log.getName(), log.getCommitTime(), log.getFullMessage());
			}
		} catch (NoHeadException e) {
			logger.warn("No Head found for {}, {}", shObjectFileName, e.getMessage());
		} catch (GitAPIException e) {
			logger.error("Failed to get logs for {}", shObjectFileName, e);
		}
		return history;
	}

	public String setShObjectRevision(String shObjectId, String shObjectPath, String revId)
			throws IOException {
		String revisionShObject = get(shObjectId, shObjectPath, revId);
		if (revisionShObject != null) {
			//Save
		}
		return revisionShObject;
	}

	public void close() {
		git.getRepository().close();
	}

	protected Git getGit() {
		return git;
	}

	void setGit(Git git) {
		this.git = git;
	}

}
