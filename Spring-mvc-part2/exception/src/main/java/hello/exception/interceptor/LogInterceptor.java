package hello.exception.interceptor;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

	public static final String LOG_ID = "logId";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		String requestURI = request.getRequestURI();
		StringBuffer requestURL = request.getRequestURL();

		String uuid = UUID.randomUUID().toString();
		request.setAttribute(LOG_ID, uuid);

		/* @RequestMapping을 사용했다면 HandlerMethod, 정적 리소스라면 ResourceHttpRequestHandler 타입을 사용한다 */
		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod) handler;
		}
		log.info("preHandle [{}][{}][{}][{}]", uuid, request.getDispatcherType(), requestURI, handler);

		return true; /* false이면 컨트롤러가 호출되지 않는다 */
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) throws Exception {
		log.info("postHandle [{}]", modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
		throws Exception {
		String requestURI = request.getRequestURI();
		String uuid = (String) request.getAttribute(LOG_ID);
		log.info("afterCompletion [{}][{}][{}][{}]", uuid, request.getDispatcherType(), requestURI, handler);
		if (ex != null) {
			log.error("aferCompletion error!!");
		}

	}
}
