# Nova Platform

> 免费开源的企业级快速开发平台 · 对标并超越商业产品（BladeX 等）· 信创友好
>
> `nova` 为工作代号，正式名称待定（全局替换 groupId/包名即可改名）。

## 为什么做这个

商业快速开发平台（BladeX 3999 元起、JNPF 万元/年、JeecgBoot 商业版）把**工作流、报表、低代码表单、多租户、数据权限**做成收费点；开源竞品要么技术栈陈旧（Spring Boot 2.7 + JDK 8），要么文档收费、组件闭源。Nova 的承诺：

- **真开源**：Apache-2.0，无附加条款，文档永久免费，商业收费点全部免费内置
- **技术代差**：Spring Boot 4 + JDK 21，领先竞品一到两代
- **信创友好**：国产 JDK/数据库/中间件/国密为一等公民，而非事后补丁

选型依据见 [docs/research](docs/research)（三轮联网调研 + 交叉验证，含信创兼容矩阵与中外技术栈分歧分析）。

## 技术栈（全部经真实构建验证）

| 层 | 选型 | 说明 |
|----|------|------|
| 运行时 | Java 21（地板 17） | 龙井/毕昇/Kona/龙芯国产 JDK 全覆盖；虚拟线程默认开启 |
| 框架 | Spring Boot 4.0.x / Spring Framework 7 | 最低仅需 JDK 17，信创可落地 |
| 构建 | Maven 3.9+ | 纯 XML，无 Kotlin |
| ORM | MyBatis-Flex 1.11.x | APT 编译期强类型查询；审计/多租户/逻辑删除开箱即用 |
| 数据库 | PostgreSQL-first | 金仓/openGauss/瀚高（PG 系）零改造；开发态 H2 PG 模式 |
| 认证 | Sa-Token 1.45+ | 登录拦截 + `@SaCheckRole/@SaCheckPermission` |
| 国密 | SM2/SM3/SM4（BouncyCastle + Hutool） | 敏感字段 SM4 加密 TypeHandler，等保友好 |
| API 文档 | springdoc-openapi 3.x | /swagger-ui.html |
| 部署 | 可执行 Jar（默认）/ WAR（规划中） | WAR 形态用于 TongWeb 等国产中间件验收 |

## 模块结构

```
nova
├── nova-framework                 框架层（每个 starter 单一职责，单向依赖 core）
│   ├── nova-core                  零依赖契约：R / ErrorCode / BizException / 租户与用户上下文
│   ├── nova-starter-web           统一响应、全局异常、参数校验
│   ├── nova-starter-orm           MyBatis-Flex：审计字段自动填充、多租户、分页
│   ├── nova-starter-security      Sa-Token：登录拦截、鉴权数据桥接、鉴权异常
│   └── nova-starter-crypto        国密：SmCryptoService + Sm4FieldTypeHandler
├── nova-modules                   业务模块层
│   └── nova-module-system         用户/角色 RBAC、登录认证、管理员初始化
└── nova-server                    启动器与环境配置
```

## 快速开始

```bash
mvn package -DskipTests
java -jar nova-server/target/nova-server.jar
```

```bash
# 登录（首启自动初始化 admin / admin123）
curl -X POST localhost:8080/auth/login -H 'Content-Type: application/json' \
     -d '{"username":"admin","password":"admin123"}'
# 携带返回的 tokenValue 访问
curl localhost:8080/system/users -H "Authorization: <tokenValue>"
```

Swagger UI：http://localhost:8080/swagger-ui.html · H2 控制台：http://localhost:8080/h2-console

生产环境：`--spring.profiles.active=postgres`（金仓/openGauss 仅需替换 driver 与 url，见 application.yml 注释）。

## 已验证能力（v0.1 骨架）

- [x] Spring Boot 4.0.6 + JDK 21 启动（约 4 秒）、虚拟线程开启
- [x] 统一响应/全局异常：业务异常带码返回，系统异常隐藏细节
- [x] Sa-Token 登录态拦截 + 角色注解鉴权（未登录 401 / 越权 403）
- [x] MyBatis-Flex 强类型查询（APT TableDef）、分页
- [x] 审计字段自动填充（create/update 时间与操作人）
- [x] 多租户上下文（tenant_id 自动条件，空上下文 = 平台级访问）
- [x] 逻辑删除（int 0/1，全数据库通用，含达梦）
- [x] 国密 SM4 敏感字段加密落库、读取透明解密
- [x] 集成测试（Spring 上下文 + H2 全链路）

## 路线图

详见 [docs/research/2026-06-java-framework-research.md](docs/research/2026-06-java-framework-research.md) 第五节。

- **v0.x 底座**：菜单/部门/字典、数据权限（行/列级）、代码生成器、操作日志、i18n、信创回归矩阵（金仓/openGauss/达梦 × TongWeb × 麒麟/统信）
- **v1.0 对齐商业版**：工作流（Warm-Flow/FlowLong）+ 设计器、低代码表单、报表打印、文件存储、租户管理控制台、前端（Vue 3 + Vite + Element Plus）
- **v2.0 超越**：AI 模块（RAG/ChatBI/AI 生成）、框架内置 MCP Server（AI 编程工具直接驱动代码生成）、GraalVM 原生发行版、微服务形态

## License

[Apache-2.0](LICENSE)（待补充 LICENSE 文件）——无附加条款，可自由商用、修改、分发。
