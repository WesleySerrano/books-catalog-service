package com.wesleyserrano.books_catalog_service.books;

import com.wesleyserrano.books_catalog_service.authors.Author;

public record BookRecord(Book bookEntity) {
    public String id() { return bookEntity.getId(); }
    public String name() { return bookEntity.getName(); }
    public long pageCount() { return bookEntity.getPageCount(); }
    public Author author() {return bookEntity.getAuthor();}
}
