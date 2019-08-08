package poll.com.zjd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/7/9
 * 文件描述：注解完成点击事件监听
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FmyClickView {
    /**
     * 保存所有需要设置点击事件控件的id
     * @return
     */
    int [] value();
}
