package com.github.linyuzai.router.autoconfigure.management;

import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.core.exception.RouterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Router Management Controller
 */
@RestController
@RequestMapping("/concept-router/management")
public class RouterManagementController {

    @Autowired
    private RouterConcept concept;

    @Autowired
    private RouterConvertor convertor;

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获得所有服务
     *
     * @return 所有服务
     */
    @GetMapping("/services")
    public ResultVO services() {
        try {
            List<String> services = new ArrayList<>(discoveryClient.getServices());
            services.add(0, "*");
            return ResultVO.builder()
                    .success(true)
                    .object(services)
                    .build();
        } catch (Throwable e) {
            return ResultVO.builder()
                    .success(false)
                    .message(e.getMessage())
                    .object(Collections.emptyList())
                    .build();
        }
    }

    /**
     * 添加路由
     *
     * @param router 路由
     * @return 添加结果
     */
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

    /**
     * 修改路由
     *
     * @param router 路由
     * @return 修改结果
     */
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

    /**
     * 删除路由
     *
     * @param id 路由ID
     * @return 删除结果
     */
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

    /**
     * 获得路由列表
     *
     * @return 路由列表
     */
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
