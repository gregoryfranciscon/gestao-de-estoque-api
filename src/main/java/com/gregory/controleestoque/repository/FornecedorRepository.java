package com.gregory.controleestoque.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gregory.controleestoque.model.Fornecedor;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    List<Fornecedor> findAllByOrderByNomeAsc();
}
