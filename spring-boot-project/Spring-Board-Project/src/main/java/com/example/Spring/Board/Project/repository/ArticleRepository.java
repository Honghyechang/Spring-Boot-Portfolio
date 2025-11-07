package com.example.Spring.Board.Project.repository;

import com.example.Spring.Board.Project.model.Article;
import com.example.Spring.Board.Project.model.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
@Transactional
void deleteAllByMember(Member member);

}
