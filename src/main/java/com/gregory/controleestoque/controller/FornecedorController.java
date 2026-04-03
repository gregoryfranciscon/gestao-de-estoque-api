package com.gregory.controleestoque.controller;

import com.gregory.controleestoque.repository.FornecedorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gregory.controleestoque.model.Fornecedor;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
    @RestController
    @RequestMapping("/fornecedores")
    public class FornecedorController {

        private final FornecedorRepository fornecedorRepository;

        public FornecedorController(FornecedorRepository fornecedorRepository) {
            this.fornecedorRepository = fornecedorRepository;

        }

        @GetMapping
        public List<Fornecedor> listar(){
            return fornecedorRepository.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Fornecedor> buscarPorId(@PathVariable Long id){
            Optional<Fornecedor> fornecedor = fornecedorRepository.findById(id);

            if(fornecedor.isPresent()){
                return  ResponseEntity.ok(fornecedor.get());
            }
            return ResponseEntity.notFound().build();
        }

        @PostMapping
    public ResponseEntity<Fornecedor> cadastrar(@RequestBody Fornecedor fornecedor){

            if (fornecedor.getNome() == null || fornecedor.getNome().trim().isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            if (fornecedor.getTelefone() == null || fornecedor.getTelefone().trim().isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            if (fornecedor.getEmail() == null || fornecedor.getEmail().trim().isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
            return ResponseEntity.ok(fornecedorSalvo);
        }

        @PutMapping("/{id}") // Para atualizar o fornecedor
    public ResponseEntity<Fornecedor> atualizar(@PathVariable Long id, @RequestBody Fornecedor dadosAtualizados){

            if (dadosAtualizados.getNome() == null || dadosAtualizados.getNome().trim().isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            if (dadosAtualizados.getTelefone() == null || dadosAtualizados.getTelefone().trim().isEmpty()){
                return ResponseEntity.badRequest().build();
            }
            if (dadosAtualizados.getEmail() == null || dadosAtualizados.getEmail().trim().isEmpty()){
                return ResponseEntity.badRequest().build();
            }

            Optional<Fornecedor> fornecedorOptional = fornecedorRepository.findById(id);

            if(fornecedorOptional.isPresent()){
                Fornecedor fornecedor = fornecedorOptional.get();

                fornecedor.setNome(dadosAtualizados.getNome());
                fornecedor.setTelefone(dadosAtualizados.getTelefone());
                fornecedor.setEmail(dadosAtualizados.getEmail());

                Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
                return ResponseEntity.ok(fornecedorSalvo);
            }
            return ResponseEntity.notFound().build();
        }

        @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
            Optional<Fornecedor> fornecedorOptional = fornecedorRepository.findById(id);

            if(fornecedorOptional.isPresent()){
                fornecedorRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }

}
