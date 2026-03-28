package com.gregory.controleestoque.dto.dashboard;

public record ProdutoEstoqueBaixoResponse(
        Long produtoId,
        String produtoNome,
        String fornecedorNome,
        Integer estoqueAtual,
        Integer estoqueMinimo
) {
}
