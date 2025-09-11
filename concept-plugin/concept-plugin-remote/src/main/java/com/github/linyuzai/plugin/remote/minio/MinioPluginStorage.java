package com.github.linyuzai.plugin.remote.minio;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.storage.RemotePluginStorage;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Getter
public class MinioPluginStorage extends RemotePluginStorage {

    private final MinioClient minioClient;

    public MinioPluginStorage(String bucket, Filter filter, MinioClient minioClient) {
        this(bucket, filter, minioClient, null);
    }

    public MinioPluginStorage(String bucket, Filter filter, MinioClient minioClient, Executor executor) {
        super(bucket, filter, executor);
        this.minioClient = minioClient;
    }

    @SneakyThrows
    @Override
    protected boolean existBucket(String bucket) {
        BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucket).build();
        return minioClient.bucketExists(args);
    }

    @SneakyThrows
    @Override
    protected void createBucket(String bucket) {
        MakeBucketArgs args = MakeBucketArgs.builder().bucket(bucket).build();
        minioClient.makeBucket(args);
    }

    @SneakyThrows
    @Override
    protected List<String> listObjects(String bucket, String prefix, String delimiter) {
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(prefix)
                .delimiter(delimiter)
                .build();
        Iterable<Result<Item>> results = minioClient.listObjects(args);
        List<String> list = new ArrayList<>();
        for (Result<Item> result : results) {
            list.add(result.get().objectName());
        }
        return list;
    }

    @SneakyThrows
    @Override
    protected Map<String, String> getUserMetadata(String bucket, String key) {
        return getObjectMetadata(bucket, key).userMetadata();
    }

    @Override
    protected void putUserMetadata(String bucket, String key, Map<String, String> userMetadata) {
        copyObject(bucket, key, bucket, key, userMetadata);
    }

    @SneakyThrows
    @Override
    protected boolean existObject(String bucket, String key) {
        try {
            getObjectMetadata(bucket, key);
            return true;
        } catch (ErrorResponseException e) {
            return false;
        }
    }

    @SneakyThrows
    @Override
    protected void copyObject(String srcBucket, String srcKey, String destBucket, String destKey, Map<String, String> userMetadata) {
        CopyObjectArgs.Builder builder = CopyObjectArgs.builder()
                .bucket(destBucket)
                .object(destKey)
                .source(CopySource.builder()
                        .bucket(srcBucket)
                        .object(srcKey)
                        .build());
        if (userMetadata == null) {
            CopyObjectArgs args = builder
                    .metadataDirective(Directive.COPY)
                    .build();
            minioClient.copyObject(args);
        } else {
            CopyObjectArgs args = builder
                    .userMetadata(userMetadata)
                    .metadataDirective(Directive.REPLACE)
                    .build();
            minioClient.copyObject(args);
        }
    }

    @SneakyThrows
    @Override
    protected InputStream getObject(String bucket, String key) {
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucket).object(key).build();
        return minioClient.getObject(args);
    }

    @SneakyThrows
    @Override
    protected void deleteObject(String bucket, String key) {
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(key)
                .build();
        minioClient.removeObject(args);
    }

    @SneakyThrows
    @Override
    protected void putObject(String bucket, String key, InputStream is, long length, Map<String, String> userMetadata) {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucket)
                .object(key)
                .stream(is, length, -1)
                .userMetadata(userMetadata)
                .build();
        minioClient.putObject(args);
    }

    @Override
    public PluginDefinition getPluginDefinition(String type, String group, String name) {
        return new PluginDefinitionImpl(getPluginPath(group, name));
    }

    private StatObjectResponse getObjectMetadata(String bucket, String key)
            throws ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        StatObjectArgs args = StatObjectArgs.builder().bucket(bucket).object(key).build();
        return minioClient.statObject(args);
    }

    @Getter
    @RequiredArgsConstructor
    public class PluginDefinitionImpl extends RemotePluginDefinition<StatObjectResponse> {

        private final String path;

        @SneakyThrows
        @Override
        public long getSize() {
            return useObjectMetadata().size();
        }

        @Override
        public long getCreateTime() {
            String creation = useObjectMetadata().userMetadata().get(METADATA_CREATION);
            try {
                return Long.parseLong(creation);
            } catch (Throwable e) {
                return -1;
            }
        }

        @SneakyThrows
        @Override
        public Object getVersion() {
            return useObjectMetadata().lastModified().toEpochSecond();
        }

        @Override
        public InputStream getInputStream() {
            return getObject(getBucket(), path);
        }

        @SneakyThrows
        @Override
        protected StatObjectResponse newObjectMetadata() {
            return getObjectMetadata(getBucket(), path);
        }
    }
}
