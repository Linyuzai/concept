package com.github.linyuzai.plugin.remote.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.github.linyuzai.plugin.core.storage.RemotePluginStorage;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
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
        copyObject(bucket, key, bucket, key, userMetadata);
    }

    @Override
    protected void copyObject(String srcBucket, String srcKey, String destBucket, String destKey, Map<String, String> userMetadata) {
        ObjectMetadata metadata = getObjectMetadata(srcBucket, srcKey);
        metadata.setUserMetadata(userMetadata);
        CopyObjectRequest request = new CopyObjectRequest();
        request.setSourceBucketName(srcBucket);
        request.setSourceKey(srcKey);
        request.setDestinationBucketName(destBucket);
        request.setDestinationKey(destKey);
        request.setNewObjectMetadata(metadata);
        request.setMetadataDirective(MetadataDirective.REPLACE.name());
        amazonS3.copyObject(request);
    }

    @Override
    protected InputStream getObject(String bucket, String key) throws IOException {
        return amazonS3.getObject(bucket, key).getObjectContent();
    }

    @Override
    protected void deleteObject(String bucket, String key) {
        amazonS3.deleteObject(bucket, key);
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
            return getObjectMetadata(getBucket(), path).getContentLength();
        }

        @Override
        public long getCreateTime() {
            String creation = getObjectMetadata(getBucket(), path)
                    .getUserMetaDataOf(METADATA_CREATION);
            try {
                return Long.parseLong(creation);
            } catch (Throwable e) {
                return -1;
            }
        }

        @Override
        public Object getVersion() {
            return getObjectMetadata(getBucket(), path).getLastModified().getTime();
        }

        @Override
        public String getUrl() {
            return "aws://" + path;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return getObject(getBucket(), path);
        }
    }
}
