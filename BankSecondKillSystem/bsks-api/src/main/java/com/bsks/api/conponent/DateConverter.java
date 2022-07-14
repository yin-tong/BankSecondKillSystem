package com.bsks.api.conponent;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全局的string 转日期
 */
@Component
public class DateConverter implements Converter<String, Date>{

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date convert(String s) {
        if(s.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            try {
                return sdf1.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
            try {
                return sdf2.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + s + "'");
        }
        return null;
    }
}
