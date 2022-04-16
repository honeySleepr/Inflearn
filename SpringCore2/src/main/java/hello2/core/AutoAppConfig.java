package hello2.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class))
// @Configuration 은 수동으로 빈을 설정해놓은 거기때문에 충돌이 나지 않도록 제외해줌
public class AutoAppConfig {
    // @Bean(name = "memoryMemberRepository")
    // MemberRepository memberRepository() {
    //     return new MemoryMemberRepository();
    // }
}
