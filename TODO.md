// 1. Fundamental spring
// 2. repository ada joinnya
// 3. extend poin 2 -> join pake 3 konsep (interface tanpa specify db query, query pake jpql (object java), query biasa)
// 4. DONE - cara kerja transaksional (@Transactional di service, bulk move items terbukti rollback saat gagal). after commit/pre commit event listener belum dipraktikin
// 5. DONE - endpoint /items/warehouse/{id} exclude warehouse pakai ItemSummaryResponse (DTO)