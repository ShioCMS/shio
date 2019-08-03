package com.viglet.shiohara.workflow;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.workflow.ShWorkflowTask;
import com.viglet.shiohara.persistence.repository.workflow.ShWorkflowTaskRepository;

@Component
public class ShWorkflow {

	@Autowired
	private ShWorkflowTaskRepository shWorkflowTaskRepository;

	public void requestWorkFlow(ShObject shObject, Principal principal) {

		if (shObject != null && shObject instanceof ShPost) {
			ShWorkflowTask shWorkflowTask = new ShWorkflowTask();
			shWorkflowTask.setDate(new Date());
			shWorkflowTask.setTitle("Content Publishing");
			shWorkflowTask.setShObject(shObject);
			shWorkflowTask.setRequester(principal.getName());
			ShPost shPost = (ShPost) shObject;
			shWorkflowTask.setRequested(shPost.getShPostType().getWorkflowPublishEntity());
			
			shWorkflowTaskRepository.save(shWorkflowTask);

		}

	}
}
