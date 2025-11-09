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

