package com.example.Spring.Board.Project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Article 의 RequestDTO 객체이다.
//이 ArticleForm이 사용되는 경우는 2가지
//1)새로운 글 생성할때
//2)글 수정할때, 수정할때는 ArticleForm에서 id를 넘겨주므로, 1)번에서는 필요없지만
//2)을 위해 id를 넣어둔다.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleForm {

    private Long id;
    @NotBlank(message = "게시글 제목을 입력하세요.")
    private String title;
    @NotBlank(message="게시글 내용을 입력하세요")
    private String description;
}
