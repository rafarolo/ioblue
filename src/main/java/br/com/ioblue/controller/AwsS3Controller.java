package br.com.ioblue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import br.com.ioblue.service.AwsS3Service;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class AwsS3Controller {

	private AwsS3Service awsS3Service;

	AwsS3Controller(AwsS3Service awsS3Service) {
		this.awsS3Service = awsS3Service;
	}

	@PostMapping("/uploadFile")
	public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file,
			@RequestParam(value = "name", required = true) String fileName) {
		return ResponseEntity.ok(awsS3Service.uploadFile(file, fileName));
	}

	@PostMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(@RequestParam(value = "name", required = true) String fileName) {
		return ResponseEntity.ok(awsS3Service.deleteFileFromBucket(fileName));
	}
}