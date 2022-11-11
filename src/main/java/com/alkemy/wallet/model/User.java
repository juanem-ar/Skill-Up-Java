package com.alkemy.wallet.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@FilterDef(name = "deletedUserFilter",
  parameters = @ParamDef(
      name = "isDeleted",
      type = "boolean"))
@Filter(name = "deletedUserFilter", condition = "deleted= :isDeleted")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
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
	@CreatedDate
	private Timestamp creationDate;

	@Column(name = "UPDATE_DATE",
	    nullable = false)
	@LastModifiedDate
	private Timestamp updateDate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "ROLE_ID")
	private Role role;
	
	@OneToMany(mappedBy = "user")
	private List<Account> accounts = new ArrayList<>();

	private Boolean deleted = Boolean.FALSE;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
	
	public void addAccount(Account account) {
	  accounts.add(account);
	  account.setUser(this);
	}
	
	public void removeAccount(Account account) {
	  accounts.remove(account);
	  account.setUser(null);
	}
}
