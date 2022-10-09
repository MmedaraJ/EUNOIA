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

/** This is an auto generated class representing the UserRoutineRelationshipBreathing type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRoutineRelationshipBreathings")
@Index(name = "byUserRoutineRelationship", fields = {"userRoutineRelationshipID"})
@Index(name = "byBreathingData", fields = {"breathingDataID"})
public final class UserRoutineRelationshipBreathing implements Model {
  public static final QueryField ID = field("UserRoutineRelationshipBreathing", "id");
  public static final QueryField USER_ROUTINE_RELATIONSHIP = field("UserRoutineRelationshipBreathing", "userRoutineRelationshipID");
  public static final QueryField BREATHING_DATA = field("UserRoutineRelationshipBreathing", "breathingDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserRoutineRelationship", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipID", type = UserRoutineRelationship.class) UserRoutineRelationship userRoutineRelationship;
  private final @ModelField(targetType="BreathingData", isRequired = true) @BelongsTo(targetName = "breathingDataID", type = BreathingData.class) BreathingData breathingData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserRoutineRelationship getUserRoutineRelationship() {
      return userRoutineRelationship;
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
  
  private UserRoutineRelationshipBreathing(String id, UserRoutineRelationship userRoutineRelationship, BreathingData breathingData) {
    this.id = id;
    this.userRoutineRelationship = userRoutineRelationship;
    this.breathingData = breathingData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserRoutineRelationshipBreathing userRoutineRelationshipBreathing = (UserRoutineRelationshipBreathing) obj;
      return ObjectsCompat.equals(getId(), userRoutineRelationshipBreathing.getId()) &&
              ObjectsCompat.equals(getUserRoutineRelationship(), userRoutineRelationshipBreathing.getUserRoutineRelationship()) &&
              ObjectsCompat.equals(getBreathingData(), userRoutineRelationshipBreathing.getBreathingData()) &&
              ObjectsCompat.equals(getCreatedAt(), userRoutineRelationshipBreathing.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRoutineRelationshipBreathing.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserRoutineRelationship())
      .append(getBreathingData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserRoutineRelationshipBreathing {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userRoutineRelationship=" + String.valueOf(getUserRoutineRelationship()) + ", ")
      .append("breathingData=" + String.valueOf(getBreathingData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserRoutineRelationshipStep builder() {
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
  public static UserRoutineRelationshipBreathing justId(String id) {
    return new UserRoutineRelationshipBreathing(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userRoutineRelationship,
      breathingData);
  }
  public interface UserRoutineRelationshipStep {
    BreathingDataStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship);
  }
  

  public interface BreathingDataStep {
    BuildStep breathingData(BreathingData breathingData);
  }
  

  public interface BuildStep {
    UserRoutineRelationshipBreathing build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserRoutineRelationshipStep, BreathingDataStep, BuildStep {
    private String id;
    private UserRoutineRelationship userRoutineRelationship;
    private BreathingData breathingData;
    @Override
     public UserRoutineRelationshipBreathing build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRoutineRelationshipBreathing(
          id,
          userRoutineRelationship,
          breathingData);
    }
    
    @Override
     public BreathingDataStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
        Objects.requireNonNull(userRoutineRelationship);
        this.userRoutineRelationship = userRoutineRelationship;
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
    private CopyOfBuilder(String id, UserRoutineRelationship userRoutineRelationship, BreathingData breathingData) {
      super.id(id);
      super.userRoutineRelationship(userRoutineRelationship)
        .breathingData(breathingData);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
      return (CopyOfBuilder) super.userRoutineRelationship(userRoutineRelationship);
    }
    
    @Override
     public CopyOfBuilder breathingData(BreathingData breathingData) {
      return (CopyOfBuilder) super.breathingData(breathingData);
    }
  }
  
}
