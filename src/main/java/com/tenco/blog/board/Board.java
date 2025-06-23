package com.tenco.blog.board;

import com.tenco.blog.util.MyDateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Data
// @Table : 실제 데이터베이스 테이블 명을 지정할 때 사용
@Table(name = "board_tb") // 데이터베이스에서 테이블 이름을 지정해줌(찾음)
// @Entity : JPA 가 이 클래스를 데이터베이스 테이블과 맵핑하는 객체(엔티티)로 인식
@Entity
@NoArgsConstructor // JPA 에서 엔티티는 기본 생성자가 필요
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String content;
    private String username;
    
    
    // 엔티티가 처음 저장할 때 현재 시간을 자동으로 설정한다
    // pc -> db(날짜 주입)
    // v1 에서는 SQL now()를 직접 사용했지만 JPA 가 자동 처리
    @CreationTimestamp // 하이버 네이트가 제공하는 어노테이션
    private Timestamp createdAt;

    public Board(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;
    }

    // 머스테치에서 포현할 시간을 포맷기능을 (행위) 스르로 만들자
    public String getTime(){
        return MyDateUtil.timestampFormat(createdAt);
    }
}
