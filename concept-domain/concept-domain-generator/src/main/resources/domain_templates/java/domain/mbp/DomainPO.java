package _PACKAGE_.domain._DOMAIN_.mbp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.linyuzai.domain.core.Identifiable;
import lombok.Data;

@Data
@TableName("t__LOWER_")
public class _UPPER_PO implements Identifiable {

    @TableId(type = IdType.INPUT)
    private String id;
}
