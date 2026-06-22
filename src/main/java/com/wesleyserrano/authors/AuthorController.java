package com.wesleyserrano.authors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @QueryMapping
    public AuthorRecord getAuthorById(@Argument String id) {
        return this.authorService.getAuthorById(id);
    }

    @QueryMapping
    public List<AuthorRecord> getAuthors() {
        return this.authorService.getAuthors();
    }

    @MutationMapping
    public AuthorRecord addAuthor(@Argument IAuthor iAuthor) {
        return this.authorService.addAuthor(iAuthor);
    }
}
