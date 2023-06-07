package hello.login.domain.login;

import hello.login.web.member.Member;
import hello.login.web.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

//로그인을 맞게 했는지에 대한 핵심 비즈니스 로직.
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null 이면 로그인 실패
     */
    public Member login(String loginId, String password){

        //밑에와 같은 코드
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//        Member member = findMemberOptional.get();
//        if(member.getPassword().equals(password)){
//            return member;
//        }else {
//            return null;
//        }

        //같으면 member 를 쓰고
        return memberRepository.findByLoginId(loginId)
                .filter(member -> member.getPassword().equals(password))
                //아니면 null 을 반환해라는 의미
                .orElse(null);

    }
}
