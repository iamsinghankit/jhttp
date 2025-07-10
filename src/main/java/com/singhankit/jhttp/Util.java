package com.singhankit.jhttp;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Ankit Singh
 */
public class Util {

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static void requireNonNull(Object obj, String message) {
        if(Objects.isNull(obj)){
            throw new HttpServerException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
    }

    public static void requireNull(Object obj, String message) {
        if(Objects.nonNull(obj)){
            throw new HttpServerException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
    }

    public static void close(Closeable closeable){
        try{
            closeable.close();
        }catch(IOException _){}
    }

    public static void requireNonEmpty(Collection<?> collection,String msg) {
        if(collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }
}
