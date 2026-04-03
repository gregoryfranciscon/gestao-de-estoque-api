package com.gregory.controleestoque.controller;

import com.gregory.controleestoque.model.Produto;
import com.gregory.controleestoque.repository.FornecedorRepository;
import com.gregory.controleestoque.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;



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

    @GetMapping
    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id)  {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isPresent()) {
            return ResponseEntity.ok(produto.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Produto cadastrar(@RequestBody Produto produto) {
        if (produto.getFornecedorId() == null) {
            throw new RuntimeException("Fornecedor Obrigatorio");
        }
        if (!fornecedorRepository.existsById(produto.getFornecedorId())) {
            throw new RuntimeException(("Fornecedor nao encontrado"));
        }
        return produtoRepository.save(produto);
    }

    @PostMapping("/teste")
    public Produto criarProdutoTeste() {
        Produto produto = new Produto();
        produto.setNome("Produto Teste");
        produto.setDescricao("Produto basico para teste");
        produto.setPreco(new BigDecimal("9.90"));
        produto.setQuantidade(10);
        produto.setEstoqueMinimo(2);
        produto.setCategoria("Teste");
        produto.setFornecedorId(1L);
        return produtoRepository.save(produto);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {

        if (produtoAtualizado.getFornecedorId() == null) {
            throw new RuntimeException("Fornecedor Obrigatorio");
        }
        if (!fornecedorRepository.existsById(produtoAtualizado.getFornecedorId())) {
            throw new RuntimeException(("Fornecedor nao encontrado"));
        }

        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));

        produto.setNome(produtoAtualizado.getNome());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setQuantidade(produtoAtualizado.getQuantidade());
        produto.setEstoqueMinimo(produtoAtualizado.getEstoqueMinimo());
        produto.setCategoria(produtoAtualizado.getCategoria());
        produto.setFornecedorId(produtoAtualizado.getFornecedorId());

        return produtoRepository.save(produto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        produtoRepository.deleteById(id);
    }
}

