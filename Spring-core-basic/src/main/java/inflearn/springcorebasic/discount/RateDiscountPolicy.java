package inflearn.springcorebasic.discount;

import inflearn.springcorebasic.annotation.MainDiscountPolicy;
import inflearn.springcorebasic.member.Grade;
import inflearn.springcorebasic.member.Member;
import org.springframework.stereotype.Component;

/* 커스텀 annotation 적용 (@MainDiscountPolicy)*/
@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy {

	private int discountPercent = 10;

	@Override
	public int discount(Member member, int price) {
		if (member.getGrade() == Grade.VIP) {
			return price * discountPercent / 100;
		} else {
			return 0;
		}
	}
}
