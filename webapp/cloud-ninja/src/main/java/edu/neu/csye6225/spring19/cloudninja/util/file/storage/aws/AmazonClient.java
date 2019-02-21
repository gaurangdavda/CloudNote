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

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import edu.neu.csye6225.spring19.cloudninja.exception.FileStorageException;

@Service
@Profile("dev")
public class AmazonClient {
	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

//@Value("${amazonProperties.accessKey}")
//private String accessKey;
//
//@Value("${amazonProperties.secretKey}")
//private String secretKey;

	@Value("${amazonProperties.clientRegion}")
	private String clientRegion;

	@PostConstruct
	private void initializeAmazon() {
//		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,
//		this.secretKey);
//BasicAWSCredentials creds = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).build();

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
		s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
	}

	public String uploadFile(MultipartFile multipartFile) throws FileStorageException {

		String fileUrl = "";

		File file;
		try {
			file = convertMultiPartToFile(multipartFile);

			String fileName = generateFileName(multipartFile);
			fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
			file.delete();
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			e.printStackTrace();
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		} catch (IOException e) {
			throw new FileStorageException("File not stored in S3 bucket. Please try again");
		}

		return fileUrl;
	}

	public void deleteFileFromS3Bucket(String fileUrl) {
		try {
			String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			e.printStackTrace();
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		}

	}
}
