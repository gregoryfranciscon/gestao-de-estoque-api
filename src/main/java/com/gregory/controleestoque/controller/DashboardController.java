package com.gregory.controleestoque.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gregory.controleestoque.dto.dashboard.DashboardResumoResponse;
import com.gregory.controleestoque.dto.dashboard.ProdutoEstoqueBaixoResponse;
import com.gregory.controleestoque.dto.movimentacao.MovimentacaoResponse;
import com.gregory.controleestoque.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumo")
    public DashboardResumoResponse resumo() {
        return dashboardService.resumo();
    }

    @GetMapping("/estoque-baixo")
    public List<ProdutoEstoqueBaixoResponse> estoqueBaixo() {
        return dashboardService.produtosEstoqueBaixo();
    }

    @GetMapping("/ultimas-movimentacoes")
    public List<MovimentacaoResponse> ultimasMovimentacoes(@RequestParam(defaultValue = "5") int limit) {
        return dashboardService.ultimasMovimentacoes(limit);
    }
}
