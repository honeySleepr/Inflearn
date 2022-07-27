package hello.exception;

import hello.exception.filter.LogFilter;
import hello.exception.interceptor.LogInterceptor;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor())
			.order(1)
			.addPathPatterns("/**")
			.excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**");
		/* 인터셉터는 Dispatcher 타입을 기준으로 조건을 거는 기능이 없다. 대신에 에러 페이지 호출 URI를 제외시키면 된다 */

	}

	//	@Bean
	public FilterRegistrationBean logFilter() {
		FilterRegistrationBean<Filter> filterBean = new FilterRegistrationBean<>();
		filterBean.setFilter(new LogFilter());
		filterBean.setOrder(1);
		filterBean.addUrlPatterns("/*");
		/* 해당 filter를 적용할 Dispatcher 타입을 지정한다. 따로 지정하지 않으면 REQUEST가 기본값이다 */
		filterBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);

		return filterBean;
	}
}
