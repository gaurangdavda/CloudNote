package edu.neu.csye6225.spring19.cloudninja.model;

import javax.persistence.*;

@Entity
@Table(name="user")
public class UserCredentials {


	@Id
	@GeneratedValue
	@Column(name="user_id")
	private long id;

	@Column(name="user_emailId")
	private String emailId;

	@Column(name="user_password")
	private String password;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public void setPassword(String password) {
		this.password = password;
	}

}
