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

/** This is an auto generated class representing the UserRoutineRelationship type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRoutineRelationships", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "UserRoutineRelationshipsOwnedByUser", fields = {"userRoutineRelationshipUserDataID","id"})
@Index(name = "UserRoutineRelationshipsOwnedByRoutine", fields = {"userRoutineRelationshipRoutineDataID","id"})
public final class UserRoutineRelationship implements Model {
  public static final QueryField ID = field("UserRoutineRelationship", "id");
  public static final QueryField USER_ROUTINE_RELATIONSHIP_OWNER = field("UserRoutineRelationship", "userRoutineRelationshipUserDataID");
  public static final QueryField USER_ROUTINE_RELATIONSHIP_ROUTINE = field("UserRoutineRelationship", "userRoutineRelationshipRoutineDataID");
  public static final QueryField NUMBER_OF_TIMES_PLAYED = field("UserRoutineRelationship", "numberOfTimesPlayed");
  public static final QueryField TOTAL_PLAY_TIME = field("UserRoutineRelationship", "totalPlayTime");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipUserDataID", type = UserData.class) UserData userRoutineRelationshipOwner;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipRoutineDataID", type = RoutineData.class) RoutineData userRoutineRelationshipRoutine;
  private final @ModelField(targetType="Int") Integer numberOfTimesPlayed;
  private final @ModelField(targetType="Int") Integer totalPlayTime;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserRoutineRelationshipOwner() {
      return userRoutineRelationshipOwner;
  }
  
  public RoutineData getUserRoutineRelationshipRoutine() {
      return userRoutineRelationshipRoutine;
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
  
  private UserRoutineRelationship(String id, UserData userRoutineRelationshipOwner, RoutineData userRoutineRelationshipRoutine, Integer numberOfTimesPlayed, Integer totalPlayTime) {
    this.id = id;
    this.userRoutineRelationshipOwner = userRoutineRelationshipOwner;
    this.userRoutineRelationshipRoutine = userRoutineRelationshipRoutine;
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
      UserRoutineRelationship userRoutineRelationship = (UserRoutineRelationship) obj;
      return ObjectsCompat.equals(getId(), userRoutineRelationship.getId()) &&
              ObjectsCompat.equals(getUserRoutineRelationshipOwner(), userRoutineRelationship.getUserRoutineRelationshipOwner()) &&
              ObjectsCompat.equals(getUserRoutineRelationshipRoutine(), userRoutineRelationship.getUserRoutineRelationshipRoutine()) &&
              ObjectsCompat.equals(getNumberOfTimesPlayed(), userRoutineRelationship.getNumberOfTimesPlayed()) &&
              ObjectsCompat.equals(getTotalPlayTime(), userRoutineRelationship.getTotalPlayTime()) &&
              ObjectsCompat.equals(getCreatedAt(), userRoutineRelationship.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRoutineRelationship.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserRoutineRelationshipOwner())
      .append(getUserRoutineRelationshipRoutine())
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
      .append("UserRoutineRelationship {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userRoutineRelationshipOwner=" + String.valueOf(getUserRoutineRelationshipOwner()) + ", ")
      .append("userRoutineRelationshipRoutine=" + String.valueOf(getUserRoutineRelationshipRoutine()) + ", ")
      .append("numberOfTimesPlayed=" + String.valueOf(getNumberOfTimesPlayed()) + ", ")
      .append("totalPlayTime=" + String.valueOf(getTotalPlayTime()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserRoutineRelationshipOwnerStep builder() {
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
  public static UserRoutineRelationship justId(String id) {
    return new UserRoutineRelationship(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userRoutineRelationshipOwner,
      userRoutineRelationshipRoutine,
      numberOfTimesPlayed,
      totalPlayTime);
  }
  public interface UserRoutineRelationshipOwnerStep {
    UserRoutineRelationshipRoutineStep userRoutineRelationshipOwner(UserData userRoutineRelationshipOwner);
  }
  

  public interface UserRoutineRelationshipRoutineStep {
    BuildStep userRoutineRelationshipRoutine(RoutineData userRoutineRelationshipRoutine);
  }
  

  public interface BuildStep {
    UserRoutineRelationship build();
    BuildStep id(String id);
    BuildStep numberOfTimesPlayed(Integer numberOfTimesPlayed);
    BuildStep totalPlayTime(Integer totalPlayTime);
  }
  

  public static class Builder implements UserRoutineRelationshipOwnerStep, UserRoutineRelationshipRoutineStep, BuildStep {
    private String id;
    private UserData userRoutineRelationshipOwner;
    private RoutineData userRoutineRelationshipRoutine;
    private Integer numberOfTimesPlayed;
    private Integer totalPlayTime;
    @Override
     public UserRoutineRelationship build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRoutineRelationship(
          id,
          userRoutineRelationshipOwner,
          userRoutineRelationshipRoutine,
          numberOfTimesPlayed,
          totalPlayTime);
    }
    
    @Override
     public UserRoutineRelationshipRoutineStep userRoutineRelationshipOwner(UserData userRoutineRelationshipOwner) {
        Objects.requireNonNull(userRoutineRelationshipOwner);
        this.userRoutineRelationshipOwner = userRoutineRelationshipOwner;
        return this;
    }
    
    @Override
     public BuildStep userRoutineRelationshipRoutine(RoutineData userRoutineRelationshipRoutine) {
        Objects.requireNonNull(userRoutineRelationshipRoutine);
        this.userRoutineRelationshipRoutine = userRoutineRelationshipRoutine;
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
    private CopyOfBuilder(String id, UserData userRoutineRelationshipOwner, RoutineData userRoutineRelationshipRoutine, Integer numberOfTimesPlayed, Integer totalPlayTime) {
      super.id(id);
      super.userRoutineRelationshipOwner(userRoutineRelationshipOwner)
        .userRoutineRelationshipRoutine(userRoutineRelationshipRoutine)
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .totalPlayTime(totalPlayTime);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationshipOwner(UserData userRoutineRelationshipOwner) {
      return (CopyOfBuilder) super.userRoutineRelationshipOwner(userRoutineRelationshipOwner);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationshipRoutine(RoutineData userRoutineRelationshipRoutine) {
      return (CopyOfBuilder) super.userRoutineRelationshipRoutine(userRoutineRelationshipRoutine);
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
