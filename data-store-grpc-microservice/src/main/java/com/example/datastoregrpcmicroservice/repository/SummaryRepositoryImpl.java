package com.example.datastoregrpcmicroservice.repository;

import com.example.datastoregrpcmicroservice.config.RedisSchema;
import com.example.datastoregrpcmicroservice.model.Data;
import com.example.datastoregrpcmicroservice.model.Summary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

@Repository
@RequiredArgsConstructor
public class SummaryRepositoryImpl implements SummaryRepository {

    private final JedisPool jedisPool;

    @Override
    public Optional<Summary> findBySensorId(Long sensorId, Set<Data.MeasurementType> mt, Set<Summary.SummaryType> st) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (!jedis.sismember(//check if there is in set some object by the key
                    RedisSchema.sensorKeys(),
                    String.valueOf(sensorId)
            )) {
                return Optional.empty();//we do not have info about such a sensor
            }
            if (mt.isEmpty() && !st.isEmpty()) {
                return getSummary(
                        sensorId,
                        Set.of(Data.MeasurementType.values()),
                        st,
                        jedis
                );
            }else if(!mt.isEmpty() && st.isEmpty()){
                return getSummary(
                        sensorId,
                        mt,
                        Set.of(Summary.SummaryType.values()),
                        jedis
                );
            }else{
                return getSummary(
                        sensorId,
                        mt,
                        st,
                        jedis
                );
            }
        }
    }

    @Override
    public void handle(Data data) {
        try(Jedis jedis = jedisPool.getResource()){
            if(!jedis.sismember(
                    RedisSchema.sensorKeys(),
                    String.valueOf(data.getSensorId())
            )){
                //here we've added to set the sensor's id and now we know that info about them we received
                jedis.sadd(
                        RedisSchema.sensorKeys(),
                        String.valueOf(data.getSensorId())
                );
            }
            updateMinValue(data,jedis);
            updateMaxValue(data,jedis);
            updateSumAndAvgValue(data,jedis);
        }
    }

    private void updateMinValue(Data data, Jedis jedis) {
        String minTypeField = Summary.SummaryType.MIN.name().toLowerCase();
        Double measurement = data.getMeasurement();
        String key = RedisSchema.summaryKey(data.getSensorId(),data.getMeasurementType());
        String value = jedis.hget(key,minTypeField);
        if(isNull(value) || measurement<Double.parseDouble(value)){
            jedis.hset(key,minTypeField,String.valueOf(measurement));
        }
    }

    private void updateMaxValue(Data data, Jedis jedis) {
        String maxTypeField = Summary.SummaryType.MAX.name().toLowerCase();
        Double measurement = data.getMeasurement();
        String key = RedisSchema.summaryKey(
                data.getSensorId(), data.getMeasurementType()
        );
        String value = jedis.hget(key, maxTypeField);
        if(isNull(value) || measurement>Double.parseDouble(value)){
            jedis.hset(key,maxTypeField,String.valueOf(measurement));
        }
    }

    private void updateSumAndAvgValue(Data data, Jedis jedis) {
        updateSumValue(data,jedis);
        String key = RedisSchema.summaryKey(data.getSensorId(),data.getMeasurementType());
        String counter = jedis.hget(key,"counter");
        if(isNull(counter)){
            counter = String.valueOf(jedis.hset(key,"counter",String.valueOf(1)));
        }else{
            counter = String.valueOf(jedis.hincrBy(key,"counter",1));
        }
        String sum = jedis.hget(key, Summary.SummaryType.SUM.name().toLowerCase());
        jedis.hset(
                key,
                Summary.SummaryType.AVG.name().toLowerCase(),
                String.valueOf(
                        Double.parseDouble(sum)/Double.parseDouble(counter)
                )
        );
    }

    private void updateSumValue(Data data, Jedis jedis) {
        String sumTypeField = Summary.SummaryType.SUM.name().toLowerCase();
        Double measurement = data.getMeasurement();
        String key = RedisSchema.summaryKey(data.getSensorId(),data.getMeasurementType());
        String value = jedis.hget(key,sumTypeField);
        if(isNull(value)){
            jedis.hset(key,sumTypeField,String.valueOf(measurement));
        }else{
            jedis.hincrByFloat(key,sumTypeField,measurement);
        }
    }

    private Optional<Summary> getSummary(
            Long sensorId,
            Set<Data.MeasurementType> mt,
            Set<Summary.SummaryType> st,
            Jedis jedis
    ) {
        Summary summary = new Summary();
        summary.setSensorId(sensorId);
        mt.forEach(mType ->{
            st.forEach(sType ->{
                Summary.SummaryEntry summaryEntry = new Summary.SummaryEntry();
                summaryEntry.setSummaryType(sType);
                String value = jedis.hget(//contacting to hash towards Redis and =>
                        RedisSchema.summaryKey(sensorId,mType),//connecting via this path
                        sType.name().toLowerCase()//trying to get value for the key(sType.name().toLowerCase())
                );
                if(!isNull(value)){
                    summaryEntry.setValue(Double.parseDouble(value));
                }
                String counter = jedis.hget(
                        RedisSchema.summaryKey(sensorId,mType),
                        "counter"
                );
                if (!isNull(counter)){
                    summaryEntry.setCounter(Long.parseLong(counter));
                }
                summary.addValue(mType,summaryEntry);
            });
        });
        return Optional.of(summary);
    }
}
