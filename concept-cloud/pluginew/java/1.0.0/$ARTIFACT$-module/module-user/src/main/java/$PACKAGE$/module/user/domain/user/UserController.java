package $PACKAGE$.module.user.domain.user;

import $PACKAGE$.module.user.domain.user.view.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserSearcher userSearcher;

    @Operation(summary = "用户详情")
    @GetMapping("{id}")
    public UserVO get(@Parameter(description = "用户ID") @PathVariable String id) {
        return userSearcher.get(id);
    }
}
