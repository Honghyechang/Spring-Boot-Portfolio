package com.example.Spring.Board.Project.service;

import com.example.Spring.Board.Project.dto.ArticleDto;
import com.example.Spring.Board.Project.model.Article;
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

}

//
//public Page<ArticleDto> findAll(Pageable pageable) {
//    return articleRepository.findAll(pageable).map(i -> mapToArticleDto(i));
//}
//의 의미
//Page<T> 객체의 두 가지 핵심 정보
//Page<Article>이 리턴된다는 것은, **전체 게시글(20개)**을 4개의 페이지로 나누었을 때, **현재 사용자가 요청한 페이지(예: 2페이지)**의 정보만 담긴 객체를 받는다는 뜻입니다.
//
//        1. 콘텐츠 정보 (현재 페이지 데이터)
//포함된 데이터: 요청된 2페이지에 해당하는 List<Article> (게시글 6번부터 10번까지 5개)
//
//접근 방법 (Thymeleaf): ${articlesPage.content}
//
//2. 메타 정보 (전체 페이지 관리 데이터)
//포함된 데이터: 현재 페이지의 상태 및 전체 구조를 파악하는 데 필요한 정보.
//
//getTotalElements(): 전체 게시글 수 (20개)
//
//getTotalPages(): 전체 페이지 수 (4개)
//
//getNumber(): 현재 페이지 번호 (2페이지는 보통 1, 또는 0부터 시작할 경우 1)
//
//getSize(): 한 페이지당 크기 (5개)
//
//접근 방법 (Thymeleaf): ${articlesPage.totalPages}, ${articlesPage.number}
//
//💡 findAll(pageable).map(...)의 전체 흐름
//입력 (Pageable): Controller에서 "2페이지, 5개씩, 정렬 기준은 최신순" 요청을 담은 Pageable이 들어옵니다.
//
//DB 조회 (findAll): articleRepository.findAll(pageable)가 실행됩니다. 이 함수는 DB에서 6번~10번 게시글의 List<Article>을 가져오고, 전체 20개라는 메타 정보를 합쳐 **Page<Article>**을 반환합니다.
//
//변환 (.map(...)): 반환된 Page<Article>에 map(i -> mapToArticleDto(i))가 적용되어, 내부의 List<Article>만 List<ArticleDto>로 변환됩니다.
//
//출력 (return): 최종적으로 내용물만 DTO로 바뀐 **Page<ArticleDto>**가 반환됩니다.
//
//이로써 Page 객체는 **"나는 지금 전체 20개 중 2페이지의 DTO 5개를 가지고 있어"**라는 완전한 정보를 전달하게 됩니다.