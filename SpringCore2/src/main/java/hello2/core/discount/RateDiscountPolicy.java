package hello2.core.discount;

import org.springframework.stereotype.Component;

import hello2.core.annotation.MainDiscountPolicy;
import hello2.core.member.Grade;
import hello2.core.member.Member;

@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy {

    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;
        }
        return 0;
    }

}
