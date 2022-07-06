package inflearn.springcorebasic.order;

import inflearn.springcorebasic.AppConfig;
import inflearn.springcorebasic.member.Grade;
import inflearn.springcorebasic.member.Member;
import inflearn.springcorebasic.member.MemberService;

public class OrderApp {

	public static void main(String[] args) {

		/* AppConfig가 공연관리자 역할을 한다. 의존관계를 주입해준다 */
		AppConfig appconfig = new AppConfig();
		MemberService memberService = appconfig.memberService();
		OrderService orderService = appconfig.orderService();

		Long memberId = 1L;
		Member member = new Member(memberId, "memberA", Grade.VIP);
		memberService.join(member);

		Order order = orderService.createOrder(memberId, "itemA", 10000);
		System.out.println("order = " + order);
		System.out.println("order.calculatePrice() = " + order.calculatePrice());
	}
}
