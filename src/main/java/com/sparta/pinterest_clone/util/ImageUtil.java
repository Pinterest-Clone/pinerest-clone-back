package com.sparta.pinterest_clone.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Component
public class ImageUtil {


    public boolean validateFile(MultipartFile file) {
        // 지원하는 파일 확장자 리스트
        List<String> fileExtensions = Arrays.asList("jpg", "png", "webp", "heif", "heic", "gif", "jpeg");

        // 파일 null check
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("이미지가 존재하지 않습니다.");
        }

        String path = Paths.get(file.getOriginalFilename()).toString(); // 원본 파일명으로 파일 경로 생성
        String extension = StringUtils.getFilenameExtension(path); // 확장자명

        // 파일 확장자 null check
        if (extension == null) {
            throw new IllegalArgumentException("파일의 확장자가 잘못되었습니다");
        }

        // 파일 확장자 검증
        if (!fileExtensions.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("지원되지 않는 확장자 형식입니다.");
        }

        // 파일 크기 검증
        long maxSize = 20 * 1024 * 1024; // 20MB
        long fileSize = file.getSize();

        if (fileSize > maxSize) {
            throw new IllegalArgumentException("파일의 크기가 기준보다 초과되었습니다");
        }
        return true;
    }

    public String uploadFileToS3(MultipartFile file, AmazonS3 amazonS3, String bucket) {
        // 새 S3 객체 업로드
        String filename = file.getOriginalFilename(); // 파일의 원본명
        String extension = StringUtils.getFilenameExtension(Paths.get(filename).toString()); // 확장자명
        String fileUuid = UUID.randomUUID() + "." + extension; // 해당 파일의 고유한 이름

        // 업로드할 파일의 메타데이터 생성(확장자 / 파일 크기.byte)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(file.getSize());

        // 요청 객체 생성(버킷명, 파일명, 스트림, 메타정보)pri
        PutObjectRequest request = null;
        try {
            request = new PutObjectRequest(bucket, fileUuid, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // S3 버킷에 PUT(등록 요청)
        amazonS3.putObject(request);

        return fileUuid;
    }


}
