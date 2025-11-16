package com.example.sbb.post;

import com.example.sbb.user.Member;
import com.example.sbb.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 글 저장 메서드
    public void create(String title, String content, String category, String username) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);
        post.setCreatedAt(LocalDateTime.now());

        // 작성자(Member) 찾아서 넣어주기
        // (로그인을 안 했거나 유저가 없으면 작성자 없이 저장됨)
        if (username != null && !username.isEmpty()) {
            Member member = memberRepository.findByUsername(username);
            if (member != null) {
                post.setMember(member);
            }
        }
        
        this.postRepository.save(post);
    }
}