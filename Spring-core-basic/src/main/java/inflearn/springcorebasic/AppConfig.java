package inflearn.springcorebasic;

import inflearn.springcorebasic.discount.DiscountPolicy;
import inflearn.springcorebasic.discount.RateDiscountPolicy;
import inflearn.springcorebasic.member.MemberRepository;
import inflearn.springcorebasic.member.MemberService;
import inflearn.springcorebasic.member.MemberServiceImpl;
import inflearn.springcorebasic.member.MemoryMemberRepository;
import inflearn.springcorebasic.order.OrderService;
import inflearn.springcorebasic.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public MemberService memberService() {
		return new MemberServiceImpl(memberRepository());
	}

	@Bean
	public OrderService orderService() {
		return new OrderServiceImpl(memberRepository(), discountPolicy());
	}

	@Bean
	public MemberRepository memberRepository() {
		return new MemoryMemberRepository();
	}

	@Bean
	public DiscountPolicy discountPolicy() {
		return new RateDiscountPolicy();
	}
}
