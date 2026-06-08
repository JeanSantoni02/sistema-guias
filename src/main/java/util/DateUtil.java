package com.transportista.sistemaguias.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Clase de utilidades para manejo de fechas
 */
public class DateUtil {
    
    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FORMATTER_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter FORMATTER_DATETIME_FILE = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    /**
     * Convierte LocalDate a String (yyyy-MM-dd)
     */
    public static String dateToString(LocalDate date) {
        return date != null ? date.format(FORMATTER_DATE) : null;
    }
    
    /**
     * Convierte String a LocalDate
     */
    public static LocalDate stringToDate(String dateStr) {
        try {
            return dateStr != null ? LocalDate.parse(dateStr, FORMATTER_DATE) : null;
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Convierte LocalDateTime a String
     */
    public static String dateTimeToString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FORMATTER_DATETIME) : null;
    }
    
    /**
     * Obtiene la fecha actual como String (yyyy-MM-dd)
     */
    public static String getCurrentDateString() {
        return LocalDate.now().format(FORMATTER_DATE);
    }
    
    /**
     * Valida si una fecha tiene formato correcto (yyyy-MM-dd)
     */
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, FORMATTER_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}