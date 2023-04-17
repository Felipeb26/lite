package com.bats.lite.service;

import java.util.Map;

public interface ThymeleafService {

    String createContent(String template, Map<String, String> variable);
}
