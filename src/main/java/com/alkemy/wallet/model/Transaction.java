package com.alkemy.wallet.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Getter
@Setter
@Table(name= "transactions")
@SQLDelete(sql = "UPDATE transaction SET deleted=true WHERE id = ?")
@Where(clause = "deleted = false")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Double amount;
    @NotNull
    private EType type;
    @Nullable
    private String description;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private User userId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
    private Account accountId;
    private Timestamp transactionDate;



}
