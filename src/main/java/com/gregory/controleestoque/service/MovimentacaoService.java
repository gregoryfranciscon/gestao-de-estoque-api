package com.gregory.controleestoque.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregory.controleestoque.dto.movimentacao.MovimentacaoRequest;
import com.gregory.controleestoque.dto.movimentacao.MovimentacaoResponse;
import com.gregory.controleestoque.exception.BusinessException;
import com.gregory.controleestoque.exception.ResourceNotFoundException;
import com.gregory.controleestoque.model.Movimentacao;
import com.gregory.controleestoque.model.Produto;
import com.gregory.controleestoque.model.TipoMovimentacao;
import com.gregory.controleestoque.repository.MovimentacaoRepository;

@Service
@Transactional(readOnly = true)
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoService produtoService;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, ProdutoService produtoService) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoService = produtoService;
    }

    public List<MovimentacaoResponse> listar(Long produtoId, TipoMovimentacao tipo) {
        return movimentacaoRepository.findAllDetailedOrderByDataHoraDesc()
                .stream()
                .filter(movimentacao -> produtoId == null || movimentacao.getProduto().getId().equals(produtoId))
                .filter(movimentacao -> tipo == null || movimentacao.getTipo() == tipo)
                .map(this::toResponse)
                .toList();
    }

    public MovimentacaoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public MovimentacaoResponse criar(MovimentacaoRequest request) {
        Produto produto = produtoService.buscarEntidade(request.produtoId());
        int quantidade = request.quantidade();

        if (request.tipo() == TipoMovimentacao.SAIDA && produto.getEstoqueAtual() < quantidade) {
            throw new BusinessException("Estoque insuficiente para registrar a saida");
        }

        int novoEstoque = request.tipo() == TipoMovimentacao.ENTRADA
                ? produto.getEstoqueAtual() + quantidade
                : produto.getEstoqueAtual() - quantidade;

        produto.setEstoqueAtual(novoEstoque);

        Movimentacao movimentacao = new Movimentacao(
                produto,
                request.tipo(),
                quantidade,
                request.dataHora() == null ? LocalDateTime.now() : request.dataHora(),
                request.observacao()
        );

        return toResponse(movimentacaoRepository.save(movimentacao));
    }

    public Movimentacao buscarEntidade(Long id) {
        return movimentacaoRepository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimentacao nao encontrada: " + id));
    }

    private MovimentacaoResponse toResponse(Movimentacao movimentacao) {
        return new MovimentacaoResponse(
                movimentacao.getId(),
                movimentacao.getProduto().getId(),
                movimentacao.getProduto().getNome(),
                movimentacao.getTipo(),
                movimentacao.getQuantidade(),
                movimentacao.getDataHora(),
                movimentacao.getObservacao()
        );
    }
}
