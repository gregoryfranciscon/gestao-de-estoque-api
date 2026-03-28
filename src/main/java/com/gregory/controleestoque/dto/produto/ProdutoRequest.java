package com.gregory.controleestoque.dto.produto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProdutoRequest(
        @NotBlank(message = "Nome do produto e obrigatorio")
        @Size(max = 120, message = "Nome do produto deve ter no maximo 120 caracteres")
        String nome,

        @NotBlank(message = "Categoria do produto e obrigatoria")
        @Size(max = 80, message = "Categoria deve ter no maximo 80 caracteres")
        String categoria,

        @NotNull(message = "Preco do produto e obrigatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "Preco deve ser maior ou igual a zero")
        @Digits(integer = 10, fraction = 2, message = "Preco deve ter no maximo 10 inteiros e 2 casas decimais")
        BigDecimal preco,

        @NotNull(message = "Estoque atual e obrigatorio")
        @Min(value = 0, message = "Estoque atual deve ser maior ou igual a zero")
        Integer estoqueAtual,

        @NotNull(message = "Estoque minimo e obrigatorio")
        @Min(value = 0, message = "Estoque minimo deve ser maior ou igual a zero")
        Integer estoqueMinimo,

        @NotNull(message = "Fornecedor do produto e obrigatorio")
        Long fornecedorId
) {
}
