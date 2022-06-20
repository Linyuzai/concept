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
    public ResultVO add(@RequestBody RouterVO router) {
        try {
            router.setId(null);
            concept.add(convertor.vo2do(router));
            return ResultVO.builder()
                    .success(true)
                    .build();
        } catch (Throwable e) {
            return ResultVO.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody RouterVO router) {
        try {
            if (!StringUtils.hasText(router.getId())) {
                throw new RouterException("Id is null");
            }
            concept.update(convertor.vo2do(router));
            return ResultVO.builder()
                    .success(true)
                    .build();
        } catch (Throwable e) {
            return ResultVO.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/delete")
    public ResultVO delete(@RequestParam String id) {
        try {
            concept.delete(id);
            return ResultVO.builder()
                    .success(true)
                    .build();
        } catch (Throwable e) {
            return ResultVO.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

    @GetMapping("/list")
    public ResultVO list() {
        try {
            List<RouterVO> vos = concept.routers()
                    .stream()
                    .map(convertor::do2vo)
                    .collect(Collectors.toList());
            return ResultVO.builder()
                    .success(true)
                    .object(vos)
                    .build();
        } catch (Throwable e) {
            return ResultVO.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }
}
