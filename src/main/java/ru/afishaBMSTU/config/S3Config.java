package ru.afishaBMSTU.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3Config {

    @Value("${integration.s3.key-id}")
    private String keyId;

    @Value("${integration.s3.secret-key}")
    private String secretKey;

    @Value("${integration.s3.region}")
    private String region;

    @Value("${integration.s3.s3-endpoint}")
    private String s3Endpoint;

    @Bean
    public S3Client s3Client() {
        AwsCredentials credentials = AwsBasicCredentials.create(keyId, secretKey);

        return S3Client.builder()
                .httpClient(ApacheHttpClient.create())
                .region(Region.of(region))
                .endpointOverride(URI.create(s3Endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
