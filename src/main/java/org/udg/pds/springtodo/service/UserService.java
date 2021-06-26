package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.controller.UserController;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Image;
import org.udg.pds.springtodo.entity.Product;
import org.udg.pds.springtodo.entity.Rate;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.UserRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RateService rateService;

    @Autowired
    ImageService imageService;

    public UserRepository crud() {
    return userRepository;
    }

    public User matchPassword(String username, String password) {

        List<User> uc = userRepository.findByUsername(username);

        if (uc.size() == 0) throw new ServiceException("User does not exists");

        User u = uc.get(0);

        if (u.getPassword().equals(password))
          return u;
        else
          throw new ServiceException("Password does not match");
    }

    public User register(String username, String name, String email, String password,
                       String location, Long tel, Date birthday, Boolean isAdmin) {

        List<User> uEmail = userRepository.findByEmail(email);
        if (uEmail.size() > 0)
           throw new ServiceException("Email already exist");

        List<User> uUsername = userRepository.findByUsername(username);
        if (uUsername.size() > 0)
           throw new ServiceException("Username already exists");

        User nu = new User(username, name, email, password, location, tel, birthday, isAdmin);
        userRepository.save(nu);
        return nu;
    }

    public User getUser(Long id) {
        Optional<User> uo = userRepository.findById(id);
        if (uo.isPresent())
          return uo.get();
        else
          throw new ServiceException(String.format("User with id = % dos not exists", id));
    }

    public User getUserProfile(long id) {
        User u = this.getUser(id);
        return u;
    }

    public void refreshToken(Long idUser, String token) {
        User u = getUser(idUser);
        u.setToken(token);
    }

    public void patchUserProfile(Long id, UserController.PatchUser u) {

        User old = this.getUser(id);
        if(u.profileImageName != null){
            imageService.addImage(null, id, u.profileImageName);
        }
        if(u.name     != null) old.setName(u.name);
        if(u.username != null) old.setUsername(u.username);
        if(u.email    != null) old.setEmail(u.email);
        if(u.birthday != null) old.setBirthday(u.birthday);
        if(u.location != null) old.setLocation(u.location);
        if(u.password != null) old.setPassword(u.password);
        if(u.tel != null) old.setTel(u.tel);

        userRepository.update(id, old.getUsername(), old.getName(), old.getEmail(), old.getPassword(), old.getLocation(), old.getTel(), old.getBirthday(), old.getProfileImage());
    }

    public Collection<Product> getUserProducts(Long idUser) {
        User u = getUser(idUser);
        return u.getOnSaleProducts();
    }

    public Collection<Product> getUserFavoriteProducts(Long idUser) {
        User u = getUser(idUser);
        return u.getFavouritedProducts();
    }

    public void addProductToWishList(Long userId, Product p){
        User u = getUser(userId);
        u.addFavouritedProduct(p);
    }

    public void removeProductFromWishList (Long userId, Product p){
        User u = getUser(userId);
        u.removeFavouritedProduct(p);
    }

    public Product checkIfExistsOnWishlist (Long userId, Product p){
        User u = getUser(userId);
        return u.checkIfExists(p);
    }

    public Collection<Rate> getUserRates(Long id){
        User u = this.getUser(id);
        return u.getRates();
    }

    public void addRate(String description, Date date, Float rate, Long ratedId, Long ratingId){
        User ratedU = this.getUser(ratedId);
        User ratingU = this.getUser(ratingId);
        rateService.createRate(description, date, rate, ratedU, ratingU);
    }

}

