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

/** This is an auto generated class representing the UserRoutineRelationshipStretch type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRoutineRelationshipStretches")
@Index(name = "byUserRoutineRelationship", fields = {"userRoutineRelationshipID"})
@Index(name = "byStretchData", fields = {"stretchDataID"})
public final class UserRoutineRelationshipStretch implements Model {
  public static final QueryField ID = field("UserRoutineRelationshipStretch", "id");
  public static final QueryField USER_ROUTINE_RELATIONSHIP = field("UserRoutineRelationshipStretch", "userRoutineRelationshipID");
  public static final QueryField STRETCH_DATA = field("UserRoutineRelationshipStretch", "stretchDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserRoutineRelationship", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipID", type = UserRoutineRelationship.class) UserRoutineRelationship userRoutineRelationship;
  private final @ModelField(targetType="StretchData", isRequired = true) @BelongsTo(targetName = "stretchDataID", type = StretchData.class) StretchData stretchData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserRoutineRelationship getUserRoutineRelationship() {
      return userRoutineRelationship;
  }
  
  public StretchData getStretchData() {
      return stretchData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserRoutineRelationshipStretch(String id, UserRoutineRelationship userRoutineRelationship, StretchData stretchData) {
    this.id = id;
    this.userRoutineRelationship = userRoutineRelationship;
    this.stretchData = stretchData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserRoutineRelationshipStretch userRoutineRelationshipStretch = (UserRoutineRelationshipStretch) obj;
      return ObjectsCompat.equals(getId(), userRoutineRelationshipStretch.getId()) &&
              ObjectsCompat.equals(getUserRoutineRelationship(), userRoutineRelationshipStretch.getUserRoutineRelationship()) &&
              ObjectsCompat.equals(getStretchData(), userRoutineRelationshipStretch.getStretchData()) &&
              ObjectsCompat.equals(getCreatedAt(), userRoutineRelationshipStretch.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRoutineRelationshipStretch.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserRoutineRelationship())
      .append(getStretchData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserRoutineRelationshipStretch {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userRoutineRelationship=" + String.valueOf(getUserRoutineRelationship()) + ", ")
      .append("stretchData=" + String.valueOf(getStretchData()) + ", ")
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
  public static UserRoutineRelationshipStretch justId(String id) {
    return new UserRoutineRelationshipStretch(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userRoutineRelationship,
      stretchData);
  }
  public interface UserRoutineRelationshipStep {
    StretchDataStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship);
  }
  

  public interface StretchDataStep {
    BuildStep stretchData(StretchData stretchData);
  }
  

  public interface BuildStep {
    UserRoutineRelationshipStretch build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserRoutineRelationshipStep, StretchDataStep, BuildStep {
    private String id;
    private UserRoutineRelationship userRoutineRelationship;
    private StretchData stretchData;
    @Override
     public UserRoutineRelationshipStretch build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRoutineRelationshipStretch(
          id,
          userRoutineRelationship,
          stretchData);
    }
    
    @Override
     public StretchDataStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
        Objects.requireNonNull(userRoutineRelationship);
        this.userRoutineRelationship = userRoutineRelationship;
        return this;
    }
    
    @Override
     public BuildStep stretchData(StretchData stretchData) {
        Objects.requireNonNull(stretchData);
        this.stretchData = stretchData;
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
    private CopyOfBuilder(String id, UserRoutineRelationship userRoutineRelationship, StretchData stretchData) {
      super.id(id);
      super.userRoutineRelationship(userRoutineRelationship)
        .stretchData(stretchData);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
      return (CopyOfBuilder) super.userRoutineRelationship(userRoutineRelationship);
    }
    
    @Override
     public CopyOfBuilder stretchData(StretchData stretchData) {
      return (CopyOfBuilder) super.stretchData(stretchData);
    }
  }
  
}
