package com.alkemy.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Entity
@Data
@Getter
@Setter
@Table(name= "accounts")
@SQLDelete(sql = "UPDATE accounts SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ECurrency currency;

    @NotNull
    private Double transactionLimit;

    @NotNull
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER, optional = false )
    @JoinColumn(name = "USER_ID", updatable = false)
    @JsonIgnore
    private User user;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    private Boolean deleted;
}
