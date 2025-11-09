package com.example.Spring.Board.Project.service;

import com.example.Spring.Board.Project.dto.MemberDto;
import com.example.Spring.Board.Project.dto.MemberForm;
import com.example.Spring.Board.Project.model.Member;
import com.example.Spring.Board.Project.repository.ArticleRepository;
import com.example.Spring.Board.Project.repository.AuthorityRepository;
import com.example.Spring.Board.Project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArticleRepository articleRepository;
    private final AuthorityRepository authorityRepository;

    public MemberDto mapToMemberDto(Member member) {
        MemberDto memberDto=MemberDto.builder().id(member.getId()).name(member.getName()).email(member.getEmail()).build();
        return memberDto;
    }

    public MemberDto findById(Long id){
        Member member=memberRepository.findById(id).orElse(null);
        return mapToMemberDto(member);
    }

    public MemberDto create(MemberForm memberForm) {
        Member member=Member.builder().name(memberForm.getName()).email(memberForm.getEmail()).password(passwordEncoder.encode(memberForm.getPassword())).build();
        memberRepository.save(member);
        return mapToMemberDto(member);
    }

    //이메일(아이디)가 이미 존재하는지 확인
//    Optional 스트림 변환의 정확한 의미고객님이 설명하신 흐름은 완벽합니다.Optional<Member>에 결과가 없을수도 있따가 null일수도 있다는 아닌건가? 쨋든 없을수도있따, 이것을 Optional<MemberDto>로 바꾸는데 없으면 없는거로 바꾼다?맞습니다! 이 패턴을 사용하면 다음과 같은 로직이 간결하게 처리됩니다.Optional<Member> 상태스트림 파이프라인최종 Optional<MemberDto> 결과값이 존재 (있다)$\text{Stream<Member>} \rightarrow \text{map()} \rightarrow \text{findFirst()}$Optional.of(MemberDto) (있는 것으로 바꾼다)값이 없음 (없다)$\text{Stream<Member>} \rightarrow \text{map() 실행 안 됨} \rightarrow \text{findFirst()}$Optional.empty() (없는 것으로 바꾼다)
//  if (member.isPresent()) {
//        MemberDto dto = mapToMemberDto(member.get());
//        return Optional.of(dto);
//    } else {
//        return Optional.empty();
//    }
    //위 코드와 같다.

    public Optional<MemberDto> findByEmail(String email) {
        Optional<Member> member= memberRepository.findByEmail(email);
        return member.stream().map(i->mapToMemberDto(i)).findFirst();
    }



    //Member의 id와  체크할 비밀번호를 보내면 비밀번호 비교하기
    public boolean checkPassword(Long id,String password){
       return  passwordEncoder.matches(password,memberRepository.findById(id).orElseThrow().getPassword());
    }

    public void updatePassword(Long id,String password){
        Member member=memberRepository.findById(id).orElseThrow();
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }


    public Page<MemberDto> findAll(Pageable pageable){
        Page<Member> member=memberRepository.findAll(pageable);
        return member.map(i->mapToMemberDto(i));
    }


    public MemberDto update(MemberForm memberForm) {
        Member member=memberRepository.findById(memberForm.getId()).orElseThrow();
        member.setEmail(memberForm.getEmail());
        member.setName(memberForm.getName());
        memberRepository.save(member);
        return mapToMemberDto(member);
    }

    @Transactional
    public void delete(Long id)
    {
        //맴버를 삭제하기 전에 맴버가 작성한 글을 삭제한다.
        Member member=memberRepository.findById(id).orElseThrow();
        articleRepository.deleteAllByMember(member);
        memberRepository.deleteById(id);
    }
}
