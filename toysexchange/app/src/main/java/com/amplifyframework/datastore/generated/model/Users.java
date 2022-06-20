package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
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

/** This is an auto generated class representing the Users type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Users")
public final class Users implements Model {
  public static final QueryField ID = field("Users", "id");
  public static final QueryField IDCOGNITO = field("Users", "idcognito");
  public static final QueryField USERNAME = field("Users", "username");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String idcognito;
  private final @ModelField(targetType="String", isRequired = true) String username;
  private final @ModelField(targetType="Toy") @HasMany(associatedWith = "usersToysId", type = Toy.class) List<Toy> toys = null;
  private final @ModelField(targetType="Comment") @HasMany(associatedWith = "usersCommentsId", type = Comment.class) List<Comment> comments = null;
  private final @ModelField(targetType="Store") @HasMany(associatedWith = "usersStoresId", type = Store.class) List<Store> stores = null;
  private final @ModelField(targetType="Event") @HasMany(associatedWith = "usersEventsaddedId", type = Event.class) List<Event> eventsadded = null;
  private final @ModelField(targetType="UserAttendEvent") @HasMany(associatedWith = "users", type = UserAttendEvent.class) List<UserAttendEvent> eventsattend = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getIdcognito() {
      return idcognito;
  }
  
  public String getUsername() {
      return username;
  }
  
  public List<Toy> getToys() {
      return toys;
  }
  
  public List<Comment> getComments() {
      return comments;
  }
  
  public List<Store> getStores() {
      return stores;
  }
  
  public List<Event> getEventsadded() {
      return eventsadded;
  }
  
  public List<UserAttendEvent> getEventsattend() {
      return eventsattend;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Users(String id, String idcognito, String username) {
    this.id = id;
    this.idcognito = idcognito;
    this.username = username;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Users users = (Users) obj;
      return ObjectsCompat.equals(getId(), users.getId()) &&
              ObjectsCompat.equals(getIdcognito(), users.getIdcognito()) &&
              ObjectsCompat.equals(getUsername(), users.getUsername()) &&
              ObjectsCompat.equals(getCreatedAt(), users.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), users.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getIdcognito())
      .append(getUsername())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Users {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("idcognito=" + String.valueOf(getIdcognito()) + ", ")
      .append("username=" + String.valueOf(getUsername()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UsernameStep builder() {
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
  public static Users justId(String id) {
    return new Users(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      idcognito,
      username);
  }
  public interface UsernameStep {
    BuildStep username(String username);
  }
  

  public interface BuildStep {
    Users build();
    BuildStep id(String id);
    BuildStep idcognito(String idcognito);
  }
  

  public static class Builder implements UsernameStep, BuildStep {
    private String id;
    private String username;
    private String idcognito;
    @Override
     public Users build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Users(
          id,
          idcognito,
          username);
    }
    
    @Override
     public BuildStep username(String username) {
        Objects.requireNonNull(username);
        this.username = username;
        return this;
    }
    
    @Override
     public BuildStep idcognito(String idcognito) {
        this.idcognito = idcognito;
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
    private CopyOfBuilder(String id, String idcognito, String username) {
      super.id(id);
      super.username(username)
        .idcognito(idcognito);
    }
    
    @Override
     public CopyOfBuilder username(String username) {
      return (CopyOfBuilder) super.username(username);
    }
    
    @Override
     public CopyOfBuilder idcognito(String idcognito) {
      return (CopyOfBuilder) super.idcognito(idcognito);
    }
  }
  
}
