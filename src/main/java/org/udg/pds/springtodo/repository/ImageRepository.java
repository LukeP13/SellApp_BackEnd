package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Image;

import java.util.List;

@Component
public interface ImageRepository extends CrudRepository<Image, Long> {
    @Query("SELECT i FROM images i WHERE i.product.idProduct=:idProducte")
    List<Image> findImagesByProductId(@Param("idProduct") Long idProducte);
    @Query("SELECT i FROM images i")
    List<Image> getAllImages();

}

