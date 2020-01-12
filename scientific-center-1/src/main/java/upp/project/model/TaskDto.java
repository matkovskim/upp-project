package upp.project.model;

import java.util.List;

import org.camunda.bpm.engine.form.FormField;

public class TaskDto {
	
	String taskId;
	String name;
	String assignee;
	List<FormField> formFields;

	public TaskDto() {
		super();
	}
	
	public TaskDto(String taskId, String name, String assignee, List<FormField> formFields) {
		super();
		this.taskId = taskId;
		this.name = name;
		this.assignee = assignee;
		this.formFields = formFields;
	}

	public TaskDto(String taskId, String name, String assignee) {
		super();
		this.taskId = taskId;
		this.name = name;
		this.assignee = assignee;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public List<FormField> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}

}
