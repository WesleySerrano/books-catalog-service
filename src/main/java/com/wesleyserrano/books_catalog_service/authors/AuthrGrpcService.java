package com.wesleyserrano.books_catalog_service.authors;

import com.google.protobuf.Empty;
import com.wesleyserrano.books_catalog_service.config.KeycloakGrpcInterceptor;
import com.wesleyserrano.books_catalog_service.mapper.AuthorGrpcMapper;
import com.wesleyserrano.books_catalog_service.mapper.BookGrpcMapper;
import com.wesleyserrano.books_catalog_service.proto.AddAuthor;
import com.wesleyserrano.books_catalog_service.proto.AddBookGrpc;
import com.wesleyserrano.books_catalog_service.proto.AuthorGrpc;
import com.wesleyserrano.books_catalog_service.proto.AuthorGrpcServiceGrpc;
import com.wesleyserrano.books_catalog_service.proto.AuthorSearch;
import com.wesleyserrano.books_catalog_service.proto.BookGrpc;
import com.wesleyserrano.books_catalog_service.proto.BookSearch;
import com.wesleyserrano.books_catalog_service.proto.ListAuthorGrpc;
import com.wesleyserrano.books_catalog_service.proto.ListBookGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService(interceptors = {KeycloakGrpcInterceptor.class})
public class AuthrGrpcService extends AuthorGrpcServiceGrpc.AuthorGrpcServiceImplBase {
    private final AuthorService authorService;

    public AuthrGrpcService(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    public void requestAuthor(
            AuthorSearch authorSearch,
            StreamObserver<AuthorGrpc> responseObserver) {
        AuthorRecord authorRecord = this.authorService.getAuthorById(authorSearch.getId());

        AuthorGrpc response = AuthorGrpcMapper.authorMapper(authorRecord);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void requestAuthors(Empty emptyInput, StreamObserver<ListAuthorGrpc> listAuthorGrpcStreamObserver) {
        List<AuthorRecord> authors = this.authorService.getAuthors();

        ListAuthorGrpc response = ListAuthorGrpc.newBuilder()
                .addAllAuthors(authors.stream().map(AuthorGrpcMapper::authorMapper).toList())
                .build();

        listAuthorGrpcStreamObserver.onNext(response);
        listAuthorGrpcStreamObserver.onCompleted();

    }

    @Override
    public void addAuthor(
            AddAuthor addAuthorGrpc,
            StreamObserver<AuthorGrpc> responseObserver) {
        AuthorRecord authorRecord = this.authorService.addAuthor(addAuthorGrpc);

        AuthorGrpc response = AuthorGrpcMapper.authorMapper(authorRecord);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
