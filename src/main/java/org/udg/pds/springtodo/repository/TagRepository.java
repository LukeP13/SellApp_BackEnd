package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Product;
import org.udg.pds.springtodo.entity.Tag;

import java.util.List;
import java.util.Optional;

@Component
public interface TagRepository extends CrudRepository<Tag, Long> {
    @Query("SELECT t FROM tags t WHERE t.name=:name")
    Optional<Tag> findByName(@Param("name") String name);
    @Query("SELECT p FROM products p JOIN tags t ON p.tags = t.products WHERE t.name LIKE %:productsSearch%")
    List<Product> searchProductsViaTag(@Param("productsSearch") String productsSearch);

}
