package org.warehouse.Kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;
import org.warehouse.Dto.ItemMoveHistoryPagedResponse;

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

    public ItemMoveHistoryPagedResponse<ItemsMovedKafkaMessage> getMoveItemHistoryByWarehouseId(int warehouseId, Pageable pageable) {
        List<ItemsMovedKafkaMessage> allFilteredMessages = new ArrayList<>();
        String groupId = "history-consumer-" + UUID.randomUUID();

        try (Consumer<String, ItemsMovedKafkaMessage> consumer = consumerFactory.createConsumer(groupId, null)) {

            // ambil list semua partisi secara dinamis
            List<TopicPartition> partitions = new ArrayList<>();
            List<PartitionInfo> partitionInfos = consumer.partitionsFor(topicName);
            if (partitionInfos != null) {
                for (PartitionInfo info : partitionInfos) {
                    partitions.add(new TopicPartition(topicName, info.partition()));
                }
            }

            if (!partitions.isEmpty()) {
                // assign dan seek ke offset paling awal untuk semua partisi
                consumer.assign(partitions);
                consumer.seekToBeginning(partitions);

                // perulangan (looping) sampai data di Kafka benar-benar habis
                boolean keepPolling = true;
                int emptyPollCount = 0; // untuk cek jika Kafka sudah tidak punya data lagi

                while (keepPolling) {
                    ConsumerRecords<String, ItemsMovedKafkaMessage> records = consumer.poll(Duration.ofMillis(500));

                    if (records.isEmpty()) {
                        emptyPollCount++;
                        // jika 2 kali poll berturut-turut kosong, artinya data sudah habis difetch
                        if (emptyPollCount >= 2) {
                            keepPolling = false;
                        }
                        continue;
                    }

                    // reset hitungan jika di poll ini kita masih dapat data
                    emptyPollCount = 0;

                    // filter message sesuai warehouseId
                    for (ConsumerRecord<String, ItemsMovedKafkaMessage> record : records) {
                        ItemsMovedKafkaMessage msg = record.value();
                        if (msg != null && msg.warehouseId() != null && msg.warehouseId() == warehouseId) {
                            allFilteredMessages.add(msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // logging error
            System.err.println("Gagal fetch data dari Kafka: " + e.getMessage());
        }

        int totalElements = allFilteredMessages.size();
        int fromIndex = (int) pageable.getOffset();
        int toIndex = Math.min(fromIndex + pageable.getPageSize(), totalElements);

        List<ItemsMovedKafkaMessage> pageContent;
        if (fromIndex > totalElements) {
            pageContent = Collections.emptyList();
        } else {
            pageContent = allFilteredMessages.subList(fromIndex, toIndex);
        }

        Page<ItemsMovedKafkaMessage> page = new PageImpl<>(pageContent, pageable, totalElements);

        return new ItemMoveHistoryPagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
