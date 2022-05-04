package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	/* 회원 가입 */
	@Transactional // 다른 메서드들은 `readOnly=true`가 적용되지만 얘는 제외
	public Long join(Member member) {
		validateDuplicateMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	/* 회원 전체 조회 */

	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	public Member findOne(Long id) {
		return memberRepository.findOne(id);
	}

	private void validateDuplicateMember(Member member) {
		List<Member> members = memberRepository.findByName(member.getName());
		if (!members.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 이름입니다.");
		}
	}


}
