package org.warehouse.Service;

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
import org.warehouse.Kafka.Dto.ItemsMovedKafkaMessage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ItemHistoryService {
    private final ConsumerFactory<String, ItemsMovedKafkaMessage> consumerFactory;
    private final String topicName;
    private final int pollTimeoutMs;
    private final int maxEmptyPolls;
    private final int maxRecords;

    public ItemHistoryService(
            ConsumerFactory<String, ItemsMovedKafkaMessage> consumerFactory,
            @Value("${app.kafka.topics.items-moved}") String topicName,
            @Value("${app.kafka.history.poll-timeout-ms:100}") int pollTimeoutMs,
            @Value("${app.kafka.history.max-empty-polls:1}") int maxEmptyPolls,
            @Value("${app.kafka.history.max-records:500}") int maxRecords
    ) {
        this.consumerFactory = consumerFactory;
        this.topicName = topicName;
        this.pollTimeoutMs = pollTimeoutMs;
        this.maxEmptyPolls = maxEmptyPolls;
        this.maxRecords = maxRecords;
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

            if (partitions.isEmpty()) {
                Page<ItemsMovedKafkaMessage> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
                return new ItemMoveHistoryPagedResponse<>(
                        emptyPage.getContent(),
                        emptyPage.getNumber(),
                        emptyPage.getSize(),
                        emptyPage.getTotalElements(),
                        emptyPage.getTotalPages()
                );
            }

            // assign dan seek ke offset terbaru (bounded lookback) untuk menghindari full scan
            consumer.assign(partitions);
            consumer.seekToEnd(partitions);
            int perPartitionLookback = Math.max(1, maxRecords / partitions.size());
            for (TopicPartition partition : partitions) {
                long currentPosition = consumer.position(partition);
                long startOffset = Math.max(0L, currentPosition - perPartitionLookback);
                consumer.seek(partition, startOffset);
            }

            // perulangan (looping) sampai data di Kafka benar-benar habis
            boolean keepPolling = true;
            int emptyPollCount = 0; // untuk cek jika Kafka sudah tidak punya data lagi

            while (keepPolling) {
                ConsumerRecords<String, ItemsMovedKafkaMessage> records = consumer.poll(Duration.ofMillis(pollTimeoutMs));

                if (records.isEmpty()) {
                    emptyPollCount++;
                    if (emptyPollCount >= maxEmptyPolls) {
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
                        if (allFilteredMessages.size() >= maxRecords) {
                            keepPolling = false;
                            break;
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
