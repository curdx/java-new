# 后端 Java 选型专版 · 信创 + 国际双视角（2026-06）

> 本篇只聚焦**后端 Java**。约束：用 **Maven**（非 Gradle）、纯 **Java**（不用 Kotlin/Kotlin DSL）、**必须支持信创**。
> 同时对照「国外真实在用什么」，标出中外选型分歧点。
> 数据来源：三路并行联网核查（国际真实栈 / 信创兼容矩阵 / Spring 官方），2026-06-10。

---

## 一、对上一版的关键纠正

| 上一版（激进版）的说法 | 纠正 | 依据 |
|----|----|----|
| **JDK 25-only** | ❌ 错。信创基线应为 **JDK 21（地板 JDK 17）** | 国产 JDK 里只有阿里龙井有 25（25.0.3，2026-05）；毕昇/腾讯Kona/龙芯**普遍只到 21**。SB4 最低只要 JDK 17 |
| **Gradle 9 + Kotlin DSL** | ❌ 改 **Maven 3.9.x**（纯 XML，不用 Kotlin） | 用户要求；且 Maven 是国际默认（start.spring.io 默认、55% JVM 项目）；Maven 4 仍是 RC（4.0.0-rc-5），未 GA |
| **MyBatis-Flex 一把梭** | ⚠️ 没那么简单——见第四节 ORM 中外分裂 | 国际几乎不用 MyBatis；且 Flex 对达梦需自写 IDialect |
| smart-doc / Hutool 当默认 | ⚠️ 这俩是「中国特有」，国际零存在感 | 国际用 springdoc + Guava/Commons |

**但「用最新 Spring Boot 4」这个激进点保住了**：SB4 最低 JDK 仅 17，国产 JDK 全部覆盖 → 信创可行。这是本次最重要的结论。

---

## 二、Spring Boot 4 在信创可行性（官方 + 实测）

| 项 | 结论 | 来源 |
|----|----|----|
| SB4 最低 JDK | **17**（向上兼容到 Java 26） | spring.io/blog 2025-11-20、docs.spring.io/spring-boot/system-requirements.html |
| SB4 最低 Maven | **3.6.3+**（无需 Maven 4） | 同上 |
| SB4 当前最新补丁 | **4.0.6**（2026-04-23） | spring.io/blog 2026-04-23 |
| SB3.5.x OSS 支持 | 截止 **2026-06**（就是现在），企业支持到 2031 | spring.io/support-policy |
| 国产中间件 | **TongWeb 8.0+ 支持 SB3/SB4（Jakarta EE）**；TongWeb 7.0.4- 不支持 | 东方通实战文档 |
| Spring Boot 2.7 | **已于 2024 停止维护，新项目禁用** | spring 官方 |

**判断：新框架直接基于 Spring Boot 4.0.x 起步**。它只要 JDK 17，国产 JDK（龙井/毕昇/Kona/龙芯都有 17/21）全部满足，TongWeb 8 也支持。唯一要做的是**对国产数据库驱动 + TongWeb 做一轮 SB4 兼容性回归**。保守兜底分支保留 SB 3.5.x（同样 JDK 17，案例最多）。

---

## 三、信创兼容矩阵（已核实）

### 3.1 国产 JDK 版本覆盖

| 发行版 | 8 | 11 | 17 | 21 | 25/26 | 架构 |
|----|----|----|----|----|----|----|
| 阿里 龙井 Dragonwell | ✓ | ✓ | ✓ | ✓ | **25（2026-05）** | x86/ARM |
| 华为 毕昇 BiSheng | ✓ | ✓ | ✓ | ✓ | — | x86/**ARM 优化** |
| 腾讯 Kona | ✓ | ✓ | ✓ | ✓ | — | 全平台 |
| 龙芯 Loongson JDK | ✓ | ✓ | ✓ | ✓ | **26（2026-04）** | **LoongArch（需专移植，已 JCK 认证）** |

→ **信创 JDK 基线 = 21（地板 17）**。21 是四家国产 JDK 的公约数；25 暂时只有龙井，作为可选高配。
→ 现实渗透：信创项目 JDK 8 仍有存量但下降，JDK 17 约 35% 渗透、21 为推荐升级目标。**新项目不碰 JDK 8。**

### 3.2 国产数据库 → PostgreSQL-first 是最优解

| 数据库 | 兼容系 | SB3/4 + ORM | 信创推荐度 |
|----|----|----|----|
| **金仓 KingbaseES** | **PG 系** | PG 驱动/方言，零改造 | ⭐⭐⭐⭐⭐ |
| **openGauss / GaussDB** | **PG 系** | PG 兼容（驱动 org.opengauss），零改造 | ⭐⭐⭐⭐⭐ |
| 瀚高 HighGo | PG 系 | PG 方言 | ⭐⭐⭐⭐ |
| TiDB / OceanBase | MySQL 系 | MySQL 方言，标准 | ⭐⭐⭐ |
| **达梦 DM8** | **Oracle 系** | 需 `compatibleMode=oracle`；Hibernate 需自定义 `DmDialect`；MyBatis-Plus 有 `DbType.DM`，**MyBatis-Flex 需自写 IDialect** | ⭐⭐⭐（除非 Oracle 迁移） |

→ **结论：框架做 PostgreSQL-first**，天然吃下金仓/openGauss/瀚高（PG 系是信创主力），还顺带对齐国际生态 + pgvector（接 AI）。达梦作为「Oracle 兼容」二等公民单独适配。

### 3.3 国密 / 等保

- **SM2/SM3/SM4 算法**：BouncyCastle + Hutool-crypto 开箱即用；字段级加密用 MyBatis **TypeHandler** 模式（敏感字段 SM4 落库），性能损耗约 10–20%。
- **国密 TLS（SM2 双证书 / GMSSL）**：Spring Boot 内嵌 Tomcat **不原生支持**，需 TongWeb 8 或自定义 SSLContext（GmSSL-Java）。等保 2.0 三级要求国密 CA 的 SM2 证书。
- 框架应内置：国密加密 starter（SM2/3/4 + TypeHandler + 可插拔密钥源/HSM）、敏感字段注解。

### 3.4 OS / CPU / 中间件

- **OS**：麒麟 V10 / 统信 UOS 对 JDK 无特殊限制（不预装，手动配）。
- **CPU**：鲲鹏/飞腾（ARM64）、海光（x86）= 标准架构，JVM 直接跑；**龙芯（LoongArch）需龙芯 JDK**。框架要出 **ARM64 + x86 双架构镜像**。
- **中间件**：信创验收常**强制国产中间件**（TongWeb/宝兰德 BES/金蝶 Apusic），不能只用内嵌 Tomcat。→ 框架必须**同时支持可执行 Jar 与 WAR 部署**，并验证 TongWeb 8。

---

## 四、国际真实栈 & 中外分歧点（核心）

### 4.1 国际后端 2026 实际在用什么（已核实）

| 领域 | 国际主流 | 中国主流 | 性质 |
|----|----|----|----|
| 构建 | **Maven**（默认/55%）+ Gradle（云原生新项目） | Maven + Gradle | 一致（都用 Maven 没错） |
| **ORM** | **Spring Data JPA + Hibernate 7.4 / jOOQ / Spring Data JDBC** | **MyBatis（-Plus/-Flex）** | **🔴 最大分歧** |
| JSON | Jackson 3.1（LTS） | Jackson + Fastjson2 | Jackson 一致；Fastjson 中国特有 |
| 工具库 | Guava + Apache Commons | Hutool | Hutool 中国特有 |
| 样板代码 | **Java Records** 取代 Lombok（DTO）；Lombok 仅留实体 | Lombok 为主 | 国际更激进去 Lombok |
| Bean 映射 | MapStruct | MapStruct / BeanUtil | 一致 |
| 测试 | **Testcontainers（事实标准）** + JUnit 6 + AssertJ + Mockito | JUnit 5 + Mockito（Testcontainers 渗透慢） | 国际更重容器化集成测试 |
| 可观测性 | **Micrometer + OpenTelemetry（OTLP）** | 同方向，落地不均 | 国际更标准化 |
| API 文档 | **springdoc-openapi**（Swagger UI） | springdoc / smart-doc | smart-doc 中国特有 |
| 架构 | **Spring Modulith 2.0（模块化单体）** 回潮（42% 从微服务回退） | Spring Boot + 微服务分解 | 国际 2026 回归理性单体 |
| 框架格局 | Spring Boot 62% 统治；Quarkus 6–8%、Micronaut 3–5% 小众 | Spring Boot 统治 | 一致 |
| 脚手架 | start.spring.io + 自建；**没有 BladeX 式一体化低代码平台** | BladeX/若依/JeecgBoot 全栈平台林立 | **国际无对标 = 机会，也=没人验证** |

### 4.2 ORM 这个岔路必须你来定

事实摆开：
- **MyBatis 在国际基本不存在**（Vlad Mihalcea 调查、ThoughtWorks Radar、Reddit r/java 都印证）。国际是 JPA/Hibernate 的天下，复杂查询用 jOOQ，轻量用 Spring Data JDBC。
- **但在中国/信创，MyBatis 是事实标准**，且信创数据库的方言生态是围着 MyBatis-Plus 转的（`DbType.DM`、`DbType.GAUSS` 现成）。
- **MyBatis-Flex（我上版推的）更现代**（APT 编译期强类型、性能好），但**对达梦要自写 IDialect**，信创现成度不如 MyBatis-Plus。
- 想「一套抽象层同时支持 MyBatis + JPA」= 维护黑洞，不建议。

所以这是个**战略选择**，取决于你这框架到底主打谁。见文末「待你拍板」。

---

## 五、修订后的后端栈（信创可落地 + 仍然激进）

```
语言/运行时：Java 21（地板 17；龙井可选 25）· 国产 JDK 龙井/毕昇/Kona/龙芯全覆盖
框架：Spring Boot 4.0.x（最低 JDK17，信创可行）· Spring Framework 7 · Jakarta EE 11
      Jackson 3 · JSpecify 空安全 · 内置 API Versioning · RestClient
构建：Maven 3.9.x（纯 XML，无 Kotlin；Maven 4 未 GA 不追）
架构：Spring Modulith 2.0 模块化单体 ⇄ 微服务双形态（同一套领域代码）
ORM：【待定·见第四节】MyBatis-Flex / MyBatis-Plus / JPA-Hibernate 三选一
数据库：PostgreSQL-first（吃下金仓/openGauss/瀚高）；达梦/MySQL 系做适配层
认证：Sa-Token（国内主流）；OAuth2/OIDC 场景 Spring Security 7 + Authorization Server
缓存：JetCache 2.8 多级缓存 + Caffeine/Redisson
任务：SnailJob / XXL-Job
工作流：Warm-Flow / FlowLong（避开 Camunda 8 商业许可、JimuReport 闭源）
国密：内置 SM2/3/4 starter（BouncyCastle+Hutool）+ 字段 TypeHandler + 国密 TLS（TongWeb/GmSSL）
可观测：Micrometer + OpenTelemetry(OTLP)——直接对齐国际标准
API 文档：springdoc-openapi（国际标准）；可选 smart-doc（国内无侵入）
测试：JUnit 6 + AssertJ + Mockito + Testcontainers（把国际工程标准带进国产框架=差异化）
部署：可执行 Jar + WAR 双形态；ARM64/x86 双架构镜像；验证 TongWeb 8 / 麒麟 / 统信
```

### 5.1 这套栈的差异化（相对国内竞品）

1. **同时拿下信创合规 + 国际工程标准**：国内竞品几乎没人内置 Testcontainers/OpenTelemetry/Spring Modulith，也没人把国密做成干净 starter。
2. **PG-first**：与所有 MySQL-first 国产竞品错位，吃 PG 系信创库 + 国际生态 + 向量检索。
3. **SB4 + JDK21 代差**：信创可落地的前提下仍领先 BladeX（SB 3.2）和 yudao（SB 2.7）。
4. **模块化单体 ⇄ 微服务一套代码**：对标 BladeX「单体/微服务两套产品两次收费」。

---

## 六、待你拍板（战略岔路）

1. **主市场定位**：纯信创/国内优先（ORM 走 MyBatis 系、文档中文优先）？还是要国际开源影响力（JPA/Hibernate、英文文档、Testcontainers 全套）？还是两者并重（默认国际最佳实践 + 信创适配层）？
2. **ORM 路线**：
   - **MyBatis-Flex**：最现代（APT 强类型、性能好），国内顺，但达梦要自写方言、国际无人识。
   - **MyBatis-Plus**：信创现成度最高（达梦/openGauss 方言现成），但已进维护期、不够新。
   - **JPA + Hibernate（+ jOOQ 复杂查询）**：唯一国际通行解，达梦需自定义 Dialect，国内开发者接受度低。
   - **双栈**：JPA 为默认、MyBatis 作可选模块（不做统一抽象，各走各的 starter）。
3. **Spring Boot 基线**：直接 **4.0.x**（推荐，greenfield + 支持期长）还是先 **3.5.x** 稳妥（信创案例多，2027 再升）？

---

## 来源（本篇）

国际栈：sonatype 2026 供应链报告 · maven.apache.org/docs/history.html · vladmihalcea.com/java-data-access-technology · spring.io/projects/spring-modulith · thoughtworks.com/radar · jhipster.tech 9.1.0 · jetbrains 2025 开发者生态调查 · testcontainers.com · springdoc.org
Spring 官方：spring.io/blog 2025-11-20 / 2026-04-23 · docs.spring.io/spring-boot/system-requirements.html · spring.io/support-policy · spring.io/projects/spring-ai (1.1 GA) · start.spring.io
信创：github.com/dragonwell-project · openeuler.org/.../bishengjdk · tencent.github.io/konajdk · loongnix.cn/zh/api/java · eco.dameng.com · kingbase.com.cn · docs.opengauss.org · mybatis-flex.com/zh/intro/support-database.html · doc.hutool.cn/pages/SmUtil · github.com/GmSSL/GmSSL-Java · 东方通 TongWeb 实战文档
