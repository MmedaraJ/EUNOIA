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

/** This is an auto generated class representing the UserRoutineRelationshipSelfLove type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRoutineRelationshipSelfLoves")
@Index(name = "byUserRoutineRelationship", fields = {"userRoutineRelationshipID"})
@Index(name = "bySelfLoveData", fields = {"selfLoveDataID"})
public final class UserRoutineRelationshipSelfLove implements Model {
  public static final QueryField ID = field("UserRoutineRelationshipSelfLove", "id");
  public static final QueryField USER_ROUTINE_RELATIONSHIP = field("UserRoutineRelationshipSelfLove", "userRoutineRelationshipID");
  public static final QueryField SELF_LOVE_DATA = field("UserRoutineRelationshipSelfLove", "selfLoveDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserRoutineRelationship", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipID", type = UserRoutineRelationship.class) UserRoutineRelationship userRoutineRelationship;
  private final @ModelField(targetType="SelfLoveData", isRequired = true) @BelongsTo(targetName = "selfLoveDataID", type = SelfLoveData.class) SelfLoveData selfLoveData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserRoutineRelationship getUserRoutineRelationship() {
      return userRoutineRelationship;
  }
  
  public SelfLoveData getSelfLoveData() {
      return selfLoveData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserRoutineRelationshipSelfLove(String id, UserRoutineRelationship userRoutineRelationship, SelfLoveData selfLoveData) {
    this.id = id;
    this.userRoutineRelationship = userRoutineRelationship;
    this.selfLoveData = selfLoveData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserRoutineRelationshipSelfLove userRoutineRelationshipSelfLove = (UserRoutineRelationshipSelfLove) obj;
      return ObjectsCompat.equals(getId(), userRoutineRelationshipSelfLove.getId()) &&
              ObjectsCompat.equals(getUserRoutineRelationship(), userRoutineRelationshipSelfLove.getUserRoutineRelationship()) &&
              ObjectsCompat.equals(getSelfLoveData(), userRoutineRelationshipSelfLove.getSelfLoveData()) &&
              ObjectsCompat.equals(getCreatedAt(), userRoutineRelationshipSelfLove.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRoutineRelationshipSelfLove.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserRoutineRelationship())
      .append(getSelfLoveData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserRoutineRelationshipSelfLove {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userRoutineRelationship=" + String.valueOf(getUserRoutineRelationship()) + ", ")
      .append("selfLoveData=" + String.valueOf(getSelfLoveData()) + ", ")
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
  public static UserRoutineRelationshipSelfLove justId(String id) {
    return new UserRoutineRelationshipSelfLove(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userRoutineRelationship,
      selfLoveData);
  }
  public interface UserRoutineRelationshipStep {
    SelfLoveDataStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship);
  }
  

  public interface SelfLoveDataStep {
    BuildStep selfLoveData(SelfLoveData selfLoveData);
  }
  

  public interface BuildStep {
    UserRoutineRelationshipSelfLove build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserRoutineRelationshipStep, SelfLoveDataStep, BuildStep {
    private String id;
    private UserRoutineRelationship userRoutineRelationship;
    private SelfLoveData selfLoveData;
    @Override
     public UserRoutineRelationshipSelfLove build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRoutineRelationshipSelfLove(
          id,
          userRoutineRelationship,
          selfLoveData);
    }
    
    @Override
     public SelfLoveDataStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
        Objects.requireNonNull(userRoutineRelationship);
        this.userRoutineRelationship = userRoutineRelationship;
        return this;
    }
    
    @Override
     public BuildStep selfLoveData(SelfLoveData selfLoveData) {
        Objects.requireNonNull(selfLoveData);
        this.selfLoveData = selfLoveData;
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
    private CopyOfBuilder(String id, UserRoutineRelationship userRoutineRelationship, SelfLoveData selfLoveData) {
      super.id(id);
      super.userRoutineRelationship(userRoutineRelationship)
        .selfLoveData(selfLoveData);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
      return (CopyOfBuilder) super.userRoutineRelationship(userRoutineRelationship);
    }
    
    @Override
     public CopyOfBuilder selfLoveData(SelfLoveData selfLoveData) {
      return (CopyOfBuilder) super.selfLoveData(selfLoveData);
    }
  }
  
}
