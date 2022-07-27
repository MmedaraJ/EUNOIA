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

/** This is an auto generated class representing the UserSoundModel type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserSounds")
@Index(name = "bySoundData", fields = {"soundDataID"})
@Index(name = "byUserData", fields = {"userDataID"})
public final class UserSound implements Model {
  public static final QueryField ID = field("UserSoundModel", "id");
  public static final QueryField SOUND_DATA = field("UserSoundModel", "soundDataID");
  public static final QueryField USER_DATA = field("UserSoundModel", "userDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="SoundData", isRequired = true) @BelongsTo(targetName = "soundDataID", type = SoundData.class) SoundData soundData;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public SoundData getSoundData() {
      return soundData;
  }
  
  public UserData getUserData() {
      return userData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserSound(String id, SoundData soundData, UserData userData) {
    this.id = id;
    this.soundData = soundData;
    this.userData = userData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserSound userSound = (UserSound) obj;
      return ObjectsCompat.equals(getId(), userSound.getId()) &&
              ObjectsCompat.equals(getSoundData(), userSound.getSoundData()) &&
              ObjectsCompat.equals(getUserData(), userSound.getUserData()) &&
              ObjectsCompat.equals(getCreatedAt(), userSound.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userSound.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getSoundData())
      .append(getUserData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserSoundModel {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("soundData=" + String.valueOf(getSoundData()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static SoundDataStep builder() {
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
  public static UserSound justId(String id) {
    return new UserSound(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      soundData,
      userData);
  }
  public interface SoundDataStep {
    UserDataStep soundData(SoundData soundData);
  }
  

  public interface UserDataStep {
    BuildStep userData(UserData userData);
  }
  

  public interface BuildStep {
    UserSound build();
    BuildStep id(String id);
  }
  

  public static class Builder implements SoundDataStep, UserDataStep, BuildStep {
    private String id;
    private SoundData soundData;
    private UserData userData;
    @Override
     public UserSound build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserSound(
          id,
          soundData,
          userData);
    }
    
    @Override
     public UserDataStep soundData(SoundData soundData) {
        Objects.requireNonNull(soundData);
        this.soundData = soundData;
        return this;
    }
    
    @Override
     public BuildStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
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
    private CopyOfBuilder(String id, SoundData soundData, UserData userData) {
      super.id(id);
      super.soundData(soundData)
        .userData(userData);
    }
    
    @Override
     public CopyOfBuilder soundData(SoundData soundData) {
      return (CopyOfBuilder) super.soundData(soundData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
  }
  
}
