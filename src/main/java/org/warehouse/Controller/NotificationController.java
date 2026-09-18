package org.warehouse.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.warehouse.Kafka.Dto.ItemsMovedKafkaMessage;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping("/items-moved")
    public ResponseEntity<Void> receivedMovedNotification(@RequestBody ItemsMovedKafkaMessage message) {
        logger.info("Notification Received");
        return ResponseEntity.ok().build();
    }
}
