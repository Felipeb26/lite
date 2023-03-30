package com.bats.lite.controller;

import com.bats.lite.dto.UserDTO;
import com.bats.lite.entity.PageDTO;
import com.bats.lite.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@Api("usuarios")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    @ApiOperation("retorna todos os usuarios")
    public ResponseEntity<PageDTO> showAllUsers(@RequestParam(required = false) Long id,
                                                @RequestParam(required = false) String nome,
                                                @RequestParam(required = false) String email,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size,
                                                @RequestParam(value = "sort", defaultValue = "ASC") Sort.Direction direction,
                                                @RequestParam(value = "propriedade", defaultValue = "nome") String props) {
        return ResponseEntity.ok(service.findByParamPaged(id, nome, email, dataInicial, dataFinal, PageRequest.of(page, size, direction, props)));
    }

    @GetMapping("/param")
    @ApiOperation("Pega usuario por parametro")
    public ResponseEntity<List<UserDTO>> showUserPerParam(@RequestParam(required = false) Long id,
                                                          @RequestParam(required = false) String nome,
                                                          @RequestParam(required = false) String email,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        return ResponseEntity.ok().body(service.findByParam(id, nome, email, dataInicial, dataFinal));
    }


    @PostMapping
    @ApiOperation("Salva usuario")
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(service.save(user));
    }

    @PutMapping
    @ApiOperation("Atualiza usuario")
    public ResponseEntity<UserDTO> updateUser(@RequestParam Long id, @RequestBody UserDTO user) {
        return ResponseEntity.ok(service.update(id, user));
    }

    @DeleteMapping
    @ApiOperation("Deletar usuario")
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        return ResponseEntity.ok(service.delete(id));
    }

}