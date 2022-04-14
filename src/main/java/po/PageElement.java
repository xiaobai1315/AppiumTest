package po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.openqa.selenium.By;

/**
 * @author zhanghongjie11
 * @date 2021/10/29 3:58 下午
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageElement {
    String by;
    String path;
    String desc;
}
