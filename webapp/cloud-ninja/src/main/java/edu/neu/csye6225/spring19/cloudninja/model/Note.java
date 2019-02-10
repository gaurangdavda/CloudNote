/**
 * 
 */
package edu.neu.csye6225.spring19.cloudninja.model;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.DATE_FORMAT;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.neu.csye6225.spring19.cloudninja.util.CommonUtil;

/**
 * @author gaurang
 *
 */
@Entity
@Table(name = "USR_NOTE_DTLS")
@JsonPropertyOrder({ "id", "content", "title", "created_on", "last_updated_on"})
public class Note {

	@Autowired
	CommonUtil commonUtil;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
			@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "NOTE_ID")
	@JsonProperty("id")
	private UUID id;

	@Column(name = "NOTE_TITLE")
	@JsonProperty("title")
	private String title;

	@Column(name = "NOTE_CONTENT")
	@JsonProperty("content")
	private String content;

	@Column(name = "CREATED_DATE", updatable = false)
	@JsonProperty("created_on")
	private String creationDate;

	@Column(name = "LAST_UPDATED_DATE")
	@JsonProperty("last_updated_on")
	private String updatedDate;

	@ManyToOne
	@JoinColumn(name = "USR_EML_ID")
	private UserCredentials userCredentials;

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public UserCredentials getUserCredentials() {
		return userCredentials;
	}

	public void setUserCredentials(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}

	@PrePersist
	private void onCreate() {
		setCreationDate(commonUtil.getCurrentDateWithFormat(DATE_FORMAT));
		setUpdatedDate(commonUtil.getCurrentDateWithFormat(DATE_FORMAT));
	}

	@PreUpdate
	private void onUpdate() {
		setUpdatedDate(commonUtil.getCurrentDateWithFormat(DATE_FORMAT));
	}

}
