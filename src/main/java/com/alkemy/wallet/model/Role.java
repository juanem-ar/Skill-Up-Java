package com.alkemy.wallet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ROLES")
public class Role implements Serializable{

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
    @Schema(required = true, example = "ROLE_USER", description = "User role")
    private ERoles name;

    @Column(name = "DESCRIPTION")
    @Schema(required = true, example = "Scope or limits", description = "Role Description")
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
