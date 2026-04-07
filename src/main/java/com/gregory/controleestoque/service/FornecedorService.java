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


    // teste do deletar
    public void deletar(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fornecedor nao encontrado"));

        if (produtoRepository.existsByFornecedorId(id)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                    "Nao e possivel excluir fornecedor com produtos vinculados"
            );
        }
        fornecedorRepository.delete(fornecedor);
    }

    // teste do atualizar
    public Fornecedor atualizar(Long id, Fornecedor dadosAtualizados) {

        if (dadosAtualizados.getNome() == null || dadosAtualizados.getNome().trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome e obrigatorio");
        }
        if (dadosAtualizados.getTelefone() == null || dadosAtualizados.getTelefone().trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone e obrigatorio");
        }
        if (dadosAtualizados.getEmail() == null || dadosAtualizados.getEmail().trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email e obrigatorio");
        }

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

    // teste do cadastrar
    public Fornecedor cadastrar(Fornecedor fornecedor){
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()){
            throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome e obrigatorio");
        }
        if (fornecedor.getTelefone() == null || fornecedor.getTelefone().trim().isEmpty()){
            throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone e obrigatorio");
        }
        if (fornecedor.getEmail() == null || fornecedor.getEmail().trim().isEmpty()){
            throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Email e obrigatorio");
        }
        return fornecedorRepository.save(fornecedor);
    }

    //teste do bucarPorID
    public Fornecedor buscarPorId(Long id){
        return fornecedorRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Fornecedor nao encontrado"
            ));
    }

    //teste do listar
    public List<Fornecedor> listar() {
        return fornecedorRepository.findAll();
    }


}
