package com.github.linyuzai.plugin.autoconfigure.management;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

@RestController
@RequestMapping("/concept-plugin/management")
public class ReactivePluginManagementController extends PluginManagementController {

    @PostMapping("upload")
    public Mono<Void> upload(@RequestPart("file") Flux<FilePart> files, @RequestPart("group") String group) {
        return files.flatMap(it -> {
            String filename = it.filename();
            String path = location.getLoadedPluginPath(group, filename);
            return it.transferTo(new File(path));
        }).collectList().then();
    }
}
