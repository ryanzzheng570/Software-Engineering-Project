package ShopifyProj.Repository;

import ShopifyProj.Model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "images", path = "images")
public interface ImageRepository extends CrudRepository<Image, Integer> {

}