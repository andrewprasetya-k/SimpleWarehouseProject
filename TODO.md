1. Fundamental spring
2. repository ada joinnya
3. extend poin 2 -> join pake 3 konsep (interface tanpa specify db query, query pake jpql (object java), query biasa)
4. cara kerja transaksional spesifik (after commit, pre commit, dkk)
5. lanjut belajar Redis (caching, TTL) & Kafka (event streaming antar service, retention/compaction) - setup via Docker, tambah dependency spring-kafka

 
scope utama di gudang:
1. service: stockholm (kafka+redis), stockholm-ui (angular), wms-ui (vue), oms-document (order management system), warehouse-item-master, warehouse-layout, warehouse-stock-movement (picklist logic berat untuk urutan ambil barang)
2. techstack: spring boot, angular 0 + spring boot, vue 3, spring boot webflux, spring boot webflux + kafka, spring boot webflux + redis
3. database: pgsql, mongodb, mongodb, pgsql, pgsql+postgis, pgsql

scope yg kecil:
1. warehouse-data-viewer untuk search data inbound & outbound (db=elastic (db untuk searching, metode cari pakai indexing kayak token AI dan bisa dicustom indexnya), spring boot)
2. warehouse-dashboard-core untuk summary data