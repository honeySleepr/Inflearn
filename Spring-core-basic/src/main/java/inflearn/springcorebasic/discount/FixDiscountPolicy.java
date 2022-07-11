package inflearn.springcorebasic.discount;

import inflearn.springcorebasic.member.Grade;
import inflearn.springcorebasic.member.Member;
import org.springframework.stereotype.Component;

@Component
public class FixDiscountPolicy implements DiscountPolicy {

	private int discountFixAmount = 1000;

	@Override
	public int discount(Member member, int price) {
		// enum은 '==' 으로 비교한다
		if (member.getGrade() == Grade.VIP) {
			return discountFixAmount;
		}
		return 0;
	}
}
