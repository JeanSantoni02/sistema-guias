package com.transportista.sistemaguias.util;

import java.util.regex.Pattern;

/**
 * Clase de utilidades para validaciones
 */
public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@(.+)$"
    );
    
    private static final Pattern RUT_PATTERN = Pattern.compile(
        "^[0-9]+-[0-9kK]{1}$"
    );
    
    /**
     * Valida si un email tiene formato correcto
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Valida si un RUT chileno tiene formato correcto
     */
    public static boolean isValidRut(String rut) {
        return rut != null && RUT_PATTERN.matcher(rut).matches();
    }
    
    /**
     * Valida que el texto no esté vacío o nulo
     */
    public static boolean isNotBlank(String text) {
        return text != null && !text.trim().isEmpty();
    }
    
    /**
     * Valida que el número sea positivo
     */
    public static boolean isPositive(Double number) {
        return number != null && number > 0;
    }
    
    /**
     * Valida que el número sea positivo
     */
    public static boolean isPositive(Integer number) {
        return number != null && number > 0;
    }
}