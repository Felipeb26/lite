package com.bats.lite.service;

import com.bats.lite.entity.Cotacao;
import com.bats.lite.entity.Currency;
import com.bats.lite.feign.CotacaoFeign;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public interface CotacaoService {

    public Object currency();

    List<Cotacao> perDate(int days);

    List<Cotacao> betweenDates(int days, LocalDate dataInicial, LocalDate dataFinal);


}
