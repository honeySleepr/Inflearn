package hello.login.web;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import javax.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

	@Bean
	public FilterRegistrationBean logFilter() {
		FilterRegistrationBean<Filter> filterBean = new FilterRegistrationBean<>();
		filterBean.setFilter(new LogFilter());
		filterBean.setOrder(1);
		filterBean.addUrlPatterns("/*");

		return filterBean;
	}

	@Bean
	public FilterRegistrationBean loginCheckFilter() {
		FilterRegistrationBean<Filter> filterBean = new FilterRegistrationBean<>();
		filterBean.setFilter(new LoginCheckFilter());
		filterBean.setOrder(2); /* 1 순위인 logFilter를 먼저 거친 후에 2 순위인 loginCheckFilter로 온다 */
		filterBean.addUrlPatterns("/*");

		return filterBean;
	}
}
