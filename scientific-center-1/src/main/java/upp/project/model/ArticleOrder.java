package upp.project.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
public class ArticleOrder extends UserOrder {

	@ManyToMany
	private Set<Article> papers;

	public ArticleOrder(Set<Article> papers) {
		super();
		this.papers = papers;
	}
	
	public ArticleOrder() {
		
	}

	public Set<Article> getPapers() {
		return papers;
	}

	public void setPapers(Set<Article> papers) {
		this.papers = papers;
	}
	
}
