package com.github.linyuzai.router.autoconfigure.management;

import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.core.exception.RouterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/concept-router/management")
public class RouterManagementController {

    @Autowired
    private RouterConcept concept;

    @Autowired
    private RouterConvertor convertor;

    @PostMapping("/add")
    public void add(@RequestBody RouterVO router) {
        concept.add(convertor.vo2do(router));
    }

    @PutMapping("/update")
    public void update(@RequestBody RouterVO router) {
        if (!StringUtils.hasText(router.getId())) {
            throw new RouterException("Id is null");
        }
        concept.update(convertor.vo2do(router));
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam String id) {
        concept.delete(id);
    }

    @GetMapping("/list")
    public List<RouterVO> list() {
        return concept.routers()
                .stream()
                .map(convertor::do2vo)
                .collect(Collectors.toList());
    }
}
