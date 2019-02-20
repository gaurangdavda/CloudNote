package edu.neu.csye6225.spring19.cloudninja.util.file.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import edu.neu.csye6225.spring19.cloudninja.exception.FileStorageException;
import edu.neu.csye6225.spring19.cloudninja.util.file.storage.FileStorageUtil;
import edu.neu.csye6225.spring19.cloudninja.util.file.storage.aws.AmazonClient;

@Component
@Scope(value = "singleton")
@Profile("prod")
public class ProdFileStorageUtil implements FileStorageUtil{

	@Autowired
	private AmazonClient amazonClient;
	
	@Override
	public String storeFile(MultipartFile file) throws FileStorageException {
		return amazonClient.uploadFile(file);
	}

	@Override
	public String replaceFile(String oldFileUrl, MultipartFile file) throws FileStorageException {
		//first delete the file and then upload the new file
		amazonClient.deleteFileFromS3Bucket(oldFileUrl);
		return amazonClient.uploadFile(file);
	}

	@Override
	public void deleteFile(String fileUrl) throws FileStorageException {
		amazonClient.deleteFileFromS3Bucket(fileUrl);
	}

}
