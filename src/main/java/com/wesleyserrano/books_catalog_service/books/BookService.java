package com.wesleyserrano.books_catalog_service.books;

import com.wesleyserrano.books_catalog_service.authors.Author;
import com.wesleyserrano.books_catalog_service.authors.AuthorRepository;
import com.wesleyserrano.books_catalog_service.exception.ResourceNotFoundException;
import com.wesleyserrano.books_catalog_service.proto.AddBookGrpc;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public BookRecord getBookById(String id) {
        Book book = this.bookRepository.findById(id).orElse(null);

        if (Objects.isNull(book)) {
            log.error("Book not found for id {}", id);
            throw new ResourceNotFoundException("Book not found");
        }

        return new BookRecord(book);
    }

    public List<BookRecord> getBooks() {
        return this.bookRepository.findAll().stream().map(BookRecord::new).toList();
    }

    @Transactional
    public Book addBook(AddBookGrpc addBookGrpc) {
        log.info("Add book");
        Author author = authorRepository.findById(addBookGrpc.getAuthorId()).orElse(null);

        if (Objects.isNull(author)) {
            log.error("No author found for id {}", addBookGrpc.getAuthorId());
            throw new ResourceNotFoundException("Author not found");
        }

        Book book = new Book(UUID.randomUUID().toString(), addBookGrpc.getName(), addBookGrpc.getPageCount());
        book.setAuthor(author);

        return bookRepository.save(book);
    }
}
