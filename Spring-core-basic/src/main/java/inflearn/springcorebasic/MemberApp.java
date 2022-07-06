package inflearn.springcorebasic;

import inflearn.springcorebasic.member.Grade;
import inflearn.springcorebasic.member.Member;
import inflearn.springcorebasic.member.MemberService;
import inflearn.springcorebasic.member.MemberServiceImpl;

public class MemberApp {

	public static void main(String[] args) {
		Long id = 1L;
		MemberService memberService = new MemberServiceImpl();
		Member member = new Member(id, "memberA", Grade.VIP);
		memberService.join(member);

		Member foundMember = memberService.findMember(id);
		System.out.println("new member= " + member.getName());
		System.out.println("found member= " + foundMember.getName());
	}
}
