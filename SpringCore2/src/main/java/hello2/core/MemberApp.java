package hello2.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello2.core.member.Grade;
import hello2.core.member.Member;
import hello2.core.member.MemberService;

public class MemberApp {

    public static void main(String[] args) {
        /*AppConfig appConfig = new AppConfig();
        MemberService memberService = appConfig.memberService();*/

        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        // AppConfig에 설정된 @Bean 들을 스프링 컨테이너에 넣어서 관리해줌

        MemberService memberService = ac.getBean("memberService", MemberService.class);
        // 설정해놓은 Bean을 꺼내온다
        //name은 AppConfig의 메서드명을 따라간다

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find member = " + findMember.getName());
    }
}
