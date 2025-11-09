package com.example.Spring.Board.Project.service;

import com.example.Spring.Board.Project.dto.ArticleDto;
import com.example.Spring.Board.Project.dto.ArticleForm;
import com.example.Spring.Board.Project.model.Article;
import com.example.Spring.Board.Project.model.Member;
import com.example.Spring.Board.Project.model.MemberUserDetails;
import com.example.Spring.Board.Project.repository.ArticleRepository;
import com.example.Spring.Board.Project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.Thymeleaf;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    final private ArticleRepository articleRepository;
    final private MemberRepository memberRepository;

    public ArticleDto mapToArticleDto(Article article){
        ArticleDto articleDto=ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .created(article.getCreated())
                .updated(article.getUpdated())
                .memberId(article.getMember().getId())
                .name(article.getMember().getName())
                .email(article.getMember().getEmail())
                .build();
        return articleDto;
    }

    public List<ArticleDto> findAll(){
        List<Article> articles=articleRepository.findAll();
        return articles.stream()
                .map(i->mapToArticleDto(i))
                .collect(Collectors.toList());
    }

    public Page<ArticleDto> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable).map(i -> mapToArticleDto(i));
    }

    public ArticleDto findById(Long id){
        Article article=articleRepository.findById(id).orElseThrow();
        return mapToArticleDto(article);
    }

    public ArticleDto add(ArticleForm articleForm, MemberUserDetails memberUserDetails){


        Member member= memberRepository.findById(memberUserDetails.getMemberId()).orElseThrow();

        //ArticleForm -> Article
        Article article=Article.builder()
                .title(articleForm.getTitle())
                .description(articleForm.getDescription())
                .member(member)
                .build();
        articleRepository.save(article);
        return mapToArticleDto(article);

    }

    public ArticleDto update(ArticleForm articleForm){
        Article article=articleRepository.findById(articleForm.getId()).orElseThrow();
        article.setTitle(articleForm.getTitle());
        article.setDescription(articleForm.getDescription());
        articleRepository.save(article);
        return mapToArticleDto(article);
    }


    public void delete(Long id){
        articleRepository.deleteById(id);
    }

}
