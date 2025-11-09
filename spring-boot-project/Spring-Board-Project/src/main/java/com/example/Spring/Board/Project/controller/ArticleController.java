package com.example.Spring.Board.Project.controller;

import com.example.Spring.Board.Project.dto.ArticleDto;
import com.example.Spring.Board.Project.dto.ArticleForm;
import com.example.Spring.Board.Project.model.Article;
import com.example.Spring.Board.Project.model.Member;
import com.example.Spring.Board.Project.model.MemberUserDetails;
import com.example.Spring.Board.Project.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.Thymeleaf;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Flow;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;


    @RequestMapping("/list")
    public String getArticleList(@PageableDefault(size = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable, Model model){
        Page<ArticleDto> page=articleService.findAll(pageable);
        model.addAttribute("page", page);
        return "article-list";
    }

    @RequestMapping("/content")
    public String getContent(@RequestParam("id")Long id,Model model){
        ArticleDto articleDto=articleService.findById(id);
        model.addAttribute("article",articleDto);
        return "article-content";
    }


    @GetMapping("/add")
    public String getAdd(@ModelAttribute("article")ArticleForm articleForm){
        return "article-add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("article")ArticleForm articleForm, BindingResult bindingResult , @AuthenticationPrincipal MemberUserDetails memberUserDetails){
        if(articleForm.getTitle().equals("T발")){
            bindingResult.rejectValue("title","SlangDetcted","욕설을 사용하지 마세요.");
        }

        if(articleForm.getDescription().equals("T발")){
            bindingResult.rejectValue("description","SlangDetcted","욕설을 사용하지 마세요.");
        }

        if(bindingResult.hasErrors()){
            return "article-add";
        }

        articleService.add(articleForm,memberUserDetails);
        return "redirect:/article/list";


    }


    //수정하기
    @GetMapping("/edit")
    public String getEdit(@RequestParam("id")Long id,@ModelAttribute("article") ArticleForm articleForm){
        ArticleDto articleDto=articleService.findById(id);
        articleForm.setTitle(articleDto.getTitle());
        articleForm.setDescription(articleDto.getDescription());
        articleForm.setId(articleDto.getId());
        return "article-edit";
    }

    @PostMapping("/edit")
    public String editArticle(@Valid @ModelAttribute("article") ArticleForm articleForm, BindingResult bindingResult){

        if(articleForm.getTitle().equals("T발")){
            bindingResult.rejectValue("title","SlangDetcted","욕설을 사용하지 마세요.");
        }

        if(articleForm.getDescription().equals("T발")){
            bindingResult.rejectValue("description","SlangDetcted","욕설을 사용하지 마세요.");
        }

        if(bindingResult.hasErrors()){
            return "article-edit";
        }

        articleService.update(articleForm);
        return "redirect:/article/list";

    }


    //삭제하기
    @GetMapping("/delete")
    public String delete(@RequestParam("id")Long id){
        articleService.delete(id);
        return "redirect:/article/list";
    }



}

////상세 내용 보여주기
//@RequestMapping("/content")
//public String getContent(@RequestParam("id")Long id,Model model){
//    ArticleDto articleDto=articleService.findById(id);
//    model.addAttribute("article",articleDto);
//    return "article-content";
//}

//public ArticleDto findById(Long id){
//    Article article=articleRepository.findById(id).orElseThrow();
//    return mapToArticleDto(article);
//}

//
////게시글 작성하기
///article/add get으로 오면 단순히 화면을 초기화해서 보여주기 앞서 배웠듯이 get 이고 ModelAttribute이용해서 초기화한 데이터 모델에 담고 단순히 보여주기
//
//@GetMapping("/add")
//public String getAdd(@ModelAttribute("article")ArticleForm articleForm){
//    return "article-add";
//}
//
//
//<!DOCTYPE html>
//
//<html lang="en" xmlns:th="http://www.thymeleaf.org"
//xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
//
//th:replace="~{/base-layout::layout(  ~{::section}  )}"
//        >
//<head>
//    <meta charset="UTF-8">
//    <title>Title</title>
//</head>
//<body>
//<section th:fragment="section">
//    <h1>게시판</h1>
//    <form th:object="${article}" th:action="@{/article/add}" method="post">
//        <div class="mb-3">
//        <label  class="form-label">제목</label>
//        <input th:type="text" th:field="*{title}" class="form-control">
//        <p th:if="${#fields.hasErrors('title')}" th:errors="*{title}" class="text-danger"></p>
//        </div>
//        <div class="mb-3">
//        <label class="form-label">내용</label>
//        <textarea th:field="*{description}" class="form-control">
//        </textarea>
//        <p th:if="${#fields.hasErrors('description')}" th:errors="*{description}" class="text-danger"></p>
//        </div>
//        <button type="submit" class="btn btn-primary">저장</button>
//    </form>
//</section>
//
//</body>
//</html>
//
//
//
//화면 보여주고 /article/add post로 넘겨서 현재 , MemnberUserDetails와 ArticleForm을 받아오기, 이떄 앞서배운 BindingResult를 사용하기
//
//@PostMapping("/add")
//public String add(@Valid @ModelAttribute("article")ArticleForm articleForm, BindingResult bindingResult , @AuthenticationPrincipal MemberUserDetails memberUserDetails){
//    if(articleForm.getTitle().equals("T발")){
//        bindingResult.rejectValue("title","SlangDetcted","욕설을 사용하지 마세요.");
//    }
//
//    if(articleForm.getDescription().equals("T발")){
//        bindingResult.rejectValue("description","SlangDetcted","욕설을 사용하지 마세요.");
//    }
//
//    if(bindingResult.hasErrors()){
//        return "article-add";
//    }
//
//    articleService.add(articleForm,memberUserDetails);
//    return "redirect:/article/list";
//
//
//}
//
//
//
//AricleService  public ArticleDto add(ArticleForm articleForm, MemberUserDetails memberUserDetails){
//
//
//    Member member= memberRepository.findById(memberUserDetails.getMemberId()).orElseThrow();
//
//    //ArticleForm -> Article
//    Article article=Article.builder()
//            .title(articleForm.getTitle())
//            .description(articleForm.getDescription())
//            .member(member)
//            .build();
//    articleRepository.save(article);
//    return mapToArticleDto(article);
//
//}
//
////즉, 조건에 맞게 문제 없으면 즉, BindingResult에 문제없으면 만들기, 문제있으면 현재 문제있느 상태 articleForm, bingResult 결과 보내주기
//
//최종흐름
//네, 알겠습니다. 새로운 게시글을 작성하고 처리하는 \*\*전체적인 웹 애플리케이션의 흐름(Flow)\*\*을 요청하신 대로 깔끔하게 순서와 과정별 핵심 동작으로 정리해 드리겠습니다.
//
//이 과정은 **폼 요청(GET)**, **폼 제출 및 검증(POST)**, **서비스 로직 처리**의 3단계로 나뉩니다.
//
//        -----
//
//        ## 🚀 게시글 작성 기능의 최종 구현 흐름 정리
//
//### 1\. 폼 초기화 및 화면 표시 (GET 요청)
//
//| 순서 | 위치 | 코드 및 동작 | 핵심 |
//        | :--- | :--- | :--- | :--- |
//        | **1. 요청** | 브라우저 | 사용자가 `/article/add` URL로 접근합니다. | |
//        | **2. Controller** | `@GetMapping("/add")` | `@ModelAttribute("article")ArticleForm articleForm`을 통해 **비어있는 새로운 `ArticleForm` 객체**를 생성하고, 이를 `"article"`이라는 이름으로 모델에 담습니다. | **모델 초기화**: 뷰(Thymeleaf)에서 사용할 폼 객체를 준비합니다. |
//        | **3. 뷰 반환** | `return "article-add"` | Thymeleaf 템플릿을 반환합니다. | |
//        | **4. Thymeleaf** | `article-add.html` | `<form th:object="${article}" ...>`를 통해 \*\*빈 `ArticleForm`\*\*과 폼 필드(`*{title}`, `*{description}`)를 연결하고, 초기화된 폼 화면을 사용자에게 보여줍니다. | **폼 렌더링**: 사용자가 데이터를 입력할 준비를 합니다. |
//
//        -----
//
//        ### 2\. 폼 제출, 검증 및 오류 처리 (POST 요청)
//
//사용자가 폼을 작성하고 **저장** 버튼을 클릭하면 이 단계가 시작됩니다.
//
//| 순서 | 위치 | 코드 및 동작 | 핵심 |
//        | :--- | :--- | :--- | :--- |
//        | **1. 요청** | 브라우저 | 폼 데이터가 `/article/add` URL로 `POST` 방식으로 전송됩니다. | |
//        | **2. Controller** | `@PostMapping("/add")` | \*\*`@Valid`\*\*가 먼저 실행되어 폼 데이터의 1차 유효성 검증(예: `@NotEmpty`)을 수행하고, 그 결과를 `BindingResult`에 담습니다. | **데이터 수신 및 1차 검증** |
//        | **3. 추가 검증** | `if(articleForm.getTitle().equals("T발"))` | **개발자가 정의한 추가적인 비즈니스 로직** 검증(예: 욕설 필터링)을 수행하고, 오류 발생 시 `bindingResult.rejectValue(...)`를 통해 오류를 `BindingResult`에 수동으로 추가합니다. | **2차 비즈니스 검증** |
//        | **4. 오류 판단** | `if(bindingResult.hasErrors())` | `BindingResult`에 \*\*(1차 `@Valid` 오류 + 2차 수동 검증 오류)\*\*가 하나라도 있는지 확인합니다. | |
//        | **5. 오류 시** | `return "article-add"` | 오류가 있다면, **오류 정보를 담은 현재 `articleForm` 객체**와 \*\*`BindingResult`\*\*를 그대로 뷰로 전달하며 `article-add` 템플릿을 재반환합니다. | **오류 피드백**: 폼이 재로딩되며, `<p th:errors="*{...}">`를 통해 사용자에게 오류 메시지를 보여줍니다. |
//
//        -----
//
//        ### 3\. 정상 처리 및 DB 저장 (Service 로직)
//
//오류가 없어 `bindingResult.hasErrors()`가 `false`일 경우, 서비스 계층으로 이동합니다.
//
//        | 순서 | 위치 | 코드 및 동작 | 핵심 |
//        | :--- | :--- | :--- | :--- |
//        | **1. 사용자 정보 획득** | `@PostMapping("/add")` | `@AuthenticationPrincipal MemberUserDetails memberUserDetails`를 통해 **현재 로그인된 사용자**의 상세 정보를 가져옵니다. | **로그인 유저 식별** |
//        | **2. Service 호출** | `articleService.add(...)` | 폼 데이터와 로그인 사용자 정보를 서비스로 전달합니다. | |
//        | **3. Service 로직** | `ArticleService.add(...)` | 1. `memberRepository.findById(...)`로 **DB에서 Member 엔티티**를 조회합니다. <br> 2. `Article.builder()...build()`를 통해 `ArticleForm` 데이터를 기반으로 **`Article` 엔티티**를 생성합니다. (작성자(`member`) 정보 포함) | **DB 저장 준비** |
//        | **4. DB 저장** | `articleRepository.save(article)` | 새로 생성된 `Article` 엔티티를 DB에 영구 저장합니다. | **트랜잭션 완료** |
//        | **5. 리다이렉트** | `return "redirect:/article/list"` | 모든 처리가 성공했으므로, 브라우저에게 게시글 목록 페이지로 이동하라는 명령을 전달합니다. | **새 페이지 이동** |

//네, 요청하신 대로 Spring Boot에서 **유효성 검증(Validation)**을 위해 가장 자주 사용되는 JSR-380 (Bean Validation) 어노테이션들을 핵심 내용과 함께 깔끔하게 정리해 드리겠습니다.이 어노테이션들은 @Valid와 함께 DTO나 폼 객체의 필드에 붙어 데이터의 무결성을 보장하는 데 사용됩니다.🚀 @Valid를 위한 주요 유효성 검증 어노테이션 정리1. 기본 제약 조건 (Null, Empty, Blank)이 세 가지는 데이터를 받기 전 값이 존재하는지 확인하는 데 가장 중요합니다.어노테이션설명 (무엇을 허용하지 않는가?)타입주의 사항@NotNull해당 필드가 **null**인 것을 허용하지 않습니다.모든 타입 (객체, 숫자, 문자열, 리스트 등)문자열의 내용(""나 " ")은 검사하지 않습니다.@NotEmpty**null**이거나 **비어있는 상태("" 또는 size=0)**인 것을 허용하지 않습니다.문자열, 컬렉션, 배열공백(" ")만 있는 문자열은 유효하다고 판단하여 통과시킵니다.@NotBlank**null**이거나, **비어있는 상태("")**이거나, 공백(" ")으로만 이루어진 문자열을 허용하지 않습니다.String 전용입력값에 대한 가장 엄격한 필수 항목 검증입니다.2. 문자열 및 형식 제약 조건어노테이션속성설명주의 사항@Email-입력된 문자열이 유효한 이메일 형식인지 검증합니다.문자열이 null이거나 비어있으면 검증을 통과시킵니다. @NotBlank와 함께 사용하는 것이 일반적입니다.@Sizemin, max문자열의 길이 또는 컬렉션/배열의 요소 개수가 지정된 범위 내에 있는지 검증합니다.null 값은 검사하지 않고 통과시킵니다.@Patternregexp입력된 문자열이 지정된 **정규 표현식(Regular Expression)**과 일치하는지 검증합니다.복잡한 형식 검사나 커스텀 규칙이 필요할 때 사용됩니다.3. 숫자 및 값 범위 제약 조건어노테이션속성설명주의 사항@Minvalue숫자가 지정된 최소값 이상인지 검증합니다.@Maxvalue숫자가 지정된 최대값 이하인지 검증합니다.@Positive-숫자가 양수인지 (0 초과) 검증합니다.@PositiveOrZero-숫자가 양수이거나 0인지 검증합니다.@Negative-숫자가 음수인지 (0 미만) 검증합니다.@NegativeOrZero-숫자가 음수이거나 0인지 검증합니다.4. 날짜/시간 제약 조건어노테이션속성설명주의 사항@Past-날짜가 현재 시점보다 과거인지 검증합니다.@PastOrPresent-날짜가 현재 시점이거나 과거인지 검증합니다.@Future-날짜가 현재 시점보다 미래인지 검증합니다.@FutureOrPresent-날짜가 현재 시점이거나 미래인지 검증합니다.


//수정하기
//내용 작성하기 전에 주의사항
//네, 요청하신 대로 하나의 `ArticleForm` DTO를 사용하여 **새 글 작성(Create)**과 **기존 글 수정(Update)** 로직을 처리하는 과정과 그에 따른 **주의사항**을 핵심만 추출하여 명확하게 정리해 드리겠습니다.
//
//        ---
//
//        ## 📝 게시글 폼 (ArticleForm)의 이중 활용 전략
//
//`ArticleForm` DTO가 `id` 필드를 포함하고 있기 때문에, 이 필드의 **값 유무**가 현재 작업이 '작성'인지 '수정'인지를 결정하는 핵심 단서가 됩니다.
//
//### 1. 새 게시글 작성 (CREATE)
//
//이 단계는 DB에 새로운 레코드를 삽입하는 과정입니다.
//
//| 단계 | 동작 | `ArticleForm`의 `id` 상태 | 핵심 주의사항 |
//        | :--- | :--- | :--- | :--- |
//        | **GET 요청** | **폼 초기화** (`/article/add`) | `id` 필드는 **`null`**로 넘어옵니다. | Controller에서 `@ModelAttribute("article")`로 빈 객체를 생성하여 전달합니다. |
//        | **POST 제출** | **DB 삽입** (`/article/add`) | `id` 필드는 여전히 **`null`**입니다. | Service 계층에서 `ArticleForm`을 `Article` 엔티티로 변환하여 `save()`하면, DB가 **자동으로 새로운 ID를 생성**합니다. 기존 ID를 사용할 필요가 없으므로 `id`는 무시됩니다. |
//
//        ### 2. 기존 게시글 수정 (UPDATE)
//
//이 단계는 기존 레코드를 찾아서 내용을 변경하는 과정입니다. `ArticleForm`의 `id` 필드가 **활용되는 지점**입니다.
//
//| 단계 | 동작 | `ArticleForm`의 `id` 상태 | 핵심 주의사항 |
//        | :--- | :--- | :--- | :--- |
//        | **GET 요청** | **폼 채우기** (`/article/edit?id=5`) | **ID 값(예: `5`)이 필수**로 넘어옵니다. 이 ID로 DB에서 기존 `Article`을 조회합니다. | **가장 중요:** Controller는 DB에서 조회한 `Article`의 내용을 `ArticleForm` 객체에 담아 뷰로 전달해야 합니다. **빈 폼이 아닙니다.** |
//        | **폼 내부** | **ID 숨기기** | `ArticleForm` 객체에 담긴 ID 값은 사용자에게 보이지 않도록 **숨겨진 필드** (`<input type="hidden">`)로 폼 안에 유지되어야 합니다. | 이 숨겨진 ID가 **'수정할 게시글'**을 식별하는 유일한 열쇠입니다. |
//        | **POST 제출** | **DB 업데이트** (`/article/edit`) | 폼 제출 시, 숨겨진 필드를 통해 `id` 값(예: `5`)이 **다시 Controller로 전송**됩니다. | **유효성 검증(`@Valid`)** 후, Service 계층에서 이 ID를 사용하여 DB에서 기존 `Article` 엔티티를 찾고, 전송된 내용으로 **업데이트(Dirty Checking) 작업**을 수행합니다. |
//
//        ---
//
//        ## 💡 최종 결론: ID 필드의 역할
//
//`ArticleForm`의 `id` 필드는 **수정(Update) 작업**을 수행할 때 **"나는 DB의 이 게시글(Article\_id)을 수정하고 싶다"**는 것을 서버에 알려주는 **식별자 역할**을 수행합니다.
//
//이러한 단일 DTO(Data Transfer Object) 재활용 패턴은 코드를 간결하게 유지하는 데 매우 효과적입니다.
//
//코드
//
////수정하기
//@GetMapping("/edit")
//public String getEdit(@RequestParam("id")Long id,@ModelAttribute("article") ArticleForm articleForm){
//    ArticleDto articleDto=articleService.findById(id);
//    articleForm.setTitle(articleDto.getTitle());
//    articleForm.setDescription(articleDto.getDescription());
//    articleForm.setId(articleDto.getId());
//    return "article-edit";
//}
//
//@PostMapping("/edit")
//public String editArticle(@Valid @ModelAttribute("article") ArticleForm articleForm, BindingResult bindingResult){
//
//    if(articleForm.getTitle().equals("T발")){
//        bindingResult.rejectValue("title","SlangDetcted","욕설을 사용하지 마세요.");
//    }
//
//    if(articleForm.getDescription().equals("T발")){
//        bindingResult.rejectValue("description","SlangDetcted","욕설을 사용하지 마세요.");
//    }
//
//    if(bindingResult.hasErrors()){
//        return "article-edit";
//    }
//
//    articleService.update(articleForm);
//    return "redirect:/article/list";
//
//}
//
//
//
//articleService
//public ArticleDto update(ArticleForm articleForm){
//    Article article=articleRepository.findById(articleForm.getId()).orElseThrow();
//    article.setTitle(articleForm.getTitle());
//    article.setDescription(articleForm.getDescription());
//    articleRepository.save(article);
//    return mapToArticleDto(article);
//}
//
//article-edit.html
//        <!DOCTYPE html>
//
//<html lang="en" xmlns:th="http://www.thymeleaf.org"
//xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
//
//th:replace="~{/base-layout::layout(  ~{::section}  )}">
//<head>
//    <meta charset="UTF-8">
//    <title>Title</title>
//</head>
//<body>
//<section th:fragment="section">
//
//<h1>게시글 수정</h1>
//    <form th:object="${article}" th:action="@{/article/edit}" method="post">
//
//        <input type="hidden" th:field="*{id}">
//
//        <div class="mb-3">
//            <label  class="form-label">제목</label>
//            <input th:type="text" th:field="*{title}" class="form-control">
//            <p th:if="${#fields.hasErrors('title')}" th:errors="*{title}" class="text-danger"></p>
//        </div>
//        <div class="mb-3">
//            <label class="form-label">내용</label>
//            <textarea th:field="*{description}" class="form-control">
//        </textarea>
//            <p th:if="${#fields.hasErrors('description')}" th:errors="*{description}" class="text-danger"></p>
//        </div>
//        <button type="submit" class="btn btn-primary">저장</button>
//
//    </form>
//</section>
//
//</body>
//</html>
//


//삭제하기는간단하다
////삭제하기
//@GetMapping("/delete")
//public String delete(@RequestParam("id")Long id){
//    articleService.delete(id);
//    return "redirect:/article/list";
//}
//
//
//ArticleService 에서 단순히 삭제하고 리스트화면으로 이동
//public void delete(Long id){
//    articleRepository.deleteById(id);
//}
