# gRPC Architecture Disclaimer

## 📋 Visão Geral

Este módulo `billing-service` implementa comunicação via **gRPC (Google Remote Procedure Call)**, um framework moderno de alta performance para comunicação entre serviços. Este documento detalha como funciona, suas vantagens e diferenças em relação ao REST.

---

## 🏗️ Como Funciona o gRPC

### 1. **Protocol Buffers (.proto)**
```protobuf
// billing_service.proto
syntax = "proto3";

service BillingService {
  rpc CreateBillingAccount (BillingRequest) returns (BillingResponse);
}

message BillingRequest {
  string patientId = 1;
  string name = 2; 
  string email = 3;
}
```

- **Definição de Contrato**: Arquivo `.proto` define interfaces, métodos e estruturas de dados
- **Language Agnostic**: Gera código para múltiplas linguagens automaticamente
- **Versionamento**: Evolução de esquemas com compatibilidade retroativa

### 2. **Geração Automática de Código**
```bash
mvn clean compile
```
Gera automaticamente:
- `BillingServiceGrpc.java` - Stubs cliente/servidor
- `BillingRequest.java` - Classe de mensagem
- `BillingResponse.java` - Classe de resposta
- `BillingServiceImplBase` - Classe base para implementação

### 3. **Implementação do Serviço**
```java
@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    
    @Override
    public void createBillingAccount(BillingRequest request, 
                                   StreamObserver<BillingResponse> responseObserver) {
        // Lógica de negócio
        BillingResponse response = BillingResponse.newBuilder()
            .setAccountId("acc-123")
            .setStatus("ACTIVE")
            .build();
            
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
```

---

## 🆚 gRPC vs REST API

### **Protocolo de Transporte**

| Aspecto | gRPC | REST |
|---------|------|------|
| **Protocolo** | HTTP/2 | HTTP/1.1 |
| **Formato** | Protocol Buffers (binário) | JSON (texto) |
| **Multiplexing** | ✅ Múltiplas requisições simultâneas | ❌ Uma requisição por conexão |
| **Compressão** | ✅ Automática (headers + payload) | ⚠️ Apenas payload |

### **Performance**

| Métrica | gRPC | REST |
|---------|------|------|
| **Tamanho Payload** | 🚀 ~30% menor (binário) | 📦 Maior (JSON texto) |
| **Velocidade** | 🚀 2-8x mais rápido | 🐌 Mais lento |
| **Latência** | ⚡ Menor (HTTP/2) | ⏰ Maior (HTTP/1.1) |
| **CPU Usage** | 💪 Menor (serialização binária) | 🔥 Maior (parsing JSON) |

### **Tipos de Comunicação**

#### **gRPC - 4 Padrões**
```java
// 1. Unary (1 request → 1 response)
rpc GetAccount(AccountRequest) returns (AccountResponse);

// 2. Server Streaming (1 request → N responses)
rpc StreamTransactions(AccountRequest) returns (stream Transaction);

// 3. Client Streaming (N requests → 1 response)
rpc CreateBulkAccounts(stream AccountRequest) returns (BulkResponse);

// 4. Bidirectional Streaming (N requests ↔ N responses)
rpc ChatSupport(stream ChatMessage) returns (stream ChatMessage);
```

#### **REST - Apenas Request/Response**
```http
POST /billing/accounts    # Criar
GET /billing/accounts/123 # Buscar
PUT /billing/accounts/123 # Atualizar
DELETE /billing/accounts/123 # Deletar
```

### **Contratos e Tipagem**

#### **gRPC - Forte Tipagem**
```protobuf
message BillingRequest {
  string patient_id = 1;      // Campo obrigatório
  int32 amount = 2;           // Tipo específico
  repeated string tags = 3;   // Array tipado
}
```

#### **REST - Tipagem Fraca**
```json
{
  "patient_id": "123",        // String
  "amount": "100.50",         // Pode ser string ou number
  "tags": ["urgent", "vip"]   // Array genérico
}
```

---

## 🔧 Stack Tecnológica Utilizada

### **Dependências Maven**
```xml
<!-- gRPC Core -->
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-netty-shaded</artifactId>
    <version>1.69.0</version>
</dependency>

<!-- Spring Boot Integration -->
<dependency>
    <groupId>net.devh</groupId>
    <artifactId>grpc-spring-boot-starter</artifactId>
    <version>3.1.0.RELEASE</version>
</dependency>

<!-- Protocol Buffers -->
<dependency>
    <groupId>com.google.protobuf</groupId>
    <artifactId>protobuf-java</artifactId>
    <version>4.29.1</version>
</dependency>
```

### **Plugin de Geração**
```xml
<plugin>
    <groupId>org.xolstice.maven.plugins</groupId>
    <artifactId>protobuf-maven-plugin</artifactId>
    <version>0.6.1</version>
    <configuration>
        <protocArtifact>com.google.protobuf:protoc:3.25.5</protocArtifact>
        <pluginId>grpc-java</pluginId>
        <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.68.1</pluginArtifact>
    </configuration>
</plugin>
```

---

## 📁 Estrutura de Arquivos

```
billing-service/
├── src/main/proto/
│   └── billing_service.proto          # Definição do contrato
├── src/main/java/.../grpc/
│   └── BillingGrpcService.java        # Implementação do serviço
└── target/generated-sources/protobuf/
    ├── java/billing/                  # Classes de mensagem
    │   ├── BillingRequest.java
    │   ├── BillingResponse.java
    │   └── BillingServiceOuterClass.java
    └── grpc-java/billing/             # Stubs gRPC
        └── BillingServiceGrpc.java
```

---

## ⚡ Vantagens do gRPC

### **1. Performance Superior**
- **Serialização Binária**: Protocol Buffers são 3-10x mais rápidos que JSON
- **HTTP/2**: Multiplexing, compressão automática, server push
- **Menor Overhead**: Headers binários vs texto

### **2. Contratos Rigorosos**
- **Schema Evolution**: Adicionar campos sem quebrar compatibilidade
- **Code Generation**: Classes tipadas geradas automaticamente
- **Validação**: Tipos e estruturas validados em compile-time

### **3. Streaming Nativo**
- **Real-time**: Comunicação bidirecional em tempo real
- **Backpressure**: Controle de fluxo automático
- **Multiplexing**: Múltiplos streams na mesma conexão

### **4. Interoperabilidade**
- **Multi-linguagem**: Java, Go, Python, C#, Node.js, etc.
- **Load Balancing**: Integração nativa com service mesh
- **Observabilidade**: Métricas e tracing built-in

---

## 🚨 Desvantagens do gRPC

### **1. Complexidade**
- **Curva de Aprendizado**: Mais complexo que REST
- **Debugging**: Payloads binários são mais difíceis de inspecionar
- **Tooling**: Menos ferramentas de debug que REST

### **2. Limitações de Browser**
- **No Support**: Browsers não suportam HTTP/2 gRPC diretamente
- **gRPC-Web**: Necessita proxy (Envoy) para comunicação web
- **REST Gateway**: Ou usar gateway REST→gRPC

### **3. Firewall/Proxy Issues**
- **HTTP/2**: Alguns firewalls corporativos bloqueiam
- **Binary Protocol**: Proxies HTTP podem ter problemas
- **Port Requirements**: Necessita configuração específica

---

## 🔄 Quando Usar gRPC vs REST

### **Use gRPC quando:**
- ✅ **Alta Performance** é crítica
- ✅ **Comunicação Interna** entre microserviços
- ✅ **Streaming** em tempo real necessário
- ✅ **Contratos Rigorosos** são importantes
- ✅ **Múltiplas Linguagens** no ecosistema

### **Use REST quando:**
- ✅ **APIs Públicas** para terceiros
- ✅ **Simplicidade** é prioridade
- ✅ **Browsers** são clientes diretos
- ✅ **Caching HTTP** é necessário
- ✅ **Time Inexperiente** com gRPC

---

## 🛠️ Como Testar o Serviço

### **1. Via gRPC Client (BloomRPC/Postman)**
```bash
# Instalar BloomRPC ou usar Postman
# Importar .proto file
# Fazer requisições tipadas
```

### **2. Via Código Java**
```java
// Cliente gRPC
ManagedChannel channel = ManagedChannelBuilder
    .forAddress("localhost", 9090)
    .usePlaintext()
    .build();

BillingServiceGrpc.BillingServiceBlockingStub stub = 
    BillingServiceGrpc.newBlockingStub(channel);

BillingRequest request = BillingRequest.newBuilder()
    .setPatientId("patient-123")
    .setName("John Doe")
    .setEmail("john@example.com")
    .build();

BillingResponse response = stub.createBillingAccount(request);
```

### **3. Via REST Gateway (se configurado)**
```bash
curl -X POST http://localhost:8080/v1/billing/accounts \
  -H "Content-Type: application/json" \
  -d '{"patientId": "123", "name": "John", "email": "john@example.com"}'
```

---

## 🔍 Monitoramento e Observabilidade

### **Métricas Disponíveis**
- **Latência**: P50, P95, P99 por método
- **Throughput**: RPS por serviço
- **Errors**: Taxa de erro por status code
- **Connections**: Conexões ativas HTTP/2

### **Health Checks**
```java
// gRPC Health Check Protocol
rpc Check(HealthCheckRequest) returns (HealthCheckResponse);
```

### **Logging Structured**
```java
@Slf4j
public class BillingGrpcService {
    // Logs automáticos com correlationId, timing, etc.
}
```

---

## 📚 Referências

- [gRPC Official Documentation](https://grpc.io/docs/)
- [Protocol Buffers Guide](https://protobuf.dev/)
- [Spring Boot gRPC Starter](https://github.com/yidongnan/grpc-spring-boot-starter)
- [gRPC vs REST Performance](https://auth0.com/blog/beating-json-performance-with-protobuf/)

---

**⚠️ Importante**: Este serviço utiliza gRPC para comunicação interna de alta performance. Para APIs públicas ou integrações web, considere implementar um gateway REST complementar.
