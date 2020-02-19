package upp.project.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class PublicationOrder extends UserOrder {
	
	@ManyToMany
	private Set<Publication> issue;

	public PublicationOrder() {
		
	}
	
	public PublicationOrder(Set<Publication> issue) {
		super();
		this.issue = issue;
	}
	
	public Set<Publication> getIssue() {
		return issue;
	}

	public void setIssue(Set<Publication> issue) {
		this.issue = issue;
	}
}
