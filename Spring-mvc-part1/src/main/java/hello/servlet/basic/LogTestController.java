package hello.servlet.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogTestController {

	private static final Logger log = LoggerFactory.getLogger(LogTestController.class);
}
