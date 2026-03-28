package com.gregory.controleestoque.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gregory.controleestoque.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("select p from Produto p join fetch p.fornecedor order by p.nome asc")
    List<Produto> findAllDetailedOrderByNomeAsc();

    @Query("""
            select p
            from Produto p
            join fetch p.fornecedor
            where lower(p.nome) like lower(concat('%', :nome, '%'))
            order by p.nome asc
            """)
    List<Produto> searchByNome(@Param("nome") String nome);

    @Query("select p from Produto p join fetch p.fornecedor where p.id = :id")
    Optional<Produto> findDetailedById(@Param("id") Long id);

    @Query("""
            select p
            from Produto p
            join fetch p.fornecedor
            where p.estoqueAtual <= p.estoqueMinimo
            order by p.nome asc
            """)
    List<Produto> findProdutosEstoqueBaixo();

    boolean existsByFornecedorId(Long fornecedorId);

    @Query("select count(p) from Produto p where p.estoqueAtual <= p.estoqueMinimo")
    long countProdutosEstoqueBaixo();
}
