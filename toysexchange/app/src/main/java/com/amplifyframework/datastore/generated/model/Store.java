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

/** This is an auto generated class representing the Store type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Stores")
public final class Store implements Model {
  public static final QueryField ID = field("Store", "id");
  public static final QueryField STORENAME = field("Store", "storename");
  public static final QueryField STOREDESCRIPTION = field("Store", "storedescription");
  public static final QueryField LATITUDE = field("Store", "latitude");
  public static final QueryField LONGITUDE = field("Store", "longitude");
  public static final QueryField ACCOUNT_STORES_ID = field("Store", "accountStoresId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String storename;
  private final @ModelField(targetType="String", isRequired = true) String storedescription;
  private final @ModelField(targetType="Float") Double latitude;
  private final @ModelField(targetType="Float") Double longitude;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  private final @ModelField(targetType="ID") String accountStoresId;
  public String getId() {
      return id;
  }
  
  public String getStorename() {
      return storename;
  }
  
  public String getStoredescription() {
      return storedescription;
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
  
  public String getAccountStoresId() {
      return accountStoresId;
  }
  
  private Store(String id, String storename, String storedescription, Double latitude, Double longitude, String accountStoresId) {
    this.id = id;
    this.storename = storename;
    this.storedescription = storedescription;
    this.latitude = latitude;
    this.longitude = longitude;
    this.accountStoresId = accountStoresId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Store store = (Store) obj;
      return ObjectsCompat.equals(getId(), store.getId()) &&
              ObjectsCompat.equals(getStorename(), store.getStorename()) &&
              ObjectsCompat.equals(getStoredescription(), store.getStoredescription()) &&
              ObjectsCompat.equals(getLatitude(), store.getLatitude()) &&
              ObjectsCompat.equals(getLongitude(), store.getLongitude()) &&
              ObjectsCompat.equals(getCreatedAt(), store.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), store.getUpdatedAt()) &&
              ObjectsCompat.equals(getAccountStoresId(), store.getAccountStoresId());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getStorename())
      .append(getStoredescription())
      .append(getLatitude())
      .append(getLongitude())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .append(getAccountStoresId())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Store {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("storename=" + String.valueOf(getStorename()) + ", ")
      .append("storedescription=" + String.valueOf(getStoredescription()) + ", ")
      .append("latitude=" + String.valueOf(getLatitude()) + ", ")
      .append("longitude=" + String.valueOf(getLongitude()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("accountStoresId=" + String.valueOf(getAccountStoresId()))
      .append("}")
      .toString();
  }
  
  public static StorenameStep builder() {
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
  public static Store justId(String id) {
    return new Store(
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
      storename,
      storedescription,
      latitude,
      longitude,
      accountStoresId);
  }
  public interface StorenameStep {
    StoredescriptionStep storename(String storename);
  }
  

  public interface StoredescriptionStep {
    BuildStep storedescription(String storedescription);
  }
  

  public interface BuildStep {
    Store build();
    BuildStep id(String id);
    BuildStep latitude(Double latitude);
    BuildStep longitude(Double longitude);
    BuildStep accountStoresId(String accountStoresId);
  }
  

  public static class Builder implements StorenameStep, StoredescriptionStep, BuildStep {
    private String id;
    private String storename;
    private String storedescription;
    private Double latitude;
    private Double longitude;
    private String accountStoresId;
    @Override
     public Store build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Store(
          id,
          storename,
          storedescription,
          latitude,
          longitude,
          accountStoresId);
    }
    
    @Override
     public StoredescriptionStep storename(String storename) {
        Objects.requireNonNull(storename);
        this.storename = storename;
        return this;
    }
    
    @Override
     public BuildStep storedescription(String storedescription) {
        Objects.requireNonNull(storedescription);
        this.storedescription = storedescription;
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
     public BuildStep accountStoresId(String accountStoresId) {
        this.accountStoresId = accountStoresId;
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
    private CopyOfBuilder(String id, String storename, String storedescription, Double latitude, Double longitude, String accountStoresId) {
      super.id(id);
      super.storename(storename)
        .storedescription(storedescription)
        .latitude(latitude)
        .longitude(longitude)
        .accountStoresId(accountStoresId);
    }
    
    @Override
     public CopyOfBuilder storename(String storename) {
      return (CopyOfBuilder) super.storename(storename);
    }
    
    @Override
     public CopyOfBuilder storedescription(String storedescription) {
      return (CopyOfBuilder) super.storedescription(storedescription);
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
     public CopyOfBuilder accountStoresId(String accountStoresId) {
      return (CopyOfBuilder) super.accountStoresId(accountStoresId);
    }
  }
  
}
