package customer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "customer")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "title")
	private String title;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "mobile")
	private String mobile;
	@JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "password")
	private String password;
	@JsonIgnore
	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "customer")
	private List<Address> addresses = new ArrayList<>();

	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void addAddress(Address address){
		this.addresses.add(address);
		address.setCustomer(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Customer customer = (Customer) o;
		return Objects.equals(title, customer.title) &&
				Objects.equals(name, customer.name) &&
				Objects.equals(email, customer.email) &&
				Objects.equals(mobile, customer.mobile) &&
				Objects.equals(password, customer.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, name, email, mobile, password);
	}
}
