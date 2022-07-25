package hello.login.web.filter;

import hello.login.web.SessionConst;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

@Slf4j
public class LoginCheckFilter implements Filter {

	/* 로그인 안된 상태에서도 접속을 허용할 URI */
	private static final String[] whiteList = {"/", "/login", "/members/add", "/logout", "/css/*"};

	/* init, destroy는 `default 메서드`라서 로직을 추가할 게 아니라면 구현하지 않아도 된다 */
	@Override
	public void doFilter(
		ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();

		HttpServletResponse httpResponse = (HttpServletResponse) response;

		try {
			log.info("인증 체크 필터 시작 {}", requestURI);

			if (isLoginCheckPath(requestURI)) {
				log.info("인증 체크 로직 실행 {}", requestURI);
				HttpSession session = httpRequest.getSession(false);

				if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
					log.info("미인증 사용자 요청 {}", requestURI);

					/* 로그인으로 redirect */
					httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
					return; /* 미인증 사용자는 서블릿, 컨트롤러를 호출하지 않고 여기서 끝냄 */
				}
			}

			chain.doFilter(request, response);
		} catch (Exception e) {
			throw e; /* 여기서 예외를 잡으면 요청이 정상 작동된 것으로 처리되기 때문에 톰캣까지 예외를 보내줘야 한다 */
		} finally {
			log.info("인증 체크 필터 종료 {}", requestURI);
		}

	}

	/* whiteList에 지정된 uri와 `매치되지 않으면` true 반환 */
	private boolean isLoginCheckPath(String requestURI) {
		return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
	}
}
