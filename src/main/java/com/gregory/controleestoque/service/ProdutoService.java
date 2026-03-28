package com.gregory.controleestoque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregory.controleestoque.dto.dashboard.ProdutoEstoqueBaixoResponse;
import com.gregory.controleestoque.dto.produto.ProdutoRequest;
import com.gregory.controleestoque.dto.produto.ProdutoResponse;
import com.gregory.controleestoque.exception.BusinessException;
import com.gregory.controleestoque.exception.ResourceNotFoundException;
import com.gregory.controleestoque.model.Fornecedor;
import com.gregory.controleestoque.model.Produto;
import com.gregory.controleestoque.repository.MovimentacaoRepository;
import com.gregory.controleestoque.repository.ProdutoRepository;

@Service
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorService fornecedorService;
    private final MovimentacaoRepository movimentacaoRepository;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            FornecedorService fornecedorService,
            MovimentacaoRepository movimentacaoRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.fornecedorService = fornecedorService;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public List<ProdutoResponse> listar(String nome) {
        List<Produto> produtos = (nome == null || nome.isBlank())
                ? produtoRepository.findAllDetailedOrderByNomeAsc()
                : produtoRepository.searchByNome(nome.trim());

        return produtos.stream()
                .map(this::toResponse)
                .toList();
    }

    public ProdutoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public List<ProdutoEstoqueBaixoResponse> listarEstoqueBaixo() {
        return produtoRepository.findProdutosEstoqueBaixo()
                .stream()
                .map(produto -> new ProdutoEstoqueBaixoResponse(
                        produto.getId(),
                        produto.getNome(),
                        produto.getFornecedor().getNome(),
                        produto.getEstoqueAtual(),
                        produto.getEstoqueMinimo()
                ))
                .toList();
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        Fornecedor fornecedor = fornecedorService.buscarEntidade(request.fornecedorId());
        Produto produto = new Produto(
                request.nome(),
                request.categoria(),
                request.preco(),
                request.estoqueAtual(),
                request.estoqueMinimo(),
                fornecedor
        );
        return toResponse(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscarEntidade(id);
        Fornecedor fornecedor = fornecedorService.buscarEntidade(request.fornecedorId());

        produto.setNome(request.nome());
        produto.setCategoria(request.categoria());
        produto.setPreco(request.preco());
        produto.setEstoqueAtual(request.estoqueAtual());
        produto.setEstoqueMinimo(request.estoqueMinimo());
        produto.setFornecedor(fornecedor);

        return toResponse(produto);
    }

    @Transactional
    public void deletar(Long id) {
        if (movimentacaoRepository.existsByProdutoId(id)) {
            throw new BusinessException("Produto possui movimentacoes e nao pode ser removido");
        }
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto nao encontrado: " + id);
        }
        produtoRepository.deleteById(id);
    }

    public Produto buscarEntidade(Long id) {
        return produtoRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado: " + id));
    }

    private ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getPreco(),
                produto.getEstoqueAtual(),
                produto.getEstoqueMinimo(),
                produto.getFornecedor().getId(),
                produto.getFornecedor().getNome(),
                produto.getStatusEstoque()
        );
    }
}
