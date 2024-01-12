package server.acode.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${presignedUrlDuration}")
    private long presignedUrlDuration;

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public String getPresignedUrl(String filename) {
        if (filename == null || filename.equals("")) {
            return null;
        }

        // 가져올 객체에 대한 요청 생성
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(filename)
                .build();

        //presignedURL 설정
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(presignedUrlDuration))
                .getObjectRequest(getObjectRequest)
                .build();

        //presignedURL 생성
        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

        String presignedURL = presignedGetObjectRequest.url().toString();

        s3Presigner.close();

        return presignedURL;
    }
}
