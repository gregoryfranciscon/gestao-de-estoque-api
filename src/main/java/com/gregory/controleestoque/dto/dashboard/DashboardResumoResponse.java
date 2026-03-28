package com.gregory.controleestoque.dto.dashboard;

public record DashboardResumoResponse(
        long totalProdutos,
        long totalFornecedores,
        long totalMovimentacoes,
        long produtosEstoqueBaixo
) {
}
