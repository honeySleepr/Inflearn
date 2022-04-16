package hello2.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello2.core.discount.DiscountPolicy;
import hello2.core.discount.RateDiscountPolicy;
import hello2.core.member.MemberService;
import hello2.core.member.MemberServiceImpl;
import hello2.core.member.MemoryMemberRepository;
import hello2.core.order.OrderService;
import hello2.core.order.OrderServiceImpl;

// 구체 클래스를 변경하는 경우 클라이언트 코드를 건드릴 필요가 없이 AppConfig만 바꾸면 된다.
@Configuration
public class AppConfig { //이게 팩토리 메소드 방식 : 외부에서 메소드를 호출해서 제공하는 방식

    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemoryMemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }

}
