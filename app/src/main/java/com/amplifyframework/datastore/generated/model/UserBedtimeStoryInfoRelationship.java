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
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userBedtimeStoryInfoRelationshipUserDataID", type = UserData.class) UserData userBedtimeStoryInfoRelationshipOwner;
  private final @ModelField(targetType="BedtimeStoryInfoData", isRequired = true) @BelongsTo(targetName = "userBedtimeStoryInfoRelationshipBedtimeStoryInfoDataID", type = BedtimeStoryInfoData.class) BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo;
  private final @ModelField(targetType="Int") Integer numberOfTimesPlayed;
  private final @ModelField(targetType="Int") Integer totalPlayTime;
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
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserBedtimeStoryInfoRelationship(String id, UserData userBedtimeStoryInfoRelationshipOwner, BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo, Integer numberOfTimesPlayed, Integer totalPlayTime) {
    this.id = id;
    this.userBedtimeStoryInfoRelationshipOwner = userBedtimeStoryInfoRelationshipOwner;
    this.userBedtimeStoryInfoRelationshipBedtimeStoryInfo = userBedtimeStoryInfoRelationshipBedtimeStoryInfo;
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
      UserBedtimeStoryInfoRelationship userBedtimeStoryInfoRelationship = (UserBedtimeStoryInfoRelationship) obj;
      return ObjectsCompat.equals(getId(), userBedtimeStoryInfoRelationship.getId()) &&
              ObjectsCompat.equals(getUserBedtimeStoryInfoRelationshipOwner(), userBedtimeStoryInfoRelationship.getUserBedtimeStoryInfoRelationshipOwner()) &&
              ObjectsCompat.equals(getUserBedtimeStoryInfoRelationshipBedtimeStoryInfo(), userBedtimeStoryInfoRelationship.getUserBedtimeStoryInfoRelationshipBedtimeStoryInfo()) &&
              ObjectsCompat.equals(getNumberOfTimesPlayed(), userBedtimeStoryInfoRelationship.getNumberOfTimesPlayed()) &&
              ObjectsCompat.equals(getTotalPlayTime(), userBedtimeStoryInfoRelationship.getTotalPlayTime()) &&
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
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userBedtimeStoryInfoRelationshipOwner,
      userBedtimeStoryInfoRelationshipBedtimeStoryInfo,
      numberOfTimesPlayed,
      totalPlayTime);
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
  }
  

  public static class Builder implements UserBedtimeStoryInfoRelationshipOwnerStep, UserBedtimeStoryInfoRelationshipBedtimeStoryInfoStep, BuildStep {
    private String id;
    private UserData userBedtimeStoryInfoRelationshipOwner;
    private BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo;
    private Integer numberOfTimesPlayed;
    private Integer totalPlayTime;
    @Override
     public UserBedtimeStoryInfoRelationship build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserBedtimeStoryInfoRelationship(
          id,
          userBedtimeStoryInfoRelationshipOwner,
          userBedtimeStoryInfoRelationshipBedtimeStoryInfo,
          numberOfTimesPlayed,
          totalPlayTime);
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
    private CopyOfBuilder(String id, UserData userBedtimeStoryInfoRelationshipOwner, BedtimeStoryInfoData userBedtimeStoryInfoRelationshipBedtimeStoryInfo, Integer numberOfTimesPlayed, Integer totalPlayTime) {
      super.id(id);
      super.userBedtimeStoryInfoRelationshipOwner(userBedtimeStoryInfoRelationshipOwner)
        .userBedtimeStoryInfoRelationshipBedtimeStoryInfo(userBedtimeStoryInfoRelationshipBedtimeStoryInfo)
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .totalPlayTime(totalPlayTime);
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
  }
  
}
