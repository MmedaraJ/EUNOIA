package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the UserPresetRelationship type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserPresetRelationships", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "UserPresetRelationshipsOwnedByUser", fields = {"userPresetRelationshipUserDataID","id"})
@Index(name = "UserPresetRelationshipsOwnedByPreset", fields = {"userPresetRelationshipPresetDataID","id"})
public final class UserPresetRelationship implements Model {
  public static final QueryField ID = field("UserPresetRelationship", "id");
  public static final QueryField USER_PRESET_RELATIONSHIP_OWNER = field("UserPresetRelationship", "userPresetRelationshipUserDataID");
  public static final QueryField USER_PRESET_RELATIONSHIP_PRESET = field("UserPresetRelationship", "userPresetRelationshipPresetDataID");
  public static final QueryField NUMBER_OF_TIMES_PLAYED = field("UserPresetRelationship", "numberOfTimesPlayed");
  public static final QueryField TOTAL_PLAY_TIME = field("UserPresetRelationship", "totalPlayTime");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userPresetRelationshipUserDataID", type = UserData.class) UserData userPresetRelationshipOwner;
  private final @ModelField(targetType="PresetData", isRequired = true) @BelongsTo(targetName = "userPresetRelationshipPresetDataID", type = PresetData.class) PresetData userPresetRelationshipPreset;
  private final @ModelField(targetType="Int") Integer numberOfTimesPlayed;
  private final @ModelField(targetType="Int") Integer totalPlayTime;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserPresetRelationshipOwner() {
      return userPresetRelationshipOwner;
  }
  
  public PresetData getUserPresetRelationshipPreset() {
      return userPresetRelationshipPreset;
  }
  
  public Integer getNumberOfTimesPlayed() {
      return numberOfTimesPlayed;
  }
  
  public Integer getTotalPlayTime() {
      return totalPlayTime;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserPresetRelationship(String id, UserData userPresetRelationshipOwner, PresetData userPresetRelationshipPreset, Integer numberOfTimesPlayed, Integer totalPlayTime) {
    this.id = id;
    this.userPresetRelationshipOwner = userPresetRelationshipOwner;
    this.userPresetRelationshipPreset = userPresetRelationshipPreset;
    this.numberOfTimesPlayed = numberOfTimesPlayed;
    this.totalPlayTime = totalPlayTime;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserPresetRelationship userPresetRelationship = (UserPresetRelationship) obj;
      return ObjectsCompat.equals(getId(), userPresetRelationship.getId()) &&
              ObjectsCompat.equals(getUserPresetRelationshipOwner(), userPresetRelationship.getUserPresetRelationshipOwner()) &&
              ObjectsCompat.equals(getUserPresetRelationshipPreset(), userPresetRelationship.getUserPresetRelationshipPreset()) &&
              ObjectsCompat.equals(getNumberOfTimesPlayed(), userPresetRelationship.getNumberOfTimesPlayed()) &&
              ObjectsCompat.equals(getTotalPlayTime(), userPresetRelationship.getTotalPlayTime()) &&
              ObjectsCompat.equals(getCreatedAt(), userPresetRelationship.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userPresetRelationship.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserPresetRelationshipOwner())
      .append(getUserPresetRelationshipPreset())
      .append(getNumberOfTimesPlayed())
      .append(getTotalPlayTime())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserPresetRelationship {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userPresetRelationshipOwner=" + String.valueOf(getUserPresetRelationshipOwner()) + ", ")
      .append("userPresetRelationshipPreset=" + String.valueOf(getUserPresetRelationshipPreset()) + ", ")
      .append("numberOfTimesPlayed=" + String.valueOf(getNumberOfTimesPlayed()) + ", ")
      .append("totalPlayTime=" + String.valueOf(getTotalPlayTime()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserPresetRelationshipOwnerStep builder() {
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
  public static UserPresetRelationship justId(String id) {
    return new UserPresetRelationship(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userPresetRelationshipOwner,
      userPresetRelationshipPreset,
      numberOfTimesPlayed,
      totalPlayTime);
  }
  public interface UserPresetRelationshipOwnerStep {
    UserPresetRelationshipPresetStep userPresetRelationshipOwner(UserData userPresetRelationshipOwner);
  }
  

  public interface UserPresetRelationshipPresetStep {
    BuildStep userPresetRelationshipPreset(PresetData userPresetRelationshipPreset);
  }
  

  public interface BuildStep {
    UserPresetRelationship build();
    BuildStep id(String id);
    BuildStep numberOfTimesPlayed(Integer numberOfTimesPlayed);
    BuildStep totalPlayTime(Integer totalPlayTime);
  }
  

  public static class Builder implements UserPresetRelationshipOwnerStep, UserPresetRelationshipPresetStep, BuildStep {
    private String id;
    private UserData userPresetRelationshipOwner;
    private PresetData userPresetRelationshipPreset;
    private Integer numberOfTimesPlayed;
    private Integer totalPlayTime;
    @Override
     public UserPresetRelationship build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserPresetRelationship(
          id,
          userPresetRelationshipOwner,
          userPresetRelationshipPreset,
          numberOfTimesPlayed,
          totalPlayTime);
    }
    
    @Override
     public UserPresetRelationshipPresetStep userPresetRelationshipOwner(UserData userPresetRelationshipOwner) {
        Objects.requireNonNull(userPresetRelationshipOwner);
        this.userPresetRelationshipOwner = userPresetRelationshipOwner;
        return this;
    }
    
    @Override
     public BuildStep userPresetRelationshipPreset(PresetData userPresetRelationshipPreset) {
        Objects.requireNonNull(userPresetRelationshipPreset);
        this.userPresetRelationshipPreset = userPresetRelationshipPreset;
        return this;
    }
    
    @Override
     public BuildStep numberOfTimesPlayed(Integer numberOfTimesPlayed) {
        this.numberOfTimesPlayed = numberOfTimesPlayed;
        return this;
    }
    
    @Override
     public BuildStep totalPlayTime(Integer totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
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
    private CopyOfBuilder(String id, UserData userPresetRelationshipOwner, PresetData userPresetRelationshipPreset, Integer numberOfTimesPlayed, Integer totalPlayTime) {
      super.id(id);
      super.userPresetRelationshipOwner(userPresetRelationshipOwner)
        .userPresetRelationshipPreset(userPresetRelationshipPreset)
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .totalPlayTime(totalPlayTime);
    }
    
    @Override
     public CopyOfBuilder userPresetRelationshipOwner(UserData userPresetRelationshipOwner) {
      return (CopyOfBuilder) super.userPresetRelationshipOwner(userPresetRelationshipOwner);
    }
    
    @Override
     public CopyOfBuilder userPresetRelationshipPreset(PresetData userPresetRelationshipPreset) {
      return (CopyOfBuilder) super.userPresetRelationshipPreset(userPresetRelationshipPreset);
    }
    
    @Override
     public CopyOfBuilder numberOfTimesPlayed(Integer numberOfTimesPlayed) {
      return (CopyOfBuilder) super.numberOfTimesPlayed(numberOfTimesPlayed);
    }
    
    @Override
     public CopyOfBuilder totalPlayTime(Integer totalPlayTime) {
      return (CopyOfBuilder) super.totalPlayTime(totalPlayTime);
    }
  }
  
}
