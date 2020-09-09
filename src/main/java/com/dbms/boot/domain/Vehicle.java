package com.dbms.boot.domain;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "VEHICLE", schema = "jiaxu")
public class Vehicle {
    @Id
    private String VEHICLE_ID;
    private BigDecimal PRICE;
    private String COLOR;
    private BigDecimal ODOMETER;
    private String TITLE_STATUS;
    private String CONDITION;
    private String REGION;
    private String STATE;
    private String MODEL;
}
