package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.udg.pds.springtodo.serializer.JsonTagSerializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@JsonSerialize(using = JsonTagSerializer.class)
@Entity(name = "tags")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Tag implements Serializable {
  /**
   * Default value included to remove warning. Remove or modify at will.
   **/
  private static final long serialVersionUID = 1L;

  public Tag() {
  }

  public Tag(String name) {
    this.name = name;
  }

  // This tells JAXB that this field can be used as ID
  // Since XmlID can only be used on Strings, we need to use LongAdapter to transform Long <-> String
  @Id
  // Don't forget to use the extra argument "strategy = GenerationType.IDENTITY" to get AUTO_INCREMENT
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "fk_prod")
  private Collection<Product> products = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public Collection<Product> getProducts(){
      products.size();
      return products;
  }

  public void addProduct (Product p){
      products.add(p);
  }

}
