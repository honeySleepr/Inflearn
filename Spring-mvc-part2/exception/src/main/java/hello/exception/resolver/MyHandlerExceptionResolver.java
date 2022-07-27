package hello.exception.resolver;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) {

		log.info("MyHandlerExceptionResolver", ex);

		try {
			if (ex instanceof IllegalArgumentException) {
				log.info("IllegalArgumentException resolver to 400");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
				return new ModelAndView();
				/* 비어있는 ModelAndView를 return 하면 500에러가 뜨지 않고
				response.sendError()를 통해 보낸 에러로 응답된다 (즉, 여기서는 IllegalArgumentException(원랜 500)을 400 에러로 바꿔치기 한 것*/
			}
		} catch (IOException e) {
			log.error("resolver ex", e);
		}
		return null;
	}
}
