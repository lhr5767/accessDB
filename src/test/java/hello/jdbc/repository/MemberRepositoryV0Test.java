package hello.jdbc.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import hello.jdbc.domain.Member;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("memberV7", 10000);
        repositoryV0.save(member);

        //findById
        Member findMember = repositoryV0.findById(member.getMemberId());
        log.info("findMember ={}", findMember);
        assertThat(findMember).isEqualTo(member);
        
        //update 
        repositoryV0.update(member.getMemberId(), 2000);
        Member updatedMember = repositoryV0.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(2000);

        //delete
        repositoryV0.delete(member.getMemberId());
        assertThatThrownBy(()-> repositoryV0.findById(member.getMemberId()))
            .isInstanceOf(NoSuchElementException.class);
   }
}