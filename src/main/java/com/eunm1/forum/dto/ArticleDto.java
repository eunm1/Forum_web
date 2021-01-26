package com.eunm1.forum.dto;

import com.eunm1.forum.db.entity.Article;
import com.eunm1.forum.db.entity.File;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class ArticleDto { //Controller와 Service사이에 데이터를 주고받는다
    private Long id;
    private String title;
    private String body;
    private String mid; //member클래스의 id를 외부참조해야한다.
    private int hit;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long fileId;

    public Article toEntity(){
        Article article = Article.builder()
                .id(id)
                .title(title)
                .body(body)
                .mid(Long.valueOf(mid))
                .hit(hit)
                .fileId(fileId)
                .build();

        return article;
    }

    @Builder
    public ArticleDto(Long id, String title, String body, String mid, int hit, LocalDateTime createdDate, LocalDateTime modifiedDate, Long fileId){
        this.id = id;
        this.title = title;
        this.body = body;
        this.mid = mid;
        this.hit = hit;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.fileId = fileId;
    }
}
