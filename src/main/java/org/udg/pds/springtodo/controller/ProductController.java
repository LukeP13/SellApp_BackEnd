package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.controller.exceptions.ControllerException;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.Product;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.repository.UserRepository;
import org.udg.pds.springtodo.serializer.JsonDateDeserializer;
import org.udg.pds.springtodo.service.ProductService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@RequestMapping(path="/products")
@RestController
public class ProductController extends BaseController {

    @Autowired
    ProductService productService;

    @GetMapping(path="/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        return productService.getProduct(id);
    }

    @GetMapping("/search/{products}")
    @JsonView(Views.Private.class)
    public Collection<Product> searchProduct(HttpSession session,
                                             @PathVariable("products") String productsSearch){
        Long userId = getLoggedUser(session);
        return productService.searchProducts(productsSearch);
    }

    @GetMapping
    @JsonView(Views.Public.class)
    public Collection<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping(consumes = "application/json")
    public IdObject addProduct(HttpSession session, @Valid @RequestBody R_Product product) {

        Long userId = getLoggedUser(session);

        if (product.name == null) {
            throw new ControllerException("No name supplied");
        }
        if (product.description == null) {
            throw new ControllerException("No description supplied");
        }
        if (product.dateAdded == null) {
            throw new ControllerException("No creation date supplied");
        }
        if (product.location == null) {
            throw new ControllerException("No location supplied");
        }
        if (product.price == null) {
            throw new ControllerException("No price supplied");
        }
        if (product.tags == null){
            product.tags = new ArrayList<String>();
        }

        return productService.addProduct(userId, product.name, product.description, product.price, product.dateAdded, product.state, product.dateSold, product.location, product.tags);
    }

    @DeleteMapping(path="/{id}")
    public String deleteProduct(HttpSession session,
                             @PathVariable("id") Long taskId) {
        getLoggedUser(session);
        productService.crud().deleteById(taskId);
        return BaseController.OK_MESSAGE;
    }

    @PatchMapping(path="/{id}", consumes = "application/json")
    public String patchProductData( @PathVariable("id") Long id, @Valid  @RequestBody PatchProd p) {
        productService.patchProduct(id, p);
        return BaseController.OK_MESSAGE;
    }

  static class R_Product {

    @NotNull
    public String name;

    @NotNull
    public String description;

    @NotNull
    public Double price;

    @NotNull
    @JsonDeserialize(using=JsonDateDeserializer.class)
    public Date dateAdded;

    @NotNull
    public byte state;

    @NotNull
    @JsonDeserialize(using=JsonDateDeserializer.class)
    public Date dateSold;

    @NotNull
    public String location;

    public ArrayList<String> tags;
  }

    public static class PatchProd {
        public Long idProduct;
        public String name;
        public String description;
        public Double price;
        public String location;
        public Date dateAdded;
        public byte state;
        public Date dateSold;
    }

}
