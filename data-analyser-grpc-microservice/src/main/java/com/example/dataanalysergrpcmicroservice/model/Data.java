package com.example.dataanalysergrpcmicroservice.model;

import com.example.grpccommon.GRPCData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "data")
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sensorId;
    private LocalDateTime timestamp;
    private Double measuerement;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private MeasurementType measurementType;

    public enum MeasurementType{
        TEMPERATURE,
        VOLTAGE,
        POWER
    }

    public Data(GRPCData grpcData){
        this.id = grpcData.getId();
        this.sensorId = grpcData.getSensorId();
        this.timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(
                        grpcData.getTimestamp().getSeconds(),
                        grpcData.getTimestamp().getNanos()
                ),
                ZoneId.systemDefault()
        );
        this.measuerement = grpcData.getMeasurement();
        this.measurementType = MeasurementType.valueOf(grpcData.getMeasurementType().name());
    }

}

