package com.example.Spring.Board.Project.dto;


import lombok.Builder;
import lombok.Data;

//MemberDto 는 Member의 responseDTO 객체

@Builder
@Data
public class MemberDto {

    private Long id;
    private String name;
    private String email;

}
