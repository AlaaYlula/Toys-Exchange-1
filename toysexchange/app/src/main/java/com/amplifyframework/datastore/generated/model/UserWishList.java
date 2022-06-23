package com.amplifyframework.datastore.generated.model;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.Objects;
import java.util.UUID;


/** This is an auto generated class representing the UserWishList type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserWishLists")
@Index(name = "byToy", fields = {"toyID"})
@Index(name = "byAccount", fields = {"accountID"})
public final class UserWishList implements Model {
  public static final QueryField ID = field("UserWishList", "id");
  public static final QueryField TOY = field("UserWishList", "toyID");
  public static final QueryField ACCOUNT = field("UserWishList", "accountID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Toy", isRequired = true) @BelongsTo(targetName = "toyID", type = Toy.class) Toy toy;
  private final @ModelField(targetType="Account", isRequired = true) @BelongsTo(targetName = "accountID", type = Account.class) Account account;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public Toy getToy() {
      return toy;
  }
  
  public Account getAccount() {
      return account;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserWishList(String id, Toy toy, Account account) {
    this.id = id;
    this.toy = toy;
    this.account = account;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserWishList userWishList = (UserWishList) obj;
      return ObjectsCompat.equals(getId(), userWishList.getId()) &&
              ObjectsCompat.equals(getToy(), userWishList.getToy()) &&
              ObjectsCompat.equals(getAccount(), userWishList.getAccount()) &&
              ObjectsCompat.equals(getCreatedAt(), userWishList.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userWishList.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getToy())
      .append(getAccount())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserWishList {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("toy=" + String.valueOf(getToy()) + ", ")
      .append("account=" + String.valueOf(getAccount()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static ToyStep builder() {
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
  public static UserWishList justId(String id) {
    return new UserWishList(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      toy,
      account);
  }
  public interface ToyStep {
    AccountStep toy(Toy toy);
  }
  

  public interface AccountStep {
    BuildStep account(Account account);
  }
  

  public interface BuildStep {
    UserWishList build();
    BuildStep id(String id);
  }
  

  public static class Builder implements ToyStep, AccountStep, BuildStep {
    private String id;
    private Toy toy;
    private Account account;
    @Override
     public UserWishList build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserWishList(
          id,
          toy,
          account);
    }
    
    @Override
     public AccountStep toy(Toy toy) {
        Objects.requireNonNull(toy);
        this.toy = toy;
        return this;
    }
    
    @Override
     public BuildStep account(Account account) {
        Objects.requireNonNull(account);
        this.account = account;
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
    private CopyOfBuilder(String id, Toy toy, Account account) {
      super.id(id);
      super.toy(toy)
        .account(account);
    }
    
    @Override
     public CopyOfBuilder toy(Toy toy) {
      return (CopyOfBuilder) super.toy(toy);
    }
    
    @Override
     public CopyOfBuilder account(Account account) {
      return (CopyOfBuilder) super.account(account);
    }
  }
  
}
