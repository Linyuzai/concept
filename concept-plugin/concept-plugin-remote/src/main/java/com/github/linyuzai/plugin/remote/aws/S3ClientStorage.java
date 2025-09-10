package com.github.linyuzai.plugin.remote.aws;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.storage.RemotePluginStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Getter
public class S3ClientStorage extends RemotePluginStorage {

    private final S3Client s3Client;

    public S3ClientStorage(String bucket, Filter filter, S3Client s3Client) {
        this(bucket, filter, s3Client, null);
    }

    public S3ClientStorage(String bucket, Filter filter, S3Client s3Client, Executor executor) {
        super(bucket, filter, executor);
        this.s3Client = s3Client;
    }

    @Override
    public PluginDefinition getPluginDefinition(String type, String group, String name) {
        return new PluginDefinitionImpl(getPluginPath(group, name));
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
        copyObject(bucket, key, bucket, key, userMetadata);
    }

    @Override
    protected boolean existObject(String bucket, String key) {
        try {
            getHeadObject(bucket, key);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    protected void copyObject(String srcBucket, String srcKey, String destBucket, String destKey, Map<String, String> userMetadata) {
        CopyObjectRequest.Builder builder = CopyObjectRequest.builder()
                .sourceBucket(srcBucket)
                .sourceKey(srcKey)
                .destinationBucket(destBucket)
                .destinationKey(destKey);
        if (userMetadata == null) {
            CopyObjectRequest request = builder.build();
            s3Client.copyObject(request);
        } else {
            CopyObjectRequest request = builder
                    .metadata(userMetadata)
                    .metadataDirective(MetadataDirective.REPLACE)
                    .build();
            s3Client.copyObject(request);
        }
    }

    @Override
    protected InputStream getObject(String bucket, String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        return s3Client.getObject(request);
    }

    @Override
    protected void deleteObject(String bucket, String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(request);
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

    @Getter
    @RequiredArgsConstructor
    public class PluginDefinitionImpl extends RemotePluginDefinition<HeadObjectResponse> {

        private final String path;

        @Override
        public long getSize() {
            return useObjectMetadata().contentLength();
        }

        @Override
        public long getCreateTime() {
            String creation = useObjectMetadata().metadata().get(METADATA_CREATION);
            try {
                return Long.parseLong(creation);
            } catch (Throwable e) {
                return -1;
            }
        }

        @Override
        public Object getVersion() {
            return useObjectMetadata().lastModified().getEpochSecond();
        }

        @Override
        public InputStream getInputStream() {
            return getObject(getBucket(), path);
        }

        @Override
        protected HeadObjectResponse newObjectMetadata() {
            return getHeadObject(getBucket(), path);
        }
    }
}
