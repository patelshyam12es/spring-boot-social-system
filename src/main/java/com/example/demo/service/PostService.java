package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post){
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }
}
