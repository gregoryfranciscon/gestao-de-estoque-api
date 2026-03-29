package com.gregory.controleestoque.controller;

import com.gregory.controleestoque.model.Produto;
import com.gregory.controleestoque.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Produto> buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id);
    }

    @PostMapping
    public Produto cadastrar(@RequestBody Produto produto) {
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
