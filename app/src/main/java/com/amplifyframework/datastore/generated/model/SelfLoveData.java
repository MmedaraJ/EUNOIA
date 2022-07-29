package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.annotations.HasMany;
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

/** This is an auto generated class representing the SelfLoveData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "SelfLoveData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "SelfLovesOwnedByUser", fields = {"userDataID","display_name"})
public final class SelfLoveData implements Model {
  public static final QueryField ID = field("SelfLoveData", "id");
  public static final QueryField SELF_LOVE_OWNER = field("SelfLoveData", "userDataID");
  public static final QueryField DISPLAY_NAME = field("SelfLoveData", "display_name");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData selfLoveOwner;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="RoutineSelfLove") @HasMany(associatedWith = "selfLoveData", type = RoutineSelfLove.class) List<RoutineSelfLove> routines = null;
  private final @ModelField(targetType="UserSelfLove") @HasMany(associatedWith = "selfLoveData", type = UserSelfLove.class) List<UserSelfLove> users = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getSelfLoveOwner() {
      return selfLoveOwner;
  }
  
  public String getDisplayName() {
      return display_name;
  }
  
  public List<RoutineSelfLove> getRoutines() {
      return routines;
  }
  
  public List<UserSelfLove> getUsers() {
      return users;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private SelfLoveData(String id, UserData selfLoveOwner, String display_name) {
    this.id = id;
    this.selfLoveOwner = selfLoveOwner;
    this.display_name = display_name;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      SelfLoveData selfLoveData = (SelfLoveData) obj;
      return ObjectsCompat.equals(getId(), selfLoveData.getId()) &&
              ObjectsCompat.equals(getSelfLoveOwner(), selfLoveData.getSelfLoveOwner()) &&
              ObjectsCompat.equals(getDisplayName(), selfLoveData.getDisplayName()) &&
              ObjectsCompat.equals(getCreatedAt(), selfLoveData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), selfLoveData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getSelfLoveOwner())
      .append(getDisplayName())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("SelfLoveData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("selfLoveOwner=" + String.valueOf(getSelfLoveOwner()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static SelfLoveOwnerStep builder() {
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
  public static SelfLoveData justId(String id) {
    return new SelfLoveData(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      selfLoveOwner,
      display_name);
  }
  public interface SelfLoveOwnerStep {
    DisplayNameStep selfLoveOwner(UserData selfLoveOwner);
  }
  

  public interface DisplayNameStep {
    BuildStep displayName(String displayName);
  }
  

  public interface BuildStep {
    SelfLoveData build();
    BuildStep id(String id);
  }
  

  public static class Builder implements SelfLoveOwnerStep, DisplayNameStep, BuildStep {
    private String id;
    private UserData selfLoveOwner;
    private String display_name;
    @Override
     public SelfLoveData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new SelfLoveData(
          id,
          selfLoveOwner,
          display_name);
    }
    
    @Override
     public DisplayNameStep selfLoveOwner(UserData selfLoveOwner) {
        Objects.requireNonNull(selfLoveOwner);
        this.selfLoveOwner = selfLoveOwner;
        return this;
    }
    
    @Override
     public BuildStep displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.display_name = displayName;
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
    private CopyOfBuilder(String id, UserData selfLoveOwner, String displayName) {
      super.id(id);
      super.selfLoveOwner(selfLoveOwner)
        .displayName(displayName);
    }
    
    @Override
     public CopyOfBuilder selfLoveOwner(UserData selfLoveOwner) {
      return (CopyOfBuilder) super.selfLoveOwner(selfLoveOwner);
    }
    
    @Override
     public CopyOfBuilder displayName(String displayName) {
      return (CopyOfBuilder) super.displayName(displayName);
    }
  }
  
}
