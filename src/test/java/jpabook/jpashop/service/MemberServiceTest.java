package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("Jinho");
        
        //when
        Long saveId =  memberService.joinMember(member);
        Member findMember = memberRepository.findOne(saveId);

        //then
        Assert.assertEquals(member, findMember);
    }
    
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("Jin1");

        Member member2 = new Member();
        member2.setName("Jin1");

        //when
        memberService.joinMember(member1);
        try {
            memberService.joinMember(member2);
        } catch (IllegalStateException e) {
            return;
        }

        //then
        Assert.fail("에러가 발생해야 한다.");
    }
}