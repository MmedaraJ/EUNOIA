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

/** This is an auto generated class representing the StretchData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "StretchData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "StretchesOwnedByUser", fields = {"userDataID","display_name"})
public final class StretchData implements Model {
  public static final QueryField ID = field("StretchData", "id");
  public static final QueryField STRETCH_OWNER = field("StretchData", "userDataID");
  public static final QueryField STRETCH_OWNER_ID = field("StretchData", "stretchOwnerId");
  public static final QueryField DISPLAY_NAME = field("StretchData", "display_name");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData stretchOwner;
  private final @ModelField(targetType="String") String stretchOwnerId;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="RoutineStretch") @HasMany(associatedWith = "stretchData", type = RoutineStretch.class) List<RoutineStretch> routines = null;
  private final @ModelField(targetType="UserRoutineRelationshipStretch") @HasMany(associatedWith = "stretchData", type = UserRoutineRelationshipStretch.class) List<UserRoutineRelationshipStretch> userRoutineRelationships = null;
  private final @ModelField(targetType="UserStretch") @HasMany(associatedWith = "stretchData", type = UserStretch.class) List<UserStretch> users = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getStretchOwner() {
      return stretchOwner;
  }
  
  public String getStretchOwnerId() {
      return stretchOwnerId;
  }
  
  public String getDisplayName() {
      return display_name;
  }
  
  public List<RoutineStretch> getRoutines() {
      return routines;
  }
  
  public List<UserRoutineRelationshipStretch> getUserRoutineRelationships() {
      return userRoutineRelationships;
  }
  
  public List<UserStretch> getUsers() {
      return users;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private StretchData(String id, UserData stretchOwner, String stretchOwnerId, String display_name) {
    this.id = id;
    this.stretchOwner = stretchOwner;
    this.stretchOwnerId = stretchOwnerId;
    this.display_name = display_name;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      StretchData stretchData = (StretchData) obj;
      return ObjectsCompat.equals(getId(), stretchData.getId()) &&
              ObjectsCompat.equals(getStretchOwner(), stretchData.getStretchOwner()) &&
              ObjectsCompat.equals(getStretchOwnerId(), stretchData.getStretchOwnerId()) &&
              ObjectsCompat.equals(getDisplayName(), stretchData.getDisplayName()) &&
              ObjectsCompat.equals(getCreatedAt(), stretchData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), stretchData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getStretchOwner())
      .append(getStretchOwnerId())
      .append(getDisplayName())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("StretchData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("stretchOwner=" + String.valueOf(getStretchOwner()) + ", ")
      .append("stretchOwnerId=" + String.valueOf(getStretchOwnerId()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static StretchOwnerStep builder() {
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
  public static StretchData justId(String id) {
    return new StretchData(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      stretchOwner,
      stretchOwnerId,
      display_name);
  }
  public interface StretchOwnerStep {
    DisplayNameStep stretchOwner(UserData stretchOwner);
  }
  

  public interface DisplayNameStep {
    BuildStep displayName(String displayName);
  }
  

  public interface BuildStep {
    StretchData build();
    BuildStep id(String id);
    BuildStep stretchOwnerId(String stretchOwnerId);
  }
  

  public static class Builder implements StretchOwnerStep, DisplayNameStep, BuildStep {
    private String id;
    private UserData stretchOwner;
    private String display_name;
    private String stretchOwnerId;
    @Override
     public StretchData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new StretchData(
          id,
          stretchOwner,
          stretchOwnerId,
          display_name);
    }
    
    @Override
     public DisplayNameStep stretchOwner(UserData stretchOwner) {
        Objects.requireNonNull(stretchOwner);
        this.stretchOwner = stretchOwner;
        return this;
    }
    
    @Override
     public BuildStep displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.display_name = displayName;
        return this;
    }
    
    @Override
     public BuildStep stretchOwnerId(String stretchOwnerId) {
        this.stretchOwnerId = stretchOwnerId;
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
    private CopyOfBuilder(String id, UserData stretchOwner, String stretchOwnerId, String displayName) {
      super.id(id);
      super.stretchOwner(stretchOwner)
        .displayName(displayName)
        .stretchOwnerId(stretchOwnerId);
    }
    
    @Override
     public CopyOfBuilder stretchOwner(UserData stretchOwner) {
      return (CopyOfBuilder) super.stretchOwner(stretchOwner);
    }
    
    @Override
     public CopyOfBuilder displayName(String displayName) {
      return (CopyOfBuilder) super.displayName(displayName);
    }
    
    @Override
     public CopyOfBuilder stretchOwnerId(String stretchOwnerId) {
      return (CopyOfBuilder) super.stretchOwnerId(stretchOwnerId);
    }
  }
  
}
