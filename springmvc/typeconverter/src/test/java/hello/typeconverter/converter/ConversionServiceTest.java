package hello.typeconverter.converter;


import hello.typeconverter.type.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import static org.assertj.core.api.Assertions.*;

public class ConversionServiceTest {

    @Test
    void conversionService(){
        //등록
        //DefaultConversionService 는 ConversionService 의 인터페이스를 구현하기 위한 구현체라고 보면 됨.
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        //사용
//        Integer result= conversionService.convert("10", Integer.class); // 문자 10 -> 숫자 10 으로 변환
//        System.out.println("result = "+result);
        assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10);
        assertThat(conversionService.convert(10 , String.class)).isEqualTo("10");
        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

         String ipPortString= conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
         assertThat(ipPortString).isEqualTo("127.0.0.1:8080");

    }
}
