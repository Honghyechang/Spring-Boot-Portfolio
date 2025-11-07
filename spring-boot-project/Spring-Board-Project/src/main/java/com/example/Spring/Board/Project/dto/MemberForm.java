package com.example.Spring.Board.Project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//MemberForm는 Member의 RequestDTO 객체
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberForm {
    @NotBlank(message="이름을 입력하세요.")
    private String name;
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    private String password;
    private String passwordConfirm;
}
