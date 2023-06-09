package hello.exception.api;

import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiExceptionV2Controller {
// 해당 예외처리 로직들은 ExControllerAdvice 로 옮김

//    // 이 컨트롤러 안에서 IllegalArgumentException 이 터지면 얘가 잡고 이 로직이 호출됨.
//    // ErrorResult 가 RestController 이기 때문에 그대로 json 으로 반환됨.
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illegalExHandler(IllegalArgumentException e){
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("BAD", e.getMessage());
//    }
//
//    @ExceptionHandler
//    //ResponseEntity 는 컨트롤러 호출한것과 거의 같다고 보면 됨.
//    public ResponseEntity<ErrorResult> userExHandler(UserException e){
//          log.error("[exceptionHandler] ex", e);
//          ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//          return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
//    }
//
//    //IllegalArgumentException 와 UserException 의 최상위는 Exception 이기 때문에 이둘을 타지 않으면
//    //Exception 이 호출됨.
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    // ExceptionHandler 는 이 컨트롤러 안에서 호출됨. 다른 컨트롤러랑은 상관 x
//    @ExceptionHandler
//    public ErrorResult exHandler(Exception e){
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("EX", "내부 오류");
//    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id){
        if(id.equals("ex")){
            //RuntimeException 도 부모가 Exception 이라 에러가 나면 IllegalArgumentException, UserException 가 아닌
            //Exception 이 호출됨.
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if(id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id, "hello"+ id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }
}
