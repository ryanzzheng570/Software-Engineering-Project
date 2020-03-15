package ShopifyProj.Repository;

import ShopifyProj.Model.Shop;
import ShopifyProj.Model.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "tag", path = "tag")
public interface TagRepository extends CrudRepository<Tag, Integer> {
    Tag findByTagName(String tagName);
}