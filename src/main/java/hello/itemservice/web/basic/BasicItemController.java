package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName, @RequestParam int price, @RequestParam int quantity, Model model){
        Item item = new Item(itemName, price, quantity);
        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //@ModelAttribute("모델명")으로 사용하면 model 객체에 자동으로 현재 parameter로 사용한 객체를 view로 전달해준다.
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item){
        itemRepository.save(item);

        return "basic/item";
    }

    //@ModelAttribute의 모델명을 전달하지 않아도 객체의 class명의 소문자 형태인 Item->item으로 model에 담겨서 view로 전달해준다.
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){
        itemRepository.save(item);

        return "basic/item";
    }

    //@ModelAttribute 자체도 생략이 가능하므로 최종적으로는 이렇게까지 사용할 수 있다.
    //@PostMapping("/add")
    public String addItemV4(Item item){
        itemRepository.save(item);

        return "basic/item";
    }

    //위 방법들의 문제점은 redirect가 아닌 페이지만 불러오는 형식이라 페이지에서 새로고침을 눌렀을때 계속해서 POST요청이 들어온다. 그래서 redirect로 문제를 해결했다
    //이 같은 문제해결 방법을 PRG라 하며 Post Redirect Get이라 한다.
    //@PostMapping("/add")
    public String addItemV5(Item item){
        itemRepository.save(item);

        return "redirect:/basic/items/"+item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
