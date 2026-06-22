package com.wesleyserrano.books;

import com.wesleyserrano.authors.Author;

public record BookRecord(Book bookEntity) {
    public String id() { return bookEntity.getId(); }
    public String name() { return bookEntity.getName(); }
    public long pageCount() { return bookEntity.getPageCount(); }
    public Author author() {return bookEntity.getAuthor();}
}
