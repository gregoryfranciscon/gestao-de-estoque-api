package com.gregory.controleestoque;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import com.gregory.controleestoque.dto.movimentacao.MovimentacaoRequest;
import com.gregory.controleestoque.model.TipoMovimentacao;
import com.gregory.controleestoque.support.IntegrationTestSupport;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MovimentacaoControllerIntegrationTest extends IntegrationTestSupport {

    @Test
    void shouldListAndFilterMovements() throws Exception {
        mockMvc.perform(get("/api/movimentacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6));

        mockMvc.perform(get("/api/movimentacoes").param("produtoId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get("/api/movimentacoes").param("tipo", "SAIDA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("SAIDA"));
    }

    @Test
    void shouldCreateEntryMovementAndUpdateProductStock() throws Exception {
        MovimentacaoRequest request = new MovimentacaoRequest(
                1L,
                TipoMovimentacao.ENTRADA,
                5,
                LocalDateTime.of(2026, 3, 28, 18, 0),
                "Teste de entrada"
        );

        mockMvc.perform(post("/api/movimentacoes")
                        .contentType(jsonContentType())
                        .content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.produtoId").value(1))
                .andExpect(jsonPath("$.tipo").value("ENTRADA"))
                .andExpect(jsonPath("$.quantidade").value(5));

        mockMvc.perform(get("/api/produtos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estoqueAtual").value(20));
    }

    @Test
    void shouldRejectOutputMovementWhenStockIsInsufficient() throws Exception {
        MovimentacaoRequest request = new MovimentacaoRequest(
                4L,
                TipoMovimentacao.SAIDA,
                10,
                LocalDateTime.of(2026, 3, 28, 18, 30),
                "Teste de saida sem estoque"
        );

        mockMvc.perform(post("/api/movimentacoes")
                        .contentType(jsonContentType())
                        .content(json(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Estoque insuficiente para registrar a saida"));
    }

    @Test
    void shouldValidateMovementPayload() throws Exception {
        MovimentacaoRequest request = new MovimentacaoRequest(
                null,
                null,
                0,
                null,
                "x".repeat(501)
        );

        mockMvc.perform(post("/api/movimentacoes")
                        .contentType(jsonContentType())
                        .content(json(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Falha de validacao"))
                .andExpect(jsonPath("$.validationErrors.produtoId").exists())
                .andExpect(jsonPath("$.validationErrors.tipo").exists())
                .andExpect(jsonPath("$.validationErrors.quantidade").exists())
                .andExpect(jsonPath("$.validationErrors.observacao").exists());
    }
}
