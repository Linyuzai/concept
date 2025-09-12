package com.github.linyuzai.plugin.autoconfigure.management;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/concept-plugin/management")
public class ServletPluginManagementController extends PluginManagementController {

    @PostMapping("/plugin/upload")
    public void upload(@RequestParam("file") MultipartFile file,
                       @RequestParam("group") String group,
                       @RequestParam("name") String name) throws IOException {
        //File finalFile = getFinalFile(group, file.getOriginalFilename());
        //file.transferTo(finalFile);
        String upload = uploadPlugin(group, file.getOriginalFilename(), file.getInputStream(), file.getSize());
        if (StringUtils.hasText(name)) {
            updatePlugin(group, name, upload);
        }
    }

    @GetMapping("/plugin/download")
    public ResponseEntity<Resource> download(@RequestParam("group") String group,
                                             @RequestParam("name") String name,
                                             @RequestParam("deleted") Boolean deleted) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        setDownloadHeaders(headers, name);
        InputStream is = downloadPlugin(group, name, deleted);
        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(is));
    }

    /*@GetMapping("/plugin/download")
    public void downloadPlugin(@RequestParam("group") String group,
                               @RequestParam("name") String name,
                               @RequestParam("deleted") Boolean deleted,
                               ServerHttpResponse response) throws IOException {
        try (InputStream is = downloadPlugin(group, name, deleted, response.getHeaders())) {
            StreamUtils.copy(is, response.getBody());
        }
    }*/
}
