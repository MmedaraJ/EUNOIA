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

/** This is an auto generated class representing the UserSelfLoveRelationship type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserSelfLoveRelationships", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "UserSelfLoveRelationshipsOwnedByUser", fields = {"userSelfLoveRelationshipUserDataID","id"})
@Index(name = "UserSelfLoveRelationshipsOwnedBySelfLove", fields = {"userSelfLoveRelationshipSelfLoveDataID","id"})
public final class UserSelfLoveRelationship implements Model {
  public static final QueryField ID = field("UserSelfLoveRelationship", "id");
  public static final QueryField USER_SELF_LOVE_RELATIONSHIP_OWNER = field("UserSelfLoveRelationship", "userSelfLoveRelationshipUserDataID");
  public static final QueryField USER_SELF_LOVE_RELATIONSHIP_SELF_LOVE = field("UserSelfLoveRelationship", "userSelfLoveRelationshipSelfLoveDataID");
  public static final QueryField NUMBER_OF_TIMES_PLAYED = field("UserSelfLoveRelationship", "numberOfTimesPlayed");
  public static final QueryField TOTAL_PLAY_TIME = field("UserSelfLoveRelationship", "totalPlayTime");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userSelfLoveRelationshipUserDataID", type = UserData.class) UserData userSelfLoveRelationshipOwner;
  private final @ModelField(targetType="SelfLoveData", isRequired = true) @BelongsTo(targetName = "userSelfLoveRelationshipSelfLoveDataID", type = SelfLoveData.class) SelfLoveData userSelfLoveRelationshipSelfLove;
  private final @ModelField(targetType="Int") Integer numberOfTimesPlayed;
  private final @ModelField(targetType="Int") Integer totalPlayTime;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserSelfLoveRelationshipOwner() {
      return userSelfLoveRelationshipOwner;
  }
  
  public SelfLoveData getUserSelfLoveRelationshipSelfLove() {
      return userSelfLoveRelationshipSelfLove;
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
  
  private UserSelfLoveRelationship(String id, UserData userSelfLoveRelationshipOwner, SelfLoveData userSelfLoveRelationshipSelfLove, Integer numberOfTimesPlayed, Integer totalPlayTime) {
    this.id = id;
    this.userSelfLoveRelationshipOwner = userSelfLoveRelationshipOwner;
    this.userSelfLoveRelationshipSelfLove = userSelfLoveRelationshipSelfLove;
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
      UserSelfLoveRelationship userSelfLoveRelationship = (UserSelfLoveRelationship) obj;
      return ObjectsCompat.equals(getId(), userSelfLoveRelationship.getId()) &&
              ObjectsCompat.equals(getUserSelfLoveRelationshipOwner(), userSelfLoveRelationship.getUserSelfLoveRelationshipOwner()) &&
              ObjectsCompat.equals(getUserSelfLoveRelationshipSelfLove(), userSelfLoveRelationship.getUserSelfLoveRelationshipSelfLove()) &&
              ObjectsCompat.equals(getNumberOfTimesPlayed(), userSelfLoveRelationship.getNumberOfTimesPlayed()) &&
              ObjectsCompat.equals(getTotalPlayTime(), userSelfLoveRelationship.getTotalPlayTime()) &&
              ObjectsCompat.equals(getCreatedAt(), userSelfLoveRelationship.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userSelfLoveRelationship.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserSelfLoveRelationshipOwner())
      .append(getUserSelfLoveRelationshipSelfLove())
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
      .append("UserSelfLoveRelationship {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userSelfLoveRelationshipOwner=" + String.valueOf(getUserSelfLoveRelationshipOwner()) + ", ")
      .append("userSelfLoveRelationshipSelfLove=" + String.valueOf(getUserSelfLoveRelationshipSelfLove()) + ", ")
      .append("numberOfTimesPlayed=" + String.valueOf(getNumberOfTimesPlayed()) + ", ")
      .append("totalPlayTime=" + String.valueOf(getTotalPlayTime()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserSelfLoveRelationshipOwnerStep builder() {
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
  public static UserSelfLoveRelationship justId(String id) {
    return new UserSelfLoveRelationship(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userSelfLoveRelationshipOwner,
      userSelfLoveRelationshipSelfLove,
      numberOfTimesPlayed,
      totalPlayTime);
  }
  public interface UserSelfLoveRelationshipOwnerStep {
    UserSelfLoveRelationshipSelfLoveStep userSelfLoveRelationshipOwner(UserData userSelfLoveRelationshipOwner);
  }
  

  public interface UserSelfLoveRelationshipSelfLoveStep {
    BuildStep userSelfLoveRelationshipSelfLove(SelfLoveData userSelfLoveRelationshipSelfLove);
  }
  

  public interface BuildStep {
    UserSelfLoveRelationship build();
    BuildStep id(String id);
    BuildStep numberOfTimesPlayed(Integer numberOfTimesPlayed);
    BuildStep totalPlayTime(Integer totalPlayTime);
  }
  

  public static class Builder implements UserSelfLoveRelationshipOwnerStep, UserSelfLoveRelationshipSelfLoveStep, BuildStep {
    private String id;
    private UserData userSelfLoveRelationshipOwner;
    private SelfLoveData userSelfLoveRelationshipSelfLove;
    private Integer numberOfTimesPlayed;
    private Integer totalPlayTime;
    @Override
     public UserSelfLoveRelationship build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserSelfLoveRelationship(
          id,
          userSelfLoveRelationshipOwner,
          userSelfLoveRelationshipSelfLove,
          numberOfTimesPlayed,
          totalPlayTime);
    }
    
    @Override
     public UserSelfLoveRelationshipSelfLoveStep userSelfLoveRelationshipOwner(UserData userSelfLoveRelationshipOwner) {
        Objects.requireNonNull(userSelfLoveRelationshipOwner);
        this.userSelfLoveRelationshipOwner = userSelfLoveRelationshipOwner;
        return this;
    }
    
    @Override
     public BuildStep userSelfLoveRelationshipSelfLove(SelfLoveData userSelfLoveRelationshipSelfLove) {
        Objects.requireNonNull(userSelfLoveRelationshipSelfLove);
        this.userSelfLoveRelationshipSelfLove = userSelfLoveRelationshipSelfLove;
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
    private CopyOfBuilder(String id, UserData userSelfLoveRelationshipOwner, SelfLoveData userSelfLoveRelationshipSelfLove, Integer numberOfTimesPlayed, Integer totalPlayTime) {
      super.id(id);
      super.userSelfLoveRelationshipOwner(userSelfLoveRelationshipOwner)
        .userSelfLoveRelationshipSelfLove(userSelfLoveRelationshipSelfLove)
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .totalPlayTime(totalPlayTime);
    }
    
    @Override
     public CopyOfBuilder userSelfLoveRelationshipOwner(UserData userSelfLoveRelationshipOwner) {
      return (CopyOfBuilder) super.userSelfLoveRelationshipOwner(userSelfLoveRelationshipOwner);
    }
    
    @Override
     public CopyOfBuilder userSelfLoveRelationshipSelfLove(SelfLoveData userSelfLoveRelationshipSelfLove) {
      return (CopyOfBuilder) super.userSelfLoveRelationshipSelfLove(userSelfLoveRelationshipSelfLove);
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