package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();   //static 실제로는 hashmap이 아니라 db나 ConcurrentHashMap을 사용해야한다.
    private static Long sequence = 0L;  //AtomicLong을 사용해야함
    
    public Item save(Item item){
        item.setId(sequence++);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values()); //실제로는 값만 반환해도 되지만 list로 반환하여 사용하면 값을 더 추가하거나 삭제해도 실제 store에는 영향이 없으므로 이렇게 코딩함.
    }

    public void update(Long itemId, Item updateParam){
        Item findItem = findById(itemId);
        //실제로는 이렇게 Item 객체를 그대로 사용하는게 아니라 update에 필요한 Name, Price, Quantity만 가질 수 있는 class를 하나 더 생성하여 데이터를 담고 사용하는게 더 좋음
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore(){
        store.clear();
    }
}
