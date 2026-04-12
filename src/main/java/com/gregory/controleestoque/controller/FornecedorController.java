package com.gregory.controleestoque.controller;

import com.gregory.controleestoque.service.FornecedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gregory.controleestoque.model.Fornecedor;


import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @GetMapping
    public ResponseEntity<List<Fornecedor>> listar(){
        return ResponseEntity.ok(fornecedorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarPorId(@PathVariable Long id){
        Fornecedor fornecedor = fornecedorService.buscarPorId(id);
        return ResponseEntity.ok(fornecedor);
    }

    @PostMapping
    public ResponseEntity<Fornecedor> cadastrar(@RequestBody Fornecedor fornecedor) {
        Fornecedor fornecedorSalvo = fornecedorService.cadastrar(fornecedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> atualizar(@PathVariable Long id, @RequestBody Fornecedor dadosAtualizados) {
        Fornecedor fornecedorAtualizado = fornecedorService.atualizar(id, dadosAtualizados);
        return ResponseEntity.ok(fornecedorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
