package edu.neu.csye6225.spring19.cloudninja.util.file.storage.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import edu.neu.csye6225.spring19.cloudninja.exception.FileStorageException;

@Service
@Profile("dev")
public class AmazonClient {
	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

	@Value("${amazonProperties.clientRegion}")
	private String clientRegion;

	@PostConstruct
	private void initializeAmazon() {

//		this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).build();
		this.s3client = AmazonS3ClientBuilder.defaultClient();
		// this.s3client =
		// AmazonS3ClientBuilder.standard().withRegion(this.clientRegion).build();

	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(bucketName, fileName, file);
	}

	public String uploadFile(MultipartFile multipartFile) throws FileStorageException {

		String fileUrl = "";

		File file = null;
		try {
			file = convertMultiPartToFile(multipartFile);

			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
			uploadFileTos3bucket(fileName, file);

		} catch (Exception e) {
			throw new FileStorageException("File not stored in S3 bucket. Please try again");
		} finally {
			if (file != null) {
				file.delete();
			}

		}

		return fileUrl;
	}

	public void deleteFileFromS3Bucket(String fileUrl) throws FileStorageException {
		try {
			String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			s3client.deleteObject(bucketName, fileName);
		} catch (Exception e) {
			throw new FileStorageException("File not stored in S3 bucket. Please try again");
		}

	}
}
