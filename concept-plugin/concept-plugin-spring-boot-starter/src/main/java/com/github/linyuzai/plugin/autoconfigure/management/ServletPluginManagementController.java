package com.github.linyuzai.plugin.autoconfigure.management;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/concept-plugin/management")
public class ServletPluginManagementController extends PluginManagementController {

    @PostMapping("upload")
    public void upload(@RequestParam("file") MultipartFile[] files, @RequestParam("group") String group) throws IOException {
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            String path = location.getUnloadedPluginPath(group, filename);
            file.transferTo(new File(path));
        }
    }
}
