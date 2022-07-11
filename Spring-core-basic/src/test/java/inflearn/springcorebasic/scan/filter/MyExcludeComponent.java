package inflearn.springcorebasic.scan.filter;

import java.lang.annotation.*;

// 커스텀 어노테이션 ; Custom Annotation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {

}
