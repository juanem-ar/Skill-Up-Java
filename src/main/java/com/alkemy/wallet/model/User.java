package com.alkemy.wallet.model;

import lombok.Data;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@FilterDef(name = "deletedUserFilter",
  parameters = @ParamDef(
      name = "isDeleted",
      type = "boolean"))
@Filter(name = "deletedUserFilter", condition = "deleted= :isDeleted")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(min = 3, max = 50)
	@NotNull
	private String firstName;

	@Size(min = 3, max = 50)
	@NotNull
	private String lastName;

	@Email
	@NotNull
	@Column(unique = true)
	private String email;

	@Length(min = 8)
	@NotNull
	private String password;
	
	@Column(name = "CREATION_DATE",
	    updatable = false)
	@CreationTimestamp
	private Timestamp creationDate;

	@Column(name = "UPDATE_DATE",
	    nullable = false)
	@UpdateTimestamp
	private Timestamp updateDate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "ROLE")
	private Role role;
	
	@OneToMany(mappedBy = "user")
	private List<Account> accounts = new ArrayList<>();

	private Boolean deleted = Boolean.FALSE;
	
	public void addAccount(Account account) {
	  accounts.add(account);
	  account.setUser(this);
	}
	
	public void removeAccount(Account account) {
	  accounts.remove(account);
	  account.setUser(null);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		User user = (User) o;
		return id != null && Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
