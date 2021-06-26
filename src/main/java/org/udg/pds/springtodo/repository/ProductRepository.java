package org.udg.pds.springtodo.repository;

    import org.springframework.data.jpa.repository.Modifying;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.CrudRepository;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Component;
    import org.udg.pds.springtodo.entity.Product;

    import javax.validation.constraints.NotNull;
    import java.util.Date;
    import java.util.List;

@Component
public interface ProductRepository extends CrudRepository<Product, Long> {
    @Query("SELECT p FROM products p WHERE p.name=:nomProducte")
    List<Product> findByName(@Param("nom") String nomProducte);
    @Query("SELECT p FROM products p WHERE p.name LIKE %:productsSearch% OR p.location LIKE %:productsSearch% OR p.description LIKE %:productsSearch%")
    List<Product> searchProducts(@Param("productsSearch") String productsSearch);
    @Query("SELECT p FROM products p")
    List<Product> getAllProducts();


    @Modifying
    @Query("UPDATE products SET name=:name, description=:description, price=:price, location=:location, state=:state, dateSold=:dateSold WHERE idProduct=:idProduct")
    void update(@Param("idProduct") Long idProduct, @Param("name") String name, @Param("description") String description,
                @Param("price") Double price,  @Param("location") String location, @Param("state") Byte state, @Param("dateSold") Date dateSold);
}
