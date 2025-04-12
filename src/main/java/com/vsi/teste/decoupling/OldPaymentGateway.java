package com.vsi.teste.decoupling;

import java.math.BigDecimal;

/**
 * Simulação de uma biblioteca de terceiros (LEGADA) para processamento de pagamentos.
 * Possui métodos com nomes e parâmetros potencialmente diferentes da nossa interface desejada.
 */
public class OldPaymentGateway {

    public String executeTransaction(double amount, String cardInfo) {
        System.out.println("[OldGateway] Executando transação de " + amount + " com cartão " + cardInfo);
        // Lógica complexa e específica da biblioteca antiga...
        boolean success = Math.random() > 0.2; // Simula sucesso/falha
        return success ? "TXN_OLD_" + System.currentTimeMillis() : "ERROR_OLD";
    }

    public int refundTransaction(String transactionId) {
        System.out.println("[OldGateway] Estornando transação " + transactionId);
        // Lógica de estorno da biblioteca antiga...
        boolean success = transactionId != null && transactionId.startsWith("TXN_OLD_");
        return success ? 1 : 0; // Retorna código de status (1=sucesso, 0=falha)
    }
}