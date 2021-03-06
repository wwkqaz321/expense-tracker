package com.tracker.domain.users;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tracker.domain.users.Role;
import com.tracker.domain.users.User;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {
	
	private String id;
	private List<Role> roles;
	private String email;
	private String password;
	private String username;
	private String name;
	private List<String> phone;
	private String status;
	private boolean isAdmin;
	
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPhone() {
		return phone;
	}

	public void setPhone(List<String> phone) {
		this.phone = phone;
	}
		
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public boolean hasRole( Role r ) {
		return roles.contains( r );
	}
	
	public boolean hasRole( String r ) {
		return hasRole( new Role( r ) );
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public User() { }
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	private User(User.Builder builder) {
		this.username = builder.username;
		this.id = builder.id;
		this.password = builder.password;
		this.roles = builder.roles;	
		this.email = builder.email;
		this.name = builder.name;
		this.phone = builder.phone;
		this.status = builder.status;
		this.isAdmin = builder.isAdmin;
	}
	
	public static class Builder {
		private String username;
		private String email;
		private String id;
		private List<Role> roles;
		private String password;
		private String name;
		private List<String> phone;
		private String status;
		private boolean isAdmin;
		

		public Builder() {			
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder roles(List<Role> roles) {
			this.roles = roles;
			return this;
		}
		
		public Builder id(String id) {
			this.id = id;
			return this;
		}
		
		
		public Builder phone(List<String> phone) {
			this.phone = phone;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}
		
		public Builder isAdmin( boolean isAdmin ){
			this.isAdmin = isAdmin;
			return this;
		}
		public User build() {
			return new User(this);
		}
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}
		
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
