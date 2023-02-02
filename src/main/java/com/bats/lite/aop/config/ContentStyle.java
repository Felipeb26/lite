package com.bats.lite.aop.config;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import lombok.experimental.UtilityClass;

import java.awt.*;

@UtilityClass
public class ContentStyle {

    public static Style headerStyle() {
        Style headerStyle = new Style();
        headerStyle.setBackgroundColor(new Color(140, 255, 255, 49));
        headerStyle.setTransparency(ar.com.fdvs.dj.domain.constants.Transparency.OPAQUE);
        headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
        headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
        headerStyle.setBorderColor(Color.BLACK);
        headerStyle.setFont(ar.com.fdvs.dj.domain.constants.Font.ARIAL_MEDIUM_BOLD);
        headerStyle.setBorder(Border.THIN());
        return headerStyle;
    }

    public static Style contentStyle() {
        Style content = new Style();
        content.setVerticalAlign(VerticalAlign.JUSTIFIED);
        content.setTransparency(ar.com.fdvs.dj.domain.constants.Transparency.TRANSPARENT);
        content.setFont(Font.ARIAL_SMALL);
        content.setPadding(7);
        return content;
    }

}
