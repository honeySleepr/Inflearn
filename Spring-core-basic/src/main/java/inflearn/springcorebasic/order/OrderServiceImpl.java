package inflearn.springcorebasic.order;

import inflearn.springcorebasic.discount.DiscountPolicy;
import inflearn.springcorebasic.discount.FixDiscountPolicy;
import inflearn.springcorebasic.member.Member;
import inflearn.springcorebasic.member.MemberRepository;
import inflearn.springcorebasic.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

	private final MemberRepository memberRepository = new MemoryMemberRepository();
	private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

	@Override
	public Order createOrder(Long memberId, String itemName, int itemPrice) {
		Member foundMember = memberRepository.findById(memberId);
		int discountPrice = discountPolicy.discount(foundMember, itemPrice);
		return new Order(memberId, itemName, itemPrice, discountPrice);
	}
}
