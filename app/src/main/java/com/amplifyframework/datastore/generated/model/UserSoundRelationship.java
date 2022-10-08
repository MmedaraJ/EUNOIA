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

/** This is an auto generated class representing the UserSoundRelationship type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserSoundRelationships", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "UserSoundRelationshipsOwnedByUser", fields = {"userSoundRelationshipUserDataID","id"})
@Index(name = "UserSoundRelationshipsOwnedBySound", fields = {"userSoundRelationshipSoundDataID","id"})
public final class UserSoundRelationship implements Model {
  public static final QueryField ID = field("UserSoundRelationship", "id");
  public static final QueryField USER_SOUND_RELATIONSHIP_OWNER = field("UserSoundRelationship", "userSoundRelationshipUserDataID");
  public static final QueryField USER_SOUND_RELATIONSHIP_SOUND = field("UserSoundRelationship", "userSoundRelationshipSoundDataID");
  public static final QueryField NUMBER_OF_TIMES_PLAYED = field("UserSoundRelationship", "numberOfTimesPlayed");
  public static final QueryField TOTAL_PLAY_TIME = field("UserSoundRelationship", "totalPlayTime");
  public static final QueryField CURRENTLY_LISTENING = field("UserSoundRelationship", "currentlyListening");
  public static final QueryField USAGE_TIMESTAMPS = field("UserSoundRelationship", "usageTimestamps");
  public static final QueryField USAGE_PLAY_TIMES = field("UserSoundRelationship", "usagePlayTimes");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userSoundRelationshipUserDataID", type = UserData.class) UserData userSoundRelationshipOwner;
  private final @ModelField(targetType="SoundData", isRequired = true) @BelongsTo(targetName = "userSoundRelationshipSoundDataID", type = SoundData.class) SoundData userSoundRelationshipSound;
  private final @ModelField(targetType="Int") Integer numberOfTimesPlayed;
  private final @ModelField(targetType="Int") Integer totalPlayTime;
  private final @ModelField(targetType="Boolean") Boolean currentlyListening;
  private final @ModelField(targetType="AWSDateTime") List<Temporal.DateTime> usageTimestamps;
  private final @ModelField(targetType="Int") List<Integer> usagePlayTimes;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserSoundRelationshipOwner() {
      return userSoundRelationshipOwner;
  }
  
  public SoundData getUserSoundRelationshipSound() {
      return userSoundRelationshipSound;
  }
  
  public Integer getNumberOfTimesPlayed() {
      return numberOfTimesPlayed;
  }
  
  public Integer getTotalPlayTime() {
      return totalPlayTime;
  }
  
  public Boolean getCurrentlyListening() {
      return currentlyListening;
  }
  
  public List<Temporal.DateTime> getUsageTimestamps() {
      return usageTimestamps;
  }
  
  public List<Integer> getUsagePlayTimes() {
      return usagePlayTimes;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserSoundRelationship(String id, UserData userSoundRelationshipOwner, SoundData userSoundRelationshipSound, Integer numberOfTimesPlayed, Integer totalPlayTime, Boolean currentlyListening, List<Temporal.DateTime> usageTimestamps, List<Integer> usagePlayTimes) {
    this.id = id;
    this.userSoundRelationshipOwner = userSoundRelationshipOwner;
    this.userSoundRelationshipSound = userSoundRelationshipSound;
    this.numberOfTimesPlayed = numberOfTimesPlayed;
    this.totalPlayTime = totalPlayTime;
    this.currentlyListening = currentlyListening;
    this.usageTimestamps = usageTimestamps;
    this.usagePlayTimes = usagePlayTimes;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserSoundRelationship userSoundRelationship = (UserSoundRelationship) obj;
      return ObjectsCompat.equals(getId(), userSoundRelationship.getId()) &&
              ObjectsCompat.equals(getUserSoundRelationshipOwner(), userSoundRelationship.getUserSoundRelationshipOwner()) &&
              ObjectsCompat.equals(getUserSoundRelationshipSound(), userSoundRelationship.getUserSoundRelationshipSound()) &&
              ObjectsCompat.equals(getNumberOfTimesPlayed(), userSoundRelationship.getNumberOfTimesPlayed()) &&
              ObjectsCompat.equals(getTotalPlayTime(), userSoundRelationship.getTotalPlayTime()) &&
              ObjectsCompat.equals(getCurrentlyListening(), userSoundRelationship.getCurrentlyListening()) &&
              ObjectsCompat.equals(getUsageTimestamps(), userSoundRelationship.getUsageTimestamps()) &&
              ObjectsCompat.equals(getUsagePlayTimes(), userSoundRelationship.getUsagePlayTimes()) &&
              ObjectsCompat.equals(getCreatedAt(), userSoundRelationship.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userSoundRelationship.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserSoundRelationshipOwner())
      .append(getUserSoundRelationshipSound())
      .append(getNumberOfTimesPlayed())
      .append(getTotalPlayTime())
      .append(getCurrentlyListening())
      .append(getUsageTimestamps())
      .append(getUsagePlayTimes())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserSoundRelationship {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userSoundRelationshipOwner=" + String.valueOf(getUserSoundRelationshipOwner()) + ", ")
      .append("userSoundRelationshipSound=" + String.valueOf(getUserSoundRelationshipSound()) + ", ")
      .append("numberOfTimesPlayed=" + String.valueOf(getNumberOfTimesPlayed()) + ", ")
      .append("totalPlayTime=" + String.valueOf(getTotalPlayTime()) + ", ")
      .append("currentlyListening=" + String.valueOf(getCurrentlyListening()) + ", ")
      .append("usageTimestamps=" + String.valueOf(getUsageTimestamps()) + ", ")
      .append("usagePlayTimes=" + String.valueOf(getUsagePlayTimes()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserSoundRelationshipOwnerStep builder() {
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
  public static UserSoundRelationship justId(String id) {
    return new UserSoundRelationship(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userSoundRelationshipOwner,
      userSoundRelationshipSound,
      numberOfTimesPlayed,
      totalPlayTime,
      currentlyListening,
      usageTimestamps,
      usagePlayTimes);
  }
  public interface UserSoundRelationshipOwnerStep {
    UserSoundRelationshipSoundStep userSoundRelationshipOwner(UserData userSoundRelationshipOwner);
  }
  

  public interface UserSoundRelationshipSoundStep {
    BuildStep userSoundRelationshipSound(SoundData userSoundRelationshipSound);
  }
  

  public interface BuildStep {
    UserSoundRelationship build();
    BuildStep id(String id);
    BuildStep numberOfTimesPlayed(Integer numberOfTimesPlayed);
    BuildStep totalPlayTime(Integer totalPlayTime);
    BuildStep currentlyListening(Boolean currentlyListening);
    BuildStep usageTimestamps(List<Temporal.DateTime> usageTimestamps);
    BuildStep usagePlayTimes(List<Integer> usagePlayTimes);
  }
  

  public static class Builder implements UserSoundRelationshipOwnerStep, UserSoundRelationshipSoundStep, BuildStep {
    private String id;
    private UserData userSoundRelationshipOwner;
    private SoundData userSoundRelationshipSound;
    private Integer numberOfTimesPlayed;
    private Integer totalPlayTime;
    private Boolean currentlyListening;
    private List<Temporal.DateTime> usageTimestamps;
    private List<Integer> usagePlayTimes;
    @Override
     public UserSoundRelationship build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserSoundRelationship(
          id,
          userSoundRelationshipOwner,
          userSoundRelationshipSound,
          numberOfTimesPlayed,
          totalPlayTime,
          currentlyListening,
          usageTimestamps,
          usagePlayTimes);
    }
    
    @Override
     public UserSoundRelationshipSoundStep userSoundRelationshipOwner(UserData userSoundRelationshipOwner) {
        Objects.requireNonNull(userSoundRelationshipOwner);
        this.userSoundRelationshipOwner = userSoundRelationshipOwner;
        return this;
    }
    
    @Override
     public BuildStep userSoundRelationshipSound(SoundData userSoundRelationshipSound) {
        Objects.requireNonNull(userSoundRelationshipSound);
        this.userSoundRelationshipSound = userSoundRelationshipSound;
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
    
    @Override
     public BuildStep currentlyListening(Boolean currentlyListening) {
        this.currentlyListening = currentlyListening;
        return this;
    }
    
    @Override
     public BuildStep usageTimestamps(List<Temporal.DateTime> usageTimestamps) {
        this.usageTimestamps = usageTimestamps;
        return this;
    }
    
    @Override
     public BuildStep usagePlayTimes(List<Integer> usagePlayTimes) {
        this.usagePlayTimes = usagePlayTimes;
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
    private CopyOfBuilder(String id, UserData userSoundRelationshipOwner, SoundData userSoundRelationshipSound, Integer numberOfTimesPlayed, Integer totalPlayTime, Boolean currentlyListening, List<Temporal.DateTime> usageTimestamps, List<Integer> usagePlayTimes) {
      super.id(id);
      super.userSoundRelationshipOwner(userSoundRelationshipOwner)
        .userSoundRelationshipSound(userSoundRelationshipSound)
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .totalPlayTime(totalPlayTime)
        .currentlyListening(currentlyListening)
        .usageTimestamps(usageTimestamps)
        .usagePlayTimes(usagePlayTimes);
    }
    
    @Override
     public CopyOfBuilder userSoundRelationshipOwner(UserData userSoundRelationshipOwner) {
      return (CopyOfBuilder) super.userSoundRelationshipOwner(userSoundRelationshipOwner);
    }
    
    @Override
     public CopyOfBuilder userSoundRelationshipSound(SoundData userSoundRelationshipSound) {
      return (CopyOfBuilder) super.userSoundRelationshipSound(userSoundRelationshipSound);
    }
    
    @Override
     public CopyOfBuilder numberOfTimesPlayed(Integer numberOfTimesPlayed) {
      return (CopyOfBuilder) super.numberOfTimesPlayed(numberOfTimesPlayed);
    }
    
    @Override
     public CopyOfBuilder totalPlayTime(Integer totalPlayTime) {
      return (CopyOfBuilder) super.totalPlayTime(totalPlayTime);
    }
    
    @Override
     public CopyOfBuilder currentlyListening(Boolean currentlyListening) {
      return (CopyOfBuilder) super.currentlyListening(currentlyListening);
    }
    
    @Override
     public CopyOfBuilder usageTimestamps(List<Temporal.DateTime> usageTimestamps) {
      return (CopyOfBuilder) super.usageTimestamps(usageTimestamps);
    }
    
    @Override
     public CopyOfBuilder usagePlayTimes(List<Integer> usagePlayTimes) {
      return (CopyOfBuilder) super.usagePlayTimes(usagePlayTimes);
    }
  }
  
}
