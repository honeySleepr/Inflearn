package hello.exception.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ServletExController {

	@GetMapping("/error-ex")
	public void errorEx() {
		/* 500 - Internal Server Error 페이지가 뜬다 */
		throw new RuntimeException("예외 발생!");
	}

	@GetMapping("/error-404")
	public void error404(HttpServletResponse response) throws IOException {
		response.sendError(404, "404 오류!");
	}

	@GetMapping("/error-403")
	public void error403(HttpServletResponse response) throws IOException {
		response.sendError(403, "403 오류!");
	}

	@GetMapping("/error-500")
	public void error500(HttpServletResponse response) throws IOException {
		response.sendError(500, "500이라니!");
	}

	@GetMapping("/error-505")
	public void error505(HttpServletResponse response) throws IOException {
		response.sendError(505, "505이라니!");
	}
}
