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

/** This is an auto generated class representing the UserRoutineModel type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRoutines")
@Index(name = "byUserData", fields = {"userDataID"})
@Index(name = "byRoutineData", fields = {"routineDataID"})
public final class UserRoutine implements Model {
  public static final QueryField ID = field("UserRoutineModel", "id");
  public static final QueryField USER_DATA = field("UserRoutineModel", "userDataID");
  public static final QueryField ROUTINE_DATA = field("UserRoutineModel", "routineDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserData() {
      return userData;
  }
  
  public RoutineData getRoutineData() {
      return routineData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserRoutine(String id, UserData userData, RoutineData routineData) {
    this.id = id;
    this.userData = userData;
    this.routineData = routineData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserRoutine userRoutine = (UserRoutine) obj;
      return ObjectsCompat.equals(getId(), userRoutine.getId()) &&
              ObjectsCompat.equals(getUserData(), userRoutine.getUserData()) &&
              ObjectsCompat.equals(getRoutineData(), userRoutine.getRoutineData()) &&
              ObjectsCompat.equals(getCreatedAt(), userRoutine.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRoutine.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserData())
      .append(getRoutineData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserRoutineModel {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserDataStep builder() {
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
  public static UserRoutine justId(String id) {
    return new UserRoutine(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userData,
      routineData);
  }
  public interface UserDataStep {
    RoutineDataStep userData(UserData userData);
  }
  

  public interface RoutineDataStep {
    BuildStep routineData(RoutineData routineData);
  }
  

  public interface BuildStep {
    UserRoutine build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserDataStep, RoutineDataStep, BuildStep {
    private String id;
    private UserData userData;
    private RoutineData routineData;
    @Override
     public UserRoutine build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRoutine(
          id,
          userData,
          routineData);
    }
    
    @Override
     public RoutineDataStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
        return this;
    }
    
    @Override
     public BuildStep routineData(RoutineData routineData) {
        Objects.requireNonNull(routineData);
        this.routineData = routineData;
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
    private CopyOfBuilder(String id, UserData userData, RoutineData routineData) {
      super.id(id);
      super.userData(userData)
        .routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
  }
  
}
