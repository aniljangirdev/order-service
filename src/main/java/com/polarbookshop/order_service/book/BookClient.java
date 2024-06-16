package com.polarbookshop.order_service.book;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class BookClient {

    private static final String BOOK_ROOT_API = "books/{isbn}";

    private final WebClient webClient;

    public BookClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Book> getBookByIsbn(String bookIsbn) {
        return this.webClient
                .get()
                .uri(BOOK_ROOT_API, bookIsbn)
                .retrieve()
                .bodyToMono(Book.class)

//                .timeout(Duration.ofMinutes(2))
//                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(2000))
//                        .filter(this::is5xxServerError));

                .timeout(Duration.ofSeconds(2), Mono.empty())
                .onErrorResume(WebClientResponseException.NotFound.class,
                        (exception) -> Mono.empty())

                .retryWhen(Retry.backoff(2, Duration.ofMillis(200)))
                .onErrorResume(Exception.class,
                        exception -> Mono.empty());
    }

    private boolean is5xxServerError(Throwable throwable) {
        return throwable instanceof WebClientResponseException &&
                ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
    }

}
