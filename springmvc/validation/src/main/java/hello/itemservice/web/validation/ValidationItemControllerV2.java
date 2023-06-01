package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder){
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }
//    @PostMapping("/add")
    //순서가 중요! => item 객체에 바인딩된 결과가 bindingResult 여기에 담기기 때문.
    //잘 담길 수도 있지만 잘 안담겨서 오류가 날 수도 있다.
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //bindingResult 가 Map 역할을 함.
        //Map<String, String> errors = new HashMap<>();

        //검증 로직
        //itemName 에 글자가 없으면
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "quantity", " 수량은 최대 9,999까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            //가격과 수량을 곱했을 때 10000원 이상이여야 하는 검증 로직.
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                //위와 달리 field 가 없기 때문에 new ObjectError 사용.
                bindingResult.addError(new ObjectError("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " +resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            //bindingResult 는 view 에 자동으로 같이 넣어가기 때문에 model 에 안담아도 됨.
            //model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수 입니다."));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "상품 이름은 필수 입니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "상품 이름은 필수 입니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                //위와 달리 field 가 없기 때문에 new ObjectError 사용.
                //ObjectError 는 기존 데이터를 보관하는게 없고 값이 넘어오는게 아니기 때문에 바인딩이 실패할 일이 없음.
                bindingResult.addError(new ObjectError("item", null , null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " +resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            //bindingResult 는 view 에 자동으로 같이 넣어가기 때문에 model 에 안담아도 됨.
            //model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            //code 자리엔 string 배열로 넣어야되는데 이유는 첫번째가 없으면 두번째로 여러개 넣을 수 있기 때문.
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 10000}, null));
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                //위와 달리 field 가 없기 때문에 new ObjectError 사용.
                //ObjectError 는 기존 데이터를 보관하는게 없고 값이 넘어오는게 아니기 때문에 바인딩이 실패할 일이 없음.
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"} , new Object[]{10000, resultPrice}, null));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            //bindingResult 는 view 에 자동으로 같이 넣어가기 때문에 model 에 안담아도 됨.
            //model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        //검증 로직
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
//      위의 Validations.rejectIfEmptyOrWhitespace 와 같은 내용.
        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.rejectValue("itemName", "required");
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            bindingResult.rejectValue("price", "range", new Object[]{100, 1000000}, null);
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증을 하면 여기 안에 다 담음. 검증할때 bindingResult 에서 다 담았기 때문에 있으면 hasErros 에서 다 걸림
        itemValidator.validate(item, bindingResult);

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        //@InitBinder 을 사용하기 위해서 @Validated 넣어야되는데
        //@Validated 을 넣으면 item 에 대해서 자동으로 검증기가 출력이 됨.
        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

