package inflearn.springcorebasic.discount;

import inflearn.springcorebasic.member.Grade;
import inflearn.springcorebasic.member.Member;

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
