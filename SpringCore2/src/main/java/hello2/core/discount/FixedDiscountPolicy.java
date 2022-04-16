package hello2.core.discount;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import hello2.core.member.Grade;
import hello2.core.member.Member;

@Component
@Primary
public class FixedDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount = 1000;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) { // Enum은 == 으로 비교한다
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
