package dev.ivanchristianjayfuncion.monolith.blog.service;

import dev.ivanchristianjayfuncion.monolith.blog.dto.PostRequestDto;
import dev.ivanchristianjayfuncion.monolith.blog.entity.Post;
import dev.ivanchristianjayfuncion.monolith.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts(String term) {
        if(term != null && !term.trim().isEmpty()) {
            return postRepository.searchByTerm(term.trim());
        }
        return postRepository.findAll();
    }

    // what is Optional ?
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(PostRequestDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(dto.getCategory());
        post.setTags(dto.getTags());
        return postRepository.save(post);
    }

    public Optional<Post> updatePost(Long id, PostRequestDto dto) {
        return postRepository.findById(id).map(existingPost -> {if (dto.getTitle() != null) existingPost.setTitle(dto.getTitle());
            if (dto.getContent() != null) existingPost.setContent(dto.getContent());
            if (dto.getCategory() != null) existingPost.setCategory(dto.getCategory());
            if (dto.getTags() != null) existingPost.setTags(dto.getTags());
            return postRepository.save(existingPost);
        });
    }

    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
