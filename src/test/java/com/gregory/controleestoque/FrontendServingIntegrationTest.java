package com.gregory.controleestoque;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import com.gregory.controleestoque.support.IntegrationTestSupport;

class FrontendServingIntegrationTest extends IntegrationTestSupport {

    @Test
    void shouldServeWelcomePage() throws Exception {
        mockMvc.perform(get("/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("data-page=\"dashboard\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("./src/js/main.js")));
    }

    @Test
    void shouldServeFrontendPagesAndAssets() throws Exception {
        mockMvc.perform(get("/src/pages/produtos.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cadastrar Produto")));

        mockMvc.perform(get("/src/pages/fornecedores.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Fornecedores")));

        mockMvc.perform(get("/src/pages/movimentacoes.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("id=\"movement-form\"")));

        mockMvc.perform(get("/src/js/main.js"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("bootstrap()")));

        mockMvc.perform(get("/src/css/app.css"))
                .andExpect(status().isOk());
    }
}
