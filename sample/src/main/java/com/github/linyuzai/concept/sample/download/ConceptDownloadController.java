package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.aop.annotation.CompressCache;
import com.github.linyuzai.download.aop.annotation.Download;
import com.github.linyuzai.download.aop.annotation.SourceCache;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/concept-download")
public class ConceptDownloadController {

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

    @Download(source = "http://127.0.0.1:8080/concept-download/text.txt?a=1&b=2")
    @GetMapping("/s9")
    public void s9() {
    }

    @Download
    @GetMapping("/s10")
    public String s10() {
        return "http://127.0.0.1:8080/concept-download/text.txt";
    }

    @Download(source = "classpath:/download/README.txt", forceCompress = true)
    @GetMapping("/s11")
    public void s11() {
    }

    @Download(source = "classpath:/download/README_GBK.txt",
            filename = "readme.zip",
            charset = "GBK",
            forceCompress = true)
    @GetMapping("/s12")
    public void s12() {
    }

    @Download(source = {
            "classpath:/download/text.txt",
            "http://127.0.0.1:8080/concept-download/image.jpg",
            "http://127.0.0.1:8080/concept-download/video.mp4"},
            filename = "压缩包13.zip")
    @GetMapping("/s13")
    public void s13() {
    }

    @Download(filename = "压缩包14.zip")
    @GetMapping("/s14")
    public List<Object> s14() {
        List<Object> list = new ArrayList<>();
        list.add(new File("/Users/Shared/README.txt"));
        list.add(new ClassPathResource("/download/image.jpg"));
        list.add("http://127.0.0.1:8080/concept-download/video.mp4");
        return list;
    }

    @Download(filename = "压缩包15.zip")
    @CompressCache(group = "s15", name = "s15.zip", delete = true)
    @GetMapping("/s15")
    public List<Object> s15() {
        return s14();
    }

    @Download(filename = "压缩包16.zip")
    @SourceCache(group = "s16")
    @CompressCache(group = "s16", name = "s16.zip")
    @GetMapping("/s16")
    public String[] s16() {
        return new String[]{
                "http://127.0.0.1:8080/concept-download/text.txt",
                "http://127.0.0.1:8080/concept-download/image.jpg",
                "http://127.0.0.1:8080/concept-download/video.mp4"
        };
    }

    @Download(source = "classpath:/download/text.txt", contentType = "text/plain")
    @GetMapping("/text.txt")
    public void readme() {
    }

    @Download(source = "classpath:/download/image.jpg", contentType = "image/jpeg")
    @GetMapping("/image.jpg")
    public void image() {
    }

    @Download(source = "classpath:/download/video.mp4", contentType = "video/mpeg4")
    @GetMapping("/video.mp4")
    public void video() {
    }
}
