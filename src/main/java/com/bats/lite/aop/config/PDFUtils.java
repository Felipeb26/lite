package com.bats.lite.aop.config;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;

import static java.util.Objects.nonNull;

@Service
public class PDFUtils {

    public void setCellStyle(Cell cell) throws IOException {
        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        cell.setFontSize(12).setFont(font);
    }

    public Map<Object, Object> recursiveSearch(String keys, JSONObject jsonObject, Map<Object, Object> map) throws JSONException {

        if (nonNull(keys) && !keys.isBlank()) {
            Object value = jsonObject.get(keys);
            if (value instanceof JSONObject) return recursiveSearch(null, (JSONObject) value, map);
            if (value instanceof String) map.put(keys, value);
        } else {
            Iterator interator = jsonObject.keys();
            while (interator.hasNext()) {
                String key = (String) interator.next();
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) return recursiveSearch(null, (JSONObject) value, map);
                if (value instanceof String) map.put(key, value);
            }
        }
        return map;
    }

    public void setWatermark(PdfDocument pdfDocument, Document document, String watermark) throws MalformedURLException {
        if (watermark == null || watermark.isBlank())
            return;
        String path = String.format("src/main/resources/%s", watermark);
        if (path == null)
            return;
        ImageData imageData = ImageDataFactory.create(path);
        Image image = new Image(imageData);
        image.setFixedPosition(pdfDocument.getDefaultPageSize().getWidth() / 2 - 250, pdfDocument.getDefaultPageSize().getHeight() / 2 - 250);
        image.setOpacity(0.5f);
        document.add(image);
    }

    public String camelString(String string) {
        if (string.lastIndexOf("_") > 0) {
            String[] words = string.split("_");
            for (int i = 0; i < words.length; i++) {
                words[i] = strings(words[i]);
            }
            return String.join("", words);
        } else {
            return strings(string);
        }
    }

    private String strings(String strings) {
        String first = strings.substring(0, 1);
        String rest = strings.substring(1);
        return first.toUpperCase() + rest;
    }
}
