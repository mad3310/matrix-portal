package com.letv.common.util;

import com.letv.common.exception.CommonException;

public class ExceptionUtils {

    static {
        org.apache.commons.lang.exception.ExceptionUtils.addCauseMethodName("getOriginalCause");
    }
    
    public static RuntimeException wrap(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        }
        else {
            return new CommonException(t);
        }
    }
    
    public static String getDescriptionWithRootCause(Throwable t) {
        Throwable rootCause = org.apache.commons.lang.exception.ExceptionUtils.getRootCause(t);
        if (rootCause != null && rootCause != t) {
            return t + " (caused by " + rootCause + ")";
        }
        else {
            return t.toString();
        }
    }
    
    public static String getRootCauseStackTrace(Throwable t) {
        StringBuilder builder = new StringBuilder();
        for (String element : org.apache.commons.lang.exception.ExceptionUtils.getRootCauseStackTrace(t)) {
            builder.append(element);
            builder.append('\n');
        }
        return builder.toString();
    }

    public static boolean isCausedBy(Throwable t, Class<? extends Throwable> c) {
        return org.apache.commons.lang.exception.ExceptionUtils.indexOfType(t, c) != -1;
    }

    @SuppressWarnings("unchecked")
    public static <C extends Throwable, CE extends C> CE getCause(Throwable t, Class<C> c) {
        int index = org.apache.commons.lang.exception.ExceptionUtils.indexOfType(t, c);
        return (CE) (index == -1 ? null : org.apache.commons.lang.exception.ExceptionUtils.getThrowableList(t).get(index));
    }
    
    public static String toString(Throwable t) {
        if (t == null) {
            return "[no exception]";
        }
        
        Throwable cause = org.apache.commons.lang.exception.ExceptionUtils.getRootCause(t);
        return t.getClass().getSimpleName() 
                   + (cause != null ? " (caused by " + cause.getClass().getSimpleName() + ")" 
                                    : "") 
                   + ": " + t.getMessage();
    }
}
