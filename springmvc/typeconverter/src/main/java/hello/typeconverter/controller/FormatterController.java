package hello.typeconverter.controller;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class FormatterController {

    @GetMapping("/formatter/edit")
    public String formatterForm(Model model){

        Form form = new Form();
        form.setNumber(10000);
        form.setLocalDateTime(LocalDateTime.now());
        model.addAttribute("form", form);
        return "formatter-form";
    }

    //"10,000"
    //"2023-06-12 14:34:41" -> localDateTime
    @PostMapping("/formatter/edit")
    public String formattingEdit(@ModelAttribute Form form){
        return "formatter-view";
    }

    @Data
    static class Form{
        //해당 패턴들로 인해 "10,000" 이 들어와도 10000 으로 바꿔줌.
        @NumberFormat(pattern = "###,###")
        private Integer number;

        //month 의 MM 대문자는 글로벌 표준임 안그럼 minutes 와 구분 x
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localDateTime;
    }
}
