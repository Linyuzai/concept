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

    @Download(source = "file:/Users/tanghanzheng/Downloads/java虚拟机3/README.txt")
    @GetMapping("/s1")
    public void s1() {

    }

    @Download
    @GetMapping("/s2")
    public String s2() {
        return "file:/Users/tanghanzheng/Downloads/java虚拟机3/README.txt";
    }

    @Download
    @GetMapping("/s3")
    public File s3() {
        return new File("/Users/tanghanzheng/Downloads/java虚拟机3/README.txt");
    }

    @Download(source = "user.home:/Downloads/java虚拟机3/README.txt")
    @GetMapping("/s4")
    public void s4() {

    }

    @Download
    @GetMapping("/s5")
    public String s5() {
        return "user.home:/Downloads/java虚拟机3/README.txt";
    }

    @Download(source = "classpath:/Downloads/java虚拟机3/README.txt")
    @GetMapping("/s6")
    public void s6() {

    }

    @Download
    @GetMapping("/s7")
    public String s7() {
        return "classpath:/Downloads/java虚拟机3/README.txt";
    }

    @Download
    @GetMapping("/s8")
    public ClassPathResource s8() {
        return new ClassPathResource("");
    }

    @Download(source = "http://127.0.0.1:8080")
    @GetMapping("/s9")
    public void s9() {

    }

    @Download
    @GetMapping("/s10")
    public String s10() {
        return "http://127.0.0.1:8080";
    }

    @Download(source = "classpath:/aa", filename = "aa", charset = "GBK", forceCompress = true)
    @GetMapping("/s11")
    public void s11() {

    }

    @Download(source = {"classpath:aaa", "http://127.0.0.1:8080"}, filename = "压缩包12.zip")
    @GetMapping("/s12")
    public void s12() {

    }

    @Download(filename = "压缩包13.zip")
    @GetMapping("/s13")
    public List<Object> s13() {
        List<Object> list = new ArrayList<>();
        list.add(new File(""));
        list.add(new ClassPathResource(""));
        list.add("http://127.0.0.1:8080");
        return list;
    }

    @Download(filename = "压缩包14.zip")
    @CompressCache(group = "s14", name = "s14.zip", delete = true)
    @GetMapping("/s14")
    public List<Object> s14() {
        return s13();
    }

    @Download(filename = "压缩包15.zip")
    @SourceCache(group = "s15")
    @CompressCache(group = "s15", name = "s15.zip", delete = true)
    @GetMapping("/s15")
    public String[] s15() {
        return new String[]{};
    }
}
