package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();  // 동시성 문제 고려할 경우 : 컨커런트 해쉬
    private static long sequence = 0L; // 동시성 문제 고려할 경우 : 어텀long

    @Override
    public Member save(Member member) {
        member.setId(++sequence);  // ID setting
        store.put(member.getId(), member);  // member를 store에 저장한다.
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {

//        return store.get(id);  // null일 경우가 있기 때문에 빨간줄
        return Optional.ofNullable(store.get(id));  // null 일 수 있다고 명시
    }

    @Override
    public Optional<Member> findByname(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny(); // 루프를 돌면서 찾아지면 그냥 걔를 반환함
        // null 이면 옵셔널에 null이 포함되어 반환됨
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
