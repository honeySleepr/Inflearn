package inflearn.springcorebasic.order;

import inflearn.springcorebasic.annotation.MainDiscountPolicy;
import inflearn.springcorebasic.discount.DiscountPolicy;
import inflearn.springcorebasic.member.Member;
import inflearn.springcorebasic.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

	private final MemberRepository memberRepository;
	private final DiscountPolicy discountPolicy;

	/* 커스텀 annotation 적용 (@MainDiscountPolicy)*/
	@Autowired
	public OrderServiceImpl(MemberRepository memberRepository,
		@MainDiscountPolicy DiscountPolicy discountPolicy) {
		this.memberRepository = memberRepository;
		this.discountPolicy = discountPolicy;
	}

	@Override
	public Order createOrder(Long memberId, String itemName, int itemPrice) {
		Member foundMember = memberRepository.findById(memberId);
		int discountPrice = discountPolicy.discount(foundMember, itemPrice);
		return new Order(memberId, itemName, itemPrice, discountPrice);
	}
}
