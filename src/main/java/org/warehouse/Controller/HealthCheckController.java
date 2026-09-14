package org.warehouse.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.warehouse.Service.NotificationService;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    public final NotificationService notificationService;

    public HealthCheckController(NotificationService notificationService, NotificationService notificationService1) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public Mono<Map<String, String>> health() {
        return Mono.just(Map.of("status", "UP"));
    }

    @GetMapping("/notification-service")
    public Mono<Map<String, Boolean>> notificationService() {
        return notificationService.checkHealth().map(reachable -> Map.of("reachable", reachable));
    }
}