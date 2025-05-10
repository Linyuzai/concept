package com.github.linyuzai.plugin.core.autoload.location;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class AmazonS3V1Location extends AmazonS3Location {

    private final AmazonS3 amazonS3;

    public AmazonS3V1Location(String bucket, AmazonS3 amazonS3) {
        super(bucket);
        this.amazonS3 = amazonS3;
    }

    @Override
    public List<String> getGroups() {
        ListObjectsRequest request = new ListObjectsRequest()
                .withBucketName(validBucket())
                .withDelimiter("/");
        ObjectListing listing = amazonS3.listObjects(request);
        return listing.getCommonPrefixes()
                .stream()
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
    public long getPluginSize(String path) {
        return getObjectMetadata(path).getContentLength();
    }

    @Override
    public long getPluginCreateTime(String path) {
        String creation = getObjectMetadata(path).getUserMetaDataOf(METADATA_CREATION);
        try {
            return Long.parseLong(creation);
        } catch (Throwable e) {
            return -1;
        }
    }

    @Override
    public Object getPluginSource(String path) {
        return null;
    }

    @Override
    public boolean existPlugin(String group, String name) {
        return amazonS3.doesObjectExist(bucket, getPluginPath(group, name));
    }

    @Override
    public void renamePlugin(String group, String name, String rename) {
        amazonS3.copyObject(bucket, getPluginPath(group, name), bucket, getPluginPath(group, rename));
    }

    @Override
    public Object getVersion(String path) {
        return getObjectMetadata(path).getLastModified().getTime();
    }

    private ObjectMetadata getObjectMetadata(String key) {
        return amazonS3.getObjectMetadata(bucket, key);
    }

    @Override
    protected String validBucket() {
        if (!amazonS3.doesBucketExistV2(bucket)) {
            amazonS3.createBucket(bucket);
        }
        return bucket;
    }

    @Override
    protected List<String> getPlugins(String group, String type) {
        ListObjectsRequest request = new ListObjectsRequest()
                .withBucketName(bucket)
                .withDelimiter("/")
                .withPrefix(group + "/");
        ObjectListing listing = amazonS3.listObjects(request);
        return listing.getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .filter(it -> {
                    ObjectMetadata metadata = amazonS3.getObjectMetadata(bucket, it);
                    String pluginStatus = metadata.getUserMetaDataOf(METADATA_STATUS);
                    return type.equals(pluginStatus);
                })
                .collect(Collectors.toList());
    }

    @Override
    protected InputStream getPluginInputStream(String group, String name) throws IOException {
        return amazonS3.getObject(bucket, getPluginPath(group, name)).getObjectContent();
    }

    @Override
    protected void updateStatus(String group, String name, String status) {
        String key = getPluginPath(group, name);
        ObjectMetadata metadata = amazonS3.getObjectMetadata(bucket, key);
        Map<String, String> userMetadata;
        if (metadata.getUserMetadata() == null) {
            userMetadata = new LinkedHashMap<>();
            userMetadata.put(METADATA_CREATION, String.valueOf(new Date().getTime()));
        } else {
            userMetadata = metadata.getUserMetadata();
        }
        userMetadata.put(METADATA_STATUS, status);
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
    protected void putObject(String bucket, String key,
                             InputStream is, long length,
                             Map<String, String> userMetadata) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setUserMetadata(userMetadata);
        metadata.setContentLength(length);

        amazonS3.putObject(bucket, key, is, metadata);
    }
}
