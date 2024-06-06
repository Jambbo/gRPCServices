package com.example.datastoregrpcmicroservice.config;

//Class, where described that keys we will turn to and where the info will be stored in redis, what keys.

import com.example.datastoregrpcmicroservice.model.Data;

public class RedisSchema {

    //set
    //In this set there are id stored of that sensors, that were already added, info already exists here, already added
    public static String sensorKeys(){
        return KeyHelper.getKey("sensors");
    }

    //hash with summary types
    //sensors:1:voltage - example
    public static String summaryKey(
            Long sensorId,
            Data.MeasurementType measurementType
    ){
        return KeyHelper.getKey("sensors:"+sensorId+":"+measurementType.name().toLowerCase());
    }

}
