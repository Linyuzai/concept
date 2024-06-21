package com.github.linyuzai.plugin.autoconfigure.management;

import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/concept-plugin/management")
public class ServletPluginManagementController extends PluginManagementController {

    @PostMapping("/plugin/upload")
    public void upload(@RequestParam("file") MultipartFile file,
                       @RequestParam("group") String group,
                       @RequestParam("name") String name) throws IOException {
        File finalFile = getFinalFile(group, file.getOriginalFilename());
        file.transferTo(finalFile);
        autoload(group, name, finalFile);
    }

    @GetMapping("/plugin/download")
    public void downloadPlugin(@RequestParam("group") String group,
                               @RequestParam("name") String name,
                               @RequestParam("deleted") Boolean deleted,
                               ServerHttpResponse response) throws IOException {
        try (InputStream is = downloadPlugin(group, name, deleted, response.getHeaders())) {
            StreamUtils.copy(is, response.getBody());
        }
    }
}
