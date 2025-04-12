package com.vsi.teste.decoupling;

import java.math.BigDecimal;

/**
 * Adaptador (Adapter Pattern) que faz a ponte entre a nossa interface PaymentGateway
 * e a biblioteca legada OldPaymentGateway.
 *
 * Este adaptador implementa nossa interface e traduz as chamadas para os métodos
 * correspondentes da biblioteca antiga.
 */
public class OldPaymentGatewayAdapter implements PaymentGateway {

    // Instância da biblioteca legada que queremos adaptar
    private final OldPaymentGateway oldGateway;

    public OldPaymentGatewayAdapter(OldPaymentGateway oldGateway) {
        this.oldGateway = oldGateway;
    }

    @Override
    public boolean processarPagamento(BigDecimal valor, String detalhesCartao) {
        // Adaptação:
        // 1. Converte BigDecimal para double (pode exigir tratamento de precisão)
        // 2. Chama o método específico da biblioteca antiga (executeTransaction)
        // 3. Traduz o resultado da biblioteca antiga (String) para o esperado pela interface (boolean)
        String result = oldGateway.executeTransaction(valor.doubleValue(), detalhesCartao);
        return !result.startsWith("ERROR"); // Considera sucesso se não começar com "ERROR"
    }

    @Override
    public boolean estornarPagamento(String idTransacao) {
        // Adaptação:
        // 1. Chama o método específico da biblioteca antiga (refundTransaction)
        // 2. Traduz o resultado da biblioteca antiga (int) para o esperado pela interface (boolean)
        int status = oldGateway.refundTransaction(idTransacao);
        return status == 1; // Considera sucesso se o status for 1
    }
}