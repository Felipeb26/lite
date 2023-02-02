package com.bats.lite.aop.files;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {

    PDF("pdf"),
    CSV("csv"),
    EXCEL("excel");

    private final String fileType;
}
