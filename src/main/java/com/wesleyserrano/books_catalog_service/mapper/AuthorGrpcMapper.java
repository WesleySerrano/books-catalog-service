package com.wesleyserrano.books_catalog_service.mapper;

import com.wesleyserrano.books_catalog_service.authors.Author;
import com.wesleyserrano.books_catalog_service.authors.AuthorRecord;
import com.wesleyserrano.books_catalog_service.proto.AuthorGrpc;
import org.springframework.stereotype.Component;

@Component
public class AuthorGrpcMapper {
    public static AuthorGrpc authorMapper(Author author) {
        return AuthorGrpc.newBuilder()
                .setId(author.getId())
                .setFirstName(author.getFirstName())
                .setLastName(author.getLastName())
                .build();
    }

    public static AuthorGrpc authorMapper(AuthorRecord author) {
        return AuthorGrpc.newBuilder()
                .setId(author.id())
                .setFirstName(author.firstName())
                .setLastName(author.lastName())
                .build();
    }
}
