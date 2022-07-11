package inflearn.springcorebasic.scan.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ComponentFilterAppConfigTest {

	@Test
	void filterScan() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
			ComponentFilterAppConfig.class);

		BeanA beanA = ac.getBean("beanA", BeanA.class);
		assertThat(beanA).isNotNull();
		assertThatThrownBy(() -> ac.getBean("beanB", BeanB.class))
			.isInstanceOf(NoSuchBeanDefinitionException.class);
	}

	@Configuration
	/* 이렇게 사용할 일은 거의 없다 */
	@ComponentScan(
		includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
		excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
	)
	static class ComponentFilterAppConfig {

	}
}
