package com.example.sbb.post;

import com.example.sbb.user.Member;
import com.example.sbb.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 댓글 추가
    public Comment addComment(Long postId, String username, String content) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new IllegalArgumentException("회원 정보를 찾을 수 없습니다.");
        }

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setMember(member);
        comment.setContent(content);

        return commentRepository.save(comment);
    }
}
