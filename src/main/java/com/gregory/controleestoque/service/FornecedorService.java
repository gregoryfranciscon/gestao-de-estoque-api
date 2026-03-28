package com.gregory.controleestoque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregory.controleestoque.dto.fornecedor.FornecedorRequest;
import com.gregory.controleestoque.dto.fornecedor.FornecedorResponse;
import com.gregory.controleestoque.exception.BusinessException;
import com.gregory.controleestoque.exception.ResourceNotFoundException;
import com.gregory.controleestoque.model.Fornecedor;
import com.gregory.controleestoque.repository.FornecedorRepository;
import com.gregory.controleestoque.repository.ProdutoRepository;

@Service
@Transactional(readOnly = true)
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository, ProdutoRepository produtoRepository) {
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<FornecedorResponse> listar() {
        return fornecedorRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public FornecedorResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public FornecedorResponse criar(FornecedorRequest request) {
        Fornecedor fornecedor = new Fornecedor(
                request.nome(),
                request.telefone(),
                request.email()
        );
        return toResponse(fornecedorRepository.save(fornecedor));
    }

    @Transactional
    public FornecedorResponse atualizar(Long id, FornecedorRequest request) {
        Fornecedor fornecedor = buscarEntidade(id);
        fornecedor.setNome(request.nome());
        fornecedor.setTelefone(request.telefone());
        fornecedor.setEmail(request.email());
        return toResponse(fornecedor);
    }

    @Transactional
    public void deletar(Long id) {
        if (produtoRepository.existsByFornecedorId(id)) {
            throw new BusinessException("Fornecedor possui produtos vinculados e nao pode ser removido");
        }
        if (!fornecedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fornecedor nao encontrado: " + id);
        }
        fornecedorRepository.deleteById(id);
    }

    public Fornecedor buscarEntidade(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor nao encontrado: " + id));
    }

    private FornecedorResponse toResponse(Fornecedor fornecedor) {
        return new FornecedorResponse(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getTelefone(),
                fornecedor.getEmail()
        );
    }
}
