package server.acode.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

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
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(filename + UUID.randomUUID().toString())
                .build();

        //presignedURL 설정
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(presignedUrlDuration))
                .putObjectRequest(putObjectRequest)
                .build();

        //presignedURL 생성
        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(presignRequest);

        String presignedURL = presignedPutObjectRequest.url().toString();

        s3Presigner.close();

        return presignedURL;
    }
}
