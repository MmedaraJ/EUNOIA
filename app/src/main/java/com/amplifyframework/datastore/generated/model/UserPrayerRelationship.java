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

/** This is an auto generated class representing the UserPrayerRelationship type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserPrayerRelationships", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "UserPrayerRelationshipsOwnedByUser", fields = {"userPrayerRelationshipUserDataID","id"})
@Index(name = "UserPrayerRelationshipsOwnedByPrayer", fields = {"userPrayerRelationshipPrayerDataID","id"})
public final class UserPrayerRelationship implements Model {
  public static final QueryField ID = field("UserPrayerRelationship", "id");
  public static final QueryField USER_PRAYER_RELATIONSHIP_OWNER = field("UserPrayerRelationship", "userPrayerRelationshipUserDataID");
  public static final QueryField USER_PRAYER_RELATIONSHIP_PRAYER = field("UserPrayerRelationship", "userPrayerRelationshipPrayerDataID");
  public static final QueryField NUMBER_OF_TIMES_PLAYED = field("UserPrayerRelationship", "numberOfTimesPlayed");
  public static final QueryField TOTAL_PLAY_TIME = field("UserPrayerRelationship", "totalPlayTime");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userPrayerRelationshipUserDataID", type = UserData.class) UserData userPrayerRelationshipOwner;
  private final @ModelField(targetType="PrayerData", isRequired = true) @BelongsTo(targetName = "userPrayerRelationshipPrayerDataID", type = PrayerData.class) PrayerData userPrayerRelationshipPrayer;
  private final @ModelField(targetType="Int") Integer numberOfTimesPlayed;
  private final @ModelField(targetType="Int") Integer totalPlayTime;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserPrayerRelationshipOwner() {
      return userPrayerRelationshipOwner;
  }
  
  public PrayerData getUserPrayerRelationshipPrayer() {
      return userPrayerRelationshipPrayer;
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
  
  private UserPrayerRelationship(String id, UserData userPrayerRelationshipOwner, PrayerData userPrayerRelationshipPrayer, Integer numberOfTimesPlayed, Integer totalPlayTime) {
    this.id = id;
    this.userPrayerRelationshipOwner = userPrayerRelationshipOwner;
    this.userPrayerRelationshipPrayer = userPrayerRelationshipPrayer;
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
      UserPrayerRelationship userPrayerRelationship = (UserPrayerRelationship) obj;
      return ObjectsCompat.equals(getId(), userPrayerRelationship.getId()) &&
              ObjectsCompat.equals(getUserPrayerRelationshipOwner(), userPrayerRelationship.getUserPrayerRelationshipOwner()) &&
              ObjectsCompat.equals(getUserPrayerRelationshipPrayer(), userPrayerRelationship.getUserPrayerRelationshipPrayer()) &&
              ObjectsCompat.equals(getNumberOfTimesPlayed(), userPrayerRelationship.getNumberOfTimesPlayed()) &&
              ObjectsCompat.equals(getTotalPlayTime(), userPrayerRelationship.getTotalPlayTime()) &&
              ObjectsCompat.equals(getCreatedAt(), userPrayerRelationship.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userPrayerRelationship.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserPrayerRelationshipOwner())
      .append(getUserPrayerRelationshipPrayer())
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
      .append("UserPrayerRelationship {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userPrayerRelationshipOwner=" + String.valueOf(getUserPrayerRelationshipOwner()) + ", ")
      .append("userPrayerRelationshipPrayer=" + String.valueOf(getUserPrayerRelationshipPrayer()) + ", ")
      .append("numberOfTimesPlayed=" + String.valueOf(getNumberOfTimesPlayed()) + ", ")
      .append("totalPlayTime=" + String.valueOf(getTotalPlayTime()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserPrayerRelationshipOwnerStep builder() {
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
  public static UserPrayerRelationship justId(String id) {
    return new UserPrayerRelationship(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userPrayerRelationshipOwner,
      userPrayerRelationshipPrayer,
      numberOfTimesPlayed,
      totalPlayTime);
  }
  public interface UserPrayerRelationshipOwnerStep {
    UserPrayerRelationshipPrayerStep userPrayerRelationshipOwner(UserData userPrayerRelationshipOwner);
  }
  

  public interface UserPrayerRelationshipPrayerStep {
    BuildStep userPrayerRelationshipPrayer(PrayerData userPrayerRelationshipPrayer);
  }
  

  public interface BuildStep {
    UserPrayerRelationship build();
    BuildStep id(String id);
    BuildStep numberOfTimesPlayed(Integer numberOfTimesPlayed);
    BuildStep totalPlayTime(Integer totalPlayTime);
  }
  

  public static class Builder implements UserPrayerRelationshipOwnerStep, UserPrayerRelationshipPrayerStep, BuildStep {
    private String id;
    private UserData userPrayerRelationshipOwner;
    private PrayerData userPrayerRelationshipPrayer;
    private Integer numberOfTimesPlayed;
    private Integer totalPlayTime;
    @Override
     public UserPrayerRelationship build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserPrayerRelationship(
          id,
          userPrayerRelationshipOwner,
          userPrayerRelationshipPrayer,
          numberOfTimesPlayed,
          totalPlayTime);
    }
    
    @Override
     public UserPrayerRelationshipPrayerStep userPrayerRelationshipOwner(UserData userPrayerRelationshipOwner) {
        Objects.requireNonNull(userPrayerRelationshipOwner);
        this.userPrayerRelationshipOwner = userPrayerRelationshipOwner;
        return this;
    }
    
    @Override
     public BuildStep userPrayerRelationshipPrayer(PrayerData userPrayerRelationshipPrayer) {
        Objects.requireNonNull(userPrayerRelationshipPrayer);
        this.userPrayerRelationshipPrayer = userPrayerRelationshipPrayer;
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
    private CopyOfBuilder(String id, UserData userPrayerRelationshipOwner, PrayerData userPrayerRelationshipPrayer, Integer numberOfTimesPlayed, Integer totalPlayTime) {
      super.id(id);
      super.userPrayerRelationshipOwner(userPrayerRelationshipOwner)
        .userPrayerRelationshipPrayer(userPrayerRelationshipPrayer)
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .totalPlayTime(totalPlayTime);
    }
    
    @Override
     public CopyOfBuilder userPrayerRelationshipOwner(UserData userPrayerRelationshipOwner) {
      return (CopyOfBuilder) super.userPrayerRelationshipOwner(userPrayerRelationshipOwner);
    }
    
    @Override
     public CopyOfBuilder userPrayerRelationshipPrayer(PrayerData userPrayerRelationshipPrayer) {
      return (CopyOfBuilder) super.userPrayerRelationshipPrayer(userPrayerRelationshipPrayer);
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
