package upp.project.model;

import java.util.List;

public class UserPurchasedItemsDTO {

	private List<Magazine> magazines;

	private List<Publication> issues;

	private List<Article> scientificPapers;
	
	private List<UserSubscription> subscriptions;
	
	public UserPurchasedItemsDTO() {
		
	}

	public UserPurchasedItemsDTO(List<Magazine> magazines, List<Publication> issues, List<Article> scientificPapers,
			List<UserSubscription> subscriptions) {
		super();
		this.magazines = magazines;
		this.issues = issues;
		this.scientificPapers = scientificPapers;
		this.subscriptions = subscriptions;
	}

	public List<Magazine> getMagazines() {
		return magazines;
	}

	public void setMagazines(List<Magazine> magazines) {
		this.magazines = magazines;
	}

	public List<Publication> getIssues() {
		return issues;
	}

	public void setIssues(List<Publication> issues) {
		this.issues = issues;
	}

	public List<Article> getScientificPapers() {
		return scientificPapers;
	}

	public void setScientificPapers(List<Article> scientificPapers) {
		this.scientificPapers = scientificPapers;
	}

	public List<UserSubscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<UserSubscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
}
