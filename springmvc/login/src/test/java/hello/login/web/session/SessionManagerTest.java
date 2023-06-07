package hello.login.web.session;

import hello.login.web.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest(){

        //세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();
        //세션 생성 해서 response 까지 담긴 것. 웹브라우저까지 응답 나갔다고 생각.
        sessionManager.createSession(member, response);

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); //mySessionId=1231231-12312-qweqwe

        //세션 조회
         Object result= sessionManager.getSession(request);
         //세션 데이터가 조회되는지 확인.
        assertThat(result).isEqualTo(member);

        //세션 만료
        sessionManager.expire(request);
         Object expired = sessionManager.getSession(request);
         assertThat(expired).isNull();
    }

}