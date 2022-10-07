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

/** This is an auto generated class representing the UserSoundPresetRelationship type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserSoundPresetRelationships", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "UserSoundPresetRelationshipsOwnedByUser", fields = {"userSoundPresetRelationshipUserDataID","id"})
@Index(name = "UserSoundPresetRelationshipsOwnedBySoundPreset", fields = {"userSoundPresetRelationshipSoundSoundPresetDataID","id"})
public final class UserSoundPresetRelationship implements Model {
  public static final QueryField ID = field("UserSoundPresetRelationship", "id");
  public static final QueryField USER_SOUND_PRESET_RELATIONSHIP_OWNER = field("UserSoundPresetRelationship", "userSoundPresetRelationshipUserDataID");
  public static final QueryField USER_SOUND_PRESET_RELATIONSHIP_SOUND_PRESET = field("UserSoundPresetRelationship", "userSoundPresetRelationshipSoundSoundPresetDataID");
  public static final QueryField NUMBER_OF_TIMES_PLAYED = field("UserSoundPresetRelationship", "numberOfTimesPlayed");
  public static final QueryField TOTAL_PLAY_TIME = field("UserSoundPresetRelationship", "totalPlayTime");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userSoundPresetRelationshipUserDataID", type = UserData.class) UserData userSoundPresetRelationshipOwner;
  private final @ModelField(targetType="SoundPresetData", isRequired = true) @BelongsTo(targetName = "userSoundPresetRelationshipSoundSoundPresetDataID", type = SoundPresetData.class) SoundPresetData userSoundPresetRelationshipSoundPreset;
  private final @ModelField(targetType="Int") Integer numberOfTimesPlayed;
  private final @ModelField(targetType="Int") Integer totalPlayTime;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserSoundPresetRelationshipOwner() {
      return userSoundPresetRelationshipOwner;
  }
  
  public SoundPresetData getUserSoundPresetRelationshipSoundPreset() {
      return userSoundPresetRelationshipSoundPreset;
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
  
  private UserSoundPresetRelationship(String id, UserData userSoundPresetRelationshipOwner, SoundPresetData userSoundPresetRelationshipSoundPreset, Integer numberOfTimesPlayed, Integer totalPlayTime) {
    this.id = id;
    this.userSoundPresetRelationshipOwner = userSoundPresetRelationshipOwner;
    this.userSoundPresetRelationshipSoundPreset = userSoundPresetRelationshipSoundPreset;
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
      UserSoundPresetRelationship userSoundPresetRelationship = (UserSoundPresetRelationship) obj;
      return ObjectsCompat.equals(getId(), userSoundPresetRelationship.getId()) &&
              ObjectsCompat.equals(getUserSoundPresetRelationshipOwner(), userSoundPresetRelationship.getUserSoundPresetRelationshipOwner()) &&
              ObjectsCompat.equals(getUserSoundPresetRelationshipSoundPreset(), userSoundPresetRelationship.getUserSoundPresetRelationshipSoundPreset()) &&
              ObjectsCompat.equals(getNumberOfTimesPlayed(), userSoundPresetRelationship.getNumberOfTimesPlayed()) &&
              ObjectsCompat.equals(getTotalPlayTime(), userSoundPresetRelationship.getTotalPlayTime()) &&
              ObjectsCompat.equals(getCreatedAt(), userSoundPresetRelationship.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userSoundPresetRelationship.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserSoundPresetRelationshipOwner())
      .append(getUserSoundPresetRelationshipSoundPreset())
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
      .append("UserSoundPresetRelationship {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userSoundPresetRelationshipOwner=" + String.valueOf(getUserSoundPresetRelationshipOwner()) + ", ")
      .append("userSoundPresetRelationshipSoundPreset=" + String.valueOf(getUserSoundPresetRelationshipSoundPreset()) + ", ")
      .append("numberOfTimesPlayed=" + String.valueOf(getNumberOfTimesPlayed()) + ", ")
      .append("totalPlayTime=" + String.valueOf(getTotalPlayTime()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserSoundPresetRelationshipOwnerStep builder() {
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
  public static UserSoundPresetRelationship justId(String id) {
    return new UserSoundPresetRelationship(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userSoundPresetRelationshipOwner,
      userSoundPresetRelationshipSoundPreset,
      numberOfTimesPlayed,
      totalPlayTime);
  }
  public interface UserSoundPresetRelationshipOwnerStep {
    UserSoundPresetRelationshipSoundPresetStep userSoundPresetRelationshipOwner(UserData userSoundPresetRelationshipOwner);
  }
  

  public interface UserSoundPresetRelationshipSoundPresetStep {
    BuildStep userSoundPresetRelationshipSoundPreset(SoundPresetData userSoundPresetRelationshipSoundPreset);
  }
  

  public interface BuildStep {
    UserSoundPresetRelationship build();
    BuildStep id(String id);
    BuildStep numberOfTimesPlayed(Integer numberOfTimesPlayed);
    BuildStep totalPlayTime(Integer totalPlayTime);
  }
  

  public static class Builder implements UserSoundPresetRelationshipOwnerStep, UserSoundPresetRelationshipSoundPresetStep, BuildStep {
    private String id;
    private UserData userSoundPresetRelationshipOwner;
    private SoundPresetData userSoundPresetRelationshipSoundPreset;
    private Integer numberOfTimesPlayed;
    private Integer totalPlayTime;
    @Override
     public UserSoundPresetRelationship build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserSoundPresetRelationship(
          id,
          userSoundPresetRelationshipOwner,
          userSoundPresetRelationshipSoundPreset,
          numberOfTimesPlayed,
          totalPlayTime);
    }
    
    @Override
     public UserSoundPresetRelationshipSoundPresetStep userSoundPresetRelationshipOwner(UserData userSoundPresetRelationshipOwner) {
        Objects.requireNonNull(userSoundPresetRelationshipOwner);
        this.userSoundPresetRelationshipOwner = userSoundPresetRelationshipOwner;
        return this;
    }
    
    @Override
     public BuildStep userSoundPresetRelationshipSoundPreset(SoundPresetData userSoundPresetRelationshipSoundPreset) {
        Objects.requireNonNull(userSoundPresetRelationshipSoundPreset);
        this.userSoundPresetRelationshipSoundPreset = userSoundPresetRelationshipSoundPreset;
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
    private CopyOfBuilder(String id, UserData userSoundPresetRelationshipOwner, SoundPresetData userSoundPresetRelationshipSoundPreset, Integer numberOfTimesPlayed, Integer totalPlayTime) {
      super.id(id);
      super.userSoundPresetRelationshipOwner(userSoundPresetRelationshipOwner)
        .userSoundPresetRelationshipSoundPreset(userSoundPresetRelationshipSoundPreset)
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .totalPlayTime(totalPlayTime);
    }
    
    @Override
     public CopyOfBuilder userSoundPresetRelationshipOwner(UserData userSoundPresetRelationshipOwner) {
      return (CopyOfBuilder) super.userSoundPresetRelationshipOwner(userSoundPresetRelationshipOwner);
    }
    
    @Override
     public CopyOfBuilder userSoundPresetRelationshipSoundPreset(SoundPresetData userSoundPresetRelationshipSoundPreset) {
      return (CopyOfBuilder) super.userSoundPresetRelationshipSoundPreset(userSoundPresetRelationshipSoundPreset);
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
