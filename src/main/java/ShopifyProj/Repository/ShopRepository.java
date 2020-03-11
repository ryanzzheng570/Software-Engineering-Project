package ShopifyProj.Repository;

import ShopifyProj.Model.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "shop", path = "shop")
public interface ShopRepository extends CrudRepository<Shop, Integer> {
    Shop findById(int id);
    Shop findByShopName(String shopName);
}