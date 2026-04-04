package com.gregory.controleestoque.controller;

import com.gregory.controleestoque.model.Produto;
import com.gregory.controleestoque.repository.FornecedorRepository;
import com.gregory.controleestoque.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.gregory.controleestoque.dto.ProdutoResponseDTO;
import com.gregory.controleestoque.model.Fornecedor;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;

    public ProdutoController(ProdutoRepository produtoRepository, FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @GetMapping // lista os produtos
    public List<ProdutoResponseDTO> listar() {
        List<Produto> produtos = produtoRepository.findAll();
        List<ProdutoResponseDTO> resposta = new ArrayList<>();

        for(Produto produto : produtos) {
            resposta.add(converterParaDTO(produto));
        }
        return resposta;
    }

    @GetMapping("/{id}") // retorna o produto para o front
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id)  {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            ProdutoResponseDTO dto = converterParaDTO(produtoOptional.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping // retorna um cadastro no padrao DTO para o front
    public ProdutoResponseDTO cadastrar(@RequestBody Produto produto) {
        validarProduto(produto);

        Produto produtoSalvo = produtoRepository.save(produto);
        return converterParaDTO(produtoSalvo);
    }


    @PutMapping("/{id}") // retorna o atualizar no padrao DTO para o front
    public ProdutoResponseDTO atualizar(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        validarProduto(produtoAtualizado);

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));


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

    @DeleteMapping("/{id}") // Deletar produto
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Optional<Produto> produtoOptional = produtoRepository.findById(id);

        if (produtoOptional.isPresent()) {
            produtoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // meteodo para validar produto
    private void validarProduto(Produto produto) {
        if (produto.getFornecedorId() == null) {
            throw new RuntimeException("Fornecedor Obrigatorio");
        }

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome Obrigatorio");
        }

        if (!fornecedorRepository.existsById(produto.getFornecedorId())) {
            throw new RuntimeException("Fornecedor nao encontrado");
        }

        if (produto.getPreco() == null) {
            throw new RuntimeException("Preco Obrigatorio");
        }

        if (produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Preco nao pode ser negativo");
        }

        if (produto.getQuantidade() < 0) {
            throw new RuntimeException("Quantidade nao pode ser negativa");
        }

        if (produto.getEstoqueMinimo() < 0) {
            throw new RuntimeException("Estoque minimo nao pode ser negativo");
        }

        if (produto.getCategoria() == null || produto.getCategoria().trim().isEmpty()) {
            throw new RuntimeException("Categoria obrigatoria");
        }
    }

    // esse metodo serve para responder o json melhor para o front
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

