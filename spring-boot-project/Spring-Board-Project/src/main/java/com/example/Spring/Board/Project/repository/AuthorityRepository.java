package com.example.Spring.Board.Project.repository;

import com.example.Spring.Board.Project.model.Authority;
import com.example.Spring.Board.Project.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository  extends JpaRepository<Authority, Long> {
    List<Authority> findByMember(Member member);
}
