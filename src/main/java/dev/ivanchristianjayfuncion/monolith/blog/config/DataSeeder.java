package dev.ivanchristianjayfuncion.monolith.blog.config;

import dev.ivanchristianjayfuncion.monolith.blog.entity.Post;
import dev.ivanchristianjayfuncion.monolith.blog.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(PostRepository postRepository) {
        return args -> {
            // Will seed only if table has no record
            if (postRepository.count() == 0) {
                Post post1 = new Post();
                post1.setTitle("My First Blog Post");
                post1.setContent("This is the content of my first blog post seeded automatically.");
                post1.setCategory("Technology");
                post1.setTags(List.of("Tech, Programming"));
                postRepository.save(post1);

                Post post2 = new Post();
                post2.setTitle("Understanding Clean Architecture");
                post2.setContent("Clean architecture helps keep systems maintainable and scalable over time.");
                post2.setCategory("Architecture");
                post2.setTags(List.of("Java, SpringBoot, Backend"));
                postRepository.save(post2);

                System.out.println(">>> Seeded initial blog posts into the database!");
            }
        };
    }
}
