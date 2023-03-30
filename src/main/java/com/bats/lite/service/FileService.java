package com.bats.lite.service;

import com.bats.lite.exceptions.BatsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.Objects.nonNull;

@Service
public class FileService {

    private String today() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        return dateTime.format(formatter);
    }

    public byte[] compressZip(MultipartFile[] multipartFiles) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(baos)) {
            for (MultipartFile multipartFile : multipartFiles) {

                UUID uuid = UUID.randomUUID();

                InputStream inputStream = multipartFile.getInputStream();
                ZipEntry zipEntry = new ZipEntry(nonNull(multipartFile.getOriginalFilename()) ? multipartFile.getOriginalFilename() : uuid.toString());
                zipOutputStream.putNextEntry(zipEntry);
                byte[] bytes = new byte[2048];
                int length;
                while ((length = inputStream.read(bytes)) >= 0) {
                    zipOutputStream.write(bytes, 0, length);
                }
                inputStream.close();
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File multipartfileToFile(MultipartFile multipartFile) throws IOException {
            UUID uuid = UUID.randomUUID();
            File file = new File(nonNull(multipartFile.getOriginalFilename()) ? multipartFile.getOriginalFilename() : uuid.toString());
        multipartFile.transferTo(file);
        return file;
    }


    public String getMimeType(byte[] bytes) {
        try {
            InputStream is = new ByteArrayInputStream(bytes);
            return URLConnection.guessContentTypeFromStream(is);
        } catch (Exception e) {
            return String.valueOf(new BatsException(HttpStatus.BAD_REQUEST, "nã foi possivel ver o tipo do arquivo", e));
        }
    }

    public List<String> mimeTypes(byte[] bytes) {
        List<String> types = new ArrayList<>();
        types.add("application/zip");
        types.add((getMimeType(bytes)));
        types.add(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return types;
    }

}
