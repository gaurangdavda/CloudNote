package edu.neu.csye6225.spring19.cloudninja.model;

import javax.persistence.*;

@Entity
@Table(name = "USR_DTLS")
public class UserCredentials {

	@Id
	@GeneratedValue
	@Column(name = "USR_ID")
	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int id) {
		this.userId = id;
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
