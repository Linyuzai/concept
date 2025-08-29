package com.github.linyuzai.plugin.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.github.linyuzai.plugin.core.storage.RemotePluginStorage;
import com.github.linyuzai.plugin.core.storage.PluginDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Getter
public class AmazonS3Storage extends RemotePluginStorage {

    private final AmazonS3 amazonS3;

    public AmazonS3Storage(String bucket, AmazonS3 amazonS3) {
        this(bucket, amazonS3, null);

    }

    public AmazonS3Storage(String bucket, AmazonS3 amazonS3, Executor executor) {
        super(bucket, executor);
        this.amazonS3 = amazonS3;
    }

    @Override
    public PluginDefinition getPluginDefinition(String path) {
        return new PluginDefinitionImpl(path);
    }

    @Override
    public boolean existPlugin(String group, String name) {
        return amazonS3.doesObjectExist(getBucket(), getPluginPath(group, name));
    }

    @Override
    public void renamePlugin(String group, String name, String rename) {
        amazonS3.copyObject(getBucket(), getPluginPath(group, name), getBucket(), getPluginPath(group, rename));
    }

    private ObjectMetadata getObjectMetadata(String bucket, String key) {
        return amazonS3.getObjectMetadata(bucket, key);
    }

    @Override
    protected boolean existBucket(String bucket) {
        return amazonS3.doesBucketExistV2(bucket);
    }

    @Override
    protected void createBucket(String bucket) {
        amazonS3.createBucket(bucket);
    }

    @Override
    protected List<String> listObjects(String bucket, String prefix, String delimiter) {
        ListObjectsRequest request = new ListObjectsRequest()
                .withBucketName(bucket)
                .withDelimiter(delimiter)
                .withPrefix(prefix);
        ObjectListing listing = amazonS3.listObjects(request);
        return listing.getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }


    @Override
    protected Map<String, String> getUserMetadata(String bucket, String key) {
        return getObjectMetadata(bucket, key).getUserMetadata();
    }

    @Override
    protected void putUserMetadata(String bucket, String key, Map<String, String> userMetadata) {
        ObjectMetadata metadata = amazonS3.getObjectMetadata(bucket, key);
        metadata.setUserMetadata(userMetadata);
        CopyObjectRequest request = new CopyObjectRequest();
        request.setSourceBucketName(bucket);
        request.setSourceKey(key);
        request.setDestinationBucketName(bucket);
        request.setDestinationKey(key);
        request.setNewObjectMetadata(metadata);
        request.setMetadataDirective(MetadataDirective.REPLACE.name());
        amazonS3.copyObject(request);
    }

    @Override
    protected InputStream getObject(String group, String name) throws IOException {
        return amazonS3.getObject(bucket, getPluginPath(group, name)).getObjectContent();
    }

    @Override
    protected void putObject(String bucket, String key,
                             InputStream is, long length,
                             Map<String, String> userMetadata) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setUserMetadata(userMetadata);
        metadata.setContentLength(length);
        amazonS3.putObject(bucket, key, is, metadata);
    }

    @Getter
    @RequiredArgsConstructor
    public class PluginDefinitionImpl implements PluginDefinition, PluginDefinition.Loadable {

        private final String path;

        @Override
        public long getSize() {
            return getObjectMetadata(getOrCreateBucket(), path).getContentLength();
        }

        @Override
        public long getCreateTime() {
            String creation = getObjectMetadata(getOrCreateBucket(), path)
                    .getUserMetaDataOf(METADATA_CREATION);
            try {
                return Long.parseLong(creation);
            } catch (Throwable e) {
                return -1;
            }
        }

        @Override
        public Object getVersion() {
            return getObjectMetadata(getOrCreateBucket(), path).getLastModified().getTime();
        }

        @Override
        public Object getSource() {
            return this;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return getObject(getOrCreateBucket(), path);
        }
    }
}
