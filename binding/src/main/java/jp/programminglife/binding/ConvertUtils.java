package jp.programminglife.binding;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;


public final class ConvertUtils {

    interface Convertible {
        boolean toBoolean(boolean defaultValue);
        int toInt(int defaultValue);
        long toLong(long defaultValue);
        float toFloat(float defaultValue);
        double toDouble(double defaultValue);
        @NonNull
        Number toNumber(@NonNull Number defaultValue);
        @Nullable
        Number toNumber();
        @NonNull
        String toString(@NonNull String defaultValue);
        @Nullable String toStringOrNull();
    }


    private ConvertUtils() {}


    public static boolean toBoolean(@Nullable Object value, boolean defaultValue) {

        if ( value == null ) return defaultValue;
        if ( value instanceof Boolean ) return (Boolean)value;
        if ( value instanceof Number ) return ((Number)value).intValue() != 0;
        return Boolean.valueOf(toString(value));

    }


    @NonNull
    public static Boolean toBoolean(@Nullable Object value, @NonNull Boolean defaultValue) {

        if ( value == null ) return defaultValue;
        if ( value instanceof Boolean ) return (Boolean)value;
        if ( value instanceof Number ) return ((Number)value).intValue() != 0;
        return Boolean.valueOf(toString(value));

    }


    @Nullable
    public static Boolean toBoolean(@Nullable Object value) {
        return value != null ? toBoolean(value, Boolean.FALSE) : null;
    }


    @NonNull
    public static String toString(@Nullable Object value, @NonNull String defaultValue) {

        if ( value == null ) return defaultValue;
        if ( value instanceof String ) return (String)value;
        return value.toString();

    }


    @Nullable
    public static String toString(@Nullable Object value) {
        return value != null ? toString(value, "") : null;
    }


    @NonNull
    public static Number toNumber(@Nullable Object value, @NonNull Number defaultValue) {

        if ( value == null ) return defaultValue;
        if ( value instanceof Number ) return (Number)value;
        if ( value instanceof Boolean ) return (Boolean) value ? 1 : 0;
        try {
            return new BigDecimal(toString(value));
        }
        catch (NumberFormatException ex) {
            return defaultValue;
        }

    }


    @Nullable
    public static Number toNumber(@Nullable Object value) {
        return value != null ? toNumber(value, 0) : null;
    }


    public static int toInt(@Nullable Object value, int defaultValue) {

        if ( value == null ) return defaultValue;
        if ( value instanceof Number ) return ((Number)value).intValue();
        Number n = toNumber(value);
        return n != null ? n.intValue() : defaultValue;

    }


    public static long toLong(@Nullable Object value, long defaultValue) {

        if ( value == null ) return defaultValue;
        if ( value instanceof Number ) return ((Number)value).longValue();
        Number n = toNumber(value);
        return n != null ? n.longValue() : defaultValue;

    }


    public static float toFloat(@Nullable Object value, float defaultValue) {

        if ( value == null ) return defaultValue;
        if ( value instanceof Number ) return ((Number)value).floatValue();
        Number n = toNumber(value);
        return n != null ? n.floatValue() : defaultValue;

    }


    public static double toDouble(@Nullable Object value, double defaultValue) {

        if ( value == null ) return defaultValue;
        if ( value instanceof Number ) return ((Number)value).doubleValue();
        Number n = toNumber(value);
        return n != null ? n.doubleValue() : defaultValue;

    }

}
