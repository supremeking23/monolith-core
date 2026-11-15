package dev.ivanchristianjayfuncion.monolith.blog.controller;

import dev.ivanchristianjayfuncion.monolith.blog.dto.PostRequestDto;
import dev.ivanchristianjayfuncion.monolith.blog.entity.Post;
import dev.ivanchristianjayfuncion.monolith.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>>getAllPosts(@RequestParam(required = false) String term) {
        List<Post> posts = postService.getAllPosts(term);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        // what does :: means ? -- I feel l
        return postService.getPostById(id).map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody PostRequestDto dto) {
        Post createdPost = postService.createPost(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @Valid @RequestBody PostRequestDto dto) {
        return postService.updatePost(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boolean deleted = postService.deletePost(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

