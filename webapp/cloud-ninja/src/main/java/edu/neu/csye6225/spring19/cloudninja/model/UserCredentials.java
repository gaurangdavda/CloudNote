package edu.neu.csye6225.spring19.cloudninja.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USR_DTLS")
public class UserCredentials {

	@Id
	@Column(name = "USR_EMAIL")
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
