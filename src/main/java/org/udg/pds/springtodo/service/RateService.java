package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.udg.pds.springtodo.entity.Rate;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.RateRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class RateService {

    @Autowired
    RateRepository rateRepository;

    public void createRate(String description, Date date, Float rate, User ratedUser, User ratingUser){
        Rate newR = new Rate(description, date, rate);
        newR.setRatedUser(ratedUser);
        newR.setRatingUser(ratingUser);

        ratedUser.addRate(newR);
        rateRepository.save(newR);
    }

    public Rate getRate(Long id) {
        Optional<Rate> rate = rateRepository.findById(id);
        return rate.get();
    }
}
