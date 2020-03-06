package ShopifyProj;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "merchant", path="merchant")
public interface MerchantRepository extends CrudRepository<Merchant, Integer> {
}
