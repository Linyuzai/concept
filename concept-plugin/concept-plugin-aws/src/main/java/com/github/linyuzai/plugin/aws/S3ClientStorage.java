package com.github.linyuzai.plugin.aws;

import com.github.linyuzai.plugin.core.factory.PluginSourceProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class S3ClientStorage extends AWSStorage {

    private final S3Client s3Client;

    public S3ClientStorage(String bucket, S3Client s3Client) {
        super(bucket);
        this.s3Client = s3Client;
    }

    @Override
    public long getPluginSize(String path) {
        return getHeadObject(getOrCreateBucket(), path).contentLength();
    }

    @Override
    public long getPluginCreateTime(String path) {
        String creation = getHeadObject(getOrCreateBucket(), path)
                .metadata().get(METADATA_CREATION);
        try {
            return Long.parseLong(creation);
        } catch (Throwable e) {
            return -1;
        }
    }

    @Override
    public Object getPluginSource(String path) {
        return new PluginSource(path);
    }

    @Override
    public boolean existPlugin(String group, String name) {
        try {
            getHeadObject(getOrCreateBucket(), getPluginPath(group, name));
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    public void renamePlugin(String group, String name, String rename) {
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
        return getHeadObject(getOrCreateBucket(), path).lastModified().getEpochSecond();
    }

    private HeadObjectResponse getHeadObject(String bucket, String key) {
        HeadObjectRequest request = HeadObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        return s3Client.headObject(request);
    }

    @Override
    protected boolean existBucket(String bucket) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder()
                    .bucket(bucket)
                    .build());
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }

    @Override
    protected void createBucket(String bucket) {
        s3Client.createBucket(CreateBucketRequest.builder()
                .bucket(bucket)
                .build());
    }

    @Override
    protected List<String> listObjects(String bucket, String prefix, String delimiter) {
        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucket)
                .delimiter(delimiter)
                .prefix(prefix)
                .build();
        ListObjectsResponse listing = s3Client.listObjects(request);
        return listing.contents()
                .stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    @Override
    protected Map<String, String> getUserMetadata(String bucket, String key) {
        return getHeadObject(bucket, key).metadata();
    }

    @Override
    protected void putUserMetadata(String bucket, String key, Map<String, String> userMetadata) {
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
    protected InputStream getObject(String group, String name) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(getPluginPath(group, name))
                .build();
        return s3Client.getObject(request);
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

    @RequiredArgsConstructor
    public class PluginSource implements PluginSourceProvider {

        private final String path;

        @Override
        public String getKey() {
            return path;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return getObject(getOrCreateBucket(), path);
        }
    }
}
