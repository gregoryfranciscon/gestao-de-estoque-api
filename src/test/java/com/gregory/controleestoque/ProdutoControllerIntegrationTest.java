package com.gregory.controleestoque;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import com.gregory.controleestoque.dto.produto.ProdutoRequest;
import com.gregory.controleestoque.support.IntegrationTestSupport;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProdutoControllerIntegrationTest extends IntegrationTestSupport {

    @Test
    void shouldListAndFilterProducts() throws Exception {
        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6));

        mockMvc.perform(get("/api/produtos").param("nome", "Logitech"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value(org.hamcrest.Matchers.containsString("Logitech")));
    }

    @Test
    void shouldCreateUpdateAndDeleteProductWithoutMovements() throws Exception {
        ProdutoRequest request = new ProdutoRequest(
                "Produto Teste",
                "Categoria Teste",
                new BigDecimal("99.90"),
                12,
                4,
                1L
        );

        String response = mockMvc.perform(post("/api/produtos")
                        .contentType(jsonContentType())
                        .content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fornecedorId").value(1))
                .andExpect(jsonPath("$.status").value("NORMAL"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long produtoId = objectMapper.readTree(response).get("id").asLong();

        ProdutoRequest update = new ProdutoRequest(
                "Produto Teste Atualizado",
                "Categoria Teste",
                new BigDecimal("109.90"),
                4,
                4,
                2L
        );

        mockMvc.perform(put("/api/produtos/{id}", produtoId)
                        .contentType(jsonContentType())
                        .content(json(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Produto Teste Atualizado"))
                .andExpect(jsonPath("$.fornecedorId").value(2))
                .andExpect(jsonPath("$.status").value("ESTOQUE_BAIXO"));

        mockMvc.perform(delete("/api/produtos/{id}", produtoId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRejectDeletionWhenProductHasMovements() throws Exception {
        mockMvc.perform(delete("/api/produtos/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Produto possui movimentacoes e nao pode ser removido"));
    }

    @Test
    void shouldReturnLowStockProducts() throws Exception {
        mockMvc.perform(get("/api/produtos/estoque-baixo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].estoqueAtual").exists())
                .andExpect(jsonPath("$[0].estoqueMinimo").exists());
    }

    @Test
    void shouldValidateProductPayload() throws Exception {
        ProdutoRequest request = new ProdutoRequest(
                "",
                "",
                new BigDecimal("-1.00"),
                -2,
                -1,
                null
        );

        mockMvc.perform(post("/api/produtos")
                        .contentType(jsonContentType())
                        .content(json(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Falha de validacao"))
                .andExpect(jsonPath("$.validationErrors.nome").exists())
                .andExpect(jsonPath("$.validationErrors.categoria").exists())
                .andExpect(jsonPath("$.validationErrors.preco").exists())
                .andExpect(jsonPath("$.validationErrors.estoqueAtual").exists())
                .andExpect(jsonPath("$.validationErrors.estoqueMinimo").exists())
                .andExpect(jsonPath("$.validationErrors.fornecedorId").exists());
    }
}
