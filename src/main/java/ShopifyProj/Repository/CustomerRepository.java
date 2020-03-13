package ShopifyProj.Repository;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "merchant", path = "customer")
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Shop findById(int id);
}
