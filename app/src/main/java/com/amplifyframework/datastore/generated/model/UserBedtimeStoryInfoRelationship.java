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

/** This is an auto generated class representing the UserBedtimeStoryInfoRelationship type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserBedtimeStoryInfoRelationships", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "UserBedtimeStoryInfoRelationshipsOwnedByUser", fields = {"userBedtimeStoryInfoRelationshipUserDataID","id"})
@Index(name = "UserBedtimeStoryInfoRelationshipsOwnedByBedtimeStoryInfo", fields = {"userBedtimeStoryInfoRelationshipBedtimeStoryInfoDataID","id"})
public final class UserBedtimeStoryInfoRelationship implements Model {
  public static final QueryField ID = field("UserBedtimeStoryInfoRelationship", "id");
  public static final QueryField USER_BEDTIME_STORY_INFO_RELATIONSHIP_OWNER = field("UserBedtimeStoryInfoRelationship", "userBedtimeStoryInfoRelationshipUserDataID");
  public static final QueryField USER_BEDTIME_STORY_INFO_RELATIONSHIP_BEDTIME_STORY_INFO = field("UserBedtimeStoryInfoRelationship", "userBedtimeStoryInfoRelationshipBedtimeStoryInfoDataID");
  public static final QueryField NUMBER_OF_TIMES_PLAYED = field("UserBedtimeStoryInfoRelationship", "numberOfTimesPlayed");
  public static final QueryField TOTAL_PLAY_TIME = field("UserBedtimeStoryInfoRelationship", "totalPlayTime");
  public static final QueryField CONTINUE_PLAYING_TIME = field("UserBedtimeStoryInfoRelationship", "continuePlayingTime");
  public static final QueryField CURRENTLY_LISTENING = field("UserBedtimeStoryInfoRelationship", "currentlyListening");
  public static final QueryField USAGE_TIMESTAMPS = field("UserBedtimeStoryInfoRelationship", "usageTimestamps");
  public static final QueryField USAGE_PLAY_TIMES = field("UserBedtimeStoryInfoRelationship", "usagePlayTimes");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userBedtimeStoryInfoRelationshipUserDataID", type = UserData.class) UserData userBedtimeStoryInfoRelationshipOwner;
  private final @ModelField(targetType="BedtimeStoryInfoData", isRequired = true) @BelongsTo(targetName = "userBedtimeStoryInfoRelationshipBedtimeStoryInfoDataID", type = BedtimeStoryInfoData.class) BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo;
  private final @ModelField(targetType="Int") Integer numberOfTimesPlayed;
  private final @ModelField(targetType="Int") Integer totalPlayTime;
  private final @ModelField(targetType="Int") Integer continuePlayingTime;
  private final @ModelField(targetType="Boolean") Boolean currentlyListening;
  private final @ModelField(targetType="AWSDateTime") List<Temporal.DateTime> usageTimestamps;
  private final @ModelField(targetType="Int") List<Integer> usagePlayTimes;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserBedtimeStoryInfoRelationshipOwner() {
      return userBedtimeStoryInfoRelationshipOwner;
  }
  
  public BedtimeStoryInfoData getUserBedtimeStoryInfoRelationshipBedtimeStoryInfo() {
      return userBedtimeStoryInfoRelationshipBedtimeStoryInfo;
  }
  
  public Integer getNumberOfTimesPlayed() {
      return numberOfTimesPlayed;
  }
  
  public Integer getTotalPlayTime() {
      return totalPlayTime;
  }
  
  public Integer getContinuePlayingTime() {
      return continuePlayingTime;
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
  
  private UserBedtimeStoryInfoRelationship(String id, UserData userBedtimeStoryInfoRelationshipOwner, BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo, Integer numberOfTimesPlayed, Integer totalPlayTime, Integer continuePlayingTime, Boolean currentlyListening, List<Temporal.DateTime> usageTimestamps, List<Integer> usagePlayTimes) {
    this.id = id;
    this.userBedtimeStoryInfoRelationshipOwner = userBedtimeStoryInfoRelationshipOwner;
    this.userBedtimeStoryInfoRelationshipBedtimeStoryInfo = userBedtimeStoryInfoRelationshipBedtimeStoryInfo;
    this.numberOfTimesPlayed = numberOfTimesPlayed;
    this.totalPlayTime = totalPlayTime;
    this.continuePlayingTime = continuePlayingTime;
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
      UserBedtimeStoryInfoRelationship userBedtimeStoryInfoRelationship = (UserBedtimeStoryInfoRelationship) obj;
      return ObjectsCompat.equals(getId(), userBedtimeStoryInfoRelationship.getId()) &&
              ObjectsCompat.equals(getUserBedtimeStoryInfoRelationshipOwner(), userBedtimeStoryInfoRelationship.getUserBedtimeStoryInfoRelationshipOwner()) &&
              ObjectsCompat.equals(getUserBedtimeStoryInfoRelationshipBedtimeStoryInfo(), userBedtimeStoryInfoRelationship.getUserBedtimeStoryInfoRelationshipBedtimeStoryInfo()) &&
              ObjectsCompat.equals(getNumberOfTimesPlayed(), userBedtimeStoryInfoRelationship.getNumberOfTimesPlayed()) &&
              ObjectsCompat.equals(getTotalPlayTime(), userBedtimeStoryInfoRelationship.getTotalPlayTime()) &&
              ObjectsCompat.equals(getContinuePlayingTime(), userBedtimeStoryInfoRelationship.getContinuePlayingTime()) &&
              ObjectsCompat.equals(getCurrentlyListening(), userBedtimeStoryInfoRelationship.getCurrentlyListening()) &&
              ObjectsCompat.equals(getUsageTimestamps(), userBedtimeStoryInfoRelationship.getUsageTimestamps()) &&
              ObjectsCompat.equals(getUsagePlayTimes(), userBedtimeStoryInfoRelationship.getUsagePlayTimes()) &&
              ObjectsCompat.equals(getCreatedAt(), userBedtimeStoryInfoRelationship.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userBedtimeStoryInfoRelationship.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserBedtimeStoryInfoRelationshipOwner())
      .append(getUserBedtimeStoryInfoRelationshipBedtimeStoryInfo())
      .append(getNumberOfTimesPlayed())
      .append(getTotalPlayTime())
      .append(getContinuePlayingTime())
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
      .append("UserBedtimeStoryInfoRelationship {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userBedtimeStoryInfoRelationshipOwner=" + String.valueOf(getUserBedtimeStoryInfoRelationshipOwner()) + ", ")
      .append("userBedtimeStoryInfoRelationshipBedtimeStoryInfo=" + String.valueOf(getUserBedtimeStoryInfoRelationshipBedtimeStoryInfo()) + ", ")
      .append("numberOfTimesPlayed=" + String.valueOf(getNumberOfTimesPlayed()) + ", ")
      .append("totalPlayTime=" + String.valueOf(getTotalPlayTime()) + ", ")
      .append("continuePlayingTime=" + String.valueOf(getContinuePlayingTime()) + ", ")
      .append("currentlyListening=" + String.valueOf(getCurrentlyListening()) + ", ")
      .append("usageTimestamps=" + String.valueOf(getUsageTimestamps()) + ", ")
      .append("usagePlayTimes=" + String.valueOf(getUsagePlayTimes()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserBedtimeStoryInfoRelationshipOwnerStep builder() {
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
  public static UserBedtimeStoryInfoRelationship justId(String id) {
    return new UserBedtimeStoryInfoRelationship(
      id,
      null,
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
      userBedtimeStoryInfoRelationshipOwner,
      userBedtimeStoryInfoRelationshipBedtimeStoryInfo,
      numberOfTimesPlayed,
      totalPlayTime,
      continuePlayingTime,
      currentlyListening,
      usageTimestamps,
      usagePlayTimes);
  }
  public interface UserBedtimeStoryInfoRelationshipOwnerStep {
    UserBedtimeStoryInfoRelationshipBedtimeStoryInfoStep userBedtimeStoryInfoRelationshipOwner(UserData userBedtimeStoryInfoRelationshipOwner);
  }
  

  public interface UserBedtimeStoryInfoRelationshipBedtimeStoryInfoStep {
    BuildStep userBedtimeStoryInfoRelationshipBedtimeStoryInfo(BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo);
  }
  

  public interface BuildStep {
    UserBedtimeStoryInfoRelationship build();
    BuildStep id(String id);
    BuildStep numberOfTimesPlayed(Integer numberOfTimesPlayed);
    BuildStep totalPlayTime(Integer totalPlayTime);
    BuildStep continuePlayingTime(Integer continuePlayingTime);
    BuildStep currentlyListening(Boolean currentlyListening);
    BuildStep usageTimestamps(List<Temporal.DateTime> usageTimestamps);
    BuildStep usagePlayTimes(List<Integer> usagePlayTimes);
  }
  

  public static class Builder implements UserBedtimeStoryInfoRelationshipOwnerStep, UserBedtimeStoryInfoRelationshipBedtimeStoryInfoStep, BuildStep {
    private String id;
    private UserData userBedtimeStoryInfoRelationshipOwner;
    private BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo;
    private Integer numberOfTimesPlayed;
    private Integer totalPlayTime;
    private Integer continuePlayingTime;
    private Boolean currentlyListening;
    private List<Temporal.DateTime> usageTimestamps;
    private List<Integer> usagePlayTimes;
    @Override
     public UserBedtimeStoryInfoRelationship build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserBedtimeStoryInfoRelationship(
          id,
          userBedtimeStoryInfoRelationshipOwner,
          userBedtimeStoryInfoRelationshipBedtimeStoryInfo,
          numberOfTimesPlayed,
          totalPlayTime,
          continuePlayingTime,
          currentlyListening,
          usageTimestamps,
          usagePlayTimes);
    }
    
    @Override
     public UserBedtimeStoryInfoRelationshipBedtimeStoryInfoStep userBedtimeStoryInfoRelationshipOwner(UserData userBedtimeStoryInfoRelationshipOwner) {
        Objects.requireNonNull(userBedtimeStoryInfoRelationshipOwner);
        this.userBedtimeStoryInfoRelationshipOwner = userBedtimeStoryInfoRelationshipOwner;
        return this;
    }
    
    @Override
     public BuildStep userBedtimeStoryInfoRelationshipBedtimeStoryInfo(BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo) {
        Objects.requireNonNull(userBedtimeStoryInfoRelationshipBedtimeStoryInfo);
        this.userBedtimeStoryInfoRelationshipBedtimeStoryInfo = userBedtimeStoryInfoRelationshipBedtimeStoryInfo;
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
     public BuildStep continuePlayingTime(Integer continuePlayingTime) {
        this.continuePlayingTime = continuePlayingTime;
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
    private CopyOfBuilder(String id, UserData userBedtimeStoryInfoRelationshipOwner, BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo, Integer numberOfTimesPlayed, Integer totalPlayTime, Integer continuePlayingTime, Boolean currentlyListening, List<Temporal.DateTime> usageTimestamps, List<Integer> usagePlayTimes) {
      super.id(id);
      super.userBedtimeStoryInfoRelationshipOwner(userBedtimeStoryInfoRelationshipOwner)
        .userBedtimeStoryInfoRelationshipBedtimeStoryInfo(userBedtimeStoryInfoRelationshipBedtimeStoryInfo)
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .totalPlayTime(totalPlayTime)
        .continuePlayingTime(continuePlayingTime)
        .currentlyListening(currentlyListening)
        .usageTimestamps(usageTimestamps)
        .usagePlayTimes(usagePlayTimes);
    }
    
    @Override
     public CopyOfBuilder userBedtimeStoryInfoRelationshipOwner(UserData userBedtimeStoryInfoRelationshipOwner) {
      return (CopyOfBuilder) super.userBedtimeStoryInfoRelationshipOwner(userBedtimeStoryInfoRelationshipOwner);
    }
    
    @Override
     public CopyOfBuilder userBedtimeStoryInfoRelationshipBedtimeStoryInfo(BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo) {
      return (CopyOfBuilder) super.userBedtimeStoryInfoRelationshipBedtimeStoryInfo(userBedtimeStoryInfoRelationshipBedtimeStoryInfo);
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
     public CopyOfBuilder continuePlayingTime(Integer continuePlayingTime) {
      return (CopyOfBuilder) super.continuePlayingTime(continuePlayingTime);
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
