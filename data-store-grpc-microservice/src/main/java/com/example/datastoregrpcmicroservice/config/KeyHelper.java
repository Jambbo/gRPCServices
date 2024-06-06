package com.example.datastoregrpcmicroservice.config;

import java.util.Objects;

//this class is to create the lines(keys) for us, for how we will store the data in redis
public class KeyHelper {

    private final static String defaultPrefix = "app";
    private static String prefix = null;

    public static void setPrefix(String keyPrefix){
        prefix = keyPrefix; // there is no "this." as it is static variable.
    }

    public static String getKey(String key){
        return getPrefix()+":"+key;
    }

    public static String getPrefix(){
        return Objects.requireNonNullElse(prefix,defaultPrefix);
    }

}
