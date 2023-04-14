package com.bats.lite.aop.service.Implements;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import com.bats.lite.aop.config.CLassUtils;
import com.bats.lite.aop.config.ExcelUtils;
import com.bats.lite.aop.config.PDFUtils;
import com.bats.lite.aop.service.FilesToGenerate;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class FilesToGenerateImplements implements FilesToGenerate {

    private static final String PDF_TYPE = "PDF";
    private static final String CSV_TYPE = "CSV";
    private static final String XLSX_TYPE = "XLSX";
    private static List<Map<Object, Object>> PDF_DATA;
    private static ByteArrayOutputStream BAOS;
    @Autowired
    private PDFUtils pdfUtils;
    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private Gson gson;

    @Override
    public Object createPDF(Class<?> aClass, Object object) {

        return null;
    }
//    @Override
//    public Object createPDF(Class<?> aClass, Object object) {
//        try {
//            List<Exception> exceptions = new ArrayList<>();
//            BAOS = new ByteArrayOutputStream();
//            PDF_DATA = new LinkedList<>();
//
//            FastReportBuilder drb = new FastReportBuilder();
//            DynamicReport dynam = pdfUtils.dynamicBuilder(drb);
//
//            object = getObject(object);
//            if (isNull(object)) {
//                return object;
//            }
//
//            if (object instanceof ArrayList) {
//                object = getObjectFromArrayList((ArrayList<Object>) object);
//                List<String> keyHeader = new ArrayList<>();
//                var maps = createMapObject(((List<?>) object).get(0));
//                List<Object> keySet = new ArrayList<>(maps.keySet());
//
//                for (Object key : keySet) {
//                    keyHeader.add(Objects.toString(key));
//                }
//                for (var i = 0; i < keyHeader.size(); i++) {
//                    try {
//                        List<Object> values = new ArrayList<>(maps.values());
//                        drb.addColumn(keyHeader.get(i), keyHeader.get(i), String.class.getName(), pdfUtils.setSize(values.get(i)));
//                    } catch (ClassNotFoundException e) {
//                        exceptions.add(e);
//                    }
//                }
//
//                for (var obj : (ArrayList<Object>) object) {
//                    var map = createMapObject(obj);
//                    PDF_DATA.add(map);
//                }
//
//                PDF_DATA = PDF_DATA.stream().distinct().collect(Collectors.toList());
//                JRDataSource ds = new JRBeanCollectionDataSource(PDF_DATA, true);
//                JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynam, new ClassicLayoutManager(), ds);
//                JasperExportManager.exportReportToPdfStream(jp, BAOS);
//
//                if (!exceptions.isEmpty()) return object;
//                return returnFile(BAOS.toByteArray(), PDF_TYPE);
//            }
//
//
//            List<String> keyHeader = new ArrayList<>();
//            var maps = createMapObject(object);
//            List<Object> keySet = new ArrayList<>(maps.keySet());
//
//            for (Object key : keySet) {
//                keyHeader.add(Objects.toString(key));
//            }
//
//            for (var i = 0; i < keyHeader.size(); i++) {
//                try {
//                    List<Object> values = new ArrayList<>(maps.values());
//                    drb.addColumn(keyHeader.get(i), keyHeader.get(i), String.class.getName(), pdfUtils.setSize(values.get(i)));
//                } catch (ClassNotFoundException e) {
//                    exceptions.add(e);
//                }
//            }
//            PDF_DATA.add(maps);
//
//            JRDataSource ds = new JRBeanCollectionDataSource(PDF_DATA, true);
//            JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynam, new ClassicLayoutManager(), ds);
//            JasperExportManager.exportReportToPdfStream(jp, BAOS);
//
//            if (!exceptions.isEmpty()) return object;
//            return returnFile(BAOS.toByteArray(), PDF_TYPE);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return object;
//    }

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