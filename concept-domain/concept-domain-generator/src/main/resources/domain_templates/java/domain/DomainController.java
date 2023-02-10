package _PACKAGE_.domain._DOMAIN_;

import com.gantong.lightinghiw.basic.login.Login;
import _PACKAGE_.domain._DOMAIN_.view.*;
import _PACKAGE_.domain.user._UPPER_User;
import com.github.linyuzai.domain.core.page.Pages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "_DESC_")
@RestController
@RequestMapping("/_DOMAIN_")
public class _UPPER_Controller {

    @Autowired
    protected _UPPER_Service _LOWER_Service;

    @Autowired
    protected _UPPER_Searcher _LOWER_Searcher;

    @Operation(summary = "创建_DESC_")
    @PostMapping
    public void create(@RequestBody _UPPER_CreateCommand create, @Login _UPPER_User user) {
        _LOWER_Service.create(create, user);
    }

    @Operation(summary = "更新_DESC_")
    @PutMapping
    public void update(@RequestBody _UPPER_UpdateCommand update, @Login _UPPER_User user) {
        _LOWER_Service.update(update, user);
    }

    @Operation(summary = "删除_DESC_")
    @DeleteMapping
    public void delete(@RequestBody _UPPER_DeleteCommand delete, @Login _UPPER_User user) {
        _LOWER_Service.delete(delete, user);
    }

    @Operation(summary = "分页查询_DESC_")
    @GetMapping("/page")
    public Pages<_UPPER_VO> page(_UPPER_Query query, Pages.Args page) {
        return _LOWER_Searcher.page(query, page);
    }
}
