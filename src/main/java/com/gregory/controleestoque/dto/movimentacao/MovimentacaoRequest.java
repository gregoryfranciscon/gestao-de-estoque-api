package com.gregory.controleestoque.dto.movimentacao;

import java.time.LocalDateTime;

import com.gregory.controleestoque.model.TipoMovimentacao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovimentacaoRequest(
        @NotNull(message = "Produto da movimentacao e obrigatorio")
        Long produtoId,

        @NotNull(message = "Tipo da movimentacao e obrigatorio")
        TipoMovimentacao tipo,

        @NotNull(message = "Quantidade da movimentacao e obrigatoria")
        @Min(value = 1, message = "Quantidade deve ser maior que zero")
        Integer quantidade,

        LocalDateTime dataHora,

        @Size(max = 500, message = "Observacao deve ter no maximo 500 caracteres")
        String observacao
) {
}
