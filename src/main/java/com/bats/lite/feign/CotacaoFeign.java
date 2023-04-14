package com.bats.lite.feign;

import com.bats.lite.entity.Cotacao;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "CotacaoClient", url = "${request.feign.url-cotacao}")
public interface CotacaoFeign {
    @GetMapping(value = "${request.feign.tipo}${request.feign.ultimas}/USD-BRL,EUR-BRL,BTC-BRL")
    Object cotacaoList();

    @GetMapping(value = "${request.feign.tipo}${request.feign.diario}BTC-BRL/{days}")
    List<Cotacao> cotacaoPerDate(@PathVariable int days);

    @GetMapping(value = "BTC-BRL/{days}")
    List<Cotacao> cotacaoBewtweenDates(@PathVariable int days,@RequestParam String start_date,@RequestParam String end_date);
}
