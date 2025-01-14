package com.green.jwt.config.jwt;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Setter
@Getter
@EqualsAndHashCode //서로 같은 값을 가졌으면 true로 반환하는 오버라이딩 에노테이션(Equals, HashCode 메소드 오버라이딩)
public class JwtUser implements UserDetails {
    private long signedUserId;
    private List<String> roles; //인가(권한)처리 때 사용, ROLE_이름. ROLE_USER, ROLE_ADMIN 같은 식으로 작성

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //리턴타입은 Collection. 방 하나하나의 타입은 <>지정을 한다.
        // 그런데 <?>라면 그 콜렉션의 타입은 Object가 됨.
        //<? extends GrantedAuthority>: GrantedAuthority를 상속받은 객체만 가능하도록 제한하는 것.(본인포함)
        //만약 <? super GrantedAuthority>라면 GrantedAuthority의 부모 객체만 가능하도록 하는 제한(본인포함)

        // Authentication에 Authorities(타입 SimpleGrantedAuthority)가 포함됨. 여기서 Authorities는 권한.

//        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
//        for(String role : roles){
//            authorities.add(new SimpleGrantedAuthority(role));
//        }
//        return authorities;
        //위와 아래가 같음
        return roles.stream() //List<String>을 stream<List<String>>으로 변환
                .map(SimpleGrantedAuthority::new) //map은 똑같은 크기의 size 스트림을 만든다. Stream<List<SimpleGrantedAuthority>>으로 변환
                .toList();

                //여기서 map() 안의 코드가 하는 일은 SimpleGrantedAuthority(n번째 roles의 값)을 하나 반환 하는 것.
//                .map(new Function<String, SimpleGrantedAuthority>() {
//                    @Override
//                    public SimpleGrantedAuthority apply(String str) {
//                        return new SimpleGrantedAuthority(str);
//                    }
//                })      혹은
                //.map(item -> new SimpleGrantedAuthority(item))과 같이 쓸 수 있음.

        //람다식: 추상메소드가 하나인 인터페이스를 한 줄로 익명클래스 구현
        //메서드참조: 추상 메소드를 구현하고 쓰는 과정에서 파라미터가 존재하며, 그 파라미터를 가공하지 않고 그대로 쓸 경우 생략할 수 있음.
        //중괄호를 생략하는 경우: 세미콜론(;) 못씀. 또한 return 메소드일 경우 return도 자동으로 해줌.<-중괄호 쓸거면 return 해야함.
    }

    //GreenSecurity 의 doFilterInternal 대신함

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
