/**
 * 
 */
package edu.neu.csye6225.spring19.cloudninja.model;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.DATE_FORMAT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author gaurang
 *
 */
@Entity
@Table(name = "USR_NOTE_DTLS")
@JsonPropertyOrder({ "id", "content", "title", "created_on", "last_updated_on", "attachments" })
public class Note {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
			@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.StandardRandomStrategy") })
	@Column(name = "NOTE_ID")
	@JsonProperty("id")
	private UUID id;

	@Column(name = "NOTE_TITLE")
	@JsonProperty(value = "title")
	private String title;

	@Column(name = "NOTE_CONTENT")
	@JsonProperty(value = "content")
	private String content;

	@Column(name = "CREATED_DATE", updatable = false)
	@JsonProperty("created_on")
	private String creationDate;

	@Column(name = "LAST_UPDATED_DATE")
	@JsonProperty("last_updated_on")
	private String updatedDate;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "USR_EMAIL", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private UserCredentials userCredentials;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "note")
	@JsonProperty("attachments")
	private List<Attachment> attachments;

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

	/**
	 * @return the userCredentials
	 */
	public UserCredentials getUserCredentials() {
		return userCredentials;
	}

	/**
	 * @param userCredentials the userCredentials to set
	 */
	public void setUserCredentials(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@PrePersist
	private void onCreate() {
		setCreationDate(getCurrentDateWithFormat(DATE_FORMAT));
		setUpdatedDate(getCurrentDateWithFormat(DATE_FORMAT));
	}

	@PreUpdate
	private void onUpdate() {
		setUpdatedDate(getCurrentDateWithFormat(DATE_FORMAT));
	}

	private String getCurrentDateWithFormat(String format) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(new Date());
	}

}
