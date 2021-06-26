package org.udg.pds.springtodo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.controller.ProductController;
import org.udg.pds.springtodo.controller.UserController;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.Product;
import org.udg.pds.springtodo.entity.Tag;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.ProductRepository;
import org.udg.pds.springtodo.repository.TagRepository;
import org.udg.pds.springtodo.repository.UserRepository;

import java.lang.reflect.Array;
import java.util.*;

@Service
@Transactional
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserService userService;

    @Autowired
    TagService tagService;

    @Autowired
    TagRepository tagRepository;

    public ProductRepository crud() {
        return productRepository;
    }

    public Collection<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public Product getProduct(Long id) {
        Optional<Product> p = productRepository.findById(id);
        if (!p.isPresent()) throw new ServiceException("Product does not exists");
        return p.get();
    }
    public Collection<Product> searchProducts(String productsSearch){
        List<Product> productsFound= productRepository.searchProducts(productsSearch);
        try {
            Tag t = tagService.getTagByName(productsSearch);
            Collection<Product> productsFound2 = t.getProducts();
            Iterator<Product> it = productsFound2.iterator();
            while (it.hasNext()){
                Product p = it.next();
                if (!productsFound.contains(p)) productsFound.add(p);
            }
        }
        catch(Exception ex){

        }

        return productsFound;
    }

    @Transactional
    public IdObject addProduct(Long userId, String name, String description, Double price, Date dateAdded, byte state, Date dateSold,
                               String location, ArrayList<String> tags) {
        try {
            User user = userService.getUser(userId);

            Product product = new Product(name, description, price, dateAdded, state, dateSold, location);

            product.setOwner(user);

            Iterator<String> it = tags.iterator();
            while (it.hasNext()) {
                String s = it.next();
                Tag t;
                try{
                    t = tagService.getTagByName(s);
                }
                catch(Exception ex){

                    t = tagService.addTag(s);
                }

                t.addProduct(product);
                product.addTag(t);
            }


            user.addOnSaleProduct(product);

            productRepository.save(product);

            return new IdObject(product.getIdProduct());
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    public void patchProduct(Long id, ProductController.PatchProd p) {

        Product old = this.getProduct(id);
        if(p.name != null) old.setName(p.name);
        if(p.location != null) old.setLocation(p.location);
        if(p.description != null) old.setDescription(p.description);
        if(p.price != null) old.setPrice(p.price);
        old.setState(p.state);
        if(p.dateSold != null) old.setDateSold(p.dateSold);

        productRepository.update(id, old.getName(), old.getDescription(), old.getPrice(), old.getLocation(), old.getState(), old.getDateSold());
    }
}
