package com.wesleyserrano.authors;

public record AuthorRecord(Author author) {
    public String id() {return author().getId();}
    public String firstName() {return author().getFirstName();}
    public String lastName() {return author().getLastName();}
}
