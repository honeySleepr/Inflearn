package hello.exception.filter;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("log filter init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		log.info("log filter doFilter");

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		DispatcherType dispatcherType = httpRequest.getDispatcherType();

		String uuid = UUID.randomUUID().toString();

		try {
			log.info("REQUEST [{}] [{}] [{}]", uuid, dispatcherType, requestURI);
			chain.doFilter(request, response);

		} catch (Exception e) {
			throw e;
		} finally {
			log.info("REQUEST [{}] [{}] [{}]", uuid, dispatcherType, requestURI);
		}

	}

	@Override
	public void destroy() {
		log.info("log filter destroy");
	}
}
