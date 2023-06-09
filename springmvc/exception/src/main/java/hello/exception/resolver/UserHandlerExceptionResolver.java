package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {

            if(ex instanceof UserException){
                log.info("UserException resolver to 400");
                String acceptHeader =  request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if("application/json".equals(acceptHeader)){
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    //객체를 문자로 바꿔주는 메서드
                    String result=objectMapper.writeValueAsString(errorResult);

                    //ModelAndView 로 반환해야되기 때문에 mvc1 처럼 response 에 다 세팅해줘야됨.
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result); //errorResult 를 json 으로 바꿔줘야됨. 안 그럼 에러남.
                    return new ModelAndView();
                }else {
                    // TEXT/HTML
                    return new ModelAndView("error/500");
                }
            }

        }catch (IOException e){
            log.error("resolver ex", e);
        }

        return null;
    }
}
