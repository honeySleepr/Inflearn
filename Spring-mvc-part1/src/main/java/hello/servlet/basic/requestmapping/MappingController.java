package hello.servlet.basic.requestmapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MappingController {

	@RequestMapping("/header")
	public String requestHeader(
		@RequestHeader("bc") String data,
		@RequestHeader String bcbc,
		@RequestHeader(required = false) String gitId,
		@RequestHeader(value = "id", defaultValue = "guest") String id,
		@RequestHeader MultiValueMap<String, String> map) {
		/* header 테스트 */
		/* defaultValue 옵션이 String id에는 적용되는데 정작 map에는 id라는 키가 들어가지 않는다. */
		log.info("bc : {}, bcbc : {}, gitId : {}, id : {}", data, bcbc, gitId, id);
		map.forEach((s, strings) -> log.info("{} : {}", s, strings));

		log.info("map>>>>  bc : {}, bcbc : {}, gitId : {}, id : {}", map.get("bc"), map.get("bcbc"),
			map.get("gitId"), map.get("id"));

		return "ok";
	}

	@RequestMapping("/param")
	public String requestParam(
		@RequestParam("username") String name,
		@RequestParam int age,
		@RequestParam(required = false) String gitId,
		@RequestParam(defaultValue = "guest") String id
	) {

		/* request parameter 테스트 */
		log.info("username : {}, age : {}, id : {}", name, age, gitId);
		return "ok";
	}
}
