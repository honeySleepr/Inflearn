package inflearn.springcorebasic;

import inflearn.springcorebasic.member.Grade;
import inflearn.springcorebasic.member.Member;
import inflearn.springcorebasic.member.MemberService;

public class MemberApp {

	public static void main(String[] args) {

		/* AppConfig가 공연관리자 역할을 한다. 의존관계를 주입해준다 */
		AppConfig appconfig = new AppConfig();
		MemberService memberService = appconfig.memberService();
		Long id = 1L;

		Member member = new Member(id, "memberA", Grade.VIP);
		memberService.join(member);

		Member foundMember = memberService.findMember(id);
		System.out.println("new member= " + member.getName());
		System.out.println("found member= " + foundMember.getName());
	}
}
