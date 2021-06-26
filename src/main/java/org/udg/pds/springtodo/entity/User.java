package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.udg.pds.springtodo.controller.ImageController;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity(name = "users")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "username"}))
public class User implements Serializable {
    /**
     * Default value included to remove warning. Remove or modify at will. *
     */
    private static final long serialVersionUID = 1L;

    public User() {
    }

    public User(String username, String name, String email, String password,
                String location, Long tel, Date birthday, Boolean isAdmin) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.tel = tel;
        this.birthday = birthday;
        this.isAdmin = isAdmin;
    }

    public User(User u) {
        this.username = u.username;
        this.name = u.name;
        this.email = u.email;
        this.password = u.password;
        this.location = u.location;
        this.tel = u.tel;
        this.birthday = u.birthday;
        this.isAdmin = u.isAdmin;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private boolean isAdmin;

    @NotNull
    private String location;

    @NotNull
    private Long tel;

    @NotNull
    private Date birthday;

    private String token;

    @OneToOne
    @JoinColumn(name = "profile_image")
    private Image profileImage;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Rate> rates;

    //LLISTA DE PRODUCTES QUE HA POSAT A LA VENDA L'USUARI
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Collection<Product> onSaleProducts;

    //LLISTA DE PRODUCTES QUE HA COMPRAT L'USUARI
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyer")
    private Collection<Product> boughtProducts;

    //LLISTA DE PRODUCTES PREFERITS DE L'USUARI
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<Product> favouritedProducts = new ArrayList<>();

    @JsonView(Views.Public.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Public.class)
    public String getName() {
        return name;
    }

    @JsonView(Views.Public.class)
    public String getEmail() {
        return email;
    }

    @JsonView(Views.Private.class)
    public String getUsername() {
        return username;
    }

    @JsonView(Views.Public.class)
    public Long getTel() {
        return tel;
    }

    @JsonView(Views.Public.class)
    public String getLocation() {
        return location;
    }

    @JsonView(Views.Public.class)
    public Date getBirthday() {
        return birthday;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public String getToken() {
        return token;
    }

    @JsonView(Views.Public.class)
    public String getProfileImageName() {
        if (profileImage == null) return null;
        else return profileImage.getImageName();
    }

    @JsonIgnore
    public Image getProfileImage() {
        return profileImage;
    }

    @JsonIgnore
    public Collection<Product> getOnSaleProducts() {
        // Since tasks is collection controlled by JPA, it has LAZY loading by default. That means
        // that you have to query the object (calling size(), for example) to get the list initialized
        // More: http://www.javabeat.net/jpa-lazy-eager-loading/
        onSaleProducts.size();
        return onSaleProducts;
    }

    @JsonIgnore
    public Collection<Product> getBoughtProducts() {
        // Since tasks is collection controlled by JPA, it has LAZY loading by default. That means
        // that you have to query the object (calling size(), for example) to get the list initialized
        // More: http://www.javabeat.net/jpa-lazy-eager-loading/
        boughtProducts.size();
        return boughtProducts;
    }

    @JsonView(Views.Complete.class)
    public Collection<Product> getFavouritedProducts() {
        // Since tasks is collection controlled by JPA, it has LAZY loading by default. That means
        // that you have to query the object (calling size(), for example) to get the list initialized
        // More: http://www.javabeat.net/jpa-lazy-eager-loading/
        favouritedProducts.size();
        return favouritedProducts;
    }

    @JsonIgnore
    public Collection<Rate> getRates() {
        rates.size();
        return rates;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setProfileImage(Image i) {
        this.profileImage = i;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTel(Long tel) {
        this.tel = tel;
    }

    public String getLoc() {
        return location;
    }

    public void addOnSaleProduct(Product product) {
        onSaleProducts.add(product);
    }

    public void addBoughtProduct(Product product) {
        boughtProducts.add(product);
    }

    public void addFavouritedProduct(Product product) {
        if (!favouritedProducts.contains(product)) favouritedProducts.add(product);
    }

    public void removeFavouritedProduct(Product product) {
        if (favouritedProducts.contains(product)) favouritedProducts.remove(product);
    }

    public Product checkIfExists(Product p) {
        if (!favouritedProducts.contains(p)) p = null;
        return p;
    }

    public void addRate(Rate rate) {
        if (!rates.contains(rate)) rates.add(rate);
    }
}
