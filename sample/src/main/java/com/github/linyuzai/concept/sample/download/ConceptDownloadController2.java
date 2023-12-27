package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.core.annotation.CompressCache;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.annotation.SourceCache;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.options.ConfigurableDownloadOptions;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.reflect.SourceModel;
import com.github.linyuzai.download.core.source.reflect.SourceName;
import com.github.linyuzai.download.core.source.reflect.SourceObject;
import com.github.linyuzai.download.core.web.async.FileConsumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("/concept-download2")
public class ConceptDownloadController2 {

    @Download
    @GetMapping("error")
    public void error() {
        throw new RuntimeException("Error");
    }

    @Download
    @GetMapping("/empty")
    public void empty() {

    }

    @Download(source = "classpath:/download/README.txt")
    @GetMapping("/oneOnAnnotation")
    public void oneOnAnnotation() {
    }

    @Download(source = {
            "classpath:/download/README.txt",
            "file:/Users/tanghanzheng/IdeaProjects/Github/x/concept/sample/src/main/resources/download/image.jpg",
            "user.home:/IdeaProjects/Github/x/concept/sample/src/main/resources/download/video.mp4",
            "https://services.gradle.org/distributions/gradle-8.4-all.zip"
    })
    @GetMapping("/listOnAnnotation")
    public void listOnAnnotation() {
    }

    @Download(source = "classpath:/download/README_GBK.txt",
            filename = "readme.zip",
            charset = "GBK",
            forceCompress = true)
    @GetMapping("/charsetAndForceCompress")
    public void charsetAndForceCompress() {
    }

    @Download(filename = "AnyText.txt")
    @GetMapping("/anyText")
    public String anyText() {
        return "任意的文本将会直接作为文本文件处理";
    }

    @Download(source = {
            "https://services.gradle.org/distributions/gradle-8.4-all.zip",
            "https://services.gradle.org/distributions/gradle-8.5-all.zip"
    }, filename = "ManyHttp.zip")
    @GetMapping("/manyHttp")
    public void manyHttp() {
    }

    @Download(source = {
            "classpath:/download/README.txt",
            "file:/Users/tanghanzheng/IdeaProjects/Github/x/concept/sample/src/main/resources/download",
    }, filename = "Dir.zip")
    @GetMapping("/dir")
    public void dir() {
    }

    @Download(filename = "List.zip")
    @GetMapping("/list")
    public List<Object> list() {
        List<Object> list = new ArrayList<>();
        list.add(new File("/Users/tanghanzheng/IdeaProjects/Github/x/concept/sample/src/main/resources/download/image.jpg"));
        list.add(new ClassPathResource("/download/README.txt"));
        list.add("https://services.gradle.org/distributions/gradle-8.4-all.zip");
        list.add("https://services.gradle.org/distributions/gradle-8.5-all.zip");
        return list;
    }

    @Download(filename = "ListAndCache.zip")
    @SourceCache(group = "listAndCache")
    @CompressCache(group = "listAndCache", name = "CompressCache.zip")
    @GetMapping("/listAndCache")
    public List<Object> listAndCache() {
        return list();
    }

    @Download(filename = "ListAndCacheAndDelete.zip")
    @SourceCache(group = "listAndCacheAndDelete_s", delete = true)
    @CompressCache(group = "listAndCacheAndDelete_c", name = "Temp.zip", delete = true)
    @GetMapping("/listAndCacheAndDelete")
    public List<Object> listAndCacheAndDelete() {
        return list();
    }

    //TODO 当加载失败或压缩失败时，会遗留错误的缓存文件

    @Download(source = "classpath:/download/README.txt")
    @SourceCache(group = "listAndCache")
    @CompressCache(group = "listAndCache", name = "Async.zip")
    @GetMapping("/async")
    public DownloadOptions.Configurer async() {
        return options -> {
            System.out.println("在这里可以修改本次下载的参数！");
            options.setAsyncConsumer((FileConsumer) file -> {
                System.out.println(file.getAbsolutePath());
            });
        };
    }

    @Download
    @GetMapping("/s20")
    public Mono<String> s20() {
        return Mono.just("123");
    }

    @Download
    @GetMapping("/s21")
    public Flux<String> s21() {
        return Flux.just("123", "classpath:/download/image.jpg");
    }

    @Download(source = "classpath:/download/text.txt", inline = true, charset = "utf-8", contentType = "text/plain;charset=utf-8")
    @GetMapping("/text.txt")
    public void inlineText() {
    }

    @Download(source = "classpath:/download/image.jpg", inline = true, contentType = "image/jpeg")
    @GetMapping("/image.jpg")
    public void inlineImage() {
    }

    @Download(source = "classpath:/download/video.mp4", inline = true, contentType = "video/mp4")
    @GetMapping("/video.mp4")
    public void inlineVideo() {
    }

    @Download(filename = "POJO.zip")
    @SourceCache(group = "pojo")
    @CompressCache(group = "pojo", name = "POJO.zip")
    @GetMapping("/pojo")
    public List<BusinessModel> pojo() {
        List<BusinessModel> businessModels = new ArrayList<>();
        businessModels.add(new BusinessModel("http.zip", "https://services.gradle.org/distributions/gradle-8.4-all.zip"));
        businessModels.add(new BusinessModel("classpath.txt", "classpath:/download/README.txt"));
        businessModels.add(new BusinessModel("dir", new File("/Users/tanghanzheng/IdeaProjects/Github/x/concept/sample/src/main/resources/download")));
        return businessModels;
    }

    @Data
    @SourceModel
    @AllArgsConstructor
    public static class BusinessModel {

        @SourceName
        private String name;

        @SourceObject
        private Object source;
    }
}
