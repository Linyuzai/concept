package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.aop.annotation.Download;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concept-download")
public class ConceptDownloadController {

    @Download(original = {
            "file:/Users/tanghanzheng/Downloads/java虚拟机3/README.txt"},
            skipCompressOnSingle = false,
            charset = "UTF-8")
    @GetMapping("static-by-annotation")
    public void downloadStaticByAnnotation() {
        //带编码的压缩
    }
}
