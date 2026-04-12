package com.gregory.controleestoque.service;

import com.gregory.controleestoque.model.Fornecedor;
import com.gregory.controleestoque.repository.FornecedorRepository;
import com.gregory.controleestoque.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository, ProdutoRepository produtoRepository) {
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
    }


    // Deletar fornecedor
    public void deletar(Long id) {

        Fornecedor fornecedor = fornecedorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Fornecedor nao encontrado"
            ));

        if (produtoRepository.existsByFornecedorId(id)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Nao e possivel excluir fornecedor com produtos vinculados"
            );
        }
        fornecedorRepository.delete(fornecedor);
    }

    // Atualizar fornecedor
    public Fornecedor atualizar(Long id, Fornecedor dadosAtualizados) {

        validarFornecedor(dadosAtualizados);

        Fornecedor fornecedor = fornecedorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Fornecedor nao encontrado"
            ));

        fornecedor.setNome(dadosAtualizados.getNome());
        fornecedor.setTelefone(dadosAtualizados.getTelefone());
        fornecedor.setEmail(dadosAtualizados.getEmail());

        return fornecedorRepository.save(fornecedor);

    }

    // Cadastrar fornecedor
    public Fornecedor cadastrar(Fornecedor fornecedor){
        validarFornecedor(fornecedor);
        return fornecedorRepository.save(fornecedor);
    }

    // Buscar fornecedor por ID
    public Fornecedor buscarPorId(Long id){
        return fornecedorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Fornecedor nao encontrado"
            ));
    }

    // Listar fornecedores
    public List<Fornecedor> listar() {
        return fornecedorRepository.findAll();
    }

    // Validar dados do fornecedor
    private void validarFornecedor(Fornecedor fornecedor) {
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome e obrigatorio");
        }
        if (fornecedor.getTelefone() == null || fornecedor.getTelefone().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone e obrigatorio");
        }
        if (fornecedor.getEmail() == null || fornecedor.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email e obrigatorio");
        }
    }


}
