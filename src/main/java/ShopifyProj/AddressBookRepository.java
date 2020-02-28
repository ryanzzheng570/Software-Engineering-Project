package ShopifyProj;

import org.springframework.data.repository.CrudRepository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "addressBook", path = "addressBook")
public interface AddressBookRepository extends CrudRepository<AddressBook, Integer> {
    //AddressBook findById(int id);
}