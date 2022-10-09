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

/** This is an auto generated class representing the UserRoutineRelationshipSound type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRoutineRelationshipSounds")
@Index(name = "bySoundData", fields = {"soundDataID"})
@Index(name = "byUserRoutineRelationship", fields = {"userRoutineRelationshipID"})
public final class UserRoutineRelationshipSound implements Model {
  public static final QueryField ID = field("UserRoutineRelationshipSound", "id");
  public static final QueryField SOUND_DATA = field("UserRoutineRelationshipSound", "soundDataID");
  public static final QueryField USER_ROUTINE_RELATIONSHIP = field("UserRoutineRelationshipSound", "userRoutineRelationshipID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="SoundData", isRequired = true) @BelongsTo(targetName = "soundDataID", type = SoundData.class) SoundData soundData;
  private final @ModelField(targetType="UserRoutineRelationship", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipID", type = UserRoutineRelationship.class) UserRoutineRelationship userRoutineRelationship;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public SoundData getSoundData() {
      return soundData;
  }
  
  public UserRoutineRelationship getUserRoutineRelationship() {
      return userRoutineRelationship;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserRoutineRelationshipSound(String id, SoundData soundData, UserRoutineRelationship userRoutineRelationship) {
    this.id = id;
    this.soundData = soundData;
    this.userRoutineRelationship = userRoutineRelationship;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserRoutineRelationshipSound userRoutineRelationshipSound = (UserRoutineRelationshipSound) obj;
      return ObjectsCompat.equals(getId(), userRoutineRelationshipSound.getId()) &&
              ObjectsCompat.equals(getSoundData(), userRoutineRelationshipSound.getSoundData()) &&
              ObjectsCompat.equals(getUserRoutineRelationship(), userRoutineRelationshipSound.getUserRoutineRelationship()) &&
              ObjectsCompat.equals(getCreatedAt(), userRoutineRelationshipSound.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRoutineRelationshipSound.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getSoundData())
      .append(getUserRoutineRelationship())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserRoutineRelationshipSound {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("soundData=" + String.valueOf(getSoundData()) + ", ")
      .append("userRoutineRelationship=" + String.valueOf(getUserRoutineRelationship()) + ", ")
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
  public static UserRoutineRelationshipSound justId(String id) {
    return new UserRoutineRelationshipSound(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      soundData,
      userRoutineRelationship);
  }
  public interface SoundDataStep {
    UserRoutineRelationshipStep soundData(SoundData soundData);
  }
  

  public interface UserRoutineRelationshipStep {
    BuildStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship);
  }
  

  public interface BuildStep {
    UserRoutineRelationshipSound build();
    BuildStep id(String id);
  }
  

  public static class Builder implements SoundDataStep, UserRoutineRelationshipStep, BuildStep {
    private String id;
    private SoundData soundData;
    private UserRoutineRelationship userRoutineRelationship;
    @Override
     public UserRoutineRelationshipSound build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRoutineRelationshipSound(
          id,
          soundData,
          userRoutineRelationship);
    }
    
    @Override
     public UserRoutineRelationshipStep soundData(SoundData soundData) {
        Objects.requireNonNull(soundData);
        this.soundData = soundData;
        return this;
    }
    
    @Override
     public BuildStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
        Objects.requireNonNull(userRoutineRelationship);
        this.userRoutineRelationship = userRoutineRelationship;
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
    private CopyOfBuilder(String id, SoundData soundData, UserRoutineRelationship userRoutineRelationship) {
      super.id(id);
      super.soundData(soundData)
        .userRoutineRelationship(userRoutineRelationship);
    }
    
    @Override
     public CopyOfBuilder soundData(SoundData soundData) {
      return (CopyOfBuilder) super.soundData(soundData);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
      return (CopyOfBuilder) super.userRoutineRelationship(userRoutineRelationship);
    }
  }
  
}
