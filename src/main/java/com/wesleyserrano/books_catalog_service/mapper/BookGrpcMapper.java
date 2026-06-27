package com.wesleyserrano.books_catalog_service.mapper;

import com.wesleyserrano.books_catalog_service.books.Book;
import com.wesleyserrano.books_catalog_service.books.BookRecord;
import com.wesleyserrano.books_catalog_service.proto.BookGrpc;
import org.springframework.stereotype.Component;

@Component
public class BookGrpcMapper {
    public static BookGrpc bookMapper(BookRecord book) {
        return BookGrpc.newBuilder()
                .setId(book.id())
                .setName(book.name())
                .setPageCount(book.pageCount())
                .setAuthor(AuthorGrpcMapper.authorMapper(book.author()))
                .build();
    }

    public static BookGrpc bookMapper(Book book) {
        return BookGrpc.newBuilder()
                .setId(book.getId())
                .setName(book.getName())
                .setPageCount(book.getPageCount())
                .setAuthor(AuthorGrpcMapper.authorMapper(book.getAuthor()))
                .build();
    }
}
