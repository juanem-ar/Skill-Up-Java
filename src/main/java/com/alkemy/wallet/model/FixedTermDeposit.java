package com.alkemy.wallet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Data
@Getter
@Setter
@Table(name = "fixed_term_deposits")
@SQLDelete(sql = "UPDATE fixed_term_deposits SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class FixedTermDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", updatable = false)
    private Account account;

    @NotNull
    private Double interest;

    private Timestamp creationDate;

    private Timestamp closingDate;

    private Boolean deleted = Boolean.FALSE;

}
