package com.example.sbb.post;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequestMapping("/post") // 이 컨트롤러는 기본적으로 /post 로 시작하는 주소를 처리함
@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostRepository postRepository;

    // 주소: http://localhost:8080/post/list
    @GetMapping("/list")
    public String list(Model model) {
        // 1. DB에서 모든 글을 가져온다.
        List<Post> postList = this.postRepository.findAll();
        
        // 2. 가져온 글 뭉치(postList)를 "postList"라는 이름으로 HTML에 전달한다.
        model.addAttribute("postList", postList);
        
        // 3. "post_list.html" 파일을 찾아서 보여준다.
        return "post_list";
    }
}
