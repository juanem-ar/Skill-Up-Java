package com.alkemy.wallet.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
//@Data
@Getter
@Setter
@Table(name= "accounts")
@SQLDelete(sql = "UPDATE accounts SET deleted=true WHERE id = ?")
@Where(clause = "deleted = false")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private enum Currency { ARS, USD} ;

    @NotNull
    //@Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    private Double transactionLimit;

    @NotNull
    private Double balance;

    @Column(name="USER_ID", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User user;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime updateData;

    private Boolean softDelete;
}
