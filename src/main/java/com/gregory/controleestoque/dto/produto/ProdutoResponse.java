package com.gregory.controleestoque.dto.produto;

import java.math.BigDecimal;

import com.gregory.controleestoque.model.StatusEstoque;

public record ProdutoResponse(
        Long id,
        String nome,
        String categoria,
        BigDecimal preco,
        Integer estoqueAtual,
        Integer estoqueMinimo,
        Long fornecedorId,
        String fornecedorNome,
        StatusEstoque status
) {
}
