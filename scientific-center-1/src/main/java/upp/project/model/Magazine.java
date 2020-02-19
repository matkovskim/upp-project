package upp.project.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Magazine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String email;
	
	@Column
	private String name;

	@Column
	private String ISBN;
	
	@Column
	private String whoPays;
	
	@Column
	boolean activated;
	
	@Column
	boolean registeredOnPaymentHub;
	
	@Column
	Double subscriptionPrice;
	
	@Column
	Long ArticlePrice;
	
	@Column
	Long PublicationPrice;
	
	@ManyToMany
	private Set<ScientificArea> scientificArea;
	
	@ManyToOne
	private RegistredUser mainEditor;
	
	@ManyToMany
	private Set<RegistredUser>reviewers;
	
	@ManyToMany
	private Set<RegistredUser>editors;

	public Magazine() {
		activated=false;
		registeredOnPaymentHub=false;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getWhoPays() {
		return whoPays;
	}

	public void setWhoPays(String whoPays) {
		this.whoPays = whoPays;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean actevated) {
		this.activated = actevated;
	}

	public Set<ScientificArea> getScientificArea() {
		return scientificArea;
	}

	public void setScientificArea(Set<ScientificArea> scientificArea) {
		this.scientificArea = scientificArea;
	}

	public RegistredUser getMainEditor() {
		return mainEditor;
	}

	public void setMainEditor(RegistredUser mainReviewer) {
		this.mainEditor = mainReviewer;
	}

	public Set<RegistredUser> getReviewers() {
		return reviewers;
	}

	public void setReviewers(Set<RegistredUser> reviewers) {
		this.reviewers = reviewers;
	}

	public Set<RegistredUser> getEditors() {
		return editors;
	}

	public void setEditors(Set<RegistredUser> editors) {
		this.editors = editors;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isRegisteredOnPaymentHub() {
		return registeredOnPaymentHub;
	}

	public void setRegisteredOnPaymentHub(boolean registeredOnPaymentHub) {
		this.registeredOnPaymentHub = registeredOnPaymentHub;
	}

	public Double getSubscriptionPrice() {
		return subscriptionPrice;
	}

	public void setSubscriptionPrice(Double subscriptionPrice) {
		this.subscriptionPrice = subscriptionPrice;
	}

	public Long getArticlePrice() {
		return ArticlePrice;
	}

	public void setArticlePrice(Long articlePrice) {
		ArticlePrice = articlePrice;
	}

	public Long getPublicationPrice() {
		return PublicationPrice;
	}

	public void setPublicationPrice(Long publicationPrice) {
		PublicationPrice = publicationPrice;
	}
	
}