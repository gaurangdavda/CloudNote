package edu.neu.csye6225.spring19.cloudninja.util.file.storage;

import org.springframework.web.multipart.MultipartFile;

import edu.neu.csye6225.spring19.cloudninja.exception.FileStorageException;

public interface FileStorageUtil {

	public String storeFile(MultipartFile file) throws FileStorageException;

	public String replaceFile(String oldFileUrl, MultipartFile file) throws FileStorageException;

	public void deleteFile(String fileUrl) throws FileStorageException;

}
