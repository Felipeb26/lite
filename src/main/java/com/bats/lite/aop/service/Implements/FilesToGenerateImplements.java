package com.bats.lite.aop.service.Implements;

import com.bats.lite.aop.config.CLassUtils;
import com.bats.lite.aop.config.ExcelUtils;
import com.bats.lite.aop.config.PDFUtils;
import com.bats.lite.aop.service.FilesToGenerate;
import com.google.gson.Gson;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.GrooveBorder;
import com.itextpdf.layout.element.Table;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class FilesToGenerateImplements implements FilesToGenerate {

    private static final String PDF_TYPE = "PDF";
    private static final String CSV_TYPE = "CSV";
    private static final String XLSX_TYPE = "XLSX";
    @Autowired
    private PDFUtils pdfUtils;
    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private Gson gson;

    @Override
    public Object createPDF(Class<?> aClass, Object object, String watermark) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            object = getObject(object);
            Field[] fields = aClass.getDeclaredFields();
            String[] headerColumn = new String[fields.length];
            float[] headerColumnLength = new float[fields.length];

            for (int i = 0; i < fields.length; i++) {
                headerColumn[i] = fields[i].getName();
                headerColumnLength[i] = ((float) fields[i].getName().length() / fields.length) * 100;
            }

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            pdfDocument.addNewPage();
            Document document = new Document(pdfDocument, PageSize.A4);
            pdfUtils.setWatermark(pdfDocument, document, watermark);

            Table table = new Table(headerColumnLength);
            table.setWidthPercent(100);

            Border border = new GrooveBorder(new DeviceRgb(0, 0, 0), 2);
            for (String header : headerColumn) {
                com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell();
                table.addCell(cell.add(pdfUtils.camelString(header))
                        .setFontColor(Color.BLACK)
                        .setBackgroundColor(Color.CYAN).setBorder(border));
                pdfUtils.setCellStyle(cell);
            }

            int rowNumber = 1;
            if (object instanceof ArrayList) {
                object = getObjectFromArrayList((ArrayList<Object>) object);
                for (var obj : (ArrayList<Object>) object) {
                    JSONArray jsonArray = objectToJSONArray(obj);
                    for (var array : jsonArray) {
                        if (array instanceof JSONObject) {
                            rowNumber++;
                            Iterator<String> iterator = ((JSONObject) array).keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                for (var i = 0; i < headerColumn.length; i++) {
                                    if (headerColumn[i].equalsIgnoreCase(key)) {
                                        com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell();
                                        pdfUtils.setCellStyle(cell);
                                        String value = (String) ((JSONObject) array).get(key);
                                        if ((rowNumber % 2) != 0)
                                            cell.setBackgroundColor(new DeviceRgb(140, 255, 255));
                                        table.addCell(cell.add(value));
                                    }
                                }
                            }
                        }
                    }
                }
                document.add(table);
                document.close();
                return returnFile(baos.toByteArray(), PDF_TYPE);
            }

            JSONArray jsonArray = objectToJSONArray(object);
            for (var array : jsonArray) {
                if (array instanceof JSONObject) {
                    rowNumber++;
                    Iterator<String> iterator = ((JSONObject) array).keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        for (var i = 0; i < headerColumn.length; i++) {
                            if (headerColumn[i].equalsIgnoreCase(key)) {
                                com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell();
                                pdfUtils.setCellStyle(cell);
                                String value = (String) ((JSONObject) array).get(key);
                                if ((rowNumber % 2) != 0)
                                    cell.setBackgroundColor(new DeviceRgb(140, 255, 255));
                                table.addCell(cell.add(value));
                            }
                        }
                    }
                }
            }

            document.add(table);
            document.close();

            return returnFile(baos.toByteArray(), PDF_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public Object createCSV(Class<?> aClass, Object object) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(baos)) {

            if (object instanceof ArrayList) {
                object = getObjectFromArrayList((ArrayList<Object>) object);
                for (var obj : (ArrayList<Object>) object) {
                    JSONArray jsonArray = objectToJSONArray(obj);
                    String csv = CDL.toString(new JSONArray(jsonArray));
                    csv = csv.replaceAll(",", ";\t");
                    writer.append(csv);
                    writer.flush();
                    writer.close();
                    return returnFile(baos.toByteArray(), CSV_TYPE);
                }
            }
            JSONArray jsonArray = objectToJSONArray(object);
            String csv = CDL.toString(new JSONArray(jsonArray));
            csv = csv.replaceAll(",", ";\t");
            writer.append(csv);
            writer.flush();
            writer.close();
            return returnFile(baos.toByteArray(), CSV_TYPE);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return object;
    }

    @Override
    public Object createEXCEL(Class<?> aClass, Object object) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            object = getObject(object);
            Field[] fields = aClass.getDeclaredFields();
            String[] headerColumn = new String[fields.length];

            for (int i = 0; i < fields.length; i++) {
                headerColumn[i] = fields[i].getName();
            }

            Sheet sheet = workbook.createSheet(CLassUtils.getTrueClassName(aClass));
            CellStyle headerCellStyle = excelUtils.createHeaderStyle(workbook);


            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < headerColumn.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headerColumn[col].toUpperCase());
                cell.setCellStyle(headerCellStyle);
            }

            int rowIndex = 1;
            object = getObject(object);

            if (object instanceof ArrayList) {
                object = getObjectFromArrayList((ArrayList<Object>) object);
                for (var obj : (ArrayList<Object>) object) {

                    JSONArray jsonArray = objectToJSONArray(obj);
                    for (var array : jsonArray) {
                        Row row = sheet.createRow(rowIndex++);
                        for (int column = 0; column < headerColumn.length; column++) {
                            if (array instanceof JSONObject) {
                                Iterator<String> iterator = ((JSONObject) array).keys();
                                while (iterator.hasNext()) {
                                    String key = iterator.next();
                                    for (var i = 0; i < headerColumn.length; i++) {
                                        if (headerColumn[i].equalsIgnoreCase(key)) {
                                            String value = (String) ((JSONObject) array).get(key);
                                            row.createCell(i).setCellValue(value);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                workbook.write(baos);
                return returnFile(baos.toByteArray(), XLSX_TYPE);
            }

            JSONArray jsonArray = objectToJSONArray(object);

            for (var array : jsonArray) {
                Row row = sheet.createRow(rowIndex++);
                for (int column = 0; column < headerColumn.length; column++) {
                    if (array instanceof JSONObject) {
                        Iterator<String> iterator = ((JSONObject) array).keys();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            for (var i = 0; i < headerColumn.length; i++) {
                                if (headerColumn[i].equalsIgnoreCase(key)) {
                                    String value = (String) ((JSONObject) array).get(key);
                                    row.createCell(i).setCellValue(value);
                                }
                            }
                        }
                    }
                }
            }

            workbook.write(baos);
            return returnFile(baos.toByteArray(), XLSX_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnFile(object, null);
    }

    private Object getObject(Object o) {
        if (o instanceof ResponseEntity) {
            o = ((ResponseEntity<?>) o).getBody();
        }
        if (o instanceof Page) {
            o = ((Page<?>) o).getContent();
        }
        return o;
    }

    private List<Map<Object, Object>> getObjectFromArrayList(ArrayList<Object> arrayList) {
        List<Map<Object, Object>> maps = new ArrayList<>();
        for (var array : arrayList) {
            var map = createMapObject(array);
            maps.add(map);
        }
        return maps;
    }

    private JSONArray objectToJSONArray(Object object) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        Map<Object, Object> map = createMapObject(object);

        for (var key : map.keySet()) {
            String value = (String) map.get(key);
            jsonObject.put((String) key, value);
        }
        jsonArray.put(jsonObject);
        return jsonArray;
    }

    private Map<Object, Object> createMapObject(Object object) throws JSONException {
        Map<Object, Object> map = new HashMap<>();
        object = getObject(object);

        if (isNull(object))
            return null;

        String json = gson.toJson(object);
        JSONObject jsonObject = new JSONObject(json);

        Iterator<String> interator = jsonObject.keys();
        while (interator.hasNext()) {
            String key = interator.next();
            pdfUtils.recursiveSearch(key, jsonObject, map);
        }
        return map;
    }

    private ResponseEntity<?> returnFile(Object file, String fileType) {
        UUID uuid = UUID.randomUUID();

        int bytes;
        if (file instanceof byte[]) bytes = ((byte[]) file).length;
        else bytes = Objects.toString(file).getBytes().length;

        return ResponseEntity.ok()
                .contentType(mediaType(fileType))
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.%s", uuid, isNull(fileType) ? "pdf" : fileType.toLowerCase()))
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes))
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*").body(file);
    }

    private MediaType mediaType(String type) {
        if (nonNull(type)) {
            if (type.equalsIgnoreCase("pdf")) {
                return MediaType.APPLICATION_PDF;
            }
            if (type.equalsIgnoreCase("csv")) {
                return MediaType.parseMediaType("application/csv");
            }
            if (type.equalsIgnoreCase("xlsx")) {
                return MediaType.APPLICATION_OCTET_STREAM;
            }
        }
        return MediaType.APPLICATION_JSON;
    }

}