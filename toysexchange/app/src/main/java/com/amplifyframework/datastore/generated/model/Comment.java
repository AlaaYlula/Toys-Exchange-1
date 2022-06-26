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

/** This is an auto generated class representing the Comment type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Comments")
public final class Comment implements Model {
  public static final QueryField ID = field("Comment", "id");
  public static final QueryField TEXT = field("Comment", "text");
  public static final QueryField ACCOUNT_COMMENTS_ID = field("Comment", "accountCommentsId");
  public static final QueryField EVENT_COMMENTS_ID = field("Comment", "eventCommentsId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String text;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  private final @ModelField(targetType="ID") String accountCommentsId;
  private final @ModelField(targetType="ID") String eventCommentsId;
  public String getId() {
      return id;
  }
  
  public String getText() {
      return text;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  public String getAccountCommentsId() {
      return accountCommentsId;
  }
  
  public String getEventCommentsId() {
      return eventCommentsId;
  }
  
  private Comment(String id, String text, String accountCommentsId, String eventCommentsId) {
    this.id = id;
    this.text = text;
    this.accountCommentsId = accountCommentsId;
    this.eventCommentsId = eventCommentsId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Comment comment = (Comment) obj;
      return ObjectsCompat.equals(getId(), comment.getId()) &&
              ObjectsCompat.equals(getText(), comment.getText()) &&
              ObjectsCompat.equals(getCreatedAt(), comment.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), comment.getUpdatedAt()) &&
              ObjectsCompat.equals(getAccountCommentsId(), comment.getAccountCommentsId()) &&
              ObjectsCompat.equals(getEventCommentsId(), comment.getEventCommentsId());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getText())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .append(getAccountCommentsId())
      .append(getEventCommentsId())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Comment {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("text=" + String.valueOf(getText()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("accountCommentsId=" + String.valueOf(getAccountCommentsId()) + ", ")
      .append("eventCommentsId=" + String.valueOf(getEventCommentsId()))
      .append("}")
      .toString();
  }
  
  public static TextStep builder() {
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
  public static Comment justId(String id) {
    return new Comment(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      text,
      accountCommentsId,
      eventCommentsId);
  }
  public interface TextStep {
    BuildStep text(String text);
  }
  

  public interface BuildStep {
    Comment build();
    BuildStep id(String id);
    BuildStep accountCommentsId(String accountCommentsId);
    BuildStep eventCommentsId(String eventCommentsId);
  }
  

  public static class Builder implements TextStep, BuildStep {
    private String id;
    private String text;
    private String accountCommentsId;
    private String eventCommentsId;
    @Override
     public Comment build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Comment(
          id,
          text,
          accountCommentsId,
          eventCommentsId);
    }
    
    @Override
     public BuildStep text(String text) {
        Objects.requireNonNull(text);
        this.text = text;
        return this;
    }
    
    @Override
     public BuildStep accountCommentsId(String accountCommentsId) {
        this.accountCommentsId = accountCommentsId;
        return this;
    }
    
    @Override
     public BuildStep eventCommentsId(String eventCommentsId) {
        this.eventCommentsId = eventCommentsId;
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
    private CopyOfBuilder(String id, String text, String accountCommentsId, String eventCommentsId) {
      super.id(id);
      super.text(text)
        .accountCommentsId(accountCommentsId)
        .eventCommentsId(eventCommentsId);
    }
    
    @Override
     public CopyOfBuilder text(String text) {
      return (CopyOfBuilder) super.text(text);
    }
    
    @Override
     public CopyOfBuilder accountCommentsId(String accountCommentsId) {
      return (CopyOfBuilder) super.accountCommentsId(accountCommentsId);
    }
    
    @Override
     public CopyOfBuilder eventCommentsId(String eventCommentsId) {
      return (CopyOfBuilder) super.eventCommentsId(eventCommentsId);
    }
  }
  
}
