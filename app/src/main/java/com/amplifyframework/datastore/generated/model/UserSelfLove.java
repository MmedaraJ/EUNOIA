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

/** This is an auto generated class representing the UserSelfLove type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserSelfLoves")
@Index(name = "byUserData", fields = {"userDataID"})
@Index(name = "bySelfLoveData", fields = {"selfLoveDataID"})
public final class UserSelfLove implements Model {
  public static final QueryField ID = field("UserSelfLove", "id");
  public static final QueryField USER_DATA = field("UserSelfLove", "userDataID");
  public static final QueryField SELF_LOVE_DATA = field("UserSelfLove", "selfLoveDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private final @ModelField(targetType="SelfLoveData", isRequired = true) @BelongsTo(targetName = "selfLoveDataID", type = SelfLoveData.class) SelfLoveData selfLoveData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserData() {
      return userData;
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
  
  private UserSelfLove(String id, UserData userData, SelfLoveData selfLoveData) {
    this.id = id;
    this.userData = userData;
    this.selfLoveData = selfLoveData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserSelfLove userSelfLove = (UserSelfLove) obj;
      return ObjectsCompat.equals(getId(), userSelfLove.getId()) &&
              ObjectsCompat.equals(getUserData(), userSelfLove.getUserData()) &&
              ObjectsCompat.equals(getSelfLoveData(), userSelfLove.getSelfLoveData()) &&
              ObjectsCompat.equals(getCreatedAt(), userSelfLove.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userSelfLove.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserData())
      .append(getSelfLoveData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserSelfLove {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("selfLoveData=" + String.valueOf(getSelfLoveData()) + ", ")
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
  public static UserSelfLove justId(String id) {
    return new UserSelfLove(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userData,
      selfLoveData);
  }
  public interface UserDataStep {
    SelfLoveDataStep userData(UserData userData);
  }
  

  public interface SelfLoveDataStep {
    BuildStep selfLoveData(SelfLoveData selfLoveData);
  }
  

  public interface BuildStep {
    UserSelfLove build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserDataStep, SelfLoveDataStep, BuildStep {
    private String id;
    private UserData userData;
    private SelfLoveData selfLoveData;
    @Override
     public UserSelfLove build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserSelfLove(
          id,
          userData,
          selfLoveData);
    }
    
    @Override
     public SelfLoveDataStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
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
    private CopyOfBuilder(String id, UserData userData, SelfLoveData selfLoveData) {
      super.id(id);
      super.userData(userData)
        .selfLoveData(selfLoveData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
    
    @Override
     public CopyOfBuilder selfLoveData(SelfLoveData selfLoveData) {
      return (CopyOfBuilder) super.selfLoveData(selfLoveData);
    }
  }
  
}
