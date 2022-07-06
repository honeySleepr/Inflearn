package inflearn.springcorebasic.order;

import inflearn.springcorebasic.member.Grade;
import inflearn.springcorebasic.member.Member;
import inflearn.springcorebasic.member.MemberService;
import inflearn.springcorebasic.member.MemberServiceImpl;

public class OrderApp {

	public static void main(String[] args) {

		MemberService memberService = new MemberServiceImpl();
		OrderService orderService = new OrderServiceImpl();

		Long memberId = 1L;
		Member member = new Member(memberId, "memberA", Grade.VIP);
		memberService.join(member);

		Order order = orderService.createOrder(memberId, "itemA", 10000);
		System.out.println("order = " + order);
		System.out.println("order.calculatePrice() = " + order.calculatePrice());
	}
}
