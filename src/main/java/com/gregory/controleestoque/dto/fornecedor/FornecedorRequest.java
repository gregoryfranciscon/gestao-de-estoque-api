package com.gregory.controleestoque.dto.fornecedor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FornecedorRequest(
        @NotBlank(message = "Nome do fornecedor e obrigatorio")
        @Size(max = 120, message = "Nome do fornecedor deve ter no maximo 120 caracteres")
        String nome,

        @Size(max = 20, message = "Telefone deve ter no maximo 20 caracteres")
        String telefone,

        @Email(message = "Email do fornecedor deve ser valido")
        @Size(max = 120, message = "Email deve ter no maximo 120 caracteres")
        String email
) {
}
