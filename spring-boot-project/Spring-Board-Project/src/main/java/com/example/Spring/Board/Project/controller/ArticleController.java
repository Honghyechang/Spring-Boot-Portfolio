package com.example.Spring.Board.Project.controller;

import com.example.Spring.Board.Project.dto.ArticleDto;
import com.example.Spring.Board.Project.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.Thymeleaf;

import javax.swing.*;
import java.util.List;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;


//    @RequestMapping("/list")
//    public String getArticleList(Model model){
//        List<ArticleDto> articles=articleService.findAll();
//        model.addAttribute("articles", articles);
//        return "article-list";
//        //return "article-list-test";
//    }

    @RequestMapping("/list")
    public String getArticleList(@PageableDefault(size = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable, Model model){
        Page<ArticleDto> page=articleService.findAll(pageable);
        model.addAttribute("page", page);
        return "article-list";
    }



}

//@Controller
//@RequestMapping("/article")
//public class ArticleController {
//
//
//    @RequestMapping("/list")
//    public String getArticleList(){
//        return "article-list-test";
//    }
//}
//게시글 목록 화면 만들기
//article-list-test 테스트에서 -> article-list.html 실제 게시글 목록 페이지를 보여주는 페이지를 구현하자.


//
//@RequestMapping("/list")
//public String getArticleList(@PageableDefault(size = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable, Model model){
//    Page<ArticleDto> page=articleService.findAll(pageable);
//    model.addAttribute("page", page);
//    return "article-list";
//}
//Spring MVC에서 파라미터가 Pageable 객체로 사용되는지 아니면 단순한 문자열/숫자로 사용되는지를 판단하는 기준에 대한 내용입니다.결론부터 말씀드리면, 매개변수에 Pageable 타입이 선언되어 있는지를 보고 판단합니다.🎯 getArticleList 메서드 상세 분석주신 컨트롤러 메서드 코드를 상세하게 분석하고, 파라미터 처리 원리를 설명해 드리겠습니다.Java@RequestMapping("/list")
//public String getArticleList(@PageableDefault(size = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable, Model model){
//    Page<ArticleDto> page=articleService.findAll(pageable);
//    model.addAttribute("page", page);
//    return "article-list";
//}
//1. 파라미터 처리 원리 (가장 중요한 부분)"매개변수에 Pageable이 써져있구나 ~ 를 보고 아는건가?"네, 맞습니다. 이것이 Spring MVC의 핵심 원리입니다.Spring은 컨트롤러 메서드를 실행하기 전에 파라미터를 처리하는 **ArgumentResolver**라는 시스템을 사용합니다.매개변수 타입Spring의 인식처리 담당 ArgumentResolverPageable특별한 객체**PageableHandlerMethodArgumentResolver**가 담당String, int단순 데이터RequestParamMethodArgumentResolver가 담당Spring은 메서드 시그니처(Pageable pageable)를 보자마자 이 파라미터가 페이징 요청임을 인지하고, URL의 쿼리 파라미터(?page=...&size=...&sort=...)를 가져다가 Pageable 객체로 자동 변환하여 주입합니다.만약 코드가 다음과 같았다면, page는 단순한 int 파라미터로 처리됩니다.Java// Spring이 Pageable 객체를 만들지 않고, 쿼리 파라미터 'page'만 정수형으로 받음.
//public String getArticleList(
//        @RequestParam(name = "page", defaultValue = "0") int pageNumber,
//        Model model) {
//    // ...
//}
//2. @PageableDefault 분석이 어노테이션은 Pageable 객체가 생성될 때 사용자가 값을 보내지 않은 경우를 위한 기본 규칙을 정해줍니다.설정 값의미클라이언트가 생략 시 적용size = 10한 페이지당 10개URL에 size 파라미터가 없으면 10으로 설정sort = "id"id 필드를 기준으로 정렬URL에 sort 파라미터가 없으면 id를 기준으로 정렬direction = Sort.Direction.DESC내림차순 정렬sort 방향이 지정되지 않으면 내림차순(최신순/번호순)으로 설정3. 메서드 내부 동작Pageable pageable 주입: Spring이 URL 파라미터(예: ?page=2)와 @PageableDefault 설정을 기반으로 Pageable 객체(예: 3페이지, ID 내림차순)를 생성하여 주입합니다.articleService.findAll(pageable) 호출: 서비스 계층으로 Pageable 객체를 전달합니다.DB 쿼리 발생: 서비스 계층에서 articleRepository.findAll(pageable)이 실행되고, DB에 **"ID 내림차순으로 2페이지의 10개 데이터"**를 요청합니다.Page<ArticleDto> page 반환: DB에서 가져온 데이터와 메타 정보가 DTO로 변환된 Page 객체가 반환됩니다.model.addAttribute("page", page): Thymeleaf 템플릿에서 사용하기 쉽도록, 반환된 Page 객체를 **page**라는 이름으로 Model에 담아 뷰로 전달합니다.결론: 이 메서드는 완벽하게 페이징 시스템을 구현하고 있으며, Pageable 타입 선언 덕분에 Spring이 모든 파라미터 처리 및 객체 생성을 자동으로 책임지는 구조입니다.

//
//페이지 번호 (page)를 기본값으로 설정할 수 없는 이유
//@PageableDefault에서 page 번호를 설정하는 속성이 없는 이유는 다음과 같습니다.
//
//Spring의 내장 규칙: Spring Data Web Support는 클라이언트가 page 파라미터를 명시하지 않으면 **자동으로 0 (첫 번째 페이지)**을 기본값으로 사용하도록 설계되어 있습니다.
//
//논리적 불필요성: 모든 페이징 시스템은 항상 첫 페이지(0)부터 시작해야 하므로, 개발자가 임의의 다른 페이지(예: 5)를 기본값으로 설정해야 할 논리적인 필요성이 없습니다.
//
//따라서 개발자는 page 속성 대신 size, sort, direction만 신경 써서 기본값을 설정하면 됩니다. page는 항상 0으로 시작하거나 클라이언트가 명시적으로 요청한 값이 사용됩니다.

//
//네, 맞습니다\! **정확히 파악하셨습니다.**
//
//가장 일반적인 게시판 페이지네이션 상황에서는 **`page` 파라미터만 보내주면 됩니다.**
//
//        ## 🎯 페이지네이션 링크의 효율성
//
//`@PageableDefault`를 컨트롤러에 사용하고 계시기 때문에, Thymeleaf의 페이지네이션 링크는 매우 간결해집니다.
//
//### 1\. 나머지 파라미터는 생략 가능
//
//`size`, `sort`, `direction`은 컨트롤러 메서드에 `@PageableDefault`로 이미 기본값이 설정되어 있습니다.
//
//```java
//@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
//Pageable pageable
//```
//
//        * \*\*`size` (10), `sort` ("id"), `direction` (DESC)\*\*는 클라이언트가 별도로 요청을 보내지 않으면 서버가 이 기본값을 **고정적으로 사용**합니다.
//
//### 2\. `page` 파라미터만 동적으로 전송
//
//따라서 사용자가 페이지를 이동할 때, 템플릿은 **달라져야 할 유일한 값**인 페이지 번호만 URL 파라미터로 전송하면 됩니다.
//
//```html
//        <!-- Thymeleaf에서 페이지 번호만 파라미터로 넘기는 예시 -->
//<a th:href="@{/article/list(page=${i})}" th:text="${i + 1}"></a>
//        ```
//
//만약 사용자가 이 링크를 클릭하여 5페이지(페이지 번호 `4`)를 요청하면, 브라우저는 다음 URL을 보냅니다:
//
//$$\text{/article/list?page=4}$$
//
//서버는 이 `page=4`를 읽고 나머지 `size=10`과 `sort="id,DESC"`는 기본값으로 채워 `Pageable` 객체를 완성하게 됩니다. 이것이 효율적인 페이징 시스템의 핵심입니다.


//
//Page<T> 객체는 페이징 처리를 위해 설계된 컨테이너인 만큼, 데이터 리스트 외에도 페이지네이션 UI를 만드는 데 필요한 모든 메타 정보를 포함하고 있습니다.컨트롤러에서 model.addAttribute("page", page)로 전달했다고 가정하고, page 객체로부터 Thymeleaf에서 접근할 수 있는 주요 속성들을 상세하게 정리해 드리겠습니다.🚀 Page<T> 객체의 주요 속성 (Thymeleaf 접근법)구분Java 메서드 (내부)Thymeleaf 접근법설명사용 예시 (UI)데이터getContent()${page.content}현재 페이지에 표시할 실제 데이터(게시글 DTO 리스트)입니다.th:each로 테이블 행을 반복할 때 사용됩니다.총계getTotalElements()${page.totalElements}DB에 저장된 전체 레코드(게시글) 수입니다."총 100건"과 같이 표시할 때 사용됩니다.총계getTotalPages()${page.totalPages}전체 페이지의 개수입니다. (페이지네이션 바의 최대 번호)페이지 번호 목록을 순회할 때 최대값으로 사용됩니다.페이지 정보getNumber()${page.number}현재 페이지 번호입니다. (0부터 시작)현재 페이지 버튼에 active 클래스를 부여할 때 사용됩니다.페이지 정보getSize()${page.size}한 페이지당 설정된 데이터 개수입니다.페이지 상태isFirst()${page.first}현재 페이지가 **첫 페이지(0번)**인지 여부 (boolean)입니다.'이전 페이지' 버튼을 비활성화할 때 사용됩니다.페이지 상태isLast()${page.last}현재 페이지가 마지막 페이지인지 여부 (boolean)입니다.'다음 페이지' 버튼을 비활성화할 때 사용됩니다.다음/이전hasNext()${page.hasNext()}다음 페이지가 존재하는지 여부 (boolean)입니다.'다음 페이지' 버튼을 활성화/비활성화할 때 사용됩니다.다음/이전hasPrevious()${page.hasPrevious()}이전 페이지가 존재하는지 여부 (boolean)입니다.'이전 페이지' 버튼을 활성화/비활성화할 때 사용됩니다.정렬 정보getSort()${page.sort}현재 요청에 적용된 정렬 정보입니다.
//
//age<T> 인터페이스에는 다음과 같은 메서드가 정의되어 있습니다.Java 메서드Thymeleaf 접근법반환 값의미isEmpty()${page.empty}booleanpage.getContent() 리스트에 데이터가 하나도 없는지 (size() == 0)를 확인합니다.
//getSize()	${page.size}	Pageable 요청에 지정된 한 페이지의 최대 크기입니다. (대부분의 페이지에서 이 값은 고정입니다.)
//추천 (내장 속성)	${page.numberOfElements}	Page 객체가 제공하는 내장 속성으로, 가장 직관적이고 효율적입니다.
//대안 (List 크기)	${page.content.size()}	List<T>를 꺼내어 size() 메서드를 호출하는 방식입니다. numberOfElements와 동일한 값을 반환합니다.