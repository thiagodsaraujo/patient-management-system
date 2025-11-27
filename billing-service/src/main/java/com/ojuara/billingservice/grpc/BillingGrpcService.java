package com.ojuara.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

// Exemplo para usar no Brun
// Anotação Lombok que adiciona um campo 'log' (org.slf4j.Logger) à classe
@Slf4j
// Registra esta classe como um serviço gRPC no contexto Spring Boot (net.devh)
@GrpcService
// Implementação do serviço gRPC gerada a partir do .proto (classe base)
public class BillingGrpcService extends BillingServiceImplBase {

    // Indica que este método sobrescreve o definido na classe base gerada
    @Override
    // Método chamado pelo runtime gRPC quando o cliente invoca 'createBillingAccount'
    public void createBillingAccount(
            // Mensagem de requisição gerada pelo protobuf (contém dados do cliente)
            billing.BillingRequest billingRequest,
            // Observador para enviar respostas de volta ao cliente (stream gRPC)
            StreamObserver<billing.BillingResponse> responseObserver) {

        // Registra no log que uma requisição de criação de conta foi recebida
        log.info("Received billing account creation request for userId: {}",
                billingRequest.toString());

        // Lugar para a lógica de negócio:
        // - criar registro de conta de cobrança no banco de dados,
        // - validar dados, executar cálculos, chamar outros serviços, etc.
        // Observação: implementar com serviços/repositórios injetados, não direto aqui.

        // Monta a resposta usando o builder gerado pelo protobuf
        BillingResponse billingResponse = BillingResponse.newBuilder()
                // Gera um identificador simples com timestamp (exemplo)
                .setAccountId("acc-" + System.currentTimeMillis())
                // Define o status inicial da conta
                .setStatus("ACTIVE")
                // Constrói a instância imutável de BillingResponse
                .build();

        // Envia a resposta (onNext pode ser chamado múltiplas vezes em streams)
        responseObserver.onNext(billingResponse);
        // Indica que não haverá mais mensagens e encerra o stream de resposta
        responseObserver.onCompleted();

    }
}
