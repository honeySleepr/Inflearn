package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")/* messages.properties에 등록된 메시지 사용 */
public class BadRequestException extends RuntimeException {

}
