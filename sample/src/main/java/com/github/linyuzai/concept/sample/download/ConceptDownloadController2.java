package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.core.annotation.CompressCache;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.annotation.SourceCache;
import com.github.linyuzai.download.core.options.ConfigurableDownloadOptions;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.reflect.SourceModel;
import com.github.linyuzai.download.core.source.reflect.SourceName;
import com.github.linyuzai.download.core.source.reflect.SourceObject;
import com.github.linyuzai.download.core.web.async.FileConsumer;
import com.github.linyuzai.download.core.web.async.InputStreamConsumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//webmvc base ok
//webflux base ok
//source loader ok
//okhttp ok
//executor ok
//compress format/password ok
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

    @GetMapping("text")
    public String text() {
        return "text1";
    }

    @Download(source = "http://localhost:18080/concept-download2/text")
    @GetMapping("/httpText")
    public void httpText() {
    }

    @Download(source = "classpath:/download/README.txt")
    @GetMapping("/oneOnAnnotation")
    public void oneOnAnnotation() {
    }

    @Download(source = {
            "classpath:/download/README.txt",
            "file:/Users/tanghanzheng/IdeaProjects/Github/x/concept/sample/src/main/resources/download/image.jpg",
            "user.home:/IdeaProjects/Github/x/concept/sample/src/main/resources/download/video.mp4",
            "https://services.gradle.org/distributions/gradle-2.0-all.zip"
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
            "https://services.gradle.org/distributions/gradle-2.0-all.zip",
            "https://services.gradle.org/distributions/gradle-3.0-all.zip"
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

    @Download(source = {
            "file:/Users/tanghanzheng/IdeaProjects/Github/x/concept/sample/src/main/resources/download",
    }, filename = "Password.zip", compressPassword = "${download.password}")
    @GetMapping("/password")
    public void password() {
    }

    @Download(source = {
            "file:/Users/tanghanzheng/IdeaProjects/Github/x/concept/sample/src/main/resources/download",
    }, filename = "Tar.tar", compressFormat = "tar")
    @GetMapping("/tar")
    public void tar() {
    }

    @Download(source = {
            "file:/Users/tanghanzheng/IdeaProjects/Github/x/concept/sample/src/main/resources/download",
    }, filename = "TarGz.tar.gz", compressFormat = "tar.gz")
    @GetMapping("/tarGz")
    public void tarGz() {
    }

    @Download(filename = "List.zip")
    @GetMapping("/list")
    public List<Object> list() {
        List<Object> list = new ArrayList<>();
        list.add(new File("/Users/tanghanzheng/IdeaProjects/Github/x/concept/sample/src/main/resources/download/image.jpg"));
        list.add(new ClassPathResource("/download/README.txt"));
        list.add("https://services.gradle.org/distributions/gradle-2.0-all.zip");
        list.add("https://services.gradle.org/distributions/gradle-3.0-all.zip");
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

    @Download(source = {
            "https://services.gradle.org/distributions/gradle-2.0-all.zip",
            "http://localhost:1234/error"
    }, filename = "LoadAndCompressError.zip")
    @SourceCache(group = "loadAndCompressError")
    @CompressCache(group = "loadAndCompressError", name = "CompressCache.zip")
    @GetMapping("/loadAndCompressError")
    public void loadAndCompressError() {

    }

    @Download
    @SourceCache(group = "listAndCache")
    @CompressCache(group = "listAndCache", name = "Configurer.zip")
    @GetMapping("/configurer")
    public DownloadOptions.Configurer configurer() {
        return new DownloadOptions.Configurer() {
            @Override
            public void configure(ConfigurableDownloadOptions options) {
                System.out.println("在这里可以修改本次下载的参数！");
                options.setSource(list());
            }
        };
    }

    @Download
    @SourceCache(group = "listAndCache")
    @CompressCache(group = "listAndCache", name = "Async.zip")
    @GetMapping("/async")
    public DownloadOptions.Configurer async() {
        return new DownloadOptions.Configurer() {
            @Override
            public void configure(ConfigurableDownloadOptions options) {
                System.out.println("在这里可以修改本次下载的参数！");
                options.setSource(list());
                options.setAsyncConsumer(new InputStreamConsumer() {
                    @Override
                    public void consumer(InputStream is) {
                        //输入流
                    }
                });
                options.setAsyncConsumer(new FileConsumer() {
                    @Override
                    public void consumer(File file) {
                        //文件
                        System.out.println(file.getAbsolutePath());
                    }
                });
            }
        };
    }

    /*@Download
    @GetMapping("/mono")
    public Mono<String> mono() {
        return Mono.just(anyText());
    }

    @Download
    @GetMapping("/flux")
    public Flux<Object> flux() {
        return Flux.fromIterable(list());
    }*/

    /*@Download
    @CompressCache(group = "reactiveAsync", name = "reactiveAsync.zip")
    @GetMapping("/reactiveAsync")
    public DownloadOptions.Configurer reactiveAsync() {
        return new DownloadOptions.Configurer() {
            @Override
            public void configure(ConfigurableDownloadOptions options) {
                System.out.println("在这里可以修改本次下载的参数！");
                options.setSource(flux());
                options.setAsyncConsumer(new InputStreamConsumer() {
                    @Override
                    public void consumer(InputStream is) {
                        //输入流
                    }
                });
                options.setAsyncConsumer(new FileConsumer() {
                    @Override
                    public void consumer(File file) {
                        //文件
                        System.out.println(file.getAbsolutePath());
                    }
                });
            }
        };
    }*/

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
        businessModels.add(new BusinessModel("http.zip", "https://services.gradle.org/distributions/gradle-2.0-all.zip"));
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
