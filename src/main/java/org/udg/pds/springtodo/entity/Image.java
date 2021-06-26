package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.udg.pds.springtodo.controller.ImageController;
import org.udg.pds.springtodo.service.ImageService;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity(name = "images")
public class Image implements Serializable {
    /**
     * Default value included to remove warning. Remove or modify at will. *
     */
    private static final long serialVersionUID = 1L;

    public Image() {
    }

    public Image(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImage;

    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_product")
    private Product product;

    @OneToOne
    @JoinColumn(name="fk_user")
    private User user;

    @JsonView(Views.Public.class)
    public Long getImageId() { return idImage; }

    @JsonView(Views.Public.class)
    public String getImageName() {
        return name;
    }

    @JsonIgnore
    public Product getProduct() { return product; }

    @JsonIgnore
    public Long getProductId() { return product.getIdProduct(); }


    public void setImageName(String name) { this.name = name; }
    public void setProduct(Product product) {
        this.product = product;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
