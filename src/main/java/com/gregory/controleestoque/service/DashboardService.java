package com.gregory.controleestoque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregory.controleestoque.dto.dashboard.DashboardResumoResponse;
import com.gregory.controleestoque.dto.dashboard.ProdutoEstoqueBaixoResponse;
import com.gregory.controleestoque.dto.movimentacao.MovimentacaoResponse;
import com.gregory.controleestoque.repository.FornecedorRepository;
import com.gregory.controleestoque.repository.MovimentacaoRepository;
import com.gregory.controleestoque.repository.ProdutoRepository;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoService produtoService;
    private final MovimentacaoService movimentacaoService;

    public DashboardService(
            ProdutoRepository produtoRepository,
            FornecedorRepository fornecedorRepository,
            MovimentacaoRepository movimentacaoRepository,
            ProdutoService produtoService,
            MovimentacaoService movimentacaoService
    ) {
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoService = produtoService;
        this.movimentacaoService = movimentacaoService;
    }

    public DashboardResumoResponse resumo() {
        return new DashboardResumoResponse(
                produtoRepository.count(),
                fornecedorRepository.count(),
                movimentacaoRepository.count(),
                produtoRepository.countProdutosEstoqueBaixo()
        );
    }

    public List<ProdutoEstoqueBaixoResponse> produtosEstoqueBaixo() {
        return produtoService.listarEstoqueBaixo();
    }

    public List<MovimentacaoResponse> ultimasMovimentacoes(int limit) {
        int safeLimit = Math.max(1, limit);
        return movimentacaoService.listar(null, null)
                .stream()
                .limit(safeLimit)
                .toList();
    }
}
