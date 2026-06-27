package com.wesleyserrano.books_catalog_service.books;

import com.google.protobuf.Empty;
import com.wesleyserrano.books_catalog_service.config.KeycloakGrpcInterceptor;
import com.wesleyserrano.books_catalog_service.mapper.BookGrpcMapper;
import com.wesleyserrano.books_catalog_service.proto.AddBookGrpc;
import com.wesleyserrano.books_catalog_service.proto.BookGrpc;
import com.wesleyserrano.books_catalog_service.proto.BookGrpcServiceGrpc;
import com.wesleyserrano.books_catalog_service.proto.BookSearch;
import com.wesleyserrano.books_catalog_service.proto.ListBookGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService(interceptors = {KeycloakGrpcInterceptor.class})
public class BookGrpcService extends BookGrpcServiceGrpc.BookGrpcServiceImplBase {
    private final BookService bookService;

    public BookGrpcService(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void requestBookById(
            BookSearch bookSearch,
            StreamObserver<BookGrpc> responseObserver) {
        BookRecord bookRecord = bookService.getBookById(bookSearch.getId());

        BookGrpc response = BookGrpcMapper.bookMapper(bookRecord);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void requestBooks(Empty emptyInput, StreamObserver<ListBookGrpc> listBookGrpcStreamObserver) {
        List<BookRecord> bookRecords = this.bookService.getBooks();
        ListBookGrpc response = ListBookGrpc.newBuilder()
                .addAllBooks(bookRecords.stream().map(BookGrpcMapper::bookMapper).toList())
                .build();

        listBookGrpcStreamObserver.onNext(response);
        listBookGrpcStreamObserver.onCompleted();
    }

    @Override
    public void addBook(
            AddBookGrpc addBookGrpc,
            StreamObserver<BookGrpc> responseObserver) {
        Book book = this.bookService.addBook(addBookGrpc);

        BookGrpc response = BookGrpcMapper.bookMapper(book);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
