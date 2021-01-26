package com.eunm1.forum.db.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(nullable = false)
    private Long mid; //member클래스의 id를 외부참조해야한다.

    @Column(nullable = false)
    private int hit;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

/*    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotFound(action = NotFoundAction.IGNORE)
    private File file;*/

    @Column
    private Long fileId;

    @Builder // 객체에 builder를 이용한다.
    public Article(Long id, String title, String body, Long mid, int hit, Long fileId){
        this.id = id;
        this.title = title;
        this.body = body;
        this.mid = mid;
        this.hit = hit;
        //this.file = file;
        this.fileId = fileId;
    }
}

/*
@Data
(아래의 모든 어노테이션을 한번에 설정할 수 있다)
-@toString - 해당 메소드의 모든 필드를 출력하는 toString 메소드를 생성합니다.
-@EqualsAndHashCode - hashcode와 equals 메소드를 생성합니다.
-@Getter / @Setter - 말 그대로 getter함수와 Setter 함수를 생성합니다.
-@NoArgsConstructor - 파라미터를 요구하지 않는 생성자를 생성합니다.
(access =AccessLevel.PROTECTED) 를 추가하게 될 경우 기본생성자의 접근 권한을 protected로 제한하게 됩니다.
-@RequiredArgsConstructor - 파라미터를 요구하는 생성자를 생성합니다.
-@AllArgsConstructor - 모든 인자를 가진 생성자를 생성합니다.

@Builder
Builder 패턴을 사용하면 다음과 같은 장점이 있습니다.

인자가 많을 경우 쉽고 안전하게 객체를 생성할 수 있습니다.
인자의 순서와 상관없이 객체를 생성할 수 있습니다.
적절한 책임을 이름에 부여하여 가독성을 높일 수 있습니다.
*/