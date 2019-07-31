package com.viglet.shiohara.workflow;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.workflow.ShWorkflowTask;
import com.viglet.shiohara.persistence.repository.workflow.ShWorkflowTaskRepository;

@Component
public class ShWorkflow {

	@Autowired
	private ShWorkflowTaskRepository shWorkflowTaskRepository;
	
	public void requestWorkFlow (ShObject shObject, Principal principal) {
		
		if (shObject != null) {
			ShWorkflowTask shWorkflowTask = new ShWorkflowTask();
			shWorkflowTask.setDate(new Date());
			shWorkflowTask.setTitle("Content Publishing");
			shWorkflowTask.setShObject(shObject);
			shWorkflowTask.setRequested(principal.getName());
			shWorkflowTask.setRequester(principal.getName());			
			shWorkflowTaskRepository.save(shWorkflowTask);
			
		}
		
	}
}
