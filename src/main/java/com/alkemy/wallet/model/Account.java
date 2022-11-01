package com.alkemy.wallet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.security.Timestamp;

@Entity
@Data
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private enum currency { ARS, USD} ;

    private Double transactionLimit;

    private Double balance;

    private long userId;

    private Timestamp creationDate;

    private Timestamp updateData;


}
