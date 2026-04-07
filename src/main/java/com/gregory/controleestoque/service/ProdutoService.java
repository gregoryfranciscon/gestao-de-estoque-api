package com.gregory.controleestoque.service;

import com.gregory.controleestoque.dto.ProdutoResponseDTO;
import com.gregory.controleestoque.model.Fornecedor;
import com.gregory.controleestoque.model.Produto;
import com.gregory.controleestoque.repository.FornecedorRepository;
import com.gregory.controleestoque.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;

    public ProdutoService(ProdutoRepository produtoRepository, FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<ProdutoResponseDTO> listar() {
        List<Produto> produtos = produtoRepository.findAll();
        List<ProdutoResponseDTO> resposta = new ArrayList<>();

        for (Produto produto : produtos) {
            resposta.add(converterParaDTO(produto));
        }

        return resposta;
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Produto nao encontrado"
            ));

        return converterParaDTO(produto);
    }

    public ProdutoResponseDTO cadastrar(Produto produto) {
        validarProduto(produto);
        Produto produtoSalvo = produtoRepository.save(produto);
        return converterParaDTO(produtoSalvo);
    }

    public ProdutoResponseDTO atualizar(Long id, Produto produtoAtualizado) {
        validarProduto(produtoAtualizado);

        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Produto nao encontrado"
            ));

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setQuantidade(produtoAtualizado.getQuantidade());
        produto.setEstoqueMinimo(produtoAtualizado.getEstoqueMinimo());
        produto.setCategoria(produtoAtualizado.getCategoria());
        produto.setFornecedorId(produtoAtualizado.getFornecedorId());

        Produto produtoSalvo = produtoRepository.save(produto);
        return converterParaDTO(produtoSalvo);
    }

    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Produto nao encontrado"
            ));

        produtoRepository.delete(produto);
    }

    private void validarProduto(Produto produto) {
        if (produto.getFornecedorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fornecedor obrigatorio");
        }

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome obrigatorio");
        }

        if (!fornecedorRepository.existsById(produto.getFornecedorId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fornecedor nao encontrado");
        }

        if (produto.getPreco() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preco obrigatorio");
        }

        if (produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preco nao pode ser negativo");
        }

        if (produto.getQuantidade() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade nao pode ser negativa");
        }

        if (produto.getEstoqueMinimo() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque minimo nao pode ser negativo");
        }

        if (produto.getCategoria() == null || produto.getCategoria().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria obrigatoria");
        }
    }

    private ProdutoResponseDTO converterParaDTO(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();

        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setQuantidade(produto.getQuantidade());
        dto.setEstoqueMinimo(produto.getEstoqueMinimo());
        dto.setCategoria(produto.getCategoria());
        dto.setFornecedorId(produto.getFornecedorId());

        if (produto.getQuantidade() <= produto.getEstoqueMinimo()) {
            dto.setStatusEstoque("Estoque Baixo");
        } else {
            dto.setStatusEstoque("Normal");
        }

        if (produto.getFornecedorId() != null) {
            Optional<Fornecedor> fornecedorOptional = fornecedorRepository.findById(produto.getFornecedorId());

            if (fornecedorOptional.isPresent()) {
                dto.setNomeFornecedor(fornecedorOptional.get().getNome());
            }
        }

        return dto;
    }
}
