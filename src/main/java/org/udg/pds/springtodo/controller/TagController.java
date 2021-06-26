package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.Product;
import org.udg.pds.springtodo.entity.Tag;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.service.TagService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RequestMapping("/tags")
@RestController
public class TagController extends BaseController {

  @Autowired
  TagService tagService;

  @GetMapping("{id}")
  public Tag getTag(HttpSession session,
                    @PathVariable("id") Long id) {

    getLoggedUser(session);
    return tagService.getTag(id);
  }

  @GetMapping
  public Collection<Tag> listAllTags(HttpSession session) {

    Long userId = getLoggedUser(session);

    return tagService.getTags();
  }

  @PostMapping(consumes = "application/json")
  public String addTag(@Valid @RequestBody R_Tag tag, HttpSession session) {

    Long userId = getLoggedUser(session);

    if (tag.description == null) {
      tag.description = "";
    }

    tagService.addTag(tag.name);
    return BaseController.OK_MESSAGE;
  }

  @GetMapping("/{nametag}/products")
  @JsonView(Views.Public.class)
  public Collection<Product> searchTags (HttpSession session,
                                         @PathVariable("nametag") String nametag){
      Tag t = tagService.getTagByName(nametag);
      return t.getProducts();
    }

  @DeleteMapping(path="/{id}")
  public String deleteTag(HttpSession session,
                            @PathVariable("id") Long tagId) {

    Long userId = getLoggedUser(session);

    tagService.crud().deleteById(tagId);
    return BaseController.OK_MESSAGE;
  }


  static class R_Tag {
    @NotNull
    public String name;

    public String description;
  }

}
