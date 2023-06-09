package hello.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {
    //오류가 발생하면 코드랑 메세지를 보냄.
    private String code;
    private String message;

}
