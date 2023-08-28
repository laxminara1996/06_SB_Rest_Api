package in.laxmi.controller;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import in.laxmi.binding.CustomerEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
public class CustomerEventController {
	@GetMapping(value="/event",produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<CustomerEvent> getEvent() {
		CustomerEvent event = new CustomerEvent("laxmi", new Date());
		return Mono.justOrEmpty(event);
	}
	
	@GetMapping(value="/events",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<CustomerEvent> getEvents(){
		CustomerEvent event = new CustomerEvent("ragh", new Date());
		Stream<CustomerEvent> stream = Stream.generate(() -> event);
		CustomerEvent[] eventArray = {event};
		Flux<CustomerEvent> dataflux = Flux.fromStream(stream);
		Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(3));
		Flux<Tuple2<Long,CustomerEvent>> zip = Flux.zip(intervalFlux, dataflux);
		Flux<CustomerEvent> fluxmap = zip.map(Tuple2::getT2);
		return fluxmap;
		
		
	}
}
