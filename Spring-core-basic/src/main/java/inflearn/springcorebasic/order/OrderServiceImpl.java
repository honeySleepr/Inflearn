package inflearn.springcorebasic.order;

import inflearn.springcorebasic.discount.DiscountPolicy;
import inflearn.springcorebasic.member.Member;
import inflearn.springcorebasic.member.MemberRepository;

public class OrderServiceImpl implements OrderService {

	private final MemberRepository memberRepository;
	private final DiscountPolicy discountPolicy;

	public OrderServiceImpl(MemberRepository memberRepository,
		DiscountPolicy discountPolicy) {
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
