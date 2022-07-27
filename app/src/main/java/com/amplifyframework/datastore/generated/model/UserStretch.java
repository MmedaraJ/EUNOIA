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

/** This is an auto generated class representing the UserStretchModel type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserStretches")
@Index(name = "byUserData", fields = {"userDataID"})
@Index(name = "byStretchData", fields = {"stretchDataID"})
public final class UserStretch implements Model {
  public static final QueryField ID = field("UserStretchModel", "id");
  public static final QueryField USER_DATA = field("UserStretchModel", "userDataID");
  public static final QueryField STRETCH_DATA = field("UserStretchModel", "stretchDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private final @ModelField(targetType="StretchData", isRequired = true) @BelongsTo(targetName = "stretchDataID", type = StretchData.class) StretchData stretchData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserData() {
      return userData;
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
  
  private UserStretch(String id, UserData userData, StretchData stretchData) {
    this.id = id;
    this.userData = userData;
    this.stretchData = stretchData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserStretch userStretch = (UserStretch) obj;
      return ObjectsCompat.equals(getId(), userStretch.getId()) &&
              ObjectsCompat.equals(getUserData(), userStretch.getUserData()) &&
              ObjectsCompat.equals(getStretchData(), userStretch.getStretchData()) &&
              ObjectsCompat.equals(getCreatedAt(), userStretch.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userStretch.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserData())
      .append(getStretchData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserStretchModel {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("stretchData=" + String.valueOf(getStretchData()) + ", ")
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
  public static UserStretch justId(String id) {
    return new UserStretch(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userData,
      stretchData);
  }
  public interface UserDataStep {
    StretchDataStep userData(UserData userData);
  }
  

  public interface StretchDataStep {
    BuildStep stretchData(StretchData stretchData);
  }
  

  public interface BuildStep {
    UserStretch build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserDataStep, StretchDataStep, BuildStep {
    private String id;
    private UserData userData;
    private StretchData stretchData;
    @Override
     public UserStretch build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserStretch(
          id,
          userData,
          stretchData);
    }
    
    @Override
     public StretchDataStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
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
    private CopyOfBuilder(String id, UserData userData, StretchData stretchData) {
      super.id(id);
      super.userData(userData)
        .stretchData(stretchData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
    
    @Override
     public CopyOfBuilder stretchData(StretchData stretchData) {
      return (CopyOfBuilder) super.stretchData(stretchData);
    }
  }
  
}
