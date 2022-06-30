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

/** This is an auto generated class representing the Notification type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Notifications")
public final class Notification implements Model {
  public static final QueryField ID = field("Notification", "id");
  public static final QueryField TOKENID = field("Notification", "tokenid");
  public static final QueryField ACCOUNTID = field("Notification", "accountid");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String tokenid;
  private final @ModelField(targetType="String", isRequired = true) String accountid;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getTokenid() {
      return tokenid;
  }
  
  public String getAccountid() {
      return accountid;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Notification(String id, String tokenid, String accountid) {
    this.id = id;
    this.tokenid = tokenid;
    this.accountid = accountid;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Notification notification = (Notification) obj;
      return ObjectsCompat.equals(getId(), notification.getId()) &&
              ObjectsCompat.equals(getTokenid(), notification.getTokenid()) &&
              ObjectsCompat.equals(getAccountid(), notification.getAccountid()) &&
              ObjectsCompat.equals(getCreatedAt(), notification.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), notification.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTokenid())
      .append(getAccountid())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Notification {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("tokenid=" + String.valueOf(getTokenid()) + ", ")
      .append("accountid=" + String.valueOf(getAccountid()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static TokenidStep builder() {
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
  public static Notification justId(String id) {
    return new Notification(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      tokenid,
      accountid);
  }
  public interface TokenidStep {
    AccountidStep tokenid(String tokenid);
  }
  

  public interface AccountidStep {
    BuildStep accountid(String accountid);
  }
  

  public interface BuildStep {
    Notification build();
    BuildStep id(String id);
  }
  

  public static class Builder implements TokenidStep, AccountidStep, BuildStep {
    private String id;
    private String tokenid;
    private String accountid;
    @Override
     public Notification build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Notification(
          id,
          tokenid,
          accountid);
    }
    
    @Override
     public AccountidStep tokenid(String tokenid) {
        Objects.requireNonNull(tokenid);
        this.tokenid = tokenid;
        return this;
    }
    
    @Override
     public BuildStep accountid(String accountid) {
        Objects.requireNonNull(accountid);
        this.accountid = accountid;
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
    private CopyOfBuilder(String id, String tokenid, String accountid) {
      super.id(id);
      super.tokenid(tokenid)
        .accountid(accountid);
    }
    
    @Override
     public CopyOfBuilder tokenid(String tokenid) {
      return (CopyOfBuilder) super.tokenid(tokenid);
    }
    
    @Override
     public CopyOfBuilder accountid(String accountid) {
      return (CopyOfBuilder) super.accountid(accountid);
    }
  }
  
}
