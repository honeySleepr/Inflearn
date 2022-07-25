package hello.login.web.filter;

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

	/* Http 요청이 올때마다 먼저 호출된다 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		log.info("log filter doFilter");

		/* ServletRequest -> HttpServletRequest 다운 캐스팅 */
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();

		String uuid = UUID.randomUUID().toString();

		try {
			log.info("REQUEST [{}] [{}]", uuid, requestURI);
			/* ? */
			chain.doFilter(request, response);

		} catch (Exception e) {
			throw e;
		} finally {
			log.info("RESPONSE [{}] [{}]", uuid, requestURI);
		}

	}

	@Override
	public void destroy() {
		log.info("log filter destroy");
	}
}
