package com.wesleyserrano.books_catalog_service.books;

public record IBook(String name,
            int pageCount,
            String authorId) {
}
