package com.singhankit.jhttp.internal;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;

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
