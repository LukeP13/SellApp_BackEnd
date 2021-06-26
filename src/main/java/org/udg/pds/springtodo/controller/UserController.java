package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.controller.exceptions.ControllerException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.service.ProductService;
import org.udg.pds.springtodo.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

// This class is used to process all the authentication related URLs
@RequestMapping(path="/users")
@RestController
public class UserController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    @PostMapping(path="/login")
    @JsonView(Views.Private.class)
    public User login(HttpSession session, @Valid @RequestBody LoginUser user) {

    checkNotLoggedIn(session);

        User u = userService.matchPassword(user.username, user.password);
        session.setAttribute("simpleapp_auth_id", u.getId());
        return u;
    }

    @PostMapping(path="/logout")
    @JsonView(Views.Private.class)
    public String logout(HttpSession session) {

        getLoggedUser(session);

        session.removeAttribute("simpleapp_auth_id");
        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/{id}")
    @JsonView(Views.Public.class)
    public User getPublicUser(HttpSession session, @PathVariable("id") Long userId) {

        getLoggedUser(session);

        return userService.getUser(userId);
    }


    @GetMapping(path="/me/products")
    @JsonView(Views.Complete.class)
    public Collection<Product> getOnSaleProducts(HttpSession session) {
      Long loggedUserId = getLoggedUser(session);
      User u = userService.getUser(loggedUserId);
      return u.getOnSaleProducts();
    }


    @DeleteMapping(path="/{id}")
    public String deleteUser(HttpSession session, @PathVariable("id") Long userId) {
        Long loggedUserId = getLoggedUser(session);

        if (!loggedUserId.equals(userId))
          throw new ControllerException("You cannot delete other users!");

        userService.crud().deleteById(userId);
        session.removeAttribute("simpleapp_auth_id");

        return BaseController.OK_MESSAGE;
    }

    @DeleteMapping(path="/me")
    public String deleteSelfUser(HttpSession session) {
        Long loggedUserId = getLoggedUser(session);
        return deleteUser(session, loggedUserId);
    }

    @PostMapping(path = "/me/token/{token}")
    public String refreshToken(HttpSession session, @PathVariable("token") String token) {
        Long id = getLoggedUser(session);
        userService.refreshToken(id, token);

        return BaseController.OK_MESSAGE;
    }

    @PostMapping(path="/register", consumes = "application/json")
    public String register(HttpSession session, @Valid  @RequestBody RegisterUser ru) {

        checkNotLoggedIn(session);
        userService.register(ru.username, ru.name, ru.email, ru.password, ru.location, ru.tel, ru.birthday, false);
        return BaseController.OK_MESSAGE;

    }

    @GetMapping(path="/me")
    @JsonView(Views.Complete.class)
    public User getUserProfile(HttpSession session) {

        Long loggedUserId = getLoggedUser(session);

        return userService.getUserProfile(loggedUserId);
    }

    @PatchMapping(path="/me", consumes = "application/json")
    public String patchUserProfile(HttpSession session, @Valid  @RequestBody PatchUser u) {

          Long loggedUserId = getLoggedUser(session);
          userService.patchUserProfile(loggedUserId, u);
          return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/check")
    public String checkLoggedIn(HttpSession session) {

        getLoggedUser(session);

        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/{id}/products")
    public Collection<Product> getUserProducts(HttpSession session, @PathVariable("id") Long userId) {
        return userService.getUserProducts(userId);
    }

    @GetMapping(path="/me/favorited")
    public Collection<Product> getUserFavoriteProducts(HttpSession session) {
        Long loggedUserId = getLoggedUser(session);
        return userService.getUserFavoriteProducts(loggedUserId);
    }

    @PostMapping(path="/me/favorited/{pid}")
    public String addProductToWishlist(HttpSession session, @PathVariable("pid") Long productId){
        Product p = productService.getProduct(productId);
        Long loggedUserId = getLoggedUser(session);
        userService.addProductToWishList(loggedUserId,p);
        return BaseController.OK_MESSAGE;
    }

    @DeleteMapping(path="/me/favorited/{pid}")
    public String deleteProductFromWishList(HttpSession session, @PathVariable("pid") Long productId) {
        Product p = productService.getProduct(productId);
        Long loggedUserId = getLoggedUser(session);
        userService.removeProductFromWishList(loggedUserId,p);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/me/favorited/{pid}")
    public Product checkIfProductExistsOnWishList(HttpSession session, @PathVariable("pid") Long productId){
        Product p = productService.getProduct(productId);
        Long loggedUserId = getLoggedUser(session);
        return userService.checkIfExistsOnWishlist(loggedUserId,p);
    }

    @GetMapping(path="/{id}/rates")
    public Collection<Rate> getRates(HttpSession session, @PathVariable("id") Long id){
        return userService.getUserRates(id);
    }

    @GetMapping(path="/me/rates")
    public Collection<Rate> getSelfRates(HttpSession session){
        Long loggedUserId = getLoggedUser(session);
        return this.getRates(session, loggedUserId);
    }

    @PostMapping(path="/{id}/rates", consumes = "application/json")
    public String addRate(HttpSession session, @PathVariable("id") Long idRated, @Valid  @RequestBody PostRate r){
        Long idRating = getLoggedUser(session); //If there's no idRating, we set the logged user
        userService.addRate(r.description, r.date, r.rate, idRated, idRating);
        return BaseController.OK_MESSAGE;
    }


    static class PostRate {
        @NotNull
        public String description;
        @NotNull
        public Date date;
        @NotNull
        public Float rate;
    }

  static class LoginUser {
    @NotNull
    public String username;
    @NotNull
    public String password;
  }

  static class RegisterUser {
    @NotNull
    public String username;
    @NotNull
    public String name;
    @NotNull
    public String email;
    @NotNull
    public String password;
    @NotNull
    public String location;
    @NotNull
    public Long tel;
    @NotNull
    public Date birthday;
  }

  public static class PatchUser {
      public String profileImageName;
      public String username;
      public String name;
      public String email;
      public String password;
      public String location;
      public Long tel;
      public Date birthday;
  }

  static class ID {
    public Long id;

    public ID(Long id) {
      this.id = id;
    }
  }

}
