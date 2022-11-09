package jpabook.jpashop.service;

import jpabook.jpashop.controller.BookForm;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Item updateItemForm(Long itemId, String name, int price, int stockQuantity, String author, String isbn) {
        Item findItem = itemRepository.findOne(itemId);

//        Book findItem = (Book) itemRepository.findOne(itemId);
//
//        findItem.setId(param.getId());
//        findItem.setName(param.getName());
//        findItem.setPrice(param.getPrice());
//        findItem.setStockQuantity(param.getStockQuantity());
//        findItem.setAuthor(param.getAuthor());
//        findItem.setIsbn(param.getIsbn());

        // 이렇게만 해줘도 Transcational에 의해 flush가 날려지고 업데이트 된다.
        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
