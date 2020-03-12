package ShopifyProj.Repository;

import ShopifyProj.Model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "item", path = "item")
public interface ItemRepository extends CrudRepository<Item, Integer> {
    Item findByItemName(String itemName);
}