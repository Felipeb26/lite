package com.bats.lite.controller;

import com.bats.lite.entity.Cotacao;
import com.bats.lite.service.CotacaoService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/cotacao", produces = {MediaType.APPLICATION_JSON_VALUE, "*"})
@Api("Feign de api de cotacao")
public class CotacaoController {

    @Autowired
    private CotacaoService cotacaoService;

    @GetMapping
    @ApiOperation("Regasta as cotação de acordo com as moedas informadas")
    public Object cotacaoList() {
        UUID uuid = UUID.randomUUID();
        var file = cotacaoService.currencies();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uuid + ".pdf")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(((byte[]) file).length))
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                .body(file);

    }

    @GetMapping("date")
    @ApiOperation("Traz lista de cotacoes do dia de acordo com moeda e dias")
    public List<Cotacao> cotacoaPorData(@RequestParam(value = "days", defaultValue = "7") int days) {
        return cotacaoService.perDate(days);
    }

    @GetMapping("between-dates")
    @ApiOperation("Traz lista de itens de acordo com quantidade e entre datas")
    public List<Cotacao> cotacaoEntreDatas(@RequestParam(value = "days", defaultValue = "7") int days,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @JsonFormat(pattern = "yyyy-MM-dd")
                                           LocalDate dataInicial,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @JsonFormat(pattern = "yyyy-MM-dd")
                                           LocalDate dataFinal) {
        return cotacaoService.betweenDates(days, dataInicial, dataFinal);
    }
}
