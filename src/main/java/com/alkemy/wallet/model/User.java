package com.alkemy.wallet.model;

import java.sql.Timestamp;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
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
	
	@CreationTimestamp
	private Timestamp creationDate;

	@UpdateTimestamp
	private Timestamp updateDate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "ROLE_ID")
	private Role role;

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
}
