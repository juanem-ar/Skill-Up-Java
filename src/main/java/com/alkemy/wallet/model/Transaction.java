package com.alkemy.wallet.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transactions")
@Data
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE transactions SET soft_delete=true WHERE id = ?")
@Where(clause = "soft_delete = false")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;
    @NotNull
    private Double amount;
    @NotNull
    private EType type;
    @Nullable
    private String description;
    private Boolean softDelete = Boolean.FALSE;

    @Column(name="ACCOUNT_ID", nullable = false)
    private Long accountId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
    private Account account;

    @CreationTimestamp
    private Timestamp transactionDate;
}


