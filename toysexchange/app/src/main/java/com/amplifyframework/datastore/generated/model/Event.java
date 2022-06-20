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

/** This is an auto generated class representing the Event type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Events")
public final class Event implements Model {
  public static final QueryField ID = field("Event", "id");
  public static final QueryField TITLE = field("Event", "title");
  public static final QueryField EVENTDESCRIPTION = field("Event", "eventdescription");
  public static final QueryField LATITUDE = field("Event", "latitude");
  public static final QueryField LONGITUDE = field("Event", "longitude");
  public static final QueryField USERS_EVENTSADDED_ID = field("Event", "usersEventsaddedId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String", isRequired = true) String eventdescription;
  private final @ModelField(targetType="Comment") @HasMany(associatedWith = "eventCommentsId", type = Comment.class) List<Comment> comments = null;
  private final @ModelField(targetType="UserAttendEvent") @HasMany(associatedWith = "event", type = UserAttendEvent.class) List<UserAttendEvent> usersattend = null;
  private final @ModelField(targetType="Float") Double latitude;
  private final @ModelField(targetType="Float") Double longitude;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  private final @ModelField(targetType="ID") String usersEventsaddedId;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getEventdescription() {
      return eventdescription;
  }
  
  public List<Comment> getComments() {
      return comments;
  }
  
  public List<UserAttendEvent> getUsersattend() {
      return usersattend;
  }
  
  public Double getLatitude() {
      return latitude;
  }
  
  public Double getLongitude() {
      return longitude;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  public String getUsersEventsaddedId() {
      return usersEventsaddedId;
  }
  
  private Event(String id, String title, String eventdescription, Double latitude, Double longitude, String usersEventsaddedId) {
    this.id = id;
    this.title = title;
    this.eventdescription = eventdescription;
    this.latitude = latitude;
    this.longitude = longitude;
    this.usersEventsaddedId = usersEventsaddedId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Event event = (Event) obj;
      return ObjectsCompat.equals(getId(), event.getId()) &&
              ObjectsCompat.equals(getTitle(), event.getTitle()) &&
              ObjectsCompat.equals(getEventdescription(), event.getEventdescription()) &&
              ObjectsCompat.equals(getLatitude(), event.getLatitude()) &&
              ObjectsCompat.equals(getLongitude(), event.getLongitude()) &&
              ObjectsCompat.equals(getCreatedAt(), event.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), event.getUpdatedAt()) &&
              ObjectsCompat.equals(getUsersEventsaddedId(), event.getUsersEventsaddedId());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getEventdescription())
      .append(getLatitude())
      .append(getLongitude())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .append(getUsersEventsaddedId())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Event {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("eventdescription=" + String.valueOf(getEventdescription()) + ", ")
      .append("latitude=" + String.valueOf(getLatitude()) + ", ")
      .append("longitude=" + String.valueOf(getLongitude()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("usersEventsaddedId=" + String.valueOf(getUsersEventsaddedId()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
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
  public static Event justId(String id) {
    return new Event(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      eventdescription,
      latitude,
      longitude,
      usersEventsaddedId);
  }
  public interface TitleStep {
    EventdescriptionStep title(String title);
  }
  

  public interface EventdescriptionStep {
    BuildStep eventdescription(String eventdescription);
  }
  

  public interface BuildStep {
    Event build();
    BuildStep id(String id);
    BuildStep latitude(Double latitude);
    BuildStep longitude(Double longitude);
    BuildStep usersEventsaddedId(String usersEventsaddedId);
  }
  

  public static class Builder implements TitleStep, EventdescriptionStep, BuildStep {
    private String id;
    private String title;
    private String eventdescription;
    private Double latitude;
    private Double longitude;
    private String usersEventsaddedId;
    @Override
     public Event build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Event(
          id,
          title,
          eventdescription,
          latitude,
          longitude,
          usersEventsaddedId);
    }
    
    @Override
     public EventdescriptionStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep eventdescription(String eventdescription) {
        Objects.requireNonNull(eventdescription);
        this.eventdescription = eventdescription;
        return this;
    }
    
    @Override
     public BuildStep latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }
    
    @Override
     public BuildStep longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }
    
    @Override
     public BuildStep usersEventsaddedId(String usersEventsaddedId) {
        this.usersEventsaddedId = usersEventsaddedId;
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
    private CopyOfBuilder(String id, String title, String eventdescription, Double latitude, Double longitude, String usersEventsaddedId) {
      super.id(id);
      super.title(title)
        .eventdescription(eventdescription)
        .latitude(latitude)
        .longitude(longitude)
        .usersEventsaddedId(usersEventsaddedId);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder eventdescription(String eventdescription) {
      return (CopyOfBuilder) super.eventdescription(eventdescription);
    }
    
    @Override
     public CopyOfBuilder latitude(Double latitude) {
      return (CopyOfBuilder) super.latitude(latitude);
    }
    
    @Override
     public CopyOfBuilder longitude(Double longitude) {
      return (CopyOfBuilder) super.longitude(longitude);
    }
    
    @Override
     public CopyOfBuilder usersEventsaddedId(String usersEventsaddedId) {
      return (CopyOfBuilder) super.usersEventsaddedId(usersEventsaddedId);
    }
  }
  
}
