package org.warehouse.Kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;
import org.warehouse.Repository.ItemRepository;
import org.warehouse.Repository.WarehouseRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ItemHistoryService {
    private final ConsumerFactory<String, ItemsMovedKafkaMessage> consumerFactory;
    private final String topicName;

    public ItemHistoryService(ConsumerFactory<String, ItemsMovedKafkaMessage> consumerFactory, @Value("${app.kafka.topics.items-moved}") String topicName) {
        this.consumerFactory = consumerFactory;
        this.topicName = topicName;
    }

    public List<ItemsMovedKafkaMessage> getMoveItemHistoryByWarehouseId(int warehouseId) {
        List<ItemsMovedKafkaMessage> result=new ArrayList<>();
        String groupId="history-consumer-"+ UUID.randomUUID();

        try (Consumer<String, ItemsMovedKafkaMessage> consumer = consumerFactory.createConsumer(groupId, null)) {
            // assign ke partisi 0 topic warehouse.items-moved
            TopicPartition partition = new TopicPartition(topicName, 0);
            consumer.assign(Collections.singletonList(partition));

            // cari ke offset paling awal
            consumer.seekToBeginning(Collections.singletonList(partition));

            // fetch timeout
            ConsumerRecords<String, ItemsMovedKafkaMessage> records = consumer.poll(Duration.ofSeconds(2));

            // filter message sesuai warehouseId
            for (ConsumerRecord<String, ItemsMovedKafkaMessage> record : records) {
                ItemsMovedKafkaMessage msg = record.value();
                if (msg != null && msg.warehouseId() != null && msg.warehouseId() == warehouseId) {
                    result.add(msg);
                }
            }
        }

        return result;
    }
}
