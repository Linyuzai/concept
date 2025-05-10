package com.github.linyuzai.plugin.autoconfigure.management;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/concept-plugin/management")
public class ReactivePluginManagementController extends PluginManagementController {

    @PostMapping("/plugin/upload")
    public Mono<Void> upload(@RequestPart("file") FilePart file,
                             @RequestPart("group") String group,
                             @RequestPart("name") String name) {
        return DataBufferUtils.join(file.content())
                .flatMap(it -> {
                    try {
                        String upload = uploadPlugin(group, file.filename(), it.asInputStream(), it.readableByteCount());
                        if (StringUtils.hasText(name)) {
                            updatePlugin(group, name, upload);
                        }
                        return Mono.empty();
                    } catch (Throwable e) {
                        return Mono.error(e);
                    } finally {
                        DataBufferUtils.release(it);
                    }
                });
        /*
        File finalFile = getFinalFile(group, file.filename());
        return file.transferTo(finalFile)
                .doOnSuccess(v -> autoload(group, name, finalFile.getName()));*/
        // return file.flatMap(it -> it.transferTo(getFinalFile(group, it.filename()))).then();
    }

    @GetMapping("/plugin/download")
    public Mono<Void> download(@RequestParam("group") String group,
                               @RequestParam("name") String name,
                               @RequestParam("deleted") Boolean deleted,
                               ServerHttpResponse response) {
        Flux<DataBuffer> flux = DataBufferUtils.readInputStream(() -> {
                    setDownloadHeaders(response.getHeaders(), name);
                    return downloadPlugin(group, name, deleted);
                },
                response.bufferFactory(), 4096);
        return response.writeWith(flux);
    }
}
