package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    /**
     * @ModelAttribute 는 이렇게 컨트롤러에 있는 별도의 메서드에 적용할 수 있다.
    이렇게하면 해당 컨트롤러를 요청할 때 regions 에서 반환한 값이 자동으로 모델( model )에 담기게 된다.
    물론 이렇게 사용하지 않고, 각각의 컨트롤러 메서드에서 모델에 직접 데이터를 담아서 처리해도 된다.
     * */
    /**멀티 체크박스 값*/
    @ModelAttribute("regions")
    public Map<String, String> regions(){
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL","서울");
        regions.put("BUSAN","부산");
        regions.put("JEJU","제주");
        return regions;
        //현재 컨트롤러에 모든 메소드가 호출되면 자동으로 🔽생성된다.
        //model.addAttribute("regions",regions);
    }

    /** 라디오 버튼 값
     * Enum으로 생성한 데이터를 보낼거당.
     * */
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        //ItemType[] values = ItemType.values();
        return ItemType.values();//ItemType.values()를 사용하면 해당 ENUM의 모든 정보를 배열로 반환한다.
    }

    /**셀렉트 박스 값*/
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(){
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST","빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NOMAL","일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW","느린 배송"));
        return deliveryCodes;
    }

    @GetMapping
    public String items(Model model) {

        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        //빈 item 객체를 넘겨준다.(빈 객체를 생성했다고 거의 비용이 들지 않는다.)
        //왜 만들까? th:filed 작성중에 필드명 오류를 잡아주기 때문이다!!(오!!) + 추가적으로 검증 수업때 자세히 알려주신다고 하셨다.
        model.addAttribute("item", new Item());

        return "form/addForm";
    }

    /** 순수 html만 사용한 단일 체크박스 */
    //@PostMapping("/add")
    public String addItem_basicHtml_checkBox(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {

        //한글이라 인코딩되서 넘어오나봄! -> itemName=%E3%85%8E%E3%85%87&price=1111&quantity=1111&open=true&_open=on
        //_open <-- 단일 체크박스 단점 보안
        log.info("item.open={}", item.getOpen()); //open=true&_open=on 둘다 넘어오면 true, _open만 넘어오면 스프링이 현재 체크박스에 값을 false로 인식한다.
        //log.info("item.name={}", item.getItemName()); // item.name=ㅎㅇ(디코딩)

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());//
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    /** 타임리프를 사용한 단일 체크박스 + 멀티 체크박스 + 라디오버튼(enum) */
    @PostMapping("/add")
    public String addItem_thymeleaf_checkBox(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        //item.open=false(체크 안하면)
        //item.open=true (체크 시)
        log.info("item.open={}", item.getOpen());//단일

        log.info("item.regions={}", item.getRegions());//멀티(vo에서 List<String> regions에 담긴다)
        // item.regions=[SEOUL, BUSAN]

        log.info("item.itemType={}",item.getItemType().getDescription());//item.itemType=음식


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());//
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        //update함수 안에 들어가면 수정한 값 그대로 반영해주는 코드가 작성되어 있다.
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }



}

