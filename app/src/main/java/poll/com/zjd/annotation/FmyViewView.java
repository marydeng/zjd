package poll.com.zjd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/9
 * 文件描述：使用注解findViewById
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface  FmyViewView {
    /**
     * 保存view控件的id
     * @return view控件id
     */
    int value();
}
