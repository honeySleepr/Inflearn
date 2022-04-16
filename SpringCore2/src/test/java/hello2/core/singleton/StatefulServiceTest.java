package hello2.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {

        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefuleService1 = ac.getBean(StatefulService.class);
        StatefulService statefuleService2 = ac.getBean(StatefulService.class);

        //ThreadA : A 사용자 10_000원 주문
        statefuleService1.order("userA", 10_000);
        //ThreadB : B 사용자 20_000원 주문
        statefuleService2.order("userB", 20_000);

        //ThreadA : 사용자 A 주문 금액 조회
        int price = statefuleService1.getPrice();
        System.out.println("price = " + price);
        
    }

    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}
