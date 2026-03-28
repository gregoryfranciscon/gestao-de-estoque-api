package com.gregory.controleestoque.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gregory.controleestoque.dto.movimentacao.MovimentacaoRequest;
import com.gregory.controleestoque.dto.movimentacao.MovimentacaoResponse;
import com.gregory.controleestoque.model.TipoMovimentacao;
import com.gregory.controleestoque.service.MovimentacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping
    public List<MovimentacaoResponse> listar(
            @RequestParam(required = false) Long produtoId,
            @RequestParam(required = false) TipoMovimentacao tipo
    ) {
        return movimentacaoService.listar(produtoId, tipo);
    }

    @GetMapping("/{id}")
    public MovimentacaoResponse buscarPorId(@PathVariable Long id) {
        return movimentacaoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoResponse criar(@Valid @RequestBody MovimentacaoRequest request) {
        return movimentacaoService.criar(request);
    }
}
