package com.vsi.teste.decoupling;

import java.math.BigDecimal;

/**
 * Classe da aplicação que utiliza um PaymentGateway para processar pagamentos.
 * Esta classe depende da *interface* PaymentGateway, não de uma implementação específica.
 */
public class PaymentProcessor {

    // Depende da abstração (Interface)
    private final PaymentGateway paymentGateway;

    // Injeção de Dependência (via construtor)
    // Recebe qualquer implementação de PaymentGateway
    public PaymentProcessor(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void realizarCompra(BigDecimal valor, String cartao) {
        System.out.println("Processando compra de " + valor + "...");
        boolean sucesso = paymentGateway.processarPagamento(valor, cartao);
        if (sucesso) {
            System.out.println("Compra realizada com sucesso!");
        } else {
            System.out.println("Falha ao processar a compra.");
        }
    }

    // --- Exemplo de Uso (Opcional) ---
    // public static void main(String[] args) {
    //     // 1. Criar a instância da biblioteca legada
    //     OldPaymentGateway oldLib = new OldPaymentGateway();

    //     // 2. Criar o Adaptador para a biblioteca legada
    //     PaymentGateway adapter = new OldPaymentGatewayAdapter(oldLib);

    //     // 3. Injetar o Adaptador no nosso processador
    //     PaymentProcessor processor = new PaymentProcessor(adapter);

    //     // 4. Usar o processador (que internamente usa o adaptador -> biblioteca legada)
    //     processor.realizarCompra(new BigDecimal("199.99"), "1234-XXXX-XXXX-5678");

    //     System.out.println("\n--- Futuramente, trocando para um Novo Gateway ---");
    //     // Suponha que exista uma nova classe NewPaymentGateway que implementa PaymentGateway
    //     // PaymentGateway newGateway = new NewPaymentGateway(); // (Implementação não mostrada)
    //     // PaymentProcessor newProcessor = new PaymentProcessor(newGateway);
    //     // newProcessor.realizarCompra(new BigDecimal("50.00"), "9876-XXXX-XXXX-1234");
    //     // A classe PaymentProcessor não precisaria ser alterada.
    // }
}