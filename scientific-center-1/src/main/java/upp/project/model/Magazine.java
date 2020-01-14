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
	private String name;

	@Column
	private String ISBN;
	
	@Column
	private String whoPays;
	
	@Column
	boolean actevated;
	
	@ManyToMany
	Set<ScientificArea> scientificArea;
	
	@ManyToOne
	RegistredUser mainReviewer;
	
	@ManyToMany
	Set<RegistredUser>reviewers;
	
	@ManyToMany
	Set<RegistredUser>editors;

	public Magazine() {
		actevated=false;
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

	public boolean isActevated() {
		return actevated;
	}

	public void setActevated(boolean actevated) {
		this.actevated = actevated;
	}

	public Set<ScientificArea> getScientificArea() {
		return scientificArea;
	}

	public void setScientificArea(Set<ScientificArea> scientificArea) {
		this.scientificArea = scientificArea;
	}

	public RegistredUser getMainReviewer() {
		return mainReviewer;
	}

	public void setMainReviewer(RegistredUser mainReviewer) {
		this.mainReviewer = mainReviewer;
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
	
}