package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.core.annotation.CompressCache;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.annotation.SourceCache;
import com.github.linyuzai.download.core.event.DownloadEventListener;
import com.github.linyuzai.download.core.options.ConfigurableDownloadOptions;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.reflect.SourceModel;
import com.github.linyuzai.download.core.source.reflect.SourceName;
import com.github.linyuzai.download.core.source.reflect.SourceObject;
import com.github.linyuzai.download.autoconfigure.web.reactive.DownloadMono;
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

@RestController
@RequestMapping("/concept-download-reactive")
public class ConceptDownloadReactiveController {

    @Download
    @GetMapping("error")
    public Mono<Void> error() {
        return DownloadMono.value(new Object());
    }

    @GetMapping("/ex")
    public Mono<Void> ex() {
        throw new RuntimeException();
    }

    @Download(source = "file:/Users/Shared/README.txt")
    @GetMapping("/s1")
    public Mono<Void> s1() {
        return DownloadMono.empty();
    }

    @Download
    @GetMapping("/s2")
    public Mono<Void> s2() {
        return DownloadMono.value("file:/Users/Shared/README.txt");
    }

    @Download
    @GetMapping("/s3")
    public Mono<Void> s3() {
        return DownloadMono.value(new File("/Users/Shared/README.txt"));
    }

    @Download(source = "user.home:/Public/README.txt")
    @GetMapping("/s4")
    public Mono<Void> s4() {
        return DownloadMono.empty();
    }

    @Download
    @GetMapping("/s5")
    public Mono<Void> s5() {
        return DownloadMono.value("user.home:/Public/README.txt");
    }

    @Download(source = "classpath:/download/README.txt")
    @GetMapping("/s6")
    public Mono<Void> s6() {
        return DownloadMono.empty();
    }

    @Download
    @GetMapping("/s7")
    public Mono<Void> s7() {
        return DownloadMono.value("classpath:/download/README.txt");
    }

    @Download
    @GetMapping("/s8")
    public Mono<Void> s8() {
        return DownloadMono.value(new ClassPathResource("/download/README.txt"));
    }

    @Download(filename = "s9.txt")
    @GetMapping("/s9")
    public Mono<Void> s9() {
        return DownloadMono.value("任意的文本将会直接作为文本文件处理");
    }

    @Download(source = "http://192.168.20.112:8088/demo/download")
    @GetMapping("/s10")
    public Mono<Void> s10() {
        return DownloadMono.empty();
    }

    @Download
    @GetMapping("/s11")
    public Mono<Void> s11() {
        return DownloadMono.value("http://192.168.20.112:8088/demo/download");
    }

    @Download(source = "classpath:/download/README.txt", forceCompress = true)
    @GetMapping("/s12")
    public Mono<Void> s12() {
        return DownloadMono.empty();
    }

    @Download(source = "classpath:/download/README_GBK.txt",
            filename = "readme.zip",
            charset = "GBK",
            forceCompress = true)
    @GetMapping("/s13")
    public Mono<Void> s13() {
        return DownloadMono.empty();
    }

    @Download(source = {
            "classpath:/download/text.txt",
            "http://192.168.20.112:8088/demo/download"},
            filename = "压缩包14.zip")
    @GetMapping("/s14")
    public Mono<Void> s14() {
        return DownloadMono.empty();
    }

    @Download(filename = "压缩包15.zip")
    @GetMapping("/s15")
    public Mono<Void> s15() {
        List<Object> list = new ArrayList<>();
        list.add(new File("/Users/Shared/README.txt"));
        list.add(new ClassPathResource("/download/image.jpg"));
        list.add("http://192.168.20.112:8088/demo/download");
        return DownloadMono.value(list);
    }

    @Download(filename = "压缩包16.zip")
    @SourceCache(group = "s16", delete = true)
    @CompressCache(group = "s16", name = "s16.zip", delete = true)
    @GetMapping("/s16")
    public Mono<Void> s16() {
        return s15();
    }


    @Download(source = "classpath:/download/README.txt")
    @GetMapping("/s17")
    public Mono<Void> s17() {
        return DownloadMono.value(new DownloadOptions.Configurer() {
            @Override
            public void configure(ConfigurableDownloadOptions options) {
                System.out.println("在这里可以修改本次下载的参数！");
                options.setEventListener(new DownloadEventListener() {
                    @Override
                    public void onEvent(Object event) {
                        System.out.println("Event " + event.getClass());
                    }
                });
            }
        });
    }

    @Download
    @GetMapping("/s18")
    public Mono<Void> s18() {
        return DownloadMono.value(new File("/Users/Shared"));
    }

    @Download(filename = "s19.zip")
    @SourceCache(group = "s19")
    @CompressCache(group = "s19")
    @GetMapping("/s19")
    public Mono<Void> s19() {
        List<BusinessModel> businessModels = new ArrayList<>();
        businessModels.add(new BusinessModel("1.jar", "http://192.168.20.112:8088/demo/download"));
        businessModels.add(new BusinessModel("2.jar", "http://192.168.20.112:8088/demo/download2"));
        businessModels.add(new BusinessModel("3.jar", "http://192.168.20.112:8088/demo/download3"));
        businessModels.add(new BusinessModel("classpath.txt", "classpath:/download/README.txt"));
        businessModels.add(new BusinessModel("file", new File("/Users/Shared")));
        return DownloadMono.value(businessModels);
    }

    @Download
    @GetMapping("/s20")
    public Mono<Void> s20() {
        return DownloadMono.value(Mono.just("123"));
    }

    @Download
    @GetMapping("/s21")
    public Mono<Void> s21() {
        return DownloadMono.value(Flux.just("123", "classpath:/download/image.jpg"));
    }

    //@Download(source = "classpath:/download/text.txt", inline = true, contentType = "text/plain")
    @Download(source = "classpath:/download/text.txt", inline = true, contentType = "text/plain;charset=utf-8")
    @GetMapping("/text.txt")
    public Mono<Void> readme() {
        return DownloadMono.empty();
    }

    @Download(source = "classpath:/download/image.jpg", inline = true, contentType = "image/jpeg")
    @GetMapping("/image.jpg")
    public Mono<Void> image() {
        return DownloadMono.empty();
    }

    @Download(source = "classpath:/download/video.mp4", inline = true, contentType = "video/mp4")
    @GetMapping("/video.mp4")
    public Mono<Void> video() {
        return DownloadMono.empty();
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
