package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Entity(name = "products")
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "username"}))
public class Product implements Serializable {
    /**
     * Default value included to remove warning. Remove or modify at will. *
     */
    private static final long serialVersionUID = 1L;

    public Product() {
    }

    public Product(String name, String description, Double price, Date dateAdded, byte state, Date dateSold,
                   String location) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.dateAdded = dateAdded;
        this.state = state;
        this.dateSold = dateSold;
        this.location = location;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduct;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Double price;

    @NotNull
    private Date dateAdded;

    @NotNull
    private byte state;

    @NotNull
    private Date dateSold;

    @NotNull
    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_owner")
    private User owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_buyer")
    private User buyer;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy="favouritedProducts")
    private Collection<User> usersFavourited = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_tag")
    private Collection<Tag> tags = new ArrayList<>();



    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Collection<Image> productImages;

    @JsonView(Views.Public.class)
    public Long getIdProduct() { return idProduct; }

    public void setIdProduct(Long id) {
        this.idProduct = id;
    }

    @JsonView(Views.Public.class)
    public String getName() {
        return name;
    }

    @JsonView(Views.Public.class)
    public String getDescription() {
        return description;
    }

    @JsonView(Views.Public.class)
    public Double getPrice() {
        return price;
    }

    @JsonView(Views.Public.class)
    public String getLocation() {
        return location;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }

    @JsonView(Views.Public.class)
    public Long getOwnerId() {
        return owner.getId();
    }

    @JsonView(Views.Public.class)
    public Byte getState() {
        return state;
    }

    @JsonView(Views.Public.class)
    public Date getDateSold() {
        return dateSold;
    }

//    @JsonIgnore
//    public User getBuyer() {
//        return buyer;
//    }
//
//    @JsonView(Views.Public.class)
//    public Long getBuyerId() {
//        return buyer.getId();
//    }

    public void setOwner(User user) {
        this.owner = user;
    }
    public void setLocation(String location) { this.location = location; }
    public void setName(String name) { this.name = name; }
    public void setPrice(Double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setState(Byte state) {
        this.state = state;
    }
    public void setDateSold(Date dateSold) {
        this.dateSold = dateSold;
    }

//    public void setBuyer(User user) {
//        this.buyer = user;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            return this.idProduct == ((Product)obj).idProduct;
        }
        else return false;
    }

    public void addTag(Tag t) {
        tags.add(t);
    }

    public void addImage(Image image) {
        productImages.add(image);
    }

}
