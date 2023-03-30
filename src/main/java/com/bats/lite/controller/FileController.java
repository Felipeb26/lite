package com.bats.lite.controller;

import com.bats.lite.service.FileService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/files")
@Api("Controller dos Arquivos")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Object> saveFile(@RequestParam("files") MultipartFile[] files) {
        var zipFile = fileService.compressZip(files);
        var fileType = fileService.getMimeType(zipFile);

        UUID uuid = UUID.randomUUID();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(nonNull(fileType) ? fileType : "application/zip"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uuid + ".zip")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(zipFile.length))
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                .body(zipFile);

    }
}
