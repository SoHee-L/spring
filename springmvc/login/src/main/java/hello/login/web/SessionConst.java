package hello.login.web;

// 웹에 값을 넣었다 뺐다 할때 쓰려고 만들었기 때문에 앞으로 생성할 객체가 아님 new 해서 생성하지 않음.
//그렇기 때문에 생성되지 않게 추상클래스로 만들거나 (abstract)
//상속 받아 쓰는 것이기 때문에 interface 로 쓰는게 좋음.
public class SessionConst {
    public static final String LOGIN_MEMBER= "loginMember";

}
