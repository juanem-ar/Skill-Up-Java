package com.alkemy.wallet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.security.Timestamp;

@Entity
@Data
@Getter
@Setter
@Table(name= "accounts")
@SQLDelete(sql = "UPDATE accounts SET deleted=true WHERE id = ?")
@Where(clause = "deleted = false")
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

    private Boolean softDelete;
}
