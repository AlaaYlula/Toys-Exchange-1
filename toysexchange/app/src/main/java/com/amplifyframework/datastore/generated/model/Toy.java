package com.amplifyframework.datastore.generated.model;

<<<<<<< HEAD
import com.amplifyframework.core.model.annotations.HasMany;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;
=======
import static com.amplifyframework.core.model.query.predicate.QueryField.field;
>>>>>>> 44e0057c760846725fd8036a98967798c8c63bb1

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.HasMany;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
  public static final QueryField CONTACTINFO = field("Toy", "contactinfo");
  public static final QueryField ACCOUNT_TOYS_ID = field("Toy", "accountToysId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String toyname;
  private final @ModelField(targetType="String", isRequired = true) String toydescription;
  private final @ModelField(targetType="String", isRequired = true) String image;
  private final @ModelField(targetType="Float") Double price;
  private final @ModelField(targetType="Condition") Condition condition;
  private final @ModelField(targetType="UserWishList") @HasMany(associatedWith = "toy", type = UserWishList.class) List<UserWishList> wishuser = null;
  private final @ModelField(targetType="String") String contactinfo;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  private final @ModelField(targetType="ID") String accountToysId;
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
  
  public List<UserWishList> getWishuser() {
      return wishuser;
  }
  
  public String getContactinfo() {
      return contactinfo;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  public String getAccountToysId() {
      return accountToysId;
  }
  
  private Toy(String id, String toyname, String toydescription, String image, Double price, Condition condition, String contactinfo, String accountToysId) {
    this.id = id;
    this.toyname = toyname;
    this.toydescription = toydescription;
    this.image = image;
    this.price = price;
    this.condition = condition;
    this.contactinfo = contactinfo;
    this.accountToysId = accountToysId;
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
              ObjectsCompat.equals(getContactinfo(), toy.getContactinfo()) &&
              ObjectsCompat.equals(getCreatedAt(), toy.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), toy.getUpdatedAt()) &&
              ObjectsCompat.equals(getAccountToysId(), toy.getAccountToysId());
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
      .append(getContactinfo())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .append(getAccountToysId())
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
      .append("contactinfo=" + String.valueOf(getContactinfo()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("accountToysId=" + String.valueOf(getAccountToysId()))
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
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
<<<<<<< HEAD
      toyname,
      toydescription,
      image,
      price,
      condition,
      contactinfo,
      accountToysId);
=======
            toyname,
            toydescription,
            image,
            price,
            condition,
            contactinfo,
            accountToysId);
>>>>>>> 44e0057c760846725fd8036a98967798c8c63bb1
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
<<<<<<< HEAD
=======

>>>>>>> 44e0057c760846725fd8036a98967798c8c63bb1
    BuildStep contactinfo(String contactinfo);
    BuildStep accountToysId(String accountToysId);
  }
  

  public static class Builder implements ToynameStep, ToydescriptionStep, ImageStep, BuildStep {
    private String id;
    private String toyname;
    private String toydescription;
    private String image;
    private Double price;
    private Condition condition;
<<<<<<< HEAD
=======

>>>>>>> 44e0057c760846725fd8036a98967798c8c63bb1
    private String contactinfo;
    private String accountToysId;
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
          contactinfo,
          accountToysId);
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
<<<<<<< HEAD
=======

>>>>>>> 44e0057c760846725fd8036a98967798c8c63bb1
     public BuildStep contactinfo(String contactinfo) {
        this.contactinfo = contactinfo;
        return this;
    }
    
    @Override
     public BuildStep accountToysId(String accountToysId) {
        this.accountToysId = accountToysId;
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
    private CopyOfBuilder(String id, String toyname, String toydescription, String image, Double price, Condition condition, String contactinfo, String accountToysId) {
      super.id(id);
      super.toyname(toyname)
        .toydescription(toydescription)
        .image(image)
        .price(price)
        .condition(condition)
<<<<<<< HEAD
        .contactinfo(contactinfo)
=======
        .contactinfo(Toy.this.contactinfo)
>>>>>>> 44e0057c760846725fd8036a98967798c8c63bb1
        .accountToysId(accountToysId);
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
     public CopyOfBuilder contactinfo(String contactinfo) {
      return (CopyOfBuilder) super.contactinfo(contactinfo);
    }
    
    @Override
     public CopyOfBuilder accountToysId(String accountToysId) {
      return (CopyOfBuilder) super.accountToysId(accountToysId);
    }
  }
  
}
