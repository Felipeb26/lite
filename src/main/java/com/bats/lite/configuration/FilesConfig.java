package com.bats.lite.configuration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLConnection;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Service
public class FilesConfig {
    private static final String JSON = "application/json";

    private static String mimeType(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(baos);
        os.writeObject(obj);

        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        String mimeType = URLConnection.guessContentTypeFromStream(is);
        if (nonNull(mimeType))
            return mimeType;
        return JSON;
    }

    public String discoverFileType(Object obj) {
        try {
            var media_type = mimeType(obj);
            if (!MediaType.parseMediaType(media_type).equals(MediaType.APPLICATION_PDF)) {
                return MediaType.APPLICATION_JSON_VALUE;
            } else {
                return MediaType.APPLICATION_PDF_VALUE;
            }
        } catch (Exception e) {
            return MediaType.APPLICATION_JSON_VALUE;
        }
    }

    public int getBytesLength(Object file) {
        int bytes;
        if (file instanceof byte[])
            bytes = ((byte[]) file).length;
        else
            bytes = Objects.toString(file).getBytes().length;
        return bytes;
    }
}
