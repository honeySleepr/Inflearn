package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final MemberRepository memberREpository;

	public Member login(String loginId, String password) {
		return memberREpository.findByLoginId(loginId)
			.filter(member -> member.verifyPassword(password))
			.orElse(null);
	}

}
