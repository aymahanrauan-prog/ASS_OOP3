package kz.hotel.util;

import java.lang.reflect.Field;

public final class ReflectionPrinter {
    private ReflectionPrinter() {}

    public static String toPrettyString(Object obj) {
        if (obj == null) return "null";
        Class<?> c = obj.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append(c.getSimpleName()).append("{");

        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            try {
                sb.append(f.getName()).append("=").append(f.get(obj));
            } catch (Exception e) {
                sb.append(f.getName()).append("=?");
            }
            if (i < fields.length - 1) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
}
