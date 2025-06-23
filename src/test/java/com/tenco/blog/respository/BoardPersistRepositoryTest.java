package com.tenco.blog.respository;

import com.tenco.blog.board.Board;
import com.tenco.blog.board.BoardPersistRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(BoardPersistRepository.class)
@DataJpaTest
public class BoardPersistRepositoryTest {

    @Autowired
    private BoardPersistRepository boardPersistRepository;

    @Test
    public void deleteById_test() {
        // given
        Long id = 1L;


        // when
        // 삭제할 게시글이 실제로 존재하는지 확인
        Board targetBoard = boardPersistRepository.findById(id);
        Assertions.assertThat(targetBoard).isNotNull();

        // 영속성 컨텍스트에서 삭제 실행
        boardPersistRepository.deleteById(id);

        // then
        List<Board> afterDeleteBoardList = boardPersistRepository.findAll();
        Assertions.assertThat(afterDeleteBoardList.size()).isEqualTo(3);
    }

    @Test
    public void save_test(){
        // given
        Board board = new Board("1","2","3");
        Assertions.assertThat(board.getId()).isNull();
        System.out.println("db에 저장 전 board : " + board);
        
        // when
        // 영속성 컨텍스를 통한 엔티티 저장
        Board saveBoard = boardPersistRepository.save(board);
        // then
        // 1. 저장후에 자동 생성된 ID 값 확인
        Assertions.assertThat(saveBoard.getId()).isNotNull();
        Assertions.assertThat(saveBoard.getId()).isGreaterThan(0);
        
        // 2. 내용 일치 여부 확인
        Assertions.assertThat(saveBoard.getTitle()).isEqualTo("1");
        
        // 3. JPA 가 자동으로 생성된 생성 시간 확인
        Assertions.assertThat(saveBoard.getCreatedAt()).isNotNull();

        // 4. 원본 객체 - board, 영속성 컨텍스트에 저장한 - savedBoard
        Assertions.assertThat(board).isSameAs(saveBoard);

    }

    @Test
    public void findAll_test() {

        // given

        // when
        List<Board> boardList = boardPersistRepository.findAll();

        // then
        System.out.println("size 테스트 : " + boardList.size() );
        System.out.println("첫번째 게시글 제목 확인 : " + boardList.get(0).getTitle());

        // 네이티브 쿼리를 사용한다는 것은 영속 컨텍스 를 우회 해서 일 처리
        // JPQL 바로 영속성 컨텍스를 우회하지만 조회된 이후에는 영속성 상태가 된다
        for(Board board : boardList){
            Assertions.assertThat(board.getId()).isNotNull();
        }

    }

}
