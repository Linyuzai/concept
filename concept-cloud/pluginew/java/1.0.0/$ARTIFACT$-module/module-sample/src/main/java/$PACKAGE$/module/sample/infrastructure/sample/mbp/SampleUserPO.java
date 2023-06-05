package $PACKAGE$.module.sample.infrastructure.sample.mbp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 关联数据模型
 */
@Data
@TableName("t_sample_user")
class SampleUserPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String sampleId;

    private String userId;
}
