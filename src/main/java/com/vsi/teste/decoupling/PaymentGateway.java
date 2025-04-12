package com.vsi.teste.decoupling;

import java.math.BigDecimal;

/**
 * Interface que define o contrato para um Gateway de Pagamento.
 * O código da aplicação dependerá desta interface, não de implementações concretas.
 *
 * @see ../../../../../../docs/Q2_2_Decoupling_Design_Pattern.md Explicação detalhada.
 */
public interface PaymentGateway {

    /**
     * Processa um pagamento.
     *
     * @param valor      O valor a ser pago.
     * @param detalhesCartao Detalhes do cartão (simplificado como String).
     * @return true se o pagamento foi bem-sucedido, false caso contrário.
     */
    boolean processarPagamento(BigDecimal valor, String detalhesCartao);

    /**
     * Realiza o estorno de um pagamento.
     *
     * @param idTransacao O identificador da transação original.
     * @return true se o estorno foi bem-sucedido, false caso contrário.
     */
    boolean estornarPagamento(String idTransacao);
}