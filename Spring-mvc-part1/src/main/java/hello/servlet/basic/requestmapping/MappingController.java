package hello.servlet.basic.requestmapping;

import hello.servlet.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class MappingController {

	/* RequestHeader 테스트 */
	@RequestMapping("/header")
	public String requestHeader(
		@RequestHeader("bc") String data,
		@RequestHeader String bcbc,
		@RequestHeader(required = false) String gitId,
		@RequestHeader(value = "id", defaultValue = "guest") String id,
		@RequestHeader MultiValueMap<String, String> map) {

		/* defaultValue 옵션이 String id에는 적용되는데 정작 map에는 id라는 키가 들어가지 않는다. */
		log.info("bc : {}, bcbc : {}, gitId : {}, id : {}", data, bcbc, gitId, id);
		map.forEach((s, strings) -> log.info("{} : {}", s, strings));

		log.info("map>>>>  bc : {}, bcbc : {}, gitId : {}, id : {}", map.get("bc"), map.get("bcbc"),
			map.get("gitId"), map.get("id"));

		return "header";
	}

	/* RequestParameter 테스트 */
	@RequestMapping("/param")
	public String requestParam(
		@RequestParam("username") String name,
		@RequestParam int age,
		@RequestParam(required = false) String gitId,
		@RequestParam(defaultValue = "guest") String id
	) {
		log.info("username : {}, age : {}, id : {}", name, age, gitId);
		return "param";
	}

	/* ModelAttribute 테스트 */
	@RequestMapping("/model")
	public String modelAttribute(@ModelAttribute HelloData helloData, String bc) {
		log.info("{}", helloData);
		log.info("bc : {}", bc);
		return "model";
	}

	/* RequestEntity 테스트 */
	@RequestMapping("/body1")
	public ResponseEntity<HelloData> requestEntity(@RequestBody HelloData helloData) {
		log.info("{}", helloData);
		return new ResponseEntity<>(helloData, HttpStatus.OK);
	}

	/* ResponseStatus 테스트 */
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping("/body2")
	// @ResponseBody는 @RestController에 포함되어있다
	public HelloData requestBody(@RequestBody HelloData helloData) {
		log.info("{}", helloData);
		return helloData;
	}

}
