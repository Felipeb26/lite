package com.bats.lite.controller;

import com.bats.lite.dto.BancoDTO;
import com.bats.lite.entity.Banco;
import com.bats.lite.entity.PageDTO;
import com.bats.lite.service.BancoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/banco", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "bancos")
public class BancoController {

    @Autowired
    private BancoService bancoService;

    @GetMapping("/")
    @Operation(summary = "Traz de forma paginada todos os bancos salvos")
    public ResponseEntity<PageDTO> findAllPage(@RequestParam(value = "id", required = false) Long id,
                                               @RequestParam(value = "nome", required = false) String nome,
                                               @RequestParam(value = "agencia", required = false) Long agencia,
                                               @RequestParam(value = "conta", required = false) Long conta,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size,
                                               @RequestParam(value = "sort", defaultValue = "ASC") Sort.Direction direction) {

        return ResponseEntity.ok().body(bancoService.findPaginate(id, nome, agencia, conta,
                PageRequest.of(page, size, direction, "dataCadastro")));
    }

    @GetMapping("/banco")
    @Operation(summary = "Traz um banco quando informado parametros")
    public ResponseEntity<List<BancoDTO>> findAll(@RequestParam(value = "id", required = false) Long id,
                                                  @RequestParam(value = "nome", required = false) String nome,
                                                  @RequestParam(value = "agencia", required = false) Long agencia,
                                                  @RequestParam(value = "conta", required = false) Long conta) {

        return ResponseEntity.ok().body(bancoService.findBank(id, nome, agencia, conta));
    }

    @PostMapping
    @Operation(summary = "Salva o banco")
    public ResponseEntity<Banco> saveBank(@RequestBody BancoDTO banco) {
        return new ResponseEntity<>(bancoService.saveBanco(banco), CREATED);
    }

    @PutMapping
    @Operation(summary = "Atualiza banco")
    public ResponseEntity<Banco> updateBank(@RequestParam Long id, @Validated @RequestBody Banco banco) {
        return new ResponseEntity<>(bancoService.updateBanco(id, banco), ACCEPTED);
    }

    @DeleteMapping
    @Operation(summary = "Deleta um banco")
    public ResponseEntity<Object> deleteBank(@RequestParam Long id) {
        return new ResponseEntity<>(bancoService.deleteBanco(id), OK);
    }

}
