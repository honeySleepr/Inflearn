package hello2.core.order;

import org.springframework.stereotype.Component;

import hello2.core.annotation.MainDiscountPolicy;
import hello2.core.discount.DiscountPolicy;
import hello2.core.member.Member;
import hello2.core.member.MemberRepository;

@Component
// @RequiredArgsConstructor // final 이 붙은 필드를 모아서 생성자를 만들어줌
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository; //final 을 이용해 초기화를 강제한다
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice); //등급만 넘겨도 되지만 확장성 등을 고려해서
        // member를 통째로 넘긴다

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
