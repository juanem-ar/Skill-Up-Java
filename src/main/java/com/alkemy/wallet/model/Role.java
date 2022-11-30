package com.alkemy.wallet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ROLES")
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "roleSequence",sequenceName = "roleSequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleSequence")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "NAME")
    private ERoles name;

    @Column(name = "DESCRIPTION")
    private String description;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "UPDATE_DATE")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Timestamp updateDate;

}
