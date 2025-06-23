package com.tenco.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardPersistRepository boardPersistRepository;


    // 주소 설계 : /board/{{board.id}}/delete

    @PostMapping("/board/{id}/delete-form")
    public String delete(@PathVariable(name = "id") Long id) {
        boardPersistRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/board/save-form")
    public String saveForm(){
        return "board/save-form";
    }

    @PostMapping("/board/save")
    public String saveForm(BoardRequest.SaveDTO reqDTO){
        // HTTP 요청 본문 : title=값&content=값&username=값
        // form 태그의 MIME 타입 ( application/x-www-form-urlencoded )

        // Board board = new Board(reqDTO.getTitle(),reqDTO.getContent(),reqDTO.getUsername());
        Board board = reqDTO.toEntity();
        boardPersistRepository.save(board);

        return "redirect:/";
    }

    @GetMapping({"/","/index"})
    public String index(Model model){
        List<Board> boards = boardPersistRepository.findAll();
        model.addAttribute("boardList",boards);
        return "index";
    }
    
    // 게시글 상세보기
    /**
     * 주소설계 : http://localhost:8080/board/{id}/update-form
     * @return update-form.mustache
     * @param : id (board pk)
     */

    @PostMapping("/board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Long id, BoardRequest.UpdateDTO reqDTO) {

        // 트랜잭션
        // 수정 -- select - 값을 확인해서 - 데이터를 수정 --> update
        // JPA 영속성 컨텍스트 활용
        boardPersistRepository.update(id, reqDTO);
        // 수정 전략을 더티 체킹을 활용
        // 장점
        // 1. UPDATE 쿼리 자동 생성
        // 2. 변경된 필드만 업데이트 (성능 최적화)
        // 3. 영속성 컨텍스트에 일관성 유지
        // 4. 1차 캐시 자동 갱신

        return "redirect:/";
    }

    @GetMapping("/board/{id}/board-update")
    public String updateForm(@PathVariable(name = "id") Long id,
            HttpServletRequest request) {


        // select * from board_tb where id = 4;
        Board board = boardPersistRepository.findById(id);
        // 머스테치 파일에 조회된 데이터를 바인딩 처리
        request.setAttribute("board",board);



        // 성공 시 리스트 화면으로 리다이렉트 처리
        return "board/board-update";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id")Long id,HttpServletRequest request){
        Board board = boardPersistRepository.findById(id);
        request.setAttribute("board",board);
        return "board/detail";
    }

}
