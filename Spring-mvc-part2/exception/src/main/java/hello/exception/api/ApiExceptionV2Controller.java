package hello.exception.api;

import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

	/* #ExceptionHandler를 사용한 예외처리 메서드들은 @ControllerAdvice를 적용한 클래스로 전부 옮긴다! */
	//	@ExceptionHandler
	//	public ResponseEntity<ErrorResult> userExHandler(UserException e) {
	//		log.error("[exceptionHandler] ex", e);
	//		ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
	//		return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
	//	}

	@GetMapping("/api2/members/{id}")
	public MemberDto getMember(@PathVariable("id") String id) {

		if (id.equals("ex")) {
			throw new RuntimeException("잘못된 사용자");
		}

		if (id.equals("bad")) {
			throw new IllegalArgumentException("잘못된 입력");
		}

		if (id.equals("user-ex")) {
			throw new UserException("잘못된 입력");
		}

		return new MemberDto(id, "hello " + id);
	}

	@Data
	@AllArgsConstructor
	static class MemberDto {

		private String memberId;
		private String name;
	}
}
