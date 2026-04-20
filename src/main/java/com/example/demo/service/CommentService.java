package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RedisService redisService;

    public Comment createComment(Comment comment){
        if (comment.getDepthLevel() > 20) {
            throw new RuntimeException("Max depth exceeded");
        }

        boolean isBot = comment.getAuthorId() < 0;
        Long postId = comment.getPostId();

        if (isBot) {
            boolean allowed = redisService.canBotReply(postId);
            if (!allowed) {
                throw new RuntimeException("Bot reply limit exceeded");
            }

            boolean cooldownOk = redisService.checkCooldown(comment.getAuthorId(), postId);
            if (!cooldownOk) {
                throw new RuntimeException("Cooldown active for this bot");
            }
        }

        redisService.handleNotification(comment.getPostId(), "New interaction");

        comment.setCreatedAt(LocalDateTime.now());
        Comment saved = commentRepository.save(comment);

        if (isBot) {
            redisService.updateViralityScore(postId, "BOT");
        } else {
            redisService.updateViralityScore(postId, "COMMENT");
        }

        return saved;
    }
}
