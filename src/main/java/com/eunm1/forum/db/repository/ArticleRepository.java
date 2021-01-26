package com.eunm1.forum.db.repository;

import com.eunm1.forum.db.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findOneById(Long id);

    List<Article> findByTitleContainingIgnoreCase(String keyword);

    List<Article> findByBodyContainingIgnoreCase(String keyword);

    List<Article> findByTitleContainingIgnoreCaseAndBodyContainingIgnoreCase(String s, String keyword);

    List<Article> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(String s, String keyword);

}
