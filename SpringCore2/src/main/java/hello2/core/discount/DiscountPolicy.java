package hello2.core.discount;

import hello2.core.member.Member;

public interface DiscountPolicy {
    //TODO : @return 할인 대상 금액
    int discount(Member member, int price);

}
