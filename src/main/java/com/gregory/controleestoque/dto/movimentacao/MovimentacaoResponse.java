package com.gregory.controleestoque.dto.movimentacao;

import java.time.LocalDateTime;

import com.gregory.controleestoque.model.TipoMovimentacao;

public record MovimentacaoResponse(
        Long id,
        Long produtoId,
        String produtoNome,
        TipoMovimentacao tipo,
        Integer quantidade,
        LocalDateTime dataHora,
        String observacao
) {
}
