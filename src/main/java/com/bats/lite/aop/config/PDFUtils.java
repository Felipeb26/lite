package com.bats.lite.aop.config;

import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Page;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Service
public class PDFUtils {

    private static Style rowStyle() {
        Style style = new Style();
        style.setBackgroundColor(new Color(200, 255, 255, 120));
        return style;
    }

    public DynamicReport dynamicBuilder(FastReportBuilder drb) {
        return drb.setTitle(" ").setDetailHeight(50)
                .addAutoText(AutoText.AUTOTEXT_PAGE_X_OF_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT, 70, 80)
                .setDefaultStyles(null, null, ContentStyle.headerStyle(), ContentStyle.contentStyle())
                .setPageSizeAndOrientation(Page.Page_A4_Portrait())
                .setPrintBackgroundOnOddRows(true)
                .setOddRowBackgroundStyle(rowStyle()).setUseFullPageWidth(true).build();
    }

    public int setSize(Object o) {
        if (o instanceof String) {
            String field = Objects.toString(o);
            int size = field.length();
            if (size <= 5) {
                return 5;
            }
            if (size >= 10) {
                return 10;
            }
            return 8;
        }
        if(o instanceof List){

        }
        return 8;
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
}
