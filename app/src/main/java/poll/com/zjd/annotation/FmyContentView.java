package poll.com.zjd.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/9
 * 文件描述：注解完成setContentView
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FmyContentView {
    /**
     * 保存布局文件的id eg:R.layout.main
     * @return 返回 布局id
     */
    int value();
}
