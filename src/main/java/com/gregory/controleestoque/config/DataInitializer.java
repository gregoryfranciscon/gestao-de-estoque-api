package com.gregory.controleestoque.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gregory.controleestoque.model.Fornecedor;
import com.gregory.controleestoque.model.Movimentacao;
import com.gregory.controleestoque.model.Produto;
import com.gregory.controleestoque.model.TipoMovimentacao;
import com.gregory.controleestoque.repository.FornecedorRepository;
import com.gregory.controleestoque.repository.MovimentacaoRepository;
import com.gregory.controleestoque.repository.ProdutoRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(
            FornecedorRepository fornecedorRepository,
            ProdutoRepository produtoRepository,
            MovimentacaoRepository movimentacaoRepository
    ) {
        return args -> {
            if (fornecedorRepository.count() > 0 || produtoRepository.count() > 0 || movimentacaoRepository.count() > 0) {
                return;
            }

            Fornecedor fornecedorAbc = new Fornecedor("Fornecedor ABC", "(11) 98765-4321", "contato@abc.com");
            Fornecedor distribuidoraXyz = new Fornecedor("Distribuidora XYZ", "(21) 99876-5432", "vendas@xyz.com");
            Fornecedor importadoraGlobal = new Fornecedor("Importadora Global", "(31) 97654-3210", "info@global.com");
            fornecedorRepository.saveAll(List.of(fornecedorAbc, distribuidoraXyz, importadoraGlobal));

            Produto notebook = new Produto(
                    "Notebook Dell Inspiron",
                    "Eletronicos",
                    new BigDecimal("3500.00"),
                    15,
                    10,
                    fornecedorAbc
            );
            Produto mouse = new Produto(
                    "Mouse Logitech MX",
                    "Perifericos",
                    new BigDecimal("250.00"),
                    45,
                    10,
                    fornecedorAbc
            );
            Produto teclado = new Produto(
                    "Teclado Mecanico RGB",
                    "Perifericos",
                    new BigDecimal("450.00"),
                    8,
                    15,
                    distribuidoraXyz
            );
            Produto monitor = new Produto(
                    "Monitor LG 27 polegadas",
                    "Eletronicos",
                    new BigDecimal("1200.00"),
                    3,
                    5,
                    distribuidoraXyz
            );
            Produto webcam = new Produto(
                    "Webcam Logitech C920",
                    "Perifericos",
                    new BigDecimal("550.00"),
                    22,
                    8,
                    fornecedorAbc
            );
            Produto ssd = new Produto(
                    "SSD Samsung 1TB",
                    "Armazenamento",
                    new BigDecimal("620.00"),
                    2,
                    8,
                    importadoraGlobal
            );
            produtoRepository.saveAll(List.of(notebook, mouse, teclado, monitor, webcam, ssd));

            movimentacaoRepository.saveAll(List.of(
                    new Movimentacao(notebook, TipoMovimentacao.ENTRADA, 10, LocalDateTime.of(2026, 3, 25, 10, 30), "Carga inicial"),
                    new Movimentacao(mouse, TipoMovimentacao.SAIDA, 5, LocalDateTime.of(2026, 3, 25, 14, 20), "Venda"),
                    new Movimentacao(teclado, TipoMovimentacao.ENTRADA, 20, LocalDateTime.of(2026, 3, 26, 9, 15), "Reposicao"),
                    new Movimentacao(monitor, TipoMovimentacao.SAIDA, 2, LocalDateTime.of(2026, 3, 26, 11, 45), "Venda"),
                    new Movimentacao(webcam, TipoMovimentacao.ENTRADA, 15, LocalDateTime.of(2026, 3, 26, 15, 30), "Reposicao"),
                    new Movimentacao(ssd, TipoMovimentacao.SAIDA, 6, LocalDateTime.of(2026, 3, 27, 8, 0), "Venda"))
            );
        };
    }
}
