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

@RequiredArgsConstructor
public class MinioPluginStorage extends RemotePluginStorage {

    private final MinioClient minioClient;

    public MinioPluginStorage(String bucket, MinioClient minioClient) {
        super(bucket, null);
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
        ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucket).prefix(prefix).build();
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

    @SneakyThrows
    @Override
    protected void putUserMetadata(String bucket, String key, Map<String, String> userMetadata) {
        CopyObjectArgs args = CopyObjectArgs.builder()
                .bucket(bucket)
                .object(key)
                .source(CopySource.builder()
                        .bucket(bucket)
                        .object(key)
                        .build())
                .userMetadata(userMetadata)
                .metadataDirective(Directive.REPLACE)
                .build();
        minioClient.copyObject(args);
    }

    @SneakyThrows
    @Override
    protected InputStream getObject(String bucket, String key) throws IOException {
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucket).object(key).build();
        return minioClient.getObject(args);
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
    public PluginDefinition getPluginDefinition(String path) {
        return new PluginDefinitionImpl(path);
    }

    @SneakyThrows
    @Override
    public boolean existPlugin(String group, String name) {
        try {
            getObjectMetadata(getBucket(), getPluginPath(group, name));
            return true;
        } catch (ErrorResponseException e) {
            return false;
        }
    }

    @SneakyThrows
    @Override
    public void renamePlugin(String group, String name, String rename) {
        CopyObjectArgs args = CopyObjectArgs.builder()
                .bucket(getBucket())
                .object(getPluginPath(group, rename))
                .source(CopySource.builder()
                        .bucket(getBucket())
                        .object(getPluginPath(group, name))
                        .build())
                .metadataDirective(Directive.COPY)
                .build();
        minioClient.copyObject(args);
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
    public class PluginDefinitionImpl implements PluginDefinition, PluginDefinition.Loadable {

        private final String path;

        @SneakyThrows
        @Override
        public long getSize() {
            return getObjectMetadata(getBucket(), path).size();
        }

        @Override
        public long getCreateTime() {
            String creation = getUserMetadata(getBucket(), path).get(METADATA_CREATION);
            try {
                return Long.parseLong(creation);
            } catch (Throwable e) {
                return -1;
            }
        }

        @SneakyThrows
        @Override
        public Object getVersion() {
            return getObjectMetadata(getBucket(), path).lastModified().toEpochSecond();
        }

        @Override
        public Object getSource() {
            return this;
        }

        @Override
        public String getUrl() {
            return "minio:" + path;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return getObject(getBucket(), path);
        }
    }
}
