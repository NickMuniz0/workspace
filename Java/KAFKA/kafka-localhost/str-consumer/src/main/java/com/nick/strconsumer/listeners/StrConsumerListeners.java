package com.nick.strconsumer.listeners;

import com.nick.strconsumer.custom.StrConsumerCustomListener;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class StrConsumerListeners {
//    @KafkaListener(groupId = "group-1", topicPartitions = @TopicPartition(topic="str-topic", partitions = {"0"}), containerFactory = "strContainerFactory")
//    public void create(String message){
//        log.info("CREATE ::: Receive message {}",message);
//    }

    @StrConsumerCustomListener(groupId="group-1")
    public void create(String message){
        log.info("CREATE ::: Receive message {}",message);
    }

    @StrConsumerCustomListener(groupId="group-1")
    public void log(String message){
        log.info("LOG ::: Receive message {}",message);
    }

    @SneakyThrows //    public void create(String message) throws Exception{
    @StrConsumerCustomListener(groupId="group-2")
    public void history(String message){
        log.info("History ::: Receive message {}",message);
        throw new IllegalArgumentException("Exception...");

    }

//    @KafkaListener(groupId = "group-2",topics="str-topic", containerFactory = "validMessageContainerFactory")
//    public void email(String message){
//        log.info("Email ::: Receive message {}",message);
//
//    }
}
