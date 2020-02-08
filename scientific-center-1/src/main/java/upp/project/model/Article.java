package upp.project.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String title;
	
	@ManyToOne
	private RegistredUser author;
	
	@ManyToOne
	private Publication publication;
	
	@Column
	private String keyWords;
	
	@Column
	private String text;
	
	@Column
	private String paperApstract;
	
	@ManyToOne
	private ScientificArea scientificArea;
	
	@ManyToMany
	private Set<CoAuthor>coauthors;
	
	@OneToMany
	private Set<Comment>coomentsAboutArticle;
	
	@OneToMany
	private Set<Comment>comentsForEditors;
	
	@OneToMany
	private Set<Comment>recomendationsList;
	
	@Column
	boolean activated;
	
	public Article() {
		this.activated=false;
		coomentsAboutArticle=new HashSet<Comment>();
		comentsForEditors=new HashSet<Comment>();
		recomendationsList=new HashSet<Comment>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public RegistredUser getAuthor() {
		return author;
	}

	public void setAuthor(RegistredUser author) {
		this.author = author;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Set<CoAuthor> getCoauthors() {
		return coauthors;
	}

	public void setCoauthors(Set<CoAuthor> coauthors) {
		this.coauthors = coauthors;
	}

	public String getPaperApstract() {
		return paperApstract;
	}

	public void setPaperApstract(String paperApstract) {
		this.paperApstract = paperApstract;
	}

	public ScientificArea getScientificArea() {
		return scientificArea;
	}

	public void setScientificArea(ScientificArea scientificArea) {
		this.scientificArea = scientificArea;
	}

	public Publication getPublication() {
		return publication;
	}

	public void setPublication(Publication publication) {
		this.publication = publication;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public Set<Comment> getCoomentsAboutArticle() {
		return coomentsAboutArticle;
	}

	public void setCoomentsAboutArticle(Set<Comment> coomentsAboutArticle) {
		this.coomentsAboutArticle = coomentsAboutArticle;
	}

	public Set<Comment> getComentsForEditors() {
		return comentsForEditors;
	}

	public void setComentsForEditors(Set<Comment> comentsForEditors) {
		this.comentsForEditors = comentsForEditors;
	}

	public Set<Comment> getRecomendationsList() {
		return recomendationsList;
	}

	public void setRecomendationsList(Set<Comment> recomendationsList) {
		this.recomendationsList = recomendationsList;
	}
	
}
