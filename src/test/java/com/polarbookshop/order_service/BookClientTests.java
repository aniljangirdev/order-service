package com.polarbookshop.order_service;

import com.polarbookshop.order_service.book.Book;
import com.polarbookshop.order_service.book.BookClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

public class BookClientTests {

    private MockWebServer mockWebServer;
    private BookClient bookClient;

    @BeforeEach
    void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();

        var webCLient = WebClient
                .builder()
                .baseUrl(mockWebServer.url("/").url().toString())
                .build();
        this.bookClient = new BookClient(webCLient);
    }

    @AfterEach
    void clean() throws IOException {
        this.mockWebServer.shutdown();
    }


    @Test
    void verifyBookByIsbnWhileInsertNewBooks() {
        // GIVEN
        var bookIsbn = "1234567893";
        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                          "isbn": %s,
                          "title": "Title",
                          "author": "Author",
                          "price": 9.90,
                          "publisher": "Polarsophia"
                        }
                        """.formatted(bookIsbn));
        mockWebServer.enqueue(mockResponse);

        // WHEN
        Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);

        //THEN
        StepVerifier.create(book)
                .expectNextMatches(b -> b.isbn().equals(bookIsbn))
                .verifyComplete();
    }
}
