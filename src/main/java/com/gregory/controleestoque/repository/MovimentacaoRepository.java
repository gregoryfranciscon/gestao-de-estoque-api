package com.gregory.controleestoque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gregory.controleestoque.model.Movimentacao;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    @Query("""
            select m
            from Movimentacao m
            join fetch m.produto p
            join fetch p.fornecedor
            where m.id = :id
            """)
    Optional<Movimentacao> findDetailedById(@Param("id") Long id);

    @Query("""
            select m
            from Movimentacao m
            join fetch m.produto p
            join fetch p.fornecedor
            order by m.dataHora desc, m.id desc
            """)
    List<Movimentacao> findAllDetailedOrderByDataHoraDesc();

    boolean existsByProdutoId(Long produtoId);
}
