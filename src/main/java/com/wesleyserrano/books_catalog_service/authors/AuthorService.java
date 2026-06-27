package com.wesleyserrano.books_catalog_service.authors;

import com.wesleyserrano.books_catalog_service.exception.ResourceNotFoundException;
import com.wesleyserrano.books_catalog_service.proto.AddAuthor;
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

    public AuthorRecord addAuthor(AddAuthor addAuthor) {
        Author author = new Author(UUID.randomUUID().toString(), addAuthor.getFirstName(), addAuthor.getLastName());
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
