package com.github.linyuzai.plugin.autoconfigure.management;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/concept-plugin/management")
public class ReactivePluginManagementController extends PluginManagementController {

    @PostMapping("/plugin/upload")
    public Mono<Void> upload(@RequestPart("file") Mono<FilePart> file, @RequestPart("group") String group) {
        return file.flatMap(it -> it.transferTo(getFinalFile(group, it.filename()))).then();
    }
}
