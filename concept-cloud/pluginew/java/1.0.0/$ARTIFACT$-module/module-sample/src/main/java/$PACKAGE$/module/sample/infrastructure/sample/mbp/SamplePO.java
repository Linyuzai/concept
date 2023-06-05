package $PACKAGE$.module.sample.infrastructure.sample.mbp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.linyuzai.domain.core.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 示例数据模型
 */
@Data
@TableName("t_sample")
class SamplePO implements Identifiable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String sample;

    private String userId;

    @TableLogic
    private Boolean deleted;
}
