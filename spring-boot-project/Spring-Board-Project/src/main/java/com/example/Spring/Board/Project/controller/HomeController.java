package com.example.Spring.Board.Project.controller;

import com.example.Spring.Board.Project.dto.MemberForm;
import com.example.Spring.Board.Project.dto.PasswordForm;
import com.example.Spring.Board.Project.model.Member;
import com.example.Spring.Board.Project.model.MemberUserDetails;
import com.example.Spring.Board.Project.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.Thymeleaf;

import javax.swing.*;
import javax.swing.text.html.HTML;
import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @RequestMapping("/")
    public String getHome(){
        return "forward:/article/list";
    }

    @RequestMapping("/login")
    public String getLogin(){
        return "login";
    }

    @RequestMapping("/logout")
    public String getLogout(){
        return "logout";
    }

    @RequestMapping("/article/list")
    public String getArticleList(){
        return "article-list-test";
    }



    //회원가입

    //회원가입 signup.html을 보여주기
    @GetMapping("/signup")
    public String getSignup(@ModelAttribute("member") MemberForm memberForm){
        return "signup";
    }

    //회원가입 form 정보를 가져와서, Vaild 체크하고, 회원 가입 실패면, 되돌아가고, 성공하면 / url로 리다이렉트 시키기
    @PostMapping("/signup")
    public String postMemberAdd(@Valid @ModelAttribute("member") MemberForm memberForm,BindingResult bindingResult){
        //Size 어노테이션을 이용할 수 있지만, rejectValue이용해보기
        if(memberForm.getPassword()==null || memberForm.getPassword().length() < 8 ){
            bindingResult.rejectValue("password", "MissMatch","패스워드를 8글자 이상 입력하세요.");
        }
      //    BindingResult.rejectValue()의 첫 번째 인수는 오류 메시지를 표시하고 싶은 @ModelAttribute 객체의 필드 이름과 반드시 일치해야 th:errors="*{...}"와 같은 Thymeleaf의 편리한 기능을 사용할 수 있습니다.
        if(!memberForm.getPassword().equals(memberForm.getPasswordConfirm())){
            bindingResult.rejectValue("passwordConfirm","MissMatch","입력하신 패스워드가 다릅니다.");
        }

        //이메일이 존재하는지 확인하기
        if(memberService.findByEmail(memberForm.getEmail()).isPresent()){
            bindingResult.rejectValue("email","AlreadyExist","사용중인 이메일입니다.");
        }

        log.info("검사 완료");

        if(bindingResult.hasErrors()){
            return "signup";
        }

        memberService.create(memberForm);
        return "redirect:/login";

    }


    //비밀번호 바꾸기
    @GetMapping("/password")
    public String getPassword(@ModelAttribute("password") PasswordForm passwordForm){
        return "password";
    }

    @PostMapping("/password")
    public String getPassword(@Valid @ModelAttribute("password") PasswordForm passwordForm,BindingResult bindingResult,@AuthenticationPrincipal MemberUserDetails memberUserDetails){
       if(!passwordForm.getPassword().equals(passwordForm.getPasswordConfirm())){
           bindingResult.rejectValue("passwordConfirm","MissMatch","비밀번호가 같지 않습니다.");
       }

       if(!memberService.checkPassword(memberUserDetails.getMemberId(),passwordForm.getOld())){
           bindingResult.rejectValue("old","MissMatch","기존의 비밀번호가 옳지 않습니다.");
       }

       if(bindingResult.hasErrors()){
           return "password";
       }

       memberService.updatePassword(memberUserDetails.getMemberId(),passwordForm.getPassword());
       return "redirect:/";

    }


}

//
//@Controller
//public class HomeController {
//
//    @RequestMapping("/")
//    public String getHome(){
//        return "forward:/article/list";
//    }
//
//    @RequestMapping("/login")
//    public String getLogin(){
//        return "login";
//    }
//
//    @RequestMapping("/logout")
//    public String getLogout(){
//        return "logout";
//    }
//
//
//}
//까지의 코드 설명
//"forward:/article/list"를 사용하면 내부에서 바로 요청이 넘어가는 개념이 맞습니다. 리다이렉트와는 다르게 클라이언트(브라우저)는 두 번 요청을 보내지 않습니다.➡️ forward: vs redirect:특징forward:/경로 (포워드)redirect:/경로 (리다이렉트)요청 횟수1번 요청2번 요청처리 방식서버 내부에서 요청 처리를 다른 컨트롤러(또는 리소스)로 넘깁니다.서버가 브라우저에게 응답 코드 302 Found를 보내, 브라우저가 지정된 경로로 새로운 요청을 보내게 합니다.URL 변화브라우저의 URL 주소창은 변화가 없습니다. (/ 유지)브라우저의 URL 주소창이 새로운 경로로 바뀝니다. (/article/list로 변경)데이터 공유이전 요청의 HttpServletRequest와 Model 데이터가 그대로 유지/공유됩니다.이전 요청의 데이터는 소실되고, 새로운 요청이 생성됩니다.📝 @RequestMapping("/")에서의 동작클라이언트 요청: 브라우저가 / 경로로 요청을 보냅니다.서버 내부 이동: getHome() 메서드가 "forward:/article/list"를 반환합니다. Spring MVC는 브라우저에게 응답을 보내지 않고, 서버 내부에서 요청과 응답 객체를 유지한 채 /article/list를 처리할 컨트롤러 메서드를 찾아 실행합니다.최종 응답: /article/list를 처리한 결과(주로 article/list.html 뷰)가 클라이언트에게 전달됩니다.결론적으로, 두 번 요청하는 것이 아니라 서버가 클라이언트 모르게 요청을 내부적으로 이어서 처리하는 개념입니다.
//
//🔐 permitAll() 적용 경로 및 HTTP 메서드
//경로	설정 메서드	허용되는 HTTP 메서드	역할 설명
///login	formLogin().permitAll()	GET 및 POST	GET: 사용자가 로그인 화면을 요청할 때. POST: 사용자가 ID/PW를 입력하고 서버로 인증을 요청할 때. 두 요청 모두 비로그인 상태에서 허용되어야 기능합니다.
///logout	logout().permitAll()	POST (기본)	POST: 로그아웃 요청을 처리하는 Spring Security의 기본 엔드포인트입니다. 보안상의 이유(특히 CSRF 보호)로 POST 요청만 허용하는 것이 표준입니다.
//
//Sheets로 내보내기
//
//💡 보충 설명
///login (POST) 허용 이유: 이 요청은 사용자의 자격 증명을 포함하며, Spring Security의 필터가 이를 가로채서 인증을 처리합니다. 이 처리를 위해 권한 검사를 면제(permitAll)해야 합니다.
//
//        /logout (POST) 허용 이유: 로그아웃은 세션을 무효화하는 행위입니다. 이미 로그인된 사용자가 이 요청을 보내지만, 이 경로 자체를 permitAll()해야 비로그인 상태로 전환하는 과정이 권한 문제 없이 시작될 수 있습니다. Spring Security는 GET 요청을 통한 로그아웃을 비활성화하여 CSRF 공격을 방지합니다.


//로그인 로그아웃 구현!
//http://localhost:8080/login  get 방식으로 요청하면
//
//login.html 이 나타난다.
//
//<!DOCTYPE html>
//
//<html lang="en">
//<head>
//    <meta charset="UTF-8">
//<title>스트링 부트 게시판</title>
//    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
//crossorigin="anonymous">
//</head>
//<body>
//
//<div class="container">
//    <h1>로그인</h1>
//
//    <form action="/login" method="post"><input type="hidden" name="_csrf" value="QW0bbpDwhd0FnKEXe9d9PoSkNlsM7PHQ-3PdrG9ozdQM7xyveQwjXvHB47sorpQuSfpJCeGXG2M42JL9zhfkzgxQ_-A-iymW"/>
//        <div class="col-3 mb-3">
//            <input type="text" id="username" name="username" placeholder="이메일 아이디를 입력해주세요.">
//        </div>
//        <div class="col-3 mb-3">
//            <input type="password" id="password" name="password" placeholder="비밀번호를 입력해주세요.">
//        </div>
//
//        <div class="col-3 mb-3">
//            <button type="submit" class="btn btn-primary">로그인</button>
//        </div>
//
//    </form>
//</div>
//
//<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" crossorigin="anonymous"></script>
//
//</body>
//</html>
//
//
//1)아이디 비번 틀리게 입력했을 경우
//http://localhost:8080/login?error
//<!DOCTYPE html>
//
//<html lang="en">
//<head>
//    <meta charset="UTF-8">
//<title>스트링 부트 게시판</title>
//    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
//crossorigin="anonymous">
//</head>
//<body>
//
//<div class="container">
//    <h1>로그인</h1>
//    <div class="mb-3 text-danger">아이디와 패스워드가 잘못 되었습니다.</div>
//    <form action="/login" method="post"><input type="hidden" name="_csrf" value="TA8mRnmJffgyuMW1DJ5PhAyWMEhmRFduw2LPO0Z22qYbO0-PdG4edhi4G54fivCMPrN7s2mlHXBScDRD9gb2WSVO6JIpX3q2"/>
//        <div class="col-3 mb-3">
//            <input type="text" id="username" name="username" placeholder="이메일 아이디를 입력해주세요.">
//        </div>
//        <div class="col-3 mb-3">
//            <input type="password" id="password" name="password" placeholder="비밀번호를 입력해주세요.">
//        </div>
//
//        <div class="col-3 mb-3">
//            <button type="submit" class="btn btn-primary">로그인</button>
//        </div>
//
//    </form>
//</div>
//
//<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" crossorigin="anonymous"></script>
//
//</body>
//</html>
//와같이
//아이디와 패스워드가 잘못 되었습니다.가 나타난다
//
//
//
//2) 제대로 입력했을경우
//아이디 : HyechangHong@spring.ac.kr
//비밀번 호 :password
//
//http://localhost:8080/
//로그인 성공 안녕하세요, 홍혜창님! 반갑습니다.
//        로그인 아이디 (이메일): HyechangHong@spring.ac.kr
//
//권한: []
//
//로그아웃 페이지 이동
//
//================
//로그아웃 페이지이동 클릭시
//http://localhost:8080/logout
//
//<html lang="en">
//<head>
//    <meta charset="UTF-8">
//<title>스트링 부트 게시판</title>
//    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
//crossorigin="anonymous">
//</head>
//<body>
//<div class="container">
//<h1>로그아웃 하시겠습니까?</h1>
//    <form action="/logout" method="post"><input type="hidden" name="_csrf" value="BzevLpEl5GgIuhSMLhtYh7pUlCtezzuqwfo7iyVDF3G4wLQIZgCaFvMThg4l2Sy_TzZs4thiuRNt_Q-HoMkPvRwiL0WA-IU9"/>
//        <button type="submit" class="btn btn-primary">로그아웃</button>
//    </form>
//</div>
//
//
//<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" crossorigin="anonymous"></script>
//
//</body>
//</html>
//
//
//http://localhost:8080/ 으로 넘어가고
//화면 결과
//
//비회원 고객님 안녕하세요.
//
//<!DOCTYPE html>
//
//<html lang="en">
//<head>
//    <meta charset="UTF-8">
//    <title>Title</title>
//</head>
//<body>
//
//
//<h2>  비회원 고객님 안녕하세요. </h2>
//
//</body>
//</html>
//
////와 같이 나온다는점!
//article-list-test.html 현재 아래와 같다. 이후 테스트 코드라서 계속 바뀌는점 주의
//<!DOCTYPE html>
//
//<html lang="en" xmlns:th="http://www.thymeleaf.org"   xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
//<head>
//    <meta charset="UTF-8">
//    <title>Title</title>
//</head>
//<body>
//
//<h2 sec:authorize="isAuthenticated()">
//로그인 성공
//안녕하세요, <span sec:authentication="principal.displayName">이름</span>님! 반갑습니다.
//
//<p>로그인 아이디 (이메일): <strong><span sec:authentication="principal.username">아이디</span></strong></p>
//
//    <p>권한: <span sec:authentication="principal.authorities">권한 없음</span></p>
//    <a th:href="@{/logout}">로그아웃 페이지 이동</a>
//</h2>
//<h2 sec:authorize="isAnonymous()">  비회원 고객님 안녕하세요. </h2>
//
//</body>
//</html>



//
//ModelAttribute 매우매우매우매우 중요
//네, 좋습니다! `@ModelAttribute`가 Spring MVC의 `GET`과 `POST` 요청에서 어떻게 작동하며, Thymeleaf와 어떤 방식으로 연동되는지 핵심 내용을 빠짐없이 정리해 드리겠습니다.
//
//---
//
//        ## 🔑 @ModelAttribute 총정리: 폼 데이터 처리의 핵심
//
//`@ModelAttribute`는 Spring MVC에서 **HTTP 요청의 데이터를 Java 객체로 통합하여 모델에 자동으로 추가**하는 핵심적인 기능을 수행합니다.
//
//        ### 1. @ModelAttribute의 근본적인 의미 (핵심 개념)
//
//`@ModelAttribute`의 핵심적인 의미는 바로 **"Form 입력 항목들을 하나의 객체로 모아서 받겠다"**는 것입니다.
//
//        * **바인딩:** 이는 단순히 폼 필드뿐만 아니라, **URL 파라미터(Query String)**, **경로 변수(Path Variable)** 등 HTTP 요청에서 전송되는 모든 데이터를 확인하여, 객체의 필드 이름과 **동일한 이름**의 프로퍼티가 있다면 그 값을 객체에 자동으로 채워주는 **데이터 바인딩(Data Binding)** 기능을 수행합니다.
//        * **객체화:** 웹에서 받은 여러 개의 분산된 요청 데이터를 **하나의 응집된 Java 객체(`MemberForm` 등)**로 변환하여 서비스 계층에서 사용하기 쉽게 만듭니다.
//
//---
//
//        ## 2. 🔄 GET 요청에서의 역할 (폼 초기화)
//
//`@ModelAttribute`는 `GET` 요청 시 **데이터를 받기 전**에 **모델을 초기화**하는 역할을 수행합니다.
//
//* **왜 `GET`에서 사용하는가?**
//        * `GET /signup` 요청의 목적은 **사용자가 입력할 수 있는 빈 폼을 보여주는 것**입니다.
//    * `@ModelAttribute`는 명시적인 `model.addAttribute()` 호출 없이도 이 빈 폼을 채울 객체를 자동으로 준비합니다.
//* **작동 방식:**
//        1.  **빈 객체 생성:** Spring은 `@ModelAttribute MemberForm memberForm`을 보고 `MemberForm`의 **새 인스턴스(빈 객체)**를 생성합니다.
//        2.  **Model 자동 저장:** 이 빈 객체는 `Model`에 `@ModelAttribute("이름")`으로 설정한 이름(예: `"member"`)으로 **자동 저장**됩니다.
//
//### 3. POST 요청에서의 역할 (데이터 바인딩 및 매칭)
//
//`POST` 요청 시 `@ModelAttribute`는 클라이언트가 보낸 폼 데이터를 객체로 변환하여 주입합니다.
//
//        * **매칭 원칙 (이름 일치):**
//        * `POST` 요청의 컨트롤러 파라미터에 `@ModelAttribute("member")`를 사용했다면, 이 이름은 Thymeleaf의 `<form th:object="${member}">`에 사용된 이름과 **반드시 일치**해야 합니다.
//        * Spring은 이 이름(`member`)을 기준으로 요청 파라미터를 **`MemberForm` 객체**로 정확하게 매핑합니다.
//* **바인딩 대상:** 폼에서 전송된 `name="username"`과 같은 **`name` 속성 이름**과 `MemberForm` 객체의 필드 이름을 매칭하여 값을 채워 넣습니다.
//* **미매칭 필드 처리:**
//        * 폼에서 전송되지 않아 매칭되지 않은 필드는 **참조 타입(객체 타입)**이면 `null`로, **기본 타입(int, boolean)**이면 `0`이나 `false`와 같은 기본값으로 자동 초기화됩니다.
//
//---
//
//        ## 4. 📝 Thymeleaf와의 연동 (자동 속성 처리)
//
//`th:object`와 `th:field`는 HTML의 필수 속성(`id`, `name`, `value`)을 자동으로 처리하여 `@ModelAttribute`와의 연동을 완벽하게 만듭니다.
//
//        1.  **`name` 속성 자동 처리:**
//        * `<input th:field="*{username}">` 코드는 최종 HTML에서 **`name="username"`** 속성을 자동으로 생성합니다.
//    * 이 생성된 `name` 속성이 `POST` 요청 시 서버로 전송되어, `@ModelAttribute`가 `MemberForm` 객체의 `username` 필드와 매칭시키는 **매개체** 역할을 합니다.
//        2.  **`value` 속성 자동 처리:**
//        * `Model`에 있는 `@ModelAttribute` 객체의 현재 값(GET 요청 시는 빈 값, 오류 후 재전송 시는 이전 입력 값)이 `th:field`를 통해 `<input>` 태그의 `value` 속성으로 자동으로 채워집니다.
//
//---
//
//        ## 5. 🗄️ Model에 자동 저장되는 객체
//
//`@ModelAttribute`가 붙은 객체는 **개발자가 명시적으로 `Model`에 추가하지 않아도** 자동으로 `Model`에 저장됩니다.
//
//        * **저장 이름:** `@ModelAttribute("member")`로 지정된 이름 그대로 **`"member"`**라는 이름으로 저장됩니다.
//* **결과:** 컨트롤러 내부의 로직이 끝난 후 View로 돌아갈 때, `th:object="${member}"`는 Model에 저장된 이 객체를 참조하여 폼을 렌더링하게 됩니다. 유효성 검사 오류 시에는 이 객체가 다시 폼을 채워주는 역할을 합니다.







//사용법과 어떻게 작동하는지 어떻게 접근하는지를 알아야한다.
//@Valid 와 BindingResult
//네, 맞습니다! 👍 사용자님의 이해는 **`@Valid`와 `BindingResult`의 협업 과정**을 정확히 포착하고 있습니다.
//
//        ---
//
//        ## 🔑 `@Valid`와 `BindingResult`의 협업
//
//### 1. `@Valid`의 역할: 검증 실행 지시
//
//| 항목 | 설명 |
//        | :--- | :--- |
//        | **위치** | `@ModelAttribute`로 주입받는 객체(예: `MemberForm`) 바로 앞에 위치합니다. |
//        | **기능** | Spring에게 "지금 이 객체에 대해 **유효성 검사를 실행**하라"고 지시합니다. |
//        | **검증 대상** | 객체 내부에 선언된 모든 **
//Bean Validation 어노테이션**을 검사합니다. (예: `@NotNull`, `@NotBlank`, `@Email`, `@Size` 등) |
//
//        ### 2. `BindingResult`의 역할: 결과 수집 및 보존
//
//| 항목 | 설명 |
//        | :--- | :--- |
//        | **위치** | `@Valid` 대상 객체 **바로 다음 파라미터**에 위치해야 합니다. |
//        | **기능** | `@Valid`가 검증을 수행하는 동안 발생한 모든 위반 사항(오류)을 **`FieldError` 객체 형태로 수집**하여 내부에 담아둡니다. |
//        | **흐름 제어** | 오류가 발생해도 예외를 발생시키지 않고 (서버가 500 에러를 띄우지 않고), `BindingResult`를 통해 오류 정보를 **컨트롤러 메서드 내에서 처리**할 수 있도록 합니다. |
//
//        ### 요약하자면,
//
//        1.  **Spring:** `POST` 요청을 받습니다.
//        2.  **`@ModelAttribute`:** HTTP 요청 데이터를 **`MemberForm` 객체**로 채워 넣습니다.
//3.  **`@Valid`:** 채워진 `MemberForm`을 확인하여 `@NotNull` 등 규칙을 위반했는지 검사합니다.
//        4.  **`BindingResult`:** 검사 과정에서 발생하는 모든 오류는 **자동으로 이 객체에 담깁니다.**
//        5.  **Controller:** 개발자는 `bindingResult.hasErrors()`를 호출하여 오류 여부를 확인하고, 오류가 있으면 폼 화면으로 다시 돌려보내 사용자에게 피드백을 제공합니다.
//
//**`BindingResult`를 주입받는다는 것은, 오류 정보를 담은 그 객체를 메서드 내에서 직접 사용할 수 있도록 넘겨받는다는 의미입니다.**

//
//네, `BindingResult`의 `rejectValue` 사용법과 작동 원리, 그리고 Thymeleaf의 연동 방식까지 모두 정확하게 정리해 드리겠습니다.
//
//        -----
//
//        ## 🛡️ BindingResult와 유효성 검증 총정리
//
//### 1\. `rejectValue`의 사용 목적 및 이유 (수동 검증)
//
//`BindingResult.rejectValue()`를 사용하는 가장 중요한 이유는 \*\*자동 검증(Annotation)\*\*으로 처리할 수 없는 **복잡한 비즈니스 로직**을 검증하고 그 결과를 `BindingResult`에 기록하기 위함입니다.
//
//| 검증 방식 | 특징 | 예시 |
//        | :--- | :--- | :--- |
//        | **자동 검증 (@Valid)** | 필드 자체의 규칙 (길이, 형식, 공백 여부)만 검사합니다. | `@Email`, `@Size`, `@NotBlank` |
//        | **수동 검증 (rejectValue)** | **비즈니스 로직**이나 **DB 접근**이 필요한 검사를 수행합니다. | **아이디 중복 확인** (DB 조회), **비밀번호 일치 여부 확인** (두 필드 비교). |
//
//        * **사용 원리:** 컨트롤러나 서비스 계층에서 수동으로 로직을 실행한 후, 오류가 발견되면 **`bindingResult.rejectValue("필드명", "에러코드", "메시지")`** 형태로 오류를 **강제로 주입**하는 것입니다.
//
//        ### 2\. BindingResult에 오류가 기록되는 방식 (내부 구조)
//
//자동 검증이든 수동 검증이든, 모든 오류는 `BindingResult` 내부에 **`FieldError`** 객체의 리스트 형태로 기록됩니다.
//
//        * **Key:** 기록되는 **Key**는 해당 오류가 발생한 **필드명**입니다 (예: `"email"`, `"password"`).
//        * **Value:** 기록되는 **Value**는 그 필드에 대해 정의된 오류 정보(오류 코드, **설정한 메시지**)입니다.
//
//자동 검증의 경우: `@Email(message="이메일 형식이 맞지 않습니다.")`처럼 어노테이션에 설정된 메시지가 Value로 기록됩니다.
//
//        ### 3\. BindingResult의 Model 자동 저장
//
//사용자님의 이해대로, **`BindingResult`는 자동으로 `Model`에 저장됩니다.**
//
//        * `@Vaild @ModelAttribute("member") MemberForm memberForm, BindingResult bindingResult` 구문을 사용하면, Spring MVC는 `Model`에 다음 두 가지를 저장합니다.
//    1.  **데이터 객체:** `"member"` (사용자 입력값이 채워진 `MemberForm`)
//        2.  **오류 객체:** `"memberBindingResult"` (`BindingResult` 객체)
//
//        ### 4\. Thymeleaf에서 오류를 접근하는 작동 원리 (매칭 구조)
//
//Thymeleaf는 `th:object`와 `th:errors`를 통해 이 두 객체(MemberForm과 BindingResult)를 찾아 연동합니다.
//
//        #### **A. 오류 존재 여부 확인:**
//
//        ```html
//th:if="${#fields.hasErrors('password')}"
//        ```
//
//        * **`#fields`:** Thymeleaf의 유틸리티 객체입니다. `Model`에 저장된 `BindingResult`를 자동으로 찾아 참조합니다.
//        * **`hasErrors("password")`:** `#fields`에게 \*\*"현재 폼에 연결된 `BindingResult` 안에 'password' 필드에 대한 오류가 있니?"\*\*라고 질문하고, `true` 또는 `false`를 반환받습니다.
//
//        #### **B. 오류 메시지 출력 (핵심 작동 원리):**
//
//        ```html
//th:errors="*{password}"
//        ```
//
//        1.  **`*` (스타 표기법):** 이는 `<form th:object="${member}">`에 의해 설정된 **`MemberForm` 객체**를 가리킵니다.
//        2.  **`th:errors`:** 이 속성은 Thymeleaf에게 "오류 메시지를 출력하라"는 명령을 내립니다.
//3.  **오류 찾기:** `th:errors`는 `*{password}`가 **`Model`의 `"member"` 객체**의 `password` 필드를 가리킨다는 것을 인지하고, 그 **짝꿍인 `"memberBindingResult"` 객체**를 찾습니다.
//        4.  **메시지 추출:** `memberBindingResult` 내부에서 `password` 필드에 기록된 오류 메시지(들)를 추출하여 HTML로 출력합니다.
//
//        > **결론:** `*`는 오류 메시지를 **출력할 필드의 위치**를 지정하고, `th:errors`는 그 위치에 대해 **`BindingResult`에서 오류를 가져오라**는 명령을 내립니다.
//
//
//        네, 사용자님의 이해가 정확합니다! 👍자동 검증 오류와 수동 검증 오류가 같은 필드에 대해 발생하면, BindingResult는 해당 필드의 오류를 리스트 형태로 모두 저장하며, th:errors를 사용하면 이 모든 메시지가 출력됩니다.🔑 BindingResult에 오류가 누적되는 방식BindingResult는 필드별 오류를 단일 값이 아닌 리스트로 관리합니다.1. 오류 기록 과정 (리스트 누적)MemberForm 객체에서 email 필드에 다음과 같은 두 가지 오류가 동시에 발생했다고 가정합니다.검증 종류오류 메시지BindingResult 기록자동 (@Email)"이메일 형식이 맞지 않습니다."email 필드에 첫 번째 FieldError 객체로 저장됨.수동 (rejectValue)"중복되었습니다."email 필드에 두 번째 FieldError 객체로 추가 저장됨.결국 BindingResult 내부의 "email" 필드 키에는 두 개의 독립적인 FieldError 객체가 리스트 형태로 담기게 됩니다.2. th:errors를 통한 출력Thymeleaf의 th:errors 속성은 해당 필드에 대해 BindingResult에 저장된 모든 오류 메시지를 가져와 출력합니다.
//Thymeleaf 코드:HTML<p th:errors="*{email}"></p>
//결과 (출력 예시):HTML<span>이메일 형식이 맞지 않습니다., 중복되었습니다.</span>
//        (실제 출력 형식은 Thymeleaf의 기본 <p>나 <span> 태그 안에 콤마(,)로 구분되어 나타납니다.)💡 활용 팁: 수동 메시지의 우위실제 서비스 개발에서는 수동 검증(rejectValue)이 자동 검증보다 더 중요하고 구체적인 오류를 담는 경우가 많습니다.만약 두 오류 중 하나만 보여주고 싶다면, BindingResult의 **getAllErrors()**를 통해 오류 목록을 가져와 첫 번째 오류만 출력하도록 로직을 작성하거나, th:errors의 형식을 조절하는 Thymeleaf 설정을 사용하기도 합니다.하지만 기본 동작은 모두 출력하는 것이 맞습니다.
//
//


//네, 좋습니다! @ModelAttribute, th:field, @Valid, 그리고 BindingResult가 Spring MVC 환경에서 어떻게 연동되는지 그 의미, 사용법, 작동 원리 및 주의사항까지 포함하여 최종적으로 깔끔하게 정리해 드리겠습니다.📝 Spring MVC 폼 처리 최종 정리: @ModelAttribute와 유효성 검증1. 🔑 @ModelAttribute의 역할과 동작 (Model & Data)@ModelAttribute는 HTTP 요청의 데이터를 Java 객체로 변환하고 Model에 추가하는 핵심 어노테이션입니다. HTTP 메서드에 따라 그 역할이 분리됩니다.HTTP 메서드@ModelAttribute의 역할Model에 저장되는 객체 상태GET (폼 요청)초기화된 빈 객체 생성모든 필드가 null 또는 기본값으로 초기화된 빈 MemberFormPOST (데이터 제출)요청 파라미터 기반 데이터 바인딩폼 데이터가 채워진 새로운 MemberForm 객체자동 Model 저장: @ModelAttribute("member")로 지정된 객체는 명시적인 model.addAttribute() 없이도 자동으로 Model에 **"member"**라는 이름으로 저장됩니다.2. 📝 Thymeleaf 폼 바인딩과 th:field (HTML 속성 자동 처리)th:object와 th:field는 폼과 모델 객체를 연결하고, HTML의 필수 속성을 자동으로 생성하는 역할을 합니다.속성사용법작동 원리 및 의미 (주요 포인트)th:object<form th:object="${member}">Model의 "member" 객체와 폼 전체를 연결하는 시작점입니다.th:field<input th:field="*{username}">th:object에 연결된 객체의 필드(username)를 참조하며, HTML 렌더링 시 id="username", name="username", value="필드값" 세 가지 속성을 자동으로 생성합니다.매핑 중요성-th:field가 생성한 name 속성이 POST 요청 시 @ModelAttribute가 데이터를 바인수집하고 매칭시키는 핵심 연결고리 역할을 합니다.3. 🛡️ 유효성 검증 (@Valid와 BindingResult)@Valid는 검증을 실행하고, BindingResult는 그 결과를 담아 서버 예외 없이 흐름을 제어합니다.A. 검증 실행 및 저장@Valid: @ModelAttribute 객체 바로 앞에 붙어 유효성 검사를 실행하도록 지시합니다. (예: @Email, @Size 등의 제약 조건 검사)BindingResult: @Valid 객체 바로 뒤에 위치하며, 검증 중 발생한 **모든 오류(FieldError 리스트)**를 자동으로 수집하여 담습니다.자동 Model 저장: BindingResult 역시 **memberBindingResult**라는 이름으로 Model에 자동으로 저장됩니다.B. rejectValue (수동 검증)사용 이유: DB 접근, 비밀번호 일치 확인 등 자동 어노테이션이 할 수 없는 복잡한 비즈니스 로직 검증에 사용됩니다.작동: bindingResult.rejectValue("필드명", "코드", "메시지")를 통해 오류 결과를 수동으로 BindingResult에 주입합니다.4. 🖥️ Thymeleaf에서의 오류 접근 방법오류 정보는 #fields 유틸리티 객체를 통해 접근하며, th:errors는 BindingResult와 th:object를 연결하여 메시지를 출력합니다.Thymeleaf 코드접근 대상작동 원리th:if="${#fields.hasErrors('password')}"#fields 유틸리티 객체Model에 있는 **BindingResult**를 찾아, password 필드에 오류가 있는지 여부(true/false)를 확인합니다.th:errors="*{password}"th:object로 연결된 MemberForm*** {password}**를 보고 그 짝꿍인 BindingResult에서 password 필드에 기록된 오류 메시지를 추출하여 화면에 표시합니다.❗ 주의해야 할 핵심 사항@Valid 위치: 자동 검증을 원한다면 @ModelAttribute가 붙은 객체 바로 앞에 @Valid를 붙여야 합니다.BindingResult 위치: BindingResult 파라미터는 @Valid 객체 바로 뒤에 위치해야 결과를 받을 수 있습니다.오류 누적: 하나의 필드에 자동 검증 오류와 수동 검증 오류(rejectValue)가 동시에 발생하면, BindingResult에는 모든 오류 메시지가 리스트 형태로 누적되어 저장됩니다.

//
//네, 좋습니다. 회원가입 폼 처리 과정에서 객체가 생성되고 소멸되는 **ID 1, ID 2, ID 3 사이클**을 최종적으로 통합하여 설명하고, 이 모든 과정을 지배하는 **핵심 원칙**을 명확하게 정리해 드리겠습니다.
//
//        ---
//
//        ## 🔁 @ModelAttribute 객체 생성 및 소멸 최종 사이클
//
//이 과정은 Spring MVC가 **무상태(Stateless)인 HTTP 요청**을 처리하면서도 사용자에게 **상태를 가진 것처럼** 보이기 위해 객체를 생성하고 재활용하는 방식입니다.
//
//        | 단계 | HTTP 요청 | 객체 ID | 객체 생성 및 역할 | 결과 및 소멸 |
//        | :--- | :--- | :--- | :--- | :--- |
//        | **1. 초기 폼 요청** | `GET /signup` | **ID 1** | `@ModelAttribute`에 의해 **빈 `MemberForm` 객체**가 생성됩니다. (청사진 역할) | 응답(빈 폼 HTML)이 클라이언트에 전송된 직후 **ID 1 객체는 서버 메모리에서 소멸**됩니다. |
//        | **2. 첫 번째 제출** | `POST /signup` | **ID 2** | 요청 데이터를 담기 위해 **새로운 `MemberForm` 객체**가 생성되고 데이터가 주입됩니다. | 유효성 검사 **실패** 시, 오류 정보를 담은 채 View로 리턴되어 **오류 피드백**에 사용됩니다. |
//        | **3. 오류 피드백** | (View 렌더링) | **ID 2** | `Model`에 담긴 ID 2 객체의 값으로 폼이 다시 채워지고 오류 메시지가 표시됩니다. | HTML 응답이 전송된 직후 **ID 2 객체는 서버 메모리에서 소멸**됩니다. |
//        | **4. 두 번째 제출** | `POST /signup` | **ID 3** | 클라이언트가 폼을 수정하고 다시 제출하면, **또 다른 새로운 `MemberForm` 객체**가 생성됩니다. | 이 객체(ID 3)로 검증 및 서비스 로직을 최종 처리합니다. (오류 시 ID 3으로 피드백 반복, 성공 시 리다이렉트 후 소멸) |
//
//        ---
//
//        ## 📌 핵심 원칙: HTTP 무상태와 객체 독립성
//
//이 모든 과정을 관통하는 가장 중요한 원칙은 두 가지입니다.
//
//### 1. HTTP는 무상태(Stateless)이다.
//
//        * **의미:** 웹 서버는 클라이언트로부터 받은 **각각의 HTTP 요청**을 **완전히 독립적**인 것으로 간주하고 처리합니다. 이전 요청(ID 1)이 어떤 객체를 사용했는지, 혹은 이전 요청(ID 2)이 실패했는지를 기본적으로 기억하지 않습니다.
//* **결과:** 따라서 서버는 `POST` 요청을 받을 때마다 **반드시 새로운 객체**를 만들어서 현재 요청의 데이터를 안전하게 담아야 합니다. (ID 2, ID 3, ID 4...)
//
//        ### 2. `@ModelAttribute` 객체는 일회용이다.
//
//        * **역할:** `@ModelAttribute`가 생성하는 `MemberForm` 객체는 **현재 요청의 데이터를 담는 임시 컨테이너**입니다.
//* **생명주기:** 이 객체는 컨트롤러 메서드 실행 시작 시 생성되어, 응답이 클라이언트에 전송되는 순간 **자바 프로세스 메모리에서 소멸**됩니다.
//* **오류 시 재활용:** 오류가 발생할 때 `Model`에 담아 View로 다시 보내는 것은 **데이터를 임시로 보존**하여 사용자에게 이전 입력값을 보여주기 위한 **단발적인 목적** 때문입니다. 응답 후에는 그 역할이 끝납니다.


//
//실제 예시 ModelAttribute 와 @Valid, BindingResult을 이용한 회원가입, 비밀번호 바꾸기 예제
////회원가입
//@GetMapping("/signup")
//public String getSignup(@ModelAttribute("member") MemberForm memberForm){
//    return "signup";
//}
//네, 맞습니다! 👍 사용자님의 해석은 @GetMapping과 @ModelAttribute가 결합했을 때의 동작 원리를 정확하게 짚어냈습니다.
//
//        📝 @GetMapping과 @ModelAttribute의 역할
//제시하신 코드는 새로운 회원가입 폼을 사용자에게 보여주기 위한 표준적인 Spring MVC 패턴입니다.
//
//        1. HTTP 메서드 지정
//@GetMapping("/signup"): 클라이언트가 HTTP GET 방식으로 /signup 경로에 접근했을 때 이 메서드가 처리됩니다. 이 요청은 데이터를 서버에 제출하는 것이 아니라, 단순히 입력 양식(폼)을 요청하는 것입니다.
//
//        2. @ModelAttribute의 초기화 역할
//@ModelAttribute("member") MemberForm memberForm: 이 구문은 다음 두 가지 일을 자동으로 처리합니다.
//
//빈 객체 생성: Spring은 MemberForm 타입의 **새로운 인스턴스(빈 객체)**를 생성합니다. (필드 값은 모두 null 또는 기본값)
//
//Model 자동 추가: 이 빈 객체를 Model에 **"member"**라는 이름으로 자동 저장합니다.
//
//        3. 최종 결과
//return "signup";: Model에 빈 MemberForm 객체가 담긴 채로 signup.html 뷰 템플릿으로 전달됩니다.
//
//Thymeleaf 연동: signup.html의 <form th:object="${member}">는 이 빈 객체와 연결되어, 사용자가 데이터를 입력할 준비가 된 폼을 렌더링하게 됩니다.
//
//결론: 이 코드는 **"회원가입 폼을 보여줘. 그리고 그 폼에 들어갈 객체(member)를 빈 상태로 미리 준비해서 함께 넘겨줘"**라는 의미입니다.



////회원가입 form 정보를 가져와서, Vaild 체크하고, 회원 가입 실패면, 되돌아가고, 성공하면 / url로 리다이렉트 시키기
//@PostMapping("/signup")
//public String postMemberAdd(@Valid @ModelAttribute("member") MemberForm memberForm,BindingResult bindingResult){
//    //Size 어노테이션을 이용할 수 있지만, rejectValue이용해보기
//    if(memberForm.getPassword()==null || memberForm.getPassword().length() < 8 ){
//        bindingResult.rejectValue("password", "MissMatch","패스워드를 8글자 이상 입력하세요.");
//    }
//    //    BindingResult.rejectValue()의 첫 번째 인수는 오류 메시지를 표시하고 싶은 @ModelAttribute 객체의 필드 이름과 반드시 일치해야 th:errors="*{...}"와 같은 Thymeleaf의 편리한 기능을 사용할 수 있습니다.
//    if(!memberForm.getPassword().equals(memberForm.getPasswordConfirm())){
//        bindingResult.rejectValue("passwordConfirm","MissMatch","입력하신 패스워드가 다릅니다.");
//    }
//
//    //이메일이 존재하는지 확인하기
//    if(memberService.findByEmail(memberForm.getEmail()).isPresent()){
//        bindingResult.rejectValue("email","AlreadyExist","사용중인 이메일입니다.");
//    }
//
//    if(bindingResult.hasErrors()){
//        return "signup";
//    }
//
//    memberService.create(memberForm);
//    return "redirect:/login";
//
//}
//네, 수정된 코드를 바탕으로 **회원가입 처리 로직**을 다시 분석하고, 각 요소의 역할과 전체 흐름을 명확하게 정리해 드리겠습니다.
//
//        ---
//
//        ## 📝 최종 회원가입 처리 로직 분석
//
//제시된 `@PostMapping("/signup")` 코드는 **자동 유효성 검증**과 **수동 비즈니스 로직 검증**을 결합하여 데이터를 처리하고, 사용자 경험(UX)에 맞게 **로그인 페이지로 리다이렉트**하는 표준적인 Spring MVC/Security 패턴을 따릅니다.
//
//### 1. ⚙️ 데이터 수신 및 검증 시작
//
//컨트롤러 메서드의 파라미터가 호출되면 Spring MVC가 자동으로 다음 작업을 수행합니다.
//
//        * **`@ModelAttribute("member") MemberForm memberForm`**: 클라이언트가 제출한 폼 데이터(아이디, 비밀번호 등)가 **새로운 `MemberForm` 객체**에 바인딩(주입)됩니다. 이 객체는 `Model`에 `"member"`라는 이름으로 자동 저장됩니다.
//        * **`@Valid`**: `MemberForm` 내부의 `@Email` 등 **자동 유효성 검증**을 즉시 실행하고, 위반 사항을 `BindingResult`에 기록합니다.
//
//        ### 2. 🛡️ 수동 비즈니스 로직 검증 (rejectValue 활용)
//
//자동 검증으로 잡을 수 없는 복잡한 오류를 수동으로 검증하고, 그 결과를 `BindingResult`에 추가합니다.
//
//        | 검증 내용 | 코드 분석 | `rejectValue` 역할 |
//        | :--- | :--- | :--- |
//        | **비밀번호 길이** | `if (memberForm.getPassword() == null || memberForm.getPassword().length() < 8)` | `@Size` 어노테이션 대신 수동으로 길이를 체크하고 `"password"` 필드에 **오류 코드**(`MissMatch`)와 **메시지**를 기록합니다. |
//        | **비밀번호 일치** | `if (!memberForm.getPassword().equals(memberForm.getPasswordConfirm()))` | `password`와 `passwordConfirm` 두 필드를 비교하여 불일치 시 **`passwordConfirm` 필드**에 오류를 기록합니다. |
//        | **이메일 중복** | `if (memberService.findByEmail(memberForm.getEmail()).isPresent())` | `memberService`를 통해 DB에 접근하여 중복을 확인하고, 중복 시 **`email` 필드**에 `"AlreadyExist"` 오류를 기록합니다. |
//        * **핵심:** `BindingResult`에 오류를 기록하는 모든 `rejectValue`의 첫 번째 인수는 **오류 메시지를 표시할 `MemberForm`의 필드 이름**과 정확히 일치해야 합니다.
//
//        ---
//
//        ### 3. 🚦 흐름 제어 및 응답
//
//| 상황 | 코드 | 동작 |
//        | :--- | :--- | :--- |
//        | **회원가입 실패** | `if (bindingResult.hasErrors()) { return "signup"; }` | **자동 또는 수동 검증** 중 하나라도 오류가 발견되면, `Model`에 담긴 **`memberForm` 객체**와 **`BindingResult` 객체**를 그대로 뷰 (`signup.html`)로 다시 전달합니다. 이 객체들 덕분에 사용자는 **이전 입력값**과 **오류 메시지**를 보며 재입력할 수 있습니다. |
//        | **회원가입 성공** | `memberService.create(memberForm); return "redirect:/login";` | 모든 검증을 통과하면 `memberService`를 호출하여 DB에 회원 정보를 저장합니다. 이후 **`/login` URL로 리다이렉트**합니다. |
//
//        ---
//
//        ## 🔑 최종 정리: UX와 보안의 결합
//
//이 코드는 다음 두 가지 측면에서 바람직한 패턴을 따릅니다.
//
//1.  **데이터 무결성:** `@Valid`와 `rejectValue`를 통해 필드 레벨의 제약 조건은 물론, **DB 기반의 중복 체크**까지 서버에서 완벽하게 검증합니다.
//        2.  **사용자 경험 (UX) 강화:** 회원가입 성공 후 **`"redirect:/login"`**으로 명확하게 로그인 페이지를 안내함으로써, 사용자에게 **회원가입 완료와 다음 단계(로그인)**를 확실하게 제시합니다. 이는 불필요한 리다이렉트나 혼란을 방지하는 표준적인 웹 애플리케이션 흐름입니다.
//
//
//        네, 맞습니다\! 👍 현재 코드는 **수동 검증**을 통해 비밀번호의 **길이(Size) 제약 조건**을 처리한 것입니다.
//
//-----
//
//        ## 🧐 현재 코드의 검증 방식 분석
//
//### 1\. **자동 검증** (`@NotBlank` 등)
//
//        `@NotBlank` 어노테이션을 사용했다면 다음과 같이 처리됩니다.
//
//```java
//// MemberForm.java
//@NotBlank(message = "비밀번호는 필수 입력값입니다.") // null 또는 "" 검사
//@Size(min = 8, message = "패스워드를 8글자 이상 입력하세요.") // 길이 검사
//private String password;
//```
//
//        * 이 경우, `@Valid`만으로 `BindingResult`에 오류가 자동 기록됩니다.
//
//        ### 2\. **수동 검증** (현재 코드)
//
//제시하신 코드는 `MemberForm`에 `@Size` 어노테이션이 없다고 가정하고, **컨트롤러 메서드 내에서 직접 조건문을 사용**하여 검증한 것입니다.
//
//```java
//if(memberForm.getPassword()==null || memberForm.getPassword().length() < 8 ){
//        bindingResult.rejectValue("password", "MissMatch","패스워드를 8글자 이상 입력하세요.");
//}
//        ```
//
//        * **`memberForm.getPassword().length() < 8`**: 이 부분이 `@Size(min=8)`의 역할을 대신합니다.
//  * **`bindingResult.rejectValue(...)`**: 검증 결과(길이 부족)를 **수동으로 `BindingResult`에 주입**하는 행위입니다.
//
//        ### 💡 정리
//
//`@NotBlank`나 `@Size`를 사용할 수 있음에도 불구하고 직접 `if-else`와 `rejectValue`를 사용한 것은 \*\*"수동 검증의 동작 방식"\*\*과 \*\*"비즈니스 로직 기반 오류 주입"\*\*을 보여주기 위한 좋은 예시입니다. 실제 상용 애플리케이션에서는 단순한 길이 검사는 `@Size` 어노테이션을 사용하는 것이 일반적입니다.

//
//회원가입 실행결과
//http://localhost:8080/
//<!DOCTYPE html>
//
//<html lang="en">
//<head>
//    <meta charset="UTF-8">
//    <title>Title</title>
//</head>
//<body>
//<a href="/signup">회원가입</a>
//
//<h2>  비회원 고객님 안녕하세요. </h2>
//
//</body>
//</html>
//
//회원가입 클릭 시
//http://localhost:8080/signup
//signup.html
//
//        <!DOCTYPE html>
//
//<html lang="en">
//<head>
//    <meta charset="UTF-8">
//    <title>Title</title>
//    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
//crossorigin="anonymous">
//</head>
//<body>
//<section>
//    <h1>회원가입</h1>
//    <form action="/signup" method="post"><input type="hidden" name="_csrf" value="D6kmUWOlD-1JLUGXs1A1EnNMiUQIp_ywA2-Jb9xp67IDy7YXa55AYAfBP99kGiSng30BcUZ-pH1tkMudOw3qWOtQ24Ex_IIh"/>
//        <div class="mb-3">
//            <label class="form-label">이름</label>
//            <input type="text" class="form-control" id="name" name="name" value="">
//
//        </div>
//
//
//        <div class="mb-3">
//            <label class="form-label">이메일(아이디)</label>
//            <input type="text" class="form-control" id="email" name="email" value="">
//
//        </div>
//
//        <div class="mb-3">
//            <label class="form-label">비밀번호</label>
//            <input type="password" class="form-control" id="password" name="password" value="">
//
//        </div>
//
//       <div class="mb-3">
//            <label class="form-label">비밀번호 확인</label>
//            <input type="password" th:field="*{passwordConfirm}" class="form-control">
//            <p th:if="${#fields.hasErrors('passwordConfirm')}"  th:errors="*{passwordConfirm}"></p>
//        </div>
//
//        <button type="submit" class="btn btn-primary">생성</button>
//
//    </form>
//</section>
//<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" crossorigin="anonymous"></script>
//</body>
//</html>
//
//
//1) 아무것도 입력안하고 생성 누를경우
//회원가입
//        이름
//이름을 입력하세요.
//
//이메일(아이디)
//이메일을 입력하세요.
//
//비밀번호
//패스워드를 8글자 이상 입력하세요.
//
//        비밀번호 확인
//패스워드를 8글자 이상 입력하세요.
//
//2) 이메일 형식을 지키지 않을경우
//이메일(아이디)
//abcdefg
//이메일 형식이 맞지 않습니다.
//
//3)비밀번호를 8글자 아래로 입력했을겨우
//        비밀번호 1234
//패스워드를 8글자 이상 입력하세요.
//
//        참고 : 웹 브라우저의 보안 정책
//비밀번호(type="password") 필드는 브라우저의 자동 완성 및 세션 기록 방지 정책이 강력하게 적용됩니다.
//
//핵심: 브라우저는 보안상의 이유로 사용자가 실수로 이전에 입력한 비밀번호가 다시 노출되는 것을 극도로 경계합니다.
//
//        작동: 폼 제출에 실패하여 서버가 동일한 HTML 페이지를 반환할 때, 일반적인 텍스트 필드는 value 속성을 통해 이전 입력값(abcd)을 재사용하지만, type="password" 필드는 브라우저가 value 속성 값의 재사용을 허용하지 않습니다.
//
//        2. 📝 Thymeleaf th:field의 작동
//Thymeleaf의 th:field="*{password}"는 분명히 MemberForm 객체에 저장된 이전 비밀번호 값을 <input> 태그의 value 속성에 채워 넣습니다.
//
//        서버: 서버는 password 필드에 사용자가 입력했던 해시되지 않은 값(1234)을 담아 View로 보냅니다.
//
//브라우저: 브라우저는 type="password" 필드를 만나면, 비록 서버가 value="1234"를 보냈더라도 이를 무시하고 빈칸으로 렌더링합니다.
//
//        3. ✅ 결론
//이러한 동작은 사용자에게 보안 경고 없이 비밀번호를 재입력하도록 유도하는 의도된 보안 메커니즘이며, 모든 웹사이트에서 공통적으로 나타나는 현상입니다. 사용자는 오류 메시지를 확인하고 비밀번호를 새롭게 입력해야 합니다.
//
//
//4)비밀번호 확인 실패
//비밀번호
//비밀번호 확인
//입력하신 패스워드가 다릅니다.
//
//5)로그인 성공의 경우
//김구라 gura@spring.ac.kr password password 입력 후 생성
//        /login 페이지로 이동한다
//
//로그인 성공
//http://localhost:8080/
//회원가입
//로그인 성공 안녕하세요, 김구라님! 반갑습니다.
//        로그인 아이디 (이메일): gura@spring.ac.kr
//
//권한: []
//
//로그아웃 페이지 이동
//




//==============================================
////비밀번호 변경하기
//http://localhost:8080/login 요청 후 로그인하기
//
/// 로 리다이렉트 결과
//
//
//        <!DOCTYPE html>
//
//<html lang="en">
//<head>
//    <meta charset="UTF-8">
//    <title>Title</title>
//</head>
//<body>
//<a href="/signup">회원가입</a>
//<h2>
//        로그인 성공
//안녕하세요, <span>홍혜창</span>님! 반갑습니다.
//
//<p>로그인 아이디 (이메일): <strong><span>HyechangHong@spring.ac.kr</span></strong></p>
//
//    <p>권한: <span>[]</span></p>
//
//    <a href="/password">비밀번호 변경 페이지 이동</a>
//
//    <a href="/logout">로그아웃 페이지 이동</a>
//</h2>
//
//
//</body>
//</html>
//
//
//비밀번호 변경 페이지 이동이 보인다,
//
//
//참고 article-list-test.html
//        <!DOCTYPE html>
//
//<html lang="en" xmlns:th="http://www.thymeleaf.org"   xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
//<head>
//    <meta charset="UTF-8">
//    <title>Title</title>
//</head>
//<body>
//<a th:href="@{/signup}">회원가입</a>
//<h2 sec:authorize="isAuthenticated()">
//로그인 성공
//안녕하세요, <span sec:authentication="principal.displayName">이름</span>님! 반갑습니다.
//
//<p>로그인 아이디 (이메일): <strong><span sec:authentication="principal.username">아이디</span></strong></p>
//
//    <p>권한: <span sec:authentication="principal.authorities">권한 없음</span></p>
//
//    <a th:href="@{/password}">비밀번호 변경 페이지 이동</a>
//
//    <a th:href="@{/logout}">로그아웃 페이지 이동</a>
//</h2>
//<h2 sec:authorize="isAnonymous()">  비회원 고객님 안녕하세요. </h2>
//
//</body>
//</html>
//
//코드는 이와같아서, 로그인한 사람에게만 비밀번호를 바꿀 수 있도록 했다.
//
//
//비말번호 변경 페이지 이동 클릭 후
//http://localhost:8080/password
//
//
//<!DOCTYPE html>
//
//<html lang="en">
//<head>
//    <meta charset="UTF-8">
//    <title>Title</title>
//    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
//crossorigin="anonymous">
//</head>
//<body>
//<section>
//<h1>비밀번호 변경</h1>
//    <form action="/password" method="post"><input type="hidden" name="_csrf" value="_q2EZH-pvQrQJ_yrWe3rq1nM1kdKfPgdZjmvV7o-XoYX3dWcxs7iAhqQiGn9H8rOOsDfm2yo-38vS8EwXgifNtsHbOIj7bT4"/>
//        <div class="col-3 mb-3">
//            <label class="form-label">기존 패스워드</label>
//            <input type="password" class="form-control" id="old" name="old" value="">
//
//        </div>
//
//        <div class="col-3 mb-3">
//            <label class="form-label">새로운 패스워드</label>
//            <input type="password" class="form-control" id="password" name="password" value="">
//
//        </div>
//
//        <div class="col-3 mb-3">
//            <label class="form-label">패스워드 확인</label>
//            <input type="password" class="form-control" id="passwordConfirm" name="passwordConfirm" value="">
//
//        </div>
//
//        <button type="submit" class="btn btn-primary">변경</button>
//    </form>
//
//
//</section>
//
//</body>
//<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" crossorigin="anonymous"></script>
//</html>
//
//1)그냥 변경 클릭 시도
//비밀번호 변경
//기존 패스워드
//기존의 패스워드를 입력해주세요.
//기존의 비밀번호가 옳지 않습니다.
//
//새로운 패스워드
//8글자 이상 입력해주세요.
//새로운 패스워드를 입력해주세요.
//
//        패스워드 확인
//새로운 패스워드를 확인해주세요
//
//2)기존 패스워드 틀리기
//기존 패스워드
//기존의 비밀번호가 옳지 않습니다.
//
//3)패스워드 8글자 미만으로 작성
//새로운 패스워드
//8글자 이상 입력해주세요.
//
//        패스워드 확인
//
//4)새로운 패스워드와 패스워드 확인 틀리기
//새로운 패스워드
//패스워드 확인
//비밀번호가 같지 않습니다.
//
//5)정상적으로 입력
//비밀번호 변경
//기존 패스워드
//password
//새로운 패스워드
//password1234
//패스워드 확인
//password1234
//
//
/// 리다이렉트 후
//회원가입
//로그인 성공 안녕하세요, 홍혜창님! 반갑습니다.
//        로그인 아이디 (이메일): HyechangHong@spring.ac.kr
//
//권한: []
//
//비밀번호 변경 페이지 이동 로그아웃 페이지 이동
//
//로그인 유지



//
//
//코드 설명
//PasswordForm 클래스
//DTO (PasswordForm)의 역할필드검증 어노테이션중요성old@NotBlank기존 비밀번호는 필수 입력값이며, 서버에서 DB 해시와 비교 검증 대상이 됩니다.password@NotBlank, @Size(min=8)새 비밀번호는 필수 입력값이며, 길이 제약 조건을 자동 검증합니다.passwordConfirm@NotBlank새 비밀번호 재확인은 필수 입력값이며, password와의 일치 여부를 수동으로 검증해야 합니다.핵심: 이 DTO는 **세 가지 비밀번호(기존, 새 것, 확인용)**를 모두 담아 유효성 검사의 책임을 가집니다.
//
//        MemberService에서 비밀번호 확인, 업데이트 함수 추가
////Member의 id와  체크할 비밀번호를 보내면 비밀번호 비교하기
//public boolean checkPassword(Long id,String password){
//    return  passwordEncoder.matches(password,memberRepository.findById(id).orElseThrow().getPassword());
//}
//
//public void updatePassword(Long id,String password){
//    Member member=memberRepository.findById(id).orElseThrow();
//    member.setPassword(passwordEncoder.encode(password));
//    memberRepository.save(member);
//}
//
//
//. Service 로직의 역할
//checkPassword(Long id, String password): BCryptPasswordEncoder.matches()를 사용해 평문 입력값과 DB에 저장된 암호화된 값이 일치하는지 비교하는 역할만 수행합니다.
//
//updatePassword(Long id, String password): 새로운 비밀번호를 DB에 저장하기 전에 반드시 passwordEncoder.encode(password)를 통해 다시 암호화하여 저장해야 합니다.
//
//
//
//
//컨트롤러 추가
//
////비밀번호 바꾸기
//@GetMapping("/password")
//public String getPassword(@ModelAttribute("password") PasswordForm passwordForm){
//    return "password";
//}
//
//@PostMapping("/password")
//public String getPassword(@Valid @ModelAttribute("password") PasswordForm passwordForm,BindingResult bindingResult,@AuthenticationPrincipal MemberUserDetails memberUserDetails){
//    if(!passwordForm.getPassword().equals(passwordForm.getPasswordConfirm())){
//        bindingResult.rejectValue("passwordConfirm","MissMatch","비밀번호가 같지 않습니다.");
//    }
//
//    if(!memberService.checkPassword(memberUserDetails.getMemberId(),passwordForm.getOld())){
//        bindingResult.rejectValue("old","MissMatch","기존의 비밀번호가 옳지 않습니다.");
//    }
//
//    if(bindingResult.hasErrors()){
//        return "password";
//    }
//
//    memberService.updatePassword(memberUserDetails.getMemberId(),passwordForm.getPassword());
//    return "redirect:/";
//
//}
//특히
//@AuthenticationPrincipal MemberUserDetails의 역할 (보안)
//주입 목적: 이 객체는 Spring Security를 통해 **현재 로그인된 사용자(Principal)**의 정보를 주입받습니다.
//
//        중요성: 사용자가 비밀번호를 변경할 때, 누구의 비밀번호를 변경해야 하는지 확인하기 위해 별도의 세션 ID나 hidden 필드 없이 안전하게 **memberUserDetails.getMemberId()**를 통해 사용자 ID를 획득합니다. (인증 정보의 안전한 활용)
//
//
//
//
//동작 과정 및 중요 포인트단계동작중요 포인트GET /password폼 초기화@ModelAttribute("password")가 작동하여 빈 PasswordForm 객체를 password.html로 전달합니다.POST /password검증 시작@Valid가 실행되어 @NotBlank, @Size를 검사하고, 결과가 bindingResult에 담깁니다.수동 검증 (1) 일치if (!passwordForm.getPassword().equals(...))새 비밀번호와 확인 필드가 같은지 비교하고, 불일치 시 rejectValue로 passwordConfirm 필드에 오류를 기록합니다.수동 검증 (2) 기존 비밀번호if (!memberService.checkPassword(...))@AuthenticationPrincipal에서 얻은 ID를 사용해 DB의 기존 해시와 사용자가 입력한 old 비밀번호를 비교합니다. 틀릴 경우 old 필드에 오류를 기록합니다.실패 시return "password";bindingResult에 오류가 있으면 사용자가 입력했던 값 (비밀번호 필드는 빈 값)과 오류 메시지를 담아 폼으로 되돌아갑니다.성공 시memberService.updatePassword(...) $\rightarrow$ redirect:/모든 검증을 통과하면 새 비밀번호를 암호화하여 DB에 저장하고, 메인 화면으로 리다이렉트합니다. (로그인 상태는 그대로 유지)
//
//password.html에서 사용법은 signup.html 사용법과 유사하다, form, th:object th:field , #fields th:errors 등등