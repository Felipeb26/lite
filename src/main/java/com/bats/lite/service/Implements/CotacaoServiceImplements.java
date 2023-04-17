package com.bats.lite.service.Implements;

import com.bats.lite.entity.Cotacao;
import com.bats.lite.entity.Currency;
import com.bats.lite.feign.CotacaoFeign;
import com.bats.lite.service.CotacaoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CotacaoServiceImplements implements CotacaoService {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CotacaoFeign cotacaoFeign;

    public Object currency() {
        List<Currency> currencies = new ArrayList<>();

        LinkedHashMap currencyListMap = (LinkedHashMap) cotacaoFeign.cotacaoList();
        Object[] keys = currencyListMap.keySet().toArray();
        Object[] values = currencyListMap.values().toArray();
        for (int i = 0; i < currencyListMap.keySet().size(); i++) {
            Currency currency = Currency.builder()
                    .currency(keys[i].toString())
                    .cotacao(objectToCotacao(values[i])).build();
            currencies.add(currency);
        }
        return currencies;
    }

    public List<Cotacao> perDate(int days) {
        return cotacaoFeign.cotacaoPerDate(days);
    }

    public List<Cotacao> betweenDates(int days, LocalDate dataInicial, LocalDate dataFinal) {

        var inicialDate = dataInicial.toString().replace("-", "");
        var finalDate = dataFinal.toString().replace("-", "");

        return cotacaoFeign.cotacaoBewtweenDates(days, inicialDate, finalDate);
    }


    private Cotacao objectToCotacao(Object value) {
        return mapper.convertValue(value, new TypeReference<>() {
        });
    }
}
