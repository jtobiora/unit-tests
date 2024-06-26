package com.swiftfingers.mockitotests.controller;

import com.swiftfingers.mockitotests.entity.Post;
import com.swiftfingers.mockitotests.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping(value = "/topics/{topicId}/posts")
    public List<Post> list(@PathVariable("topicId") Long topicId){
        return postService.list(topicId);
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post){
        try{
            return postService.create(post);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @GetMapping(value = "/posts/{postId}")
    public Post get(@PathVariable("postId") Long postId){
        try{
            return postService.get(postId);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/posts")
    public Post update(@RequestBody Post post){
        try{
            return postService.update(post);
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping(value = "/posts/{postId}")
    public void delete(@PathVariable("postId") Long postId){
        try{
            postService.delete(postId);
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
