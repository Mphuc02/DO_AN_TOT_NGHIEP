package dev.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ClientService {
    private final S3Client s3Client;

    public List<String> getUrlsOfObjectsInBucket(String bucketName) {
        ListObjectsRequest listObjectsRequest = ListObjectsRequest
                .builder()
                .bucket(bucketName)
                .build();

        ListObjectsResponse listObjectsResponse = this.s3Client.listObjects(listObjectsRequest);
        List<S3Object> objectsInBucket = listObjectsResponse.contents();
        List<String> result = new ArrayList<>();

        objectsInBucket.forEach(object -> result.add(bucketName + "/" + object.key()));

        return result;
    }

    public String uploadObject(String bucketName, byte[] data, String key) {
        this.s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                RequestBody.fromByteBuffer(ByteBuffer.wrap(data)));

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        String imageUrl = s3Client.utilities().getUrl(getUrlRequest).toString();
        log.info("Upload success Object with key: " + imageUrl + " to bucket: " + bucketName);
        return imageUrl;
    }

    public void updateObject(String bucketName, byte[] data, String key) {
        HeadObjectRequest headObjectRequest = this.createHeadObjectRequest(bucketName, key);
        try {
            this.s3Client.headObject(headObjectRequest);
            this.uploadObject(bucketName, data, key);
        } catch (SdkClientException | AwsServiceException e) {
            log.error("Not found object");
        }
    }

    public void deleteObject(String bucketName, String key) {
        HeadObjectRequest headObjectRequest = this.createHeadObjectRequest(bucketName, key);
        try {
            this.s3Client.headObject(headObjectRequest);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            this.s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            log.error("Not found object");
        }
    }

    private HeadObjectRequest createHeadObjectRequest(String bucketName, String key) {
        return HeadObjectRequest
                .builder()
                .bucket(bucketName)
                .key(key)
                .build();
    }
}