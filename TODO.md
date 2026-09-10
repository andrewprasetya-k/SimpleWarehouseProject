1. cara kerja transaksional spesifik (after commit, pre commit, dkk) (requires_new membuat transaction session sendiri kalau dipanggil di method lain. kalau required, commitnya bareng)(rollbackFor() untuk rollback kalau ada exception yang tertentu)(afterCompletion: setelah commit/rollback (punya status) -> class TransactionSynchronization.java). Kenapa pakai transaction? Kalau error, tinggal rollback. afterCompletion() biasa digunakan untuk rollback data, untuk method utama pakai afterCommit().
2. lanjut belajar Redis (caching, TTL) & Kafka (event streaming antar service, retention/compaction) - setup via Docker, tambah dependency spring-kafka
3. Kenapa publish kafka di afterCommit() atau di afterCompletion()

Kafka:
1. topic: tempat message dikirim per consumer, misal dari app picker kemarin dibagi satu lantai - satu topic 
2. partition: pembagian topic menjadi lebih kecil, misal dari app picker kemarin satu lantai (satu topic) dibagi jadi beberapa partition per bin
3. groupId: grup consumer yang akan menerima data dari topic