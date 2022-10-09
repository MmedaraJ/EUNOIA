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

/** This is an auto generated class representing the UserRoutineRelationshipSoundPreset type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRoutineRelationshipSoundPresets")
@Index(name = "byUserRoutineRelationship", fields = {"userRoutineRelationshipID"})
@Index(name = "bySoundPresetData", fields = {"soundPresetDataID"})
public final class UserRoutineRelationshipSoundPreset implements Model {
  public static final QueryField ID = field("UserRoutineRelationshipSoundPreset", "id");
  public static final QueryField USER_ROUTINE_RELATIONSHIP = field("UserRoutineRelationshipSoundPreset", "userRoutineRelationshipID");
  public static final QueryField SOUND_PRESET_DATA = field("UserRoutineRelationshipSoundPreset", "soundPresetDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserRoutineRelationship", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipID", type = UserRoutineRelationship.class) UserRoutineRelationship userRoutineRelationship;
  private final @ModelField(targetType="SoundPresetData", isRequired = true) @BelongsTo(targetName = "soundPresetDataID", type = SoundPresetData.class) SoundPresetData soundPresetData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserRoutineRelationship getUserRoutineRelationship() {
      return userRoutineRelationship;
  }
  
  public SoundPresetData getSoundPresetData() {
      return soundPresetData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserRoutineRelationshipSoundPreset(String id, UserRoutineRelationship userRoutineRelationship, SoundPresetData soundPresetData) {
    this.id = id;
    this.userRoutineRelationship = userRoutineRelationship;
    this.soundPresetData = soundPresetData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserRoutineRelationshipSoundPreset userRoutineRelationshipSoundPreset = (UserRoutineRelationshipSoundPreset) obj;
      return ObjectsCompat.equals(getId(), userRoutineRelationshipSoundPreset.getId()) &&
              ObjectsCompat.equals(getUserRoutineRelationship(), userRoutineRelationshipSoundPreset.getUserRoutineRelationship()) &&
              ObjectsCompat.equals(getSoundPresetData(), userRoutineRelationshipSoundPreset.getSoundPresetData()) &&
              ObjectsCompat.equals(getCreatedAt(), userRoutineRelationshipSoundPreset.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRoutineRelationshipSoundPreset.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserRoutineRelationship())
      .append(getSoundPresetData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserRoutineRelationshipSoundPreset {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userRoutineRelationship=" + String.valueOf(getUserRoutineRelationship()) + ", ")
      .append("soundPresetData=" + String.valueOf(getSoundPresetData()) + ", ")
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
  public static UserRoutineRelationshipSoundPreset justId(String id) {
    return new UserRoutineRelationshipSoundPreset(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userRoutineRelationship,
      soundPresetData);
  }
  public interface UserRoutineRelationshipStep {
    SoundPresetDataStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship);
  }
  

  public interface SoundPresetDataStep {
    BuildStep soundPresetData(SoundPresetData soundPresetData);
  }
  

  public interface BuildStep {
    UserRoutineRelationshipSoundPreset build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserRoutineRelationshipStep, SoundPresetDataStep, BuildStep {
    private String id;
    private UserRoutineRelationship userRoutineRelationship;
    private SoundPresetData soundPresetData;
    @Override
     public UserRoutineRelationshipSoundPreset build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRoutineRelationshipSoundPreset(
          id,
          userRoutineRelationship,
          soundPresetData);
    }
    
    @Override
     public SoundPresetDataStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
        Objects.requireNonNull(userRoutineRelationship);
        this.userRoutineRelationship = userRoutineRelationship;
        return this;
    }
    
    @Override
     public BuildStep soundPresetData(SoundPresetData soundPresetData) {
        Objects.requireNonNull(soundPresetData);
        this.soundPresetData = soundPresetData;
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
    private CopyOfBuilder(String id, UserRoutineRelationship userRoutineRelationship, SoundPresetData soundPresetData) {
      super.id(id);
      super.userRoutineRelationship(userRoutineRelationship)
        .soundPresetData(soundPresetData);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
      return (CopyOfBuilder) super.userRoutineRelationship(userRoutineRelationship);
    }
    
    @Override
     public CopyOfBuilder soundPresetData(SoundPresetData soundPresetData) {
      return (CopyOfBuilder) super.soundPresetData(soundPresetData);
    }
  }
  
}
