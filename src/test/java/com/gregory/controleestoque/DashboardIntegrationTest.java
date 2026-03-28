package com.gregory.controleestoque;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import com.gregory.controleestoque.support.IntegrationTestSupport;

class DashboardIntegrationTest extends IntegrationTestSupport {

    @Test
    void shouldReturnDashboardSummary() throws Exception {
        mockMvc.perform(get("/api/dashboard/resumo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProdutos").value(6))
                .andExpect(jsonPath("$.totalFornecedores").value(3))
                .andExpect(jsonPath("$.totalMovimentacoes").value(6))
                .andExpect(jsonPath("$.produtosEstoqueBaixo").value(3));
    }

    @Test
    void shouldReturnLowStockProductsAndLatestMovements() throws Exception {
        mockMvc.perform(get("/api/dashboard/estoque-baixo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].produtoNome").exists())
                .andExpect(jsonPath("$[0].fornecedorNome").exists());

        mockMvc.perform(get("/api/dashboard/ultimas-movimentacoes").param("limit", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].produtoNome").exists())
                .andExpect(jsonPath("$[0].tipo").exists());
    }
}
