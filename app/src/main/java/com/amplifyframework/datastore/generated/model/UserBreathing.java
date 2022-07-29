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

/** This is an auto generated class representing the UserBreathing type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserBreathings")
@Index(name = "byUserData", fields = {"userDataID"})
@Index(name = "byBreathingData", fields = {"breathingDataID"})
public final class UserBreathing implements Model {
  public static final QueryField ID = field("UserBreathing", "id");
  public static final QueryField USER_DATA = field("UserBreathing", "userDataID");
  public static final QueryField BREATHING_DATA = field("UserBreathing", "breathingDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private final @ModelField(targetType="BreathingData", isRequired = true) @BelongsTo(targetName = "breathingDataID", type = BreathingData.class) BreathingData breathingData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserData() {
      return userData;
  }
  
  public BreathingData getBreathingData() {
      return breathingData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserBreathing(String id, UserData userData, BreathingData breathingData) {
    this.id = id;
    this.userData = userData;
    this.breathingData = breathingData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserBreathing userBreathing = (UserBreathing) obj;
      return ObjectsCompat.equals(getId(), userBreathing.getId()) &&
              ObjectsCompat.equals(getUserData(), userBreathing.getUserData()) &&
              ObjectsCompat.equals(getBreathingData(), userBreathing.getBreathingData()) &&
              ObjectsCompat.equals(getCreatedAt(), userBreathing.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userBreathing.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserData())
      .append(getBreathingData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserBreathing {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("breathingData=" + String.valueOf(getBreathingData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserDataStep builder() {
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
  public static UserBreathing justId(String id) {
    return new UserBreathing(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userData,
      breathingData);
  }
  public interface UserDataStep {
    BreathingDataStep userData(UserData userData);
  }
  

  public interface BreathingDataStep {
    BuildStep breathingData(BreathingData breathingData);
  }
  

  public interface BuildStep {
    UserBreathing build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserDataStep, BreathingDataStep, BuildStep {
    private String id;
    private UserData userData;
    private BreathingData breathingData;
    @Override
     public UserBreathing build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserBreathing(
          id,
          userData,
          breathingData);
    }
    
    @Override
     public BreathingDataStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
        return this;
    }
    
    @Override
     public BuildStep breathingData(BreathingData breathingData) {
        Objects.requireNonNull(breathingData);
        this.breathingData = breathingData;
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
    private CopyOfBuilder(String id, UserData userData, BreathingData breathingData) {
      super.id(id);
      super.userData(userData)
        .breathingData(breathingData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
    
    @Override
     public CopyOfBuilder breathingData(BreathingData breathingData) {
      return (CopyOfBuilder) super.breathingData(breathingData);
    }
  }
  
}
