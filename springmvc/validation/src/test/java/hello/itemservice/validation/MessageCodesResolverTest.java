package hello.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){
        String[] messageCodes= codesResolver.resolveMessageCodes("required", "item");
        assertThat(messageCodes).containsExactly("required.item", "required");
//        for(String messageCode : messageCodes){
//            System.out.println("messageCode = " + messageCode);
//        }
//          해당 messageCode 를 찍어보면 두 가지를 뱉는데 이 코드들을 넣어주면 우선순위 순서대로 찾아줌.
//          new ObjectError("item", new String[]{"required.item", "required"})
    }

    @Test
    void messageCodesResolverField(){
        String[] messageCodes= codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
//        for(String messageCode : messageCodes){
//            System.out.println("messageCode = " + messageCode);
//
//        }
//          bindingResult 가 내부적으로 메세지 코드를 씀
//          bindingResult.rejectValue("itemName", "required");
//          rejectValue 안에서 codesResolver 를 호출함. 그래서 new FieldError 를 만듬.
//          new FieldError("item", "itemName", null, false, messageCodes, null);
//          여기의 messageCodes 위에서 만든 messageCodes 여서 4개의 메세지가 순서대로 들어감.

    }
}
