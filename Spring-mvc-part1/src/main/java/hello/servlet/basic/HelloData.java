package hello.servlet.basic;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class HelloData {

	private String username;
	private int age;

	public HelloData(String username, int age) {
		this.username = username;
		this.age = age;
	}
}
