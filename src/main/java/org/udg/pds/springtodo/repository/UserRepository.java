package org.udg.pds.springtodo.repository;


import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Image;
import org.udg.pds.springtodo.entity.User;


import java.util.Date;

import java.util.List;

@Component
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM users u WHERE u.username=:username")
    List<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM users u WHERE u.email=:email")
    List<User> findByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE users SET profileImage=:profileImage, username=:username, name=:name, email=:email, password=:pwd, location=:loc, tel=:tel, birthday=:bday WHERE id=:id")
    void update(@Param("id") Long id, @Param("username") String username, @Param("name") String name,
                @Param("email") String email, @Param("pwd") String password,@Param("loc") String location,
                @Param("tel") Long tel, @Param("bday") Date birthday, @Param("profileImage") Image image);
}
