package com.swiftfingers.mockitotests.service;

import com.swiftfingers.mockitotests.entity.Post;
import com.swiftfingers.mockitotests.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public Post create(Post post){
        return postRepository.save(post);
    }

    public List<Post> list(Long topicId){
        return postRepository.findAllByTopic_TopicId(topicId);
    }

    public Post get(Long postId){
        return postRepository.findById(postId).orElseThrow();
    }

    public Post update(Post post){
        LocalDateTime created = postRepository.findById(post.getPostId())
                .orElseThrow()
                .getCreated();
        post.setCreated(created);
        return postRepository.save(post);
    }

    public void delete(Long postId){
        if(!postRepository.existsById(postId)) throw new NoSuchElementException();
        postRepository.deleteById(postId);
    }
}