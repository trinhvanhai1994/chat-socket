package com.kma.chat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-mm-yyyy");

    public static Date convertStringToDate(String dob) {
        try {
            return DATE_FORMAT.parse(dob);
        } catch (Exception e) {
            return null;
        }
    }
}
