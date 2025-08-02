# SaaS Xbox Backend API 设计文档

## 1. 统一响应格式

所有API接口统一使用以下响应格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1703000000000
}
```

### 响应码规范
- `200`: 操作成功
- `400`: 请求参数错误
- `401`: 未授权访问
- `403`: 权限不足
- `404`: 资源不存在
- `500`: 服务器内部错误

## 2. Xbox系统集成API

### 2.1 Xbox同步管理接口

基础路径：`/api/xbox-sync`

#### 检查Xbox Controller连接状态
```http
GET /xbox-sync/check-connection
```

**响应示例**：
```json
{
  "code": 200,
  "message": "连接成功",
  "data": true,
  "timestamp": 1703000000000
}
```

#### 获取Xbox节点列表
```http
GET /xbox-sync/nodes
```

**响应示例**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "nodeCode": "test-node-agent",
      "nodeName": "test-node",
      "serverIp": "165.254.16.244",
      "nodeStatus": "运行中",
      "cpuUsage": 15.5,
      "memoryUsage": 45.2,
      "currentConnections": 128
    }
  ],
  "timestamp": 1703000000000
}
```

#### 同步所有节点信息
```http
POST /xbox-sync/sync-all
```

**响应示例**：
```json
{
  "code": 200,
  "message": "同步成功",
  "data": "节点同步完成。总计: 2, 新增: 1, 更新: 1",
  "timestamp": 1703000000000
}
```

#### 同步指定节点信息
```http
POST /xbox-sync/sync/{agentId}
```

**路径参数**：
- `agentId`: Agent ID

**响应示例**：
```json
{
  "code": 200,
  "message": "同步成功",
  "data": "节点更新成功",
  "timestamp": 1703000000000
}
```

### 2.2 Agent部署和管理接口

#### 部署Agent到远程节点
```http
POST /xbox-sync/deploy-agent
```

**请求参数**：
- `nodeIp` (required): 节点IP地址
- `sshPort` (optional): SSH端口，默认22
- `sshUser` (optional): SSH用户名，默认root
- `sshPassword` (required): SSH密码

**请求示例**：
```http
POST /xbox-sync/deploy-agent?nodeIp=165.254.16.244&sshPassword=Asd2025#
```

**响应示例**：
```json
{
  "code": 200,
  "message": "部署成功",
  "data": "Agent部署成功: [部署日志信息]",
  "timestamp": 1703000000000
}
```

#### 更新Agent配置
```http
POST /xbox-sync/update-agent-config
```

**请求参数**：
- `agentId` (required): Agent ID
- `configType` (required): 配置类型 (blacklist/whitelist/protocols)

**请求体**：
```json
{
  "domains": ["example.com", "test.com"],
  "action": "block",
  "protocols": ["socks", "http", "shadowsocks"]
}
```

**响应示例**：
```json
{
  "code": 200,
  "message": "配置更新成功",
  "data": "配置更新成功: Agent配置已应用",
  "timestamp": 1703000000000
}
```

#### 获取Agent系统监控信息
```http
GET /xbox-sync/agent-monitoring/{agentId}
```

**路径参数**：
- `agentId`: Agent ID

**响应示例**：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "cpu_usage": 15.5,
    "memory_usage": 45.2,
    "disk_usage": 67.8,
    "network_connections": 128,
    "singbox_status": "running",
    "last_update": "2024-01-01T10:00:00Z"
  },
  "timestamp": 1703000000000
}
```

#### 测试Agent连接和功能
```http
POST /xbox-sync/test-agent/{agentId}
```

**路径参数**：
- `agentId`: Agent ID

**请求参数**：
- `testType` (optional): 测试类型 (connection/proxy/all)，默认all

**响应示例**：
```json
{
  "code": 200,
  "message": "测试成功",
  "data": "✓ 连接测试: 正常\n✓ 代理测试: 正常\n✓ 监控数据: 正常",
  "timestamp": 1703000000000
}
```

### 2.3 强制重新同步
```http
POST /xbox-sync/force-sync
```

**响应示例**：
```json
{
  "code": 200,
  "message": "同步成功",
  "data": "强制同步完成。节点: 成功, 状态: 成功, 协议: 成功",
  "timestamp": 1703000000000
}
```

## 2. 分页响应格式

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [],
    "total": 100,
    "size": 20,
    "current": 1,
    "pages": 5
  },
  "timestamp": 1703000000000
}
```

## 3. 统一请求参数

### 分页参数
- `current`: 当前页码，默认为1
- `size`: 每页大小，默认为20，最大为100

### 时间范围参数
- `startTime`: 开始时间 (YYYY-MM-DD HH:mm:ss)
- `endTime`: 结束时间 (YYYY-MM-DD HH:mm:ss)

### 排序参数
- `orderBy`: 排序字段
- `orderDirection`: 排序方向 (ASC/DESC)

## 4. 核心模块API接口

### 4.1 意向客户管理 (/admin/intention-customer)

```
GET    /admin/intention-customer/page           # 分页查询
POST   /admin/intention-customer/save           # 新增/编辑
DELETE /admin/intention-customer/{id}           # 软删除
GET    /admin/intention-customer/{id}           # 详情查询
GET    /admin/intention-customer/export         # 导出Excel
```

### 4.2 销售提成管理 (/admin/commission)

```
GET    /admin/commission/page                   # 分页查询
POST   /admin/commission/settle                 # 批量结算
POST   /admin/commission/unsettle              # 批量取消结算
GET    /admin/commission/export                # 导出Excel
GET    /admin/commission/statistics            # 提成统计
```

### 4.3 销售记录管理 (/admin/sales-record)

```
GET    /admin/sales-record/page                # 分页查询（只读）
GET    /admin/sales-record/export              # 导出Excel
GET    /admin/sales-record/statistics          # 销售统计
```

### 4.4 订单管理 (/admin/order)

```
GET    /admin/order/page                       # 分页查询
GET    /admin/order/{id}                       # 详情查询
PUT    /admin/order/{id}/status                # 更新状态
POST   /admin/order/{id}/ip-replace/prepare    # 准备IP更换
POST   /admin/order/{id}/ip-replace/apply      # 执行IP更换
POST   /admin/order/{id}/ip-replace/test       # 测试IP连通性
GET    /admin/order/{id}/ip-replace/logs       # 更换记录
```

### 4.5 IP池管理 (/admin/ip-pool)

```
GET    /admin/ip-pool/page                     # 分页查询
POST   /admin/ip-pool/save                     # 新增/编辑
DELETE /admin/ip-pool/{id}                     # 删除
POST   /admin/ip-pool/test                     # 测试IP连通性
POST   /admin/ip-pool/batch-import             # 批量导入
GET    /admin/ip-pool/select-for-replace       # 选择IP用于更换
```

### 4.6 产品管理 (/admin/product)

```
GET    /admin/product/page                     # 分页查询  
POST   /admin/product/save                     # 新增/编辑
DELETE /admin/product/{id}                     # 删除
PUT    /admin/product/{id}/status              # 更新状态
GET    /admin/product/options                  # 产品选项
```

### 4.7 员工管理 (/admin/employee)

```
GET    /admin/employee/page                    # 分页查询
POST   /admin/employee/save                    # 新增/编辑
DELETE /admin/employee/{id}                    # 删除
PUT    /admin/employee/{id}/status             # 更新状态
PUT    /admin/employee/{id}/password           # 重置密码
GET    /admin/employee/options                 # 员工选项（客户经理等）
```

### 4.8 售后管理 (/admin/support)

```
GET    /admin/support/page                     # 分页查询
POST   /admin/support/save                     # 新增/编辑
PUT    /admin/support/{id}/assign              # 分配工单
PUT    /admin/support/{id}/status              # 更新状态
POST   /admin/support/{id}/reply               # 回复工单
```

### 4.9 客户管理 (/admin/customer)

```
GET    /admin/customer/page                    # 分页查询
POST   /admin/customer/save                    # 新增/编辑
PUT    /admin/customer/{id}/status             # 更新状态
POST   /admin/customer/{id}/recharge           # 余额充值
GET    /admin/customer/{id}/balance-logs       # 余额记录
GET    /admin/customer/options                 # 客户选项
```

### 4.10 节点管理 (/admin/node)

```
GET    /admin/node/page                        # 分页查询
POST   /admin/node/save                        # 新增/编辑
DELETE /admin/node/{id}                        # 删除
POST   /admin/node/{id}/test                   # 测试节点
GET    /admin/node/{id}/monitor                # 节点监控数据
PUT    /admin/node/{id}/status                 # 更新状态
```

### 4.11 系统管理

#### 部门管理 (/admin/department)
```
GET    /admin/department/tree                  # 部门树
POST   /admin/department/save                  # 新增/编辑
DELETE /admin/department/{id}                  # 删除
```

#### 操作日志 (/admin/operation-log)
```
GET    /admin/operation-log/page               # 分页查询
GET    /admin/operation-log/export             # 导出Excel
```

## 5. 数据传输对象 (DTO) 规范

### 5.1 分页查询DTO

```java
@Data
public class PageQuery {
    @Min(1)
    private Integer current = 1;
    
    @Min(1) @Max(100)
    private Integer size = 20;
    
    private String orderBy;
    
    private String orderDirection = "DESC";
}
```

### 5.2 时间范围查询DTO

```java
@Data
public class TimeRangeQuery extends PageQuery {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
```

## 6. 权限控制

### 6.1 角色权限矩阵

| 模块 | 超级管理员 | 管理员 | 财务 | 客户经理 | 普通员工 |
|------|------------|--------|------|----------|----------|
| 意向客户管理 | CRUD | R | R | R(自己) | R |
| 销售提成管理 | CRUD | R | CRU | R(自己) | R |
| 销售记录管理 | R | R | R | R(自己) | R |
| 订单管理 | CRUD | RU | R | R(自己) | R |
| IP池管理 | CRUD | RU | - | - | - |
| 产品管理 | CRUD | RU | - | - | - |
| 员工管理 | CRUD | R | - | - | - |
| 售后管理 | CRUD | CRUD | R | R(自己) | R |
| 客户管理 | CRUD | RU | R | R(自己) | R |
| 节点管理 | CRUD | R | - | - | - |

> C=Create, R=Read, U=Update, D=Delete

### 6.2 权限注解

```java
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
@PreAuthorize("@permissionService.hasDataPermission(#managerId)")
```

## 7. 异常处理

### 7.1 业务异常

```java
public class BusinessException extends RuntimeException {
    private Integer code;
    private String message;
}
```

### 7.2 全局异常处理器

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        // 参数校验异常处理
    }
}
```

## 8. 操作日志

所有CUD操作自动记录操作日志：

```java
@OperationLog(module = "意向客户管理", operation = "新增客户")
public Result<Void> saveIntentionCustomer(@RequestBody IntentionCustomerDTO dto) {
    // 业务逻辑
}
```

## 9. 数据校验

### 9.1 通用校验注解

```java
public class IntentionCustomerDTO {
    @NotBlank(message = "客户名称不能为空")
    @Length(max = 50, message = "客户名称长度不能超过50个字符")
    private String customerName;
    
    @NotNull(message = "客户来源不能为空")
    private CustomerSource customerSource;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    private String email;
}
```

## 10. 缓存策略

### 10.1 缓存配置

```java
@Cacheable(value = "employee:options", key = "#role")
public List<EmployeeOptionVO> getEmployeeOptions(String role) {
    // 获取员工选项数据
}

@CacheEvict(value = "employee:options", allEntries = true)
public void saveEmployee(EmployeeDTO dto) {
    // 保存员工信息，清除相关缓存
}
```

## 11. 接口文档

使用 Swagger/OpenAPI 3.0 自动生成接口文档：

```java
@Api(tags = "意向客户管理")
@RestController
@RequestMapping("/admin/intention-customer")
public class IntentionCustomerController {
    
    @ApiOperation("分页查询意向客户")
    @GetMapping("/page")
    public Result<IPage<IntentionCustomerVO>> page(IntentionCustomerQuery query) {
        // 实现逻辑
    }
}
```

## 12. 测试规范

### 12.1 单元测试

```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IntentionCustomerServiceTest {
    
    @Test
    void testSaveIntentionCustomer() {
        // 测试保存意向客户
    }
}
```

### 12.2 集成测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IntentionCustomerControllerTest {
    
    @Test
    void testPageQuery() {
        // 测试分页查询接口
    }
}
```