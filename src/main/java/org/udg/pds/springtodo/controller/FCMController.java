package org.udg.pds.springtodo.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.udg.pds.springtodo.entity.Rate;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.service.FCMService;
import org.udg.pds.springtodo.service.RateService;
import org.udg.pds.springtodo.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequestMapping(path="/send")
@RestController
public class FCMController extends BaseController {

    Logger logger = LoggerFactory.getLogger(FCMController.class);

    @Autowired
    FCMService fcmService;

    @Autowired
    UserService userService;

    @Autowired
    RateService rateService;

    @PostMapping(path = "/buyRequest/{userIdDest}")
    public String sendBuyRequestMessage(HttpSession session, @PathVariable("userIdDest") Long userIdDest) {
        User userDest = userService.getUser(userIdDest);
        Long idOrigin = getLoggedUser(session);
        User userOrigin = userService.getUser(idOrigin);

        if (userDest.getToken() != null) {
            Message message = Message.builder()
                .putData("title", "Buy request")
                .putData("body", "The user " + userOrigin.getUsername() + " has sent you a buy request.")
                .setToken(userDest.getToken())
                .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);

                logger.info("Successfully sent message: " + response);
            } catch (Exception ex) {
                logger.error("Error sending a message. " + ex.getMessage());
            }
        }

        return BaseController.OK_MESSAGE;
    }

    @PostMapping(path = "/rate/{userIdDest}/{rateId}")
    public String sendRateMessage(HttpSession session, @PathVariable("userIdDest") Long userIdDest, @PathVariable("rateId") Long rateId) {
        User userDest = userService.getUser(userIdDest);
        Long idOrigin = getLoggedUser(session);
        User userOrigin = userService.getUser(idOrigin);
        Rate rate = rateService.getRate(rateId);

        if (userDest.getToken() != null) {
            Message message = Message.builder()
                .putData("title", "Rate added")
                .putData("body", "The user " + userOrigin.getUsername() + " has rated your profile.")
                .setToken(userDest.getToken())
                .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);

                logger.info("Successfully sent message: " + response);
            } catch (Exception ex) {
                logger.error("Error sending a message. " + ex.getMessage());
            }
        }

        return BaseController.OK_MESSAGE;
    }
}
