package com.example.Spring.Board.Project.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberUserDetails implements UserDetails{

    //필수 필드
    private Collection<SimpleGrantedAuthority> authorities;
    private  String password;
    private String username; //아이디를 이메일로 할것

    //추가 필드
    private String displayName;
    private Long memberId;

    public MemberUserDetails(Member member, List<Authority> authorities) {

        //필수설정
        this.password = member.getPassword();
        this.username=member.getEmail();
        this.authorities=authorities.stream()
                .map(i->new SimpleGrantedAuthority(i.getAuthority()))
                .collect(Collectors.toList());
        //추가 필드 설정
        this.displayName=member.getName();
        this.memberId=member.getId();

    }




}
