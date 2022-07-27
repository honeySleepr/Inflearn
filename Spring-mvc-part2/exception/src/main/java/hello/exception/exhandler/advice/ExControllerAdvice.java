package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST) /* 여기서 error code를 붙여주지 않으면 정상 응답으로 간주하여 200 응답을 내보낸다 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResult illegalExHandler(IllegalArgumentException e) {
		log.error("[exceptionHandler] ex", e);
		return new ErrorResult("BAD", e.getMessage());
	}

	@ExceptionHandler /* 예외 class 생략 가능 */
	public ResponseEntity<ErrorResult> userExHandler(UserException e) {
		log.error("[exceptionHandler] ex", e);
		ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
		return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler
	public ErrorResult exHandler(Exception e) { /* 위의 세부적인 예외들에 해당되지 않는 예외는 여기서 잡힌다 */
		log.error("[exceptionHandler] ex", e);
		return new ErrorResult("EX", "내부 오류");
	}
}
