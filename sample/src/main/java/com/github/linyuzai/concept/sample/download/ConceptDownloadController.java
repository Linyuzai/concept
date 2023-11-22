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
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/concept-download")
public class ConceptDownloadController {

    @Download
    @GetMapping("error")
    public Object error() {
        return new Object();
    }

    @GetMapping("/ex")
    public void ex() {
        throw new RuntimeException();
    }

    @Download(source = "file:/Users/Shared/README.txt")
    @GetMapping("/s1")
    public void s1() {
    }

    @Download
    @GetMapping("/s2")
    public String s2() {
        return "file:/Users/Shared/README.txt";
    }

    @Download
    @GetMapping("/s3")
    public File s3() {
        return new File("/Users/Shared/README.txt");
    }

    @Download(source = "user.home:/Public/README.txt")
    @GetMapping("/s4")
    public void s4() {
    }

    @Download
    @GetMapping("/s5")
    public String s5() {
        return "user.home:/Public/README.txt";
    }

    @Download(source = "classpath:/download/README.txt")
    @GetMapping("/s6")
    public void s6() {
    }

    @Download
    @GetMapping("/s7")
    public String s7() {
        return "classpath:/download/README.txt";
    }

    @Download
    @GetMapping("/s8")
    public ClassPathResource s8() {
        return new ClassPathResource("/download/README.txt");
    }

    @Download(filename = "s9.txt")
    @GetMapping("/s9")
    public String s9() {
        return "任意的文本将会直接作为文本文件处理";
    }

    @Download(source = "http://192.168.20.112:8088/demo/download")
    @GetMapping("/s10")
    public void s10() {
    }

    @Download
    @GetMapping("/s11")
    public String s11() {
        return "http://192.168.20.112:8088/demo/download";
    }

    @Download(source = "classpath:/download/README.txt", forceCompress = true)
    @GetMapping("/s12")
    public void s12() {
    }

    @Download(source = "classpath:/download/README_GBK.txt",
            filename = "readme.zip",
            charset = "GBK",
            forceCompress = true)
    @GetMapping("/s13")
    public void s13() {
    }

    @Download(source = {
            "classpath:/download/text.txt",
            "http://192.168.20.112:8088/demo/download"},
            filename = "压缩包14.zip")
    @GetMapping("/s14")
    public void s14() {
    }

    @Download(filename = "压缩包15.zip")
    @GetMapping("/s15")
    public List<Object> s15() {
        List<Object> list = new ArrayList<>();
        list.add(new File("/Users/Shared/README.txt"));
        list.add(new ClassPathResource("/download/image.jpg"));
        list.add("http://192.168.20.112:8088/demo/download");
        return list;
    }

    @Download(filename = "压缩包16.zip")
    @SourceCache(group = "s16", delete = true)
    @CompressCache(group = "s16", name = "s16.zip", delete = true)
    @GetMapping("/s16")
    public List<Object> s16() {
        return s15();
    }


    @Download(source = "classpath:/download/README.txt")
    @GetMapping("/s17")
    public DownloadOptions.Configurer s17() {
        return new DownloadOptions.Configurer() {
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
        };
    }

    @Download
    @GetMapping("/s18")
    public File s18() {
        return new File("/Users/Shared");
    }

    @Download(filename = "s19.zip")
    @SourceCache(group = "s19")
    @CompressCache(group = "s19", name = "s19.zip")
    @GetMapping("/s19")
    public List<BusinessModel> s19() {
        List<BusinessModel> businessModels = new ArrayList<>();
        businessModels.add(new BusinessModel("1.jar", "http://192.168.20.112:8088/demo/download"));
        businessModels.add(new BusinessModel("2.jar", "http://192.168.20.112:8088/demo/download2"));
        businessModels.add(new BusinessModel("3.jar", "http://192.168.20.112:8088/demo/download3"));
        businessModels.add(new BusinessModel("classpath.txt", "classpath:/download/README.txt"));
        businessModels.add(new BusinessModel("file", new File("/Users/Shared")));
        return businessModels;
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

    //@Download(source = "classpath:/download/text.txt", inline = true, charset = "utf-8", contentType = "text/plain;charset=utf-8")
    @Download(source = "classpath:/download/text.txt")
    @GetMapping("/text.txt")
    public void readme() {
    }

    @Download(source = "classpath:/download/image.jpg", inline = true, contentType = "image/jpeg")
    @GetMapping("/image.jpg")
    public void image() {
    }

    @Download(source = "classpath:/download/video.mp4", inline = true, contentType = "video/mp4")
    @GetMapping("/video.mp4")
    public void video() {
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
