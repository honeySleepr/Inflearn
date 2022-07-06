package inflearn.springcorebasic;

import inflearn.springcorebasic.discount.FixDiscountPolicy;
import inflearn.springcorebasic.member.MemberService;
import inflearn.springcorebasic.member.MemberServiceImpl;
import inflearn.springcorebasic.member.MemoryMemberRepository;
import inflearn.springcorebasic.order.OrderService;
import inflearn.springcorebasic.order.OrderServiceImpl;

public class AppConfig {

	public MemberService memberService() {
		return new MemberServiceImpl(new MemoryMemberRepository());
	}

	public OrderService orderService() {
		return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
	}
}
