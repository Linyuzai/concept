package com.github.linyuzai.plugin.autoconfigure.management;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/concept-plugin/management")
public class ServletPluginManagementController extends PluginManagementController {

    @PostMapping("upload")
    public void upload(@RequestParam("file") MultipartFile file, @RequestParam("group") String group) throws IOException {
        file.transferTo(getFinalFile(group, file.getOriginalFilename()));
    }
}
