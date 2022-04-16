package hello2.core.order;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hello2.core.discount.FixedDiscountPolicy;
import hello2.core.member.Grade;
import hello2.core.member.Member;
import hello2.core.member.MemoryMemberRepository;

class OrderServiceImplTest {

    @Test
    void createOrder() {
        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "name", Grade.VIP));

        OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, new FixedDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 10_000);
        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}
