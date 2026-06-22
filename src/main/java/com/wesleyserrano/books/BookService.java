package com.wesleyserrano.books;

import com.wesleyserrano.authors.Author;
import com.wesleyserrano.authors.AuthorRepository;
import com.wesleyserrano.exception.ResourceNotFoundException;
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
        return this.bookRepository.findAll().stream().map(b -> {
            BookRecord bookRecord = new BookRecord(b);
            return bookRecord;
        }).toList();
    }

    @Transactional
    public Book addBook(IBook iBook) {
        log.info("Add book");
        Author author = authorRepository.findById(iBook.authorId()).orElse(null);

        if (Objects.isNull(author)) {
            log.error("No author found for id {}", iBook.authorId());
            throw new ResourceNotFoundException("Author not found");
        }

        Book book = new Book(UUID.randomUUID().toString(), iBook.name(), iBook.pageCount());
        book.setAuthor(author);

        return bookRepository.save(book);
    }
}
