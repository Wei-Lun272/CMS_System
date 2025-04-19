## 🔄 Redis 快取整合說明

本系統使用 Spring Boot 整合 Redis 作為快取機制，有效提升資料查詢效率，並減少資料庫負擔。

### ✅ 快取實作內容

- 使用 `@Cacheable` 快取單筆資料（`getSiteById(id)`）
- 使用 `@CacheEvict` 同步清除快取（`deleteSite(id)`、`updateSiteById()`）
- 快取結果以 `siteCache::id` 為格式存入 Redis
- 快取 TTL（過期時間）預設為 60 分鐘，避免長期使用舊資料

### ✅ 優勢與目的

- 資料不常變動但查詢頻繁的內容，可加速回應時間
- Redis 採用 JSON 格式序列化，資料可讀性佳
- 控制台 log 可驗證是否命中快取（第二次不查 DB）

### ✅ 快取鍵範例

- 查詢單筆工地：`siteCache::3`
- 查詢全部工地列表：`siteList`

### ✅ 快取行為驗證

- 第一次查詢資料時執行 SQL 並寫入快取
- 第二次查詢同一筆資料時，無 SQL 輸出，表示來自 Redis 快取

