package com.gregory.controleestoque.dto.fornecedor;

public record FornecedorResponse(
        Long id,
        String nome,
        String telefone,
        String email
) {
}
