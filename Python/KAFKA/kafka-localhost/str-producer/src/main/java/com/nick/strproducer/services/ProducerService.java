package com.nick.strproducer.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Log4j2
@RequiredArgsConstructor
@Service
public class ProducerService {


    private final KafkaTemplate<String, String> kafkaTemplate;


    public void send(String message) {
        log.info("send message: " + message);
        kafkaTemplate.send("str-topic", message);
//                .whenComplete(
//                        (result, error) -> {
//                            if (error != null) {
//                                log.error(error);
//                            }else {
//                                log.info(result);
//                            }
//                        }
//                );
    }
}