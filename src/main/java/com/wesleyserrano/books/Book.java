package com.wesleyserrano.books;

import com.wesleyserrano.authors.Author;
import com.wesleyserrano.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Book extends BaseEntity {
    private String name;
    private long pageCount;

    @ManyToOne
    private Author author;

    public Book(String id, String name, int pageCount) {
        this.id = id;
        this.name = name;
        this.pageCount = pageCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return pageCount == book.pageCount && Objects.equals(id, book.id) && Objects.equals(name, book.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pageCount);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pageCount=" + pageCount +
                '}';
    }
}
