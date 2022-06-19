package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Toy type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Toys")
public final class Toy implements Model {
  public static final QueryField ID = field("Toy", "id");
  public static final QueryField TOYNAME = field("Toy", "toyname");
  public static final QueryField TOYDESCRIPTION = field("Toy", "toydescription");
  public static final QueryField IMAGE = field("Toy", "image");
  public static final QueryField PRICE = field("Toy", "price");
  public static final QueryField CONDITION = field("Toy", "condition");
  public static final QueryField USERS_TOYS_ID = field("Toy", "usersToysId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String toyname;
  private final @ModelField(targetType="String", isRequired = true) String toydescription;
  private final @ModelField(targetType="String", isRequired = true) String image;
  private final @ModelField(targetType="Float") Double price;
  private final @ModelField(targetType="Condition") Condition condition;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  private final @ModelField(targetType="ID") String usersToysId;
  public String getId() {
      return id;
  }
  
  public String getToyname() {
      return toyname;
  }
  
  public String getToydescription() {
      return toydescription;
  }
  
  public String getImage() {
      return image;
  }
  
  public Double getPrice() {
      return price;
  }
  
  public Condition getCondition() {
      return condition;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  public String getUsersToysId() {
      return usersToysId;
  }
  
  private Toy(String id, String toyname, String toydescription, String image, Double price, Condition condition, String usersToysId) {
    this.id = id;
    this.toyname = toyname;
    this.toydescription = toydescription;
    this.image = image;
    this.price = price;
    this.condition = condition;
    this.usersToysId = usersToysId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Toy toy = (Toy) obj;
      return ObjectsCompat.equals(getId(), toy.getId()) &&
              ObjectsCompat.equals(getToyname(), toy.getToyname()) &&
              ObjectsCompat.equals(getToydescription(), toy.getToydescription()) &&
              ObjectsCompat.equals(getImage(), toy.getImage()) &&
              ObjectsCompat.equals(getPrice(), toy.getPrice()) &&
              ObjectsCompat.equals(getCondition(), toy.getCondition()) &&
              ObjectsCompat.equals(getCreatedAt(), toy.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), toy.getUpdatedAt()) &&
              ObjectsCompat.equals(getUsersToysId(), toy.getUsersToysId());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getToyname())
      .append(getToydescription())
      .append(getImage())
      .append(getPrice())
      .append(getCondition())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .append(getUsersToysId())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Toy {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("toyname=" + String.valueOf(getToyname()) + ", ")
      .append("toydescription=" + String.valueOf(getToydescription()) + ", ")
      .append("image=" + String.valueOf(getImage()) + ", ")
      .append("price=" + String.valueOf(getPrice()) + ", ")
      .append("condition=" + String.valueOf(getCondition()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("usersToysId=" + String.valueOf(getUsersToysId()))
      .append("}")
      .toString();
  }
  
  public static ToynameStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Toy justId(String id) {
    return new Toy(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      toyname,
      toydescription,
      image,
      price,
      condition,
      usersToysId);
  }
  public interface ToynameStep {
    ToydescriptionStep toyname(String toyname);
  }
  

  public interface ToydescriptionStep {
    ImageStep toydescription(String toydescription);
  }
  

  public interface ImageStep {
    BuildStep image(String image);
  }
  

  public interface BuildStep {
    Toy build();
    BuildStep id(String id);
    BuildStep price(Double price);
    BuildStep condition(Condition condition);
    BuildStep usersToysId(String usersToysId);
  }
  

  public static class Builder implements ToynameStep, ToydescriptionStep, ImageStep, BuildStep {
    private String id;
    private String toyname;
    private String toydescription;
    private String image;
    private Double price;
    private Condition condition;
    private String usersToysId;
    @Override
     public Toy build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Toy(
          id,
          toyname,
          toydescription,
          image,
          price,
          condition,
          usersToysId);
    }
    
    @Override
     public ToydescriptionStep toyname(String toyname) {
        Objects.requireNonNull(toyname);
        this.toyname = toyname;
        return this;
    }
    
    @Override
     public ImageStep toydescription(String toydescription) {
        Objects.requireNonNull(toydescription);
        this.toydescription = toydescription;
        return this;
    }
    
    @Override
     public BuildStep image(String image) {
        Objects.requireNonNull(image);
        this.image = image;
        return this;
    }
    
    @Override
     public BuildStep price(Double price) {
        this.price = price;
        return this;
    }
    
    @Override
     public BuildStep condition(Condition condition) {
        this.condition = condition;
        return this;
    }
    
    @Override
     public BuildStep usersToysId(String usersToysId) {
        this.usersToysId = usersToysId;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String toyname, String toydescription, String image, Double price, Condition condition, String usersToysId) {
      super.id(id);
      super.toyname(toyname)
        .toydescription(toydescription)
        .image(image)
        .price(price)
        .condition(condition)
        .usersToysId(usersToysId);
    }
    
    @Override
     public CopyOfBuilder toyname(String toyname) {
      return (CopyOfBuilder) super.toyname(toyname);
    }
    
    @Override
     public CopyOfBuilder toydescription(String toydescription) {
      return (CopyOfBuilder) super.toydescription(toydescription);
    }
    
    @Override
     public CopyOfBuilder image(String image) {
      return (CopyOfBuilder) super.image(image);
    }
    
    @Override
     public CopyOfBuilder price(Double price) {
      return (CopyOfBuilder) super.price(price);
    }
    
    @Override
     public CopyOfBuilder condition(Condition condition) {
      return (CopyOfBuilder) super.condition(condition);
    }
    
    @Override
     public CopyOfBuilder usersToysId(String usersToysId) {
      return (CopyOfBuilder) super.usersToysId(usersToysId);
    }
  }
  
}
