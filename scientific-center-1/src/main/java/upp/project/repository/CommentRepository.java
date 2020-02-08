package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import upp.project.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
