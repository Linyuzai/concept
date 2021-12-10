package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.aop.annotation.Download;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/concept-download")
public class ConceptDownloadController {

    @Download(source = "file:/Users/tanghanzheng/Downloads/java虚拟机3/README_GBK.txt")
    @GetMapping("one")
    public void downloadOne() {

    }

    @Download(source = "file:/Users/tanghanzheng/Downloads/java虚拟机3/README_GBK.txt", forceCompress = true)
    @GetMapping("one-zip")
    public void downloadOneZip() {

    }

    @Download(source = "file:/Users/tanghanzheng/Downloads/java虚拟机3/README_GBK.txt", charset = "GBK")
    @GetMapping("one-charset")
    public void downloadOneCharset() {

    }

    @Download
    @GetMapping("dynamic")
    public File downloadDynamic() {
        return new File("/Users/tanghanzheng/Downloads/java虚拟机3/README.txt");
    }

    @Download(source = {
            "file:/Users/tanghanzheng/Downloads/java虚拟机3/README.txt",
            "file:/Users/tanghanzheng/Downloads/java虚拟机3/README_GBK.txt"})
    @GetMapping("two")
    public void downloadTwo() {

    }

    @Download(source = {
            "file:/Users/tanghanzheng/Downloads/java虚拟机3/README.txt",
            "file:/Users/tanghanzheng/Downloads/java虚拟机3/README_GBK.txt"},
            charset = "UTF-8")
    @GetMapping("two-charset")
    public void downloadTwoCharset() {

    }
}
