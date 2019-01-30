package edu.neu.csye6225.spring19.cloudninja.model;

import javax.persistence.*;

@Entity
@Table(name = "USR_DTLS")
public class UserCredentials {

	@Id
	@GeneratedValue
	@Column(name = "USR_ID")
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "USR_EMAIL", unique = true)
	private String emailId;

	@Column(name = "USR_PWD")
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

	public void setPassword(String password) {
		this.password = password;
	}

}
