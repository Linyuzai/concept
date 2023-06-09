package $PACKAGE$.module.sample.domain.sample;

import $PACKAGE$.domain.user.User;
import $PACKAGE$.login.Login;
import $PACKAGE$.module.sample.domain.sample.view.SampleCreateCommand;
import $PACKAGE$.module.sample.domain.sample.view.SampleUpdateCommand;
import $PACKAGE$.module.sample.domain.sample.view.SampleDeleteCommand;
import $PACKAGE$.module.sample.domain.sample.view.SampleQuery;
import $PACKAGE$.module.sample.domain.sample.view.SampleVO;
import $PACKAGE$.token.Token;
import com.github.linyuzai.domain.core.page.Pages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "示例")
@RestController
@RequestMapping("sample")
public class SampleController {

    @Autowired
    private SampleApplicationService sampleApplicationService;

    @Autowired
    private SampleSearcher sampleSearcher;

    @Operation(summary = "创建")
    @PostMapping
    public void create(@RequestBody SampleCreateCommand create, @Login User user) {
        sampleApplicationService.create(create, user);
    }

    @Operation(summary = "更新")
    @PutMapping
    public void update(@RequestBody SampleUpdateCommand update, @Login User user) {
        sampleApplicationService.update(update, user);
    }

    @Operation(summary = "删除")
    @DeleteMapping
    public void delete(@RequestBody SampleDeleteCommand delete, @Login User user) {
        sampleApplicationService.delete(delete, user);
    }

    /**
     * 默认集成了 <a href="https://github.com/Linyuzai/concept/wiki/Concept-Cloud-Web">concept-cloud-web</a>
     * 进行请求和响应的拦截，这里为了方便调试演示，将不拦截 Token
     */
    @Token(required = false)
    @Operation(summary = "详情")
    @GetMapping("{id}")
    public SampleVO get(@Parameter(description = "ID") @PathVariable String id) {
        return sampleSearcher.get(id);
    }

    /**
     * 默认集成了 <a href="https://github.com/Linyuzai/concept/wiki/Concept-Cloud-Web">concept-cloud-web</a>
     * 进行请求和响应的拦截，这里为了方便调试演示，将不拦截 Token
     */
    @Token(required = false)
    @Operation(summary = "列表查询")
    @GetMapping("list")
    public List<SampleVO> list(SampleQuery query) {
        return sampleSearcher.list(query);
    }

    /**
     * 默认集成了 <a href="https://github.com/Linyuzai/concept/wiki/Concept-Cloud-Web">concept-cloud-web</a>
     * 进行请求和响应的拦截，这里为了方便调试演示，将不拦截 Token
     */
    @Token(required = false)
    @Operation(summary = "分页查询")
    @GetMapping("page")
    public Pages<SampleVO> page(SampleQuery query, Pages.Args page) {
        return sampleSearcher.page(query, page);
    }
}