package br.com.ioblue.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AwsS3Service {

	private AmazonS3 amazonS3;

	@Value("${aws.s3.endpoint}")
	private String endpoint;

	@Value("${aws.s3.bucket}")
	private String bucket;

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
		if (multipartFile != null && StringUtils.hasLength(fileName)) {
			// int fileNameLength = multipartFile.getOriginalFilename().length();
			// String ext = multipartFile.getOriginalFilename().substring(fileNameLength -
			// 5, fileNameLength);
			// String newFileName = fileName + ext;
			try {
				File file = new File(fileName);
				ImageIO.write(ImageIO.read(convertMultipartFileToFile(multipartFile, file)), "png", file);
				// File file = convertMultipartFileToFile(multipartFile);
				// fileName = multipartFile.getOriginalFilename();
				uploadFileToBucketS3(fileName, file);
				file.deleteOnExit();
				return fileName;
			} catch (Exception e) {
				e.printStackTrace();				
			}			
		}
		return null;
	}

	private File convertMultipartFileToFile(MultipartFile multipartFile, File file) throws IOException {
		//File convertedFile = new File(multipartFile.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(multipartFile.getBytes());
		} catch (IOException ex) {
			log.error("Error occurred while convert Multipart to File!");
		}
		return file;
	}

	private void uploadFileToBucketS3(String fileName, File file) {
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, file)
				.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public String deleteFileFromBucket(String fileName) {
		log.info("Try to delete the file " + fileName);
		amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
		return "File deletion successful!";
	}

	public AWSCredentialsProvider amazonAWSCredentialsProvider() {
		return new AWSStaticCredentialsProvider(amazonAWSCredentials);
	}

}