package com.bats.lite.controller;

import com.bats.lite.aop.files.FileGenerate;
import com.bats.lite.aop.files.FileType;
import com.bats.lite.configuration.FilesConfig;
import com.bats.lite.entity.Cotacao;
import com.bats.lite.service.CotacaoService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/cotacao")
@Api("Feign de api de cotacao")
public class CotacaoController {

    @Autowired
    private CotacaoService cotacaoService;
    @Autowired
    private FilesConfig filesConfig;

    @FileGenerate(ClassName = Cotacao.class, FILE_TYPE = FileType.PDF)
    @GetMapping("/arquivo")
    @ApiOperation("Regasta as cotação de acordo com as moedas informadas")
    public Object cotacaoList() {
        var file = cotacaoService.currency();
        return ResponseEntity.ok()
                .header("file-type", "excel")
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
