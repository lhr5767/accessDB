package hello.jdbc.repository;

import static hello.jdbc.connection.ConnectionConst.PASSWORD;
import static hello.jdbc.connection.ConnectionConst.URL;
import static hello.jdbc.connection.ConnectionConst.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repositoryV1;

    @BeforeEach
    void beforeEach() {
        //기본 DriverManager - 항상 새로운 커넥션을 획득
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);


        //커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        repositoryV1 = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException, InterruptedException {
        //save
        Member member = new Member("memberV7", 10000);
        repositoryV1.save(member);

        //findById
        Member findMember = repositoryV1.findById(member.getMemberId());
        log.info("findMember ={}", findMember);
        assertThat(findMember).isEqualTo(member);
        
        //update 
        repositoryV1.update(member.getMemberId(), 2000);
        Member updatedMember = repositoryV1.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(2000);

        //delete
        repositoryV1.delete(member.getMemberId());
        assertThatThrownBy(()-> repositoryV1.findById(member.getMemberId()))
            .isInstanceOf(NoSuchElementException.class);

        Thread.sleep(1000);
   }
}