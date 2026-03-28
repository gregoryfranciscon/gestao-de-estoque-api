package com.gregory.controleestoque;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import com.gregory.controleestoque.dto.fornecedor.FornecedorRequest;
import com.gregory.controleestoque.support.IntegrationTestSupport;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FornecedorControllerIntegrationTest extends IntegrationTestSupport {

    @Test
    void shouldListSeededSuppliers() throws Exception {
        mockMvc.perform(get("/api/fornecedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].nome").exists());
    }

    @Test
    void shouldCreateUpdateAndDeleteSupplierWithoutProducts() throws Exception {
        FornecedorRequest request = new FornecedorRequest("Fornecedor Teste", "(11) 90000-0000", "teste@fornecedor.com");

        String response = mockMvc.perform(post("/api/fornecedores")
                        .contentType(jsonContentType())
                        .content(json(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Fornecedor Teste"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long fornecedorId = objectMapper.readTree(response).get("id").asLong();

        FornecedorRequest update = new FornecedorRequest("Fornecedor Teste Atualizado", "(11) 91111-1111", "atualizado@fornecedor.com");

        mockMvc.perform(put("/api/fornecedores/{id}", fornecedorId)
                        .contentType(jsonContentType())
                        .content(json(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fornecedor Teste Atualizado"));

        mockMvc.perform(delete("/api/fornecedores/{id}", fornecedorId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/fornecedores/{id}", fornecedorId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Fornecedor nao encontrado: " + fornecedorId));
    }

    @Test
    void shouldRejectSupplierDeletionWhenThereAreLinkedProducts() throws Exception {
        mockMvc.perform(delete("/api/fornecedores/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Fornecedor possui produtos vinculados e nao pode ser removido"));
    }

    @Test
    void shouldValidateSupplierPayload() throws Exception {
        FornecedorRequest request = new FornecedorRequest("", "(11) 90000-0000", "email-invalido");

        mockMvc.perform(post("/api/fornecedores")
                        .contentType(jsonContentType())
                        .content(json(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Falha de validacao"))
                .andExpect(jsonPath("$.validationErrors.nome").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists());
    }
}
