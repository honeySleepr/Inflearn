package hello2.core.discount;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import hello2.core.member.Grade;
import hello2.core.member.Member;

class RateDiscountPolicyTest {
    RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.")
    void vip_O() {
        //given
        Member member = new Member(1L, "memberVIP", Grade.VIP);

        //when
        int discountPrice = rateDiscountPolicy.discount(member, 10_000);

        //then
        assertThat(discountPrice).isEqualTo(1_000);
    }

    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다.")
    void vip_X() {
        //given
        Member member = new Member(2L, "memberBasic", Grade.BASIC);

        //when
        int discountPrice = rateDiscountPolicy.discount(member, 10_000);

        //then
        assertThat(discountPrice).isEqualTo(0);
    }

}
