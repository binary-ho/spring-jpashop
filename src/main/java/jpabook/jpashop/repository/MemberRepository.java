package jpabook.jpashop.repository;

import java.util.Optional;
import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findById(Long id);
    public Optional<Member> findByName(String name);
    public Member findOne(Long id);
}
