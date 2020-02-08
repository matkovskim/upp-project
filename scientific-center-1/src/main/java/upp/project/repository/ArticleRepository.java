package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.Article;
import upp.project.model.Magazine;
import upp.project.model.RegistredUser;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	
	Article findByTitle(String title);
	
	Article findByAuthorAndPublicationMagazine(RegistredUser author, Magazine magazine);
	
}