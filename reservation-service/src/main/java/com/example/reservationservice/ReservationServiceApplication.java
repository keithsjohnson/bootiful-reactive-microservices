package com.example.reservationservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootApplication
public class ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
/*


    @Bean
    @RefreshScope
    RouterFunction<?> routes(@Value("${message}") String msg) {
        return RouterFunctions.route(RequestPredicates.GET("/message"),
                req -> ServerResponse.ok().body(Mono.just(msg), String.class));
    }
*/

    @Bean
    ApplicationRunner init(ReservationRepository rr) {
        return args ->
                rr.deleteAll()
                        .thenMany(Flux.just("A", "B", "C", "D")
                                .map(x -> (new Reservation(null, x)))
                                .flatMap(rr::save))
                        .thenMany(rr.findAll())
                        .subscribe(x -> log.info(x.toString()));
    }
}
/*
@RestController
class ReservationRestController {

    private final ReservationRepository reservationRepository;

    ReservationRestController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reservations")
    Flux<Reservation> reservations() {
        return this.reservationRepository.findAll();
    }
}
*/
interface ReservationRepository extends ReactiveMongoRepository<Reservation, String> {
}

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Reservation {
    @Id
    private String id;
    private String reservationName;
}