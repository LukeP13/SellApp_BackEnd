package org.udg.pds.springtodo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.Image;
import org.udg.pds.springtodo.entity.Product;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.ImageRepository;
import org.udg.pds.springtodo.repository.ProductRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    public ImageRepository crud() {
        return imageRepository;
    }

    public Collection<Image> getAllImages() {
        return imageRepository.getAllImages();
    }

    public Image getImage(Long id) {
        Optional<Image> i = imageRepository.findById(id);
        if (!i.isPresent()) throw new ServiceException("Image does not exists");
        return i.get();
    }
    public Collection<Image> findImagesByProductId(Long productId){
        List<Image> imagesFound= imageRepository.findImagesByProductId(productId);
        return imagesFound;
    }

    @Transactional
    public IdObject addImage(Long productId, Long userId, String name) {
        try {
            Image image = new Image(name);

            Product product = null;
            User user = null;
            if(productId != null){
                product = productService.getProduct(productId);
                image.setProduct(product);
                product.addImage(image);
            }
            if(userId != null){
                user = userService.getUser(userId);
                image.setUser(user);
                user.setProfileImage(image);
            }

            imageRepository.save(image);

            return new IdObject(image.getImageId());
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
}
