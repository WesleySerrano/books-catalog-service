package com.wesleyserrano.authors;

import com.wesleyserrano.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorRecord addAuthor(IAuthor iAuthor) {
        Author author = new Author(UUID.randomUUID().toString(), iAuthor.firstName(), iAuthor.lastName());
        return new AuthorRecord(authorRepository.save(author));
    }

    public List<AuthorRecord> getAuthors() {
        return this.authorRepository.findAll().stream().map(AuthorRecord::new).toList();
    }

    public AuthorRecord getAuthorById(String id) {
        Author author = authorRepository.findById(id).orElse(null);

        if (Objects.isNull(author)) {
            log.error("Author not found for id {}", id);
            throw new ResourceNotFoundException("Author not found");
        }

        return new AuthorRecord(author);
    }
}
