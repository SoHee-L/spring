package hello.login.web.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    //원래는 나중 관리를 위해서 멤버리포지토리의 인터페이스를 구현하는게 더 좋음.
    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequence = 0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

//optional 사용 x
//    public Member findByLoginId(String loginId){
//        List<Member> all = findAll();
//        for (Member m : all){
//            //멤버의 LoginId 가 파라미터로 넘어온 loginId 와 같은가
//            if(m.getLoginId().equals(loginId)){
//                //같으면 m을 반환
//                return m;
//            }
//        }
//        return null;
//    }

//optional 사용 o
//    public Optional<Member> findByLoginId(String loginId){
//        List<Member> all = findAll();
//        for (Member m : all){
//            //멤버의 LoginId 가 파라미터로 넘어온 loginId 와 같은가
//            if(m.getLoginId().equals(loginId)){
//                //같으면 m을 반환
//                return Optional.of(m);
//            }
//        }
//        return Optional.empty();
//    }

    //람다 사용
    public Optional<Member> findByLoginId(String loginId){
        //list 를 stream 이라는 것으로 바꿈 그럼 그게 루프를 돈다고 생각.
        //그래서 filter 하면 그 조건에 만족할 때만 다음단계로 넘어감. 만족하지 않으면 버려짐
        //findFirst 는 먼저나오는 애를 받아다가 반환해버림.
        return findAll().stream()
                .filter(m-> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll(){
        //store 에 있는 멤버의 키,밸류중 밸류만 뽑아서 list 로 반환함.
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }

}
