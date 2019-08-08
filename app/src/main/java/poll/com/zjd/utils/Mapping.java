package poll.com.zjd.utils;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 创建者:   张文辉
 * 创建时间: 2017/8/2  23:07
 * 包名:     poll.com.zjd.utils
 * 项目名:   zjd
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {
    public String name() default "";
}