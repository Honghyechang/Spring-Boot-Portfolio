package com.example.Spring.Board.Project.controller;

import com.example.Spring.Board.Project.dto.MemberDto;
import com.example.Spring.Board.Project.dto.MemberForm;
import com.example.Spring.Board.Project.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//관리자용 컨트롤러
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/list")
    public String memberList(Model model,@PageableDefault(size = 2,sort = "id",direction = Sort.Direction.DESC) Pageable pageable) {

        Page<MemberDto> page= memberService.findAll(pageable);
        model.addAttribute("page",page);
        return "member-list";

    }
    @GetMapping("/edit")
    public String getEdit(Model model, @ModelAttribute("member") MemberForm memberForm , @RequestParam("id") Long id) {
        MemberDto memberDto=memberService.findById(id);
        memberForm.setName(memberDto.getName());
        memberForm.setEmail(memberDto.getEmail());
        memberForm.setId(memberDto.getId());
        return "member-edit";
    }

    @PostMapping("/edit")
    public String memberEdit(@Valid @ModelAttribute("member") MemberForm memberForm, BindingResult bindingResult) {
        log.info("memberEdit post method called");

        if(bindingResult.hasErrors()) {
            return "member-edit";
        }
        memberService.update(memberForm);
        return "redirect:/member/list";
    }

    @GetMapping("/delete")
    public String memberDelete(@RequestParam("id") Long id) {
        memberService.delete(id);
        return "redirect:/member/list";
    }
}
