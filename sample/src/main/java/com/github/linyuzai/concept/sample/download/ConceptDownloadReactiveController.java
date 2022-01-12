/*
package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.core.aop.annotation.Download;
import com.github.linyuzai.download.core.web.reactive.DownloadMono;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("/concept-download-reactive")
public class ConceptDownloadReactiveController {

    @GetMapping("/video.mp4")
    public Mono<Void> video(ServerHttpResponse response) {
        ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=video.mp4");
        response.getHeaders().setContentType(MediaType.parseMediaType("video/mpeg4"));
        File file = new File("/Users/Shared/video.mp4");
        return zeroCopyResponse.writeWith(file, 0, file.length())
                .doOnNext(context -> System.out.println("Next"))
                .doOnSubscribe(subscription -> System.out.println("Subscribe"))
                .doOnSuccess(context -> System.out.println("Success"));
    }

    @Download
    //@SourceCache(group = "s21")
    //@CompressCache(group = "s21")
    @GetMapping("/rs1")
    public Mono<Void> s21() {
        List<ConceptDownloadController.BusinessModel> businessModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            //String url = "https://img2.baidu.com/it/u=3801884915,270435659&fm=26&fmt=auto";
            String url = "http://127.0.0.1:8080/concept-download-reactive/video.mp4";
            businessModels.add(new ConceptDownloadController.BusinessModel(i + ".mp4", url));
        }
        businessModels.add(new ConceptDownloadController.BusinessModel("classpath.txt", new ClassPathResource("/download/README.txt")));
        businessModels.add(new ConceptDownloadController.BusinessModel("file", new File("/Users/Shared")));
        return new DownloadMono(businessModels);
    }
}
*/
