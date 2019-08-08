package com.viglet.shiohara.workflow;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.auth.ShGroup;
import com.viglet.shiohara.persistence.model.auth.ShUser;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.workflow.ShWorkflowTask;
import com.viglet.shiohara.persistence.repository.auth.ShGroupRepository;
import com.viglet.shiohara.persistence.repository.auth.ShUserRepository;
import com.viglet.shiohara.persistence.repository.workflow.ShWorkflowTaskRepository;

@Component
public class ShWorkflow {
	private static final Log logger = LogFactory.getLog(ShWorkflow.class);

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private ShWorkflowTaskRepository shWorkflowTaskRepository;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShGroupRepository shGroupRepository;


	public void requestWorkFlow(ShObject shObject, Principal principal) {
		System.out.println("requestWorkFlow");
		if (shObject != null && shObject instanceof ShPost) {
			if (shWorkflowTaskRepository.countByShObject(shObject) == 0) {
				ShWorkflowTask shWorkflowTask = new ShWorkflowTask();
				shWorkflowTask.setDate(new Date());
				shWorkflowTask.setTitle("Request to Publish");
				shWorkflowTask.setShObject(shObject);
				shWorkflowTask.setRequester(principal.getName());
				ShPost shPost = (ShPost) shObject;
				shWorkflowTask.setRequested(shPost.getShPostType().getWorkflowPublishEntity());

				shWorkflowTaskRepository.save(shWorkflowTask);

				this.sendWorkflowEmail(shWorkflowTask);
			}

		}

	}
	public String sendWorkflowEmail(ShWorkflowTask shWorkflowTask) {
		System.out.println("sendWorkflowEmail");
		try {

			String title = "";
			if (shWorkflowTask.getShObject() != null && shWorkflowTask.getShObject() instanceof ShPost) {
				ShPost shPost = (ShPost) shWorkflowTask.getShObject();
				title = shPost.getTitle();
			} else if (shWorkflowTask.getShObject() != null && shWorkflowTask.getShObject() instanceof ShFolder) {
				ShFolder shFolder = (ShFolder) shWorkflowTask.getShObject();
				title = shFolder.getName();
			}

			List<ShGroup> shGroups = new ArrayList<ShGroup>();
			System.out.println("Requested: " + shWorkflowTask.getRequested());
			ShGroup shGroup = shGroupRepository.findByName(shWorkflowTask.getRequested());
			shGroups.add(shGroup);
			Set<ShUser> shUsers = shUserRepository.findByShGroupsIn(shGroups);

			for (ShUser shUser : shUsers) {
				System.out.println("shUser.getEmail(): " + shUser.getEmail());
				SimpleMailMessage msg = new SimpleMailMessage();
				msg.setTo(shUser.getEmail());

				msg.setSubject("New publish request");
				msg.setText(String.format("Hi %s, \n There a new publish request for content: %s",
						shUser.getFirstName(), title));

				javaMailSender.send(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isDebugEnabled()) {
				logger.debug("Test Connection Email failed");
			}
		}

		return null;

	}
}
