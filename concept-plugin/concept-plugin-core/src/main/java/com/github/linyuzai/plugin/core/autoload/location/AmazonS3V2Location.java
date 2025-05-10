package com.github.linyuzai.plugin.core.autoload.location;

import lombok.Getter;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class AmazonS3V2Location extends AmazonS3Location {

    private final S3Client s3Client;

    public AmazonS3V2Location(String bucket, S3Client s3Client) {
        super(bucket);
        this.s3Client = s3Client;
    }

    @Override
    public List<String> getGroups() {
        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(validBucket())
                .delimiter("/")
                .build();
        ListObjectsResponse listing = s3Client.listObjects(request);
        return listing.commonPrefixes()
                .stream()
                .map(CommonPrefix::prefix)
                .map(it -> {
                    if (it.endsWith("/")) {
                        return it.substring(0, it.length() - 1);
                    } else {
                        return it;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public long getSize(String path) {
        return getHeadObject(path).contentLength();
    }

    @Override
    public long getCreationTimestamp(String path) {
        String creation = getHeadObject(path).metadata().get(METADATA_CREATION);
        try {
            return Long.parseLong(creation);
        } catch (Throwable e) {
            return -1;
        }
    }

    @Override
    public boolean exist(String group, String name) {
        try {
            getHeadObject(getPluginPath(group, name));
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    public void rename(String group, String name, String rename) {
        CopyObjectRequest request = CopyObjectRequest.builder()
                .sourceBucket(bucket)
                .sourceKey(getPluginPath(group, name))
                .destinationBucket(bucket)
                .destinationKey(getPluginPath(group, rename))
                .build();
        s3Client.copyObject(request);
    }

    @Override
    public Object getVersion(String path) {
        return getHeadObject(path).lastModified().getEpochSecond();
    }

    private HeadObjectResponse getHeadObject(String key) {
        HeadObjectRequest request = HeadObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        return s3Client.headObject(request);
    }

    @Override
    protected String validBucket() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder()
                    .bucket(bucket)
                    .build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(bucket)
                    .build());
        }
        return bucket;
    }

    @Override
    protected List<String> getPlugins(String group, String type) {
        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucket)
                .delimiter("/")
                .prefix(group + "/")
                .build();
        ListObjectsResponse listing = s3Client.listObjects(request);
        return listing.contents()
                .stream()
                .map(S3Object::key)
                .filter(it -> {
                    String pluginStatus = getHeadObject(it).metadata().get(METADATA_STATUS);
                    return type.equals(pluginStatus);
                })
                .collect(Collectors.toList());
    }

    @Override
    protected InputStream getPluginInputStream(String group, String name) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(getPluginPath(group, name))
                .build();
        return s3Client.getObject(request);
    }

    @Override
    protected void updateStatus(String group, String name, String status) {
        String key = getPluginPath(group, name);
        HeadObjectResponse metadata = getHeadObject(key);
        Map<String, String> userMetadata;
        if (metadata.metadata() == null) {
            userMetadata = new LinkedHashMap<>();
            userMetadata.put(METADATA_CREATION, String.valueOf(new Date().getTime()));
        } else {
            userMetadata = metadata.metadata();
        }
        userMetadata.put(METADATA_STATUS, status);
        CopyObjectRequest request = CopyObjectRequest.builder()
                .sourceBucket(bucket)
                .sourceKey(key)
                .destinationBucket(bucket)
                .destinationKey(key)
                .metadata(userMetadata)
                .metadataDirective(MetadataDirective.REPLACE)
                .build();
        s3Client.copyObject(request);
    }

    @Override
    protected void putObject(String bucket, String key,
                             InputStream is, long length,
                             Map<String, String> userMetadata) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .metadata(userMetadata)
                .build();
        s3Client.putObject(request, RequestBody.fromInputStream(is, length));
    }
}
