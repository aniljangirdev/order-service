package com.polarbookshop.order_service.domain;

import com.polarbookshop.order_service.web.OrderRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Flux<Order> getBooks() {
        return orderService.getAllOrders();
    }

//    @GetMapping("/{isbn}")
//    public ResponseEntity<Book> findBook(@PathVariable("isbn") String isbn){
//        return ResponseEntity.ok(bookService.findBookByIsbn(isbn));
//    }

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Order> createBook(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.submitOrder(orderRequest.isbn(), orderRequest.quantity());
    }
//
//    @DeleteMapping("/{isbn}")
////    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public ResponseEntity<Void> deleteBook(@PathVariable String isbn){
//        bookService.deleteByIsbn(isbn);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping("/{isbn}")
//    public ResponseEntity<Book> editBookDetails( @Valid  @RequestBody Book book, @PathVariable String isbn) {
//        return ResponseEntity.ok(bookService.updateBook(book, isbn));
//    }
}
