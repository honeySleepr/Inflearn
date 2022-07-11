package inflearn.springcorebasic.annotation;

import java.lang.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;

/* Qualifier를 편하게 사용하기 위한 커스텀 Annotation */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {

}
