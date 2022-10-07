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

/** This is an auto generated class representing the UserSoundPreset type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserSoundPresets")
@Index(name = "byUserData", fields = {"userDataID"})
@Index(name = "bySoundPresetData", fields = {"soundPresetDataID"})
public final class UserSoundPreset implements Model {
  public static final QueryField ID = field("UserSoundPreset", "id");
  public static final QueryField USER_DATA = field("UserSoundPreset", "userDataID");
  public static final QueryField SOUND_PRESET_DATA = field("UserSoundPreset", "soundPresetDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private final @ModelField(targetType="SoundPresetData", isRequired = true) @BelongsTo(targetName = "soundPresetDataID", type = SoundPresetData.class) SoundPresetData soundPresetData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserData() {
      return userData;
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
  
  private UserSoundPreset(String id, UserData userData, SoundPresetData soundPresetData) {
    this.id = id;
    this.userData = userData;
    this.soundPresetData = soundPresetData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserSoundPreset userSoundPreset = (UserSoundPreset) obj;
      return ObjectsCompat.equals(getId(), userSoundPreset.getId()) &&
              ObjectsCompat.equals(getUserData(), userSoundPreset.getUserData()) &&
              ObjectsCompat.equals(getSoundPresetData(), userSoundPreset.getSoundPresetData()) &&
              ObjectsCompat.equals(getCreatedAt(), userSoundPreset.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userSoundPreset.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserData())
      .append(getSoundPresetData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserSoundPreset {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("soundPresetData=" + String.valueOf(getSoundPresetData()) + ", ")
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
  public static UserSoundPreset justId(String id) {
    return new UserSoundPreset(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userData,
      soundPresetData);
  }
  public interface UserDataStep {
    SoundPresetDataStep userData(UserData userData);
  }
  

  public interface SoundPresetDataStep {
    BuildStep soundPresetData(SoundPresetData soundPresetData);
  }
  

  public interface BuildStep {
    UserSoundPreset build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserDataStep, SoundPresetDataStep, BuildStep {
    private String id;
    private UserData userData;
    private SoundPresetData soundPresetData;
    @Override
     public UserSoundPreset build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserSoundPreset(
          id,
          userData,
          soundPresetData);
    }
    
    @Override
     public SoundPresetDataStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
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
    private CopyOfBuilder(String id, UserData userData, SoundPresetData soundPresetData) {
      super.id(id);
      super.userData(userData)
        .soundPresetData(soundPresetData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
    
    @Override
     public CopyOfBuilder soundPresetData(SoundPresetData soundPresetData) {
      return (CopyOfBuilder) super.soundPresetData(soundPresetData);
    }
  }
  
}
