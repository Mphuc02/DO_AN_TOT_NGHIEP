package dev.common.configuration;

import dev.common.constant.MinioConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.net.URI;

@Configuration
@Slf4j
public class S3Configuration {
    @Value(MinioConstant.URL)
    private String endpoint;

    @Value(MinioConstant.USER_NAME)
    private String userName;

    @Value(MinioConstant.PASS_WORD)
    private String passWord;

    @Bean
    public S3Client s3Client(){
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(userName, passWord);

        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .forcePathStyle(true)
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        this.createBucketIfNotExist(s3Client, MinioConstant.MESSAGE_IMAGE_BUCKET);
        return s3Client;
    }

    private void createBucketIfNotExist(S3Client s3Client, String bucketName){
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();

        try {
            s3Client.headBucket(headBucketRequest);
            System.out.println("Bucket " + bucketName + " is existed");
        } catch (S3Exception e) {
            log.error("aws exception", e);
            //If bucket is not exist, then create the bucket
            if(e instanceof NoSuchBucketException) {
                S3Waiter s3Waiter = s3Client.waiter();
                CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build();

                s3Client.createBucket(bucketRequest);

                // Wait until the bucket is created and print out the response.
                WaiterResponse<HeadBucketResponse> waiterResponse = s3Waiter.waitUntilBucketExists(headBucketRequest);
                waiterResponse.matched().response().ifPresent(System.out::println);
                System.out.println("Bucket " + bucketName +" is created");
            }

            System.err.println(e.awsErrorDetails().errorMessage());
        }
    }
}