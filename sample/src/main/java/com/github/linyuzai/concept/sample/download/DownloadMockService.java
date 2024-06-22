package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.autoconfigure.web.mock.DownloadMock;
import com.github.linyuzai.download.core.annotation.Download;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

@Service
public class DownloadMockService {

    @Autowired
    private DownloadMock downloadMock;

    @SneakyThrows
    public void mock() {
        File out = new File("/Users/tanghanzheng/concept/download/mock.zip");
        Object download = downloadMock.download(this, Files.newOutputStream(out.toPath()));
        System.out.println(download);
        /*if (download instanceof Mono) {
            ((Mono<?>) download).subscribe();
        }*/
    }

    @Download
    public File file() {
        return new File("/Users/tanghanzheng/IdeaProjects/Github/x/concept/sample/src/main/resources/download");
    }
}
