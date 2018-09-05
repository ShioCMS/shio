package com.viglet.shiohara.mongodb;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.UserTempNaming;

public class MongoInMemory {
	private int port;
	private String host;
	private MongodProcess process = null;

	public MongoInMemory(int port, String host) {
		this.port = port;
		this.host = host;
	}

	@PostConstruct
	public void init() throws IOException {
		Storage storage = new Storage(System.getProperty("user.home") + "/.ttraining-storage", null, 0);

		IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder().defaults(Command.MongoD)
				.artifactStore(new ExtractedArtifactStoreBuilder().defaults(Command.MongoD)
						.download(new DownloadConfigBuilder().defaultsForCommand(Command.MongoD).build())
						.executableNaming(new UserTempNaming()))
				.build();

		IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
				.net(new Net(host, port, false)).replication(storage).build();

		MongodStarter runtime = MongodStarter.getInstance(runtimeConfig);
		process = runtime.prepare(mongodConfig).start();
	}

	@PreDestroy
	public void stop() {
		process.stop();
	}
}