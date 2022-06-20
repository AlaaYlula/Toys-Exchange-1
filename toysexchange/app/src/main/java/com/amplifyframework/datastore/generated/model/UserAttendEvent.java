package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
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

/** This is an auto generated class representing the UserAttendEvent type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserAttendEvents")
@Index(name = "byUsers", fields = {"usersID"})
@Index(name = "byEvent", fields = {"eventID"})
public final class UserAttendEvent implements Model {
  public static final QueryField ID = field("UserAttendEvent", "id");
  public static final QueryField USERS = field("UserAttendEvent", "usersID");
  public static final QueryField EVENT = field("UserAttendEvent", "eventID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Users", isRequired = true) @BelongsTo(targetName = "usersID", type = Users.class) Users users;
  private final @ModelField(targetType="Event", isRequired = true) @BelongsTo(targetName = "eventID", type = Event.class) Event event;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public Users getUsers() {
      return users;
  }
  
  public Event getEvent() {
      return event;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserAttendEvent(String id, Users users, Event event) {
    this.id = id;
    this.users = users;
    this.event = event;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserAttendEvent userAttendEvent = (UserAttendEvent) obj;
      return ObjectsCompat.equals(getId(), userAttendEvent.getId()) &&
              ObjectsCompat.equals(getUsers(), userAttendEvent.getUsers()) &&
              ObjectsCompat.equals(getEvent(), userAttendEvent.getEvent()) &&
              ObjectsCompat.equals(getCreatedAt(), userAttendEvent.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userAttendEvent.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUsers())
      .append(getEvent())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserAttendEvent {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("users=" + String.valueOf(getUsers()) + ", ")
      .append("event=" + String.valueOf(getEvent()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UsersStep builder() {
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
  public static UserAttendEvent justId(String id) {
    return new UserAttendEvent(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      users,
      event);
  }
  public interface UsersStep {
    EventStep users(Users users);
  }
  

  public interface EventStep {
    BuildStep event(Event event);
  }
  

  public interface BuildStep {
    UserAttendEvent build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UsersStep, EventStep, BuildStep {
    private String id;
    private Users users;
    private Event event;
    @Override
     public UserAttendEvent build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserAttendEvent(
          id,
          users,
          event);
    }
    
    @Override
     public EventStep users(Users users) {
        Objects.requireNonNull(users);
        this.users = users;
        return this;
    }
    
    @Override
     public BuildStep event(Event event) {
        Objects.requireNonNull(event);
        this.event = event;
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
    private CopyOfBuilder(String id, Users users, Event event) {
      super.id(id);
      super.users(users)
        .event(event);
    }
    
    @Override
     public CopyOfBuilder users(Users users) {
      return (CopyOfBuilder) super.users(users);
    }
    
    @Override
     public CopyOfBuilder event(Event event) {
      return (CopyOfBuilder) super.event(event);
    }
  }
  
}
