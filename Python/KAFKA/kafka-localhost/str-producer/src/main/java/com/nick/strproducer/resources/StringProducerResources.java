package com.nick.strproducer.resources;

import com.nick.strproducer.services.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/producer")
public class StringProducerResources {

    private final ProducerService producerService;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody String message) {
        producerService.send(message);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
}
