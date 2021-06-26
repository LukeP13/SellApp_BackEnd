package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.controller.exceptions.ControllerException;
import org.udg.pds.springtodo.entity.IdObject;
import org.udg.pds.springtodo.entity.Product;
import org.udg.pds.springtodo.entity.Rate;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.repository.UserRepository;
import org.udg.pds.springtodo.serializer.JsonDateDeserializer;
import org.udg.pds.springtodo.service.ProductService;
import org.udg.pds.springtodo.service.RateService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@RequestMapping(path="/rates")
@RestController
public class RateController extends BaseController {
    @Autowired
    RateService rateService;

//    @GetMapping(path="/{id}")
//    @JsonView(Views.Private.class)
//    public Rate getRate(@PathVariable("id") Long id) {
//        return RateService.getRate(id);
//    }

}
