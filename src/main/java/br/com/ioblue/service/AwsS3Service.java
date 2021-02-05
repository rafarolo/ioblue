package br.com.ioblue.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class AwsS3Service {

	private AmazonS3 amazonS3;

	@Value("${aws.s3.endpoint}")
	private String endpoint;

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	@Value("${aws.accesskey}")
	private String awsAccessKey;

	@Value("${aws.secretkey}")
	private String awsSecretKey;

	@Autowired
	AWSCredentials amazonAWSCredentials;

	@PostConstruct
	private void initializeAmazon() {
		amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
				.withRegion(Regions.US_EAST_2).build();
	}

	public String uploadFile(MultipartFile multipartFile, String fileName) {
		// String fileName = "";
		try {
			File file = convertMultipartFileToFile(multipartFile);
			// fileName = multipartFile.getOriginalFilename();
			// fileURL = endpoint + "/" + bucketName + "/" + fileName;
			uploadFileToBucket(fileName + multipartFile.getOriginalFilename().substring(
					multipartFile.getOriginalFilename().length() - 5, multipartFile.getOriginalFilename().length()),
					file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	private File convertMultipartFileToFile(MultipartFile file) throws IOException {
		File convertedFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertedFile);
		fos.write(file.getBytes());
		fos.close();
		return convertedFile;
	}

	private void uploadFileToBucket(String fileName, File file) {
		amazonS3.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public String deleteFileFromBucket(String fileName) {
		amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		return "Deletion Successful";
	}

	public AWSCredentialsProvider amazonAWSCredentialsProvider() {
		return new AWSStaticCredentialsProvider(amazonAWSCredentials);
	}

}