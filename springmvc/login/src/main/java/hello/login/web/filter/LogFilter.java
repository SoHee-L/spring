package hello.login.web.filter;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
//ServletRequest 는 HttpServletRequest 의 부모인데 ServletRequest 는 기능이 별로 없어서 다운캐스팅 해줘야됨.
        //그렇기 때문에 ServletRequest 거의 사용안하는 기술.
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            //chain.doFilter 는 있으면 doFilter 호출 없으면 서블릿이 호출됨.
            chain.doFilter(request, response);
        } catch (Exception e) {
            //예외 발생시 그냥 예외 던져버림
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }
    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}