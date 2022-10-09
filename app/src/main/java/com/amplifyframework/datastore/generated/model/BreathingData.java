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

/** This is an auto generated class representing the BreathingData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "BreathingData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "BreathingsOwnedByUser", fields = {"userDataID","display_name"})
public final class BreathingData implements Model {
  public static final QueryField ID = field("BreathingData", "id");
  public static final QueryField BREATHING_OWNER = field("BreathingData", "userDataID");
  public static final QueryField BREATHING_OWNER_ID = field("BreathingData", "breathingOwnerId");
  public static final QueryField DISPLAY_NAME = field("BreathingData", "display_name");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData breathingOwner;
  private final @ModelField(targetType="String") String breathingOwnerId;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="RoutineBreathing") @HasMany(associatedWith = "breathingData", type = RoutineBreathing.class) List<RoutineBreathing> routines = null;
  private final @ModelField(targetType="UserRoutineRelationshipBreathing") @HasMany(associatedWith = "breathingData", type = UserRoutineRelationshipBreathing.class) List<UserRoutineRelationshipBreathing> userRoutineRelationships = null;
  private final @ModelField(targetType="UserBreathing") @HasMany(associatedWith = "breathingData", type = UserBreathing.class) List<UserBreathing> users = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getBreathingOwner() {
      return breathingOwner;
  }
  
  public String getBreathingOwnerId() {
      return breathingOwnerId;
  }
  
  public String getDisplayName() {
      return display_name;
  }
  
  public List<RoutineBreathing> getRoutines() {
      return routines;
  }
  
  public List<UserRoutineRelationshipBreathing> getUserRoutineRelationships() {
      return userRoutineRelationships;
  }
  
  public List<UserBreathing> getUsers() {
      return users;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private BreathingData(String id, UserData breathingOwner, String breathingOwnerId, String display_name) {
    this.id = id;
    this.breathingOwner = breathingOwner;
    this.breathingOwnerId = breathingOwnerId;
    this.display_name = display_name;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      BreathingData breathingData = (BreathingData) obj;
      return ObjectsCompat.equals(getId(), breathingData.getId()) &&
              ObjectsCompat.equals(getBreathingOwner(), breathingData.getBreathingOwner()) &&
              ObjectsCompat.equals(getBreathingOwnerId(), breathingData.getBreathingOwnerId()) &&
              ObjectsCompat.equals(getDisplayName(), breathingData.getDisplayName()) &&
              ObjectsCompat.equals(getCreatedAt(), breathingData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), breathingData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getBreathingOwner())
      .append(getBreathingOwnerId())
      .append(getDisplayName())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("BreathingData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("breathingOwner=" + String.valueOf(getBreathingOwner()) + ", ")
      .append("breathingOwnerId=" + String.valueOf(getBreathingOwnerId()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BreathingOwnerStep builder() {
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
  public static BreathingData justId(String id) {
    return new BreathingData(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      breathingOwner,
      breathingOwnerId,
      display_name);
  }
  public interface BreathingOwnerStep {
    DisplayNameStep breathingOwner(UserData breathingOwner);
  }
  

  public interface DisplayNameStep {
    BuildStep displayName(String displayName);
  }
  

  public interface BuildStep {
    BreathingData build();
    BuildStep id(String id);
    BuildStep breathingOwnerId(String breathingOwnerId);
  }
  

  public static class Builder implements BreathingOwnerStep, DisplayNameStep, BuildStep {
    private String id;
    private UserData breathingOwner;
    private String display_name;
    private String breathingOwnerId;
    @Override
     public BreathingData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new BreathingData(
          id,
          breathingOwner,
          breathingOwnerId,
          display_name);
    }
    
    @Override
     public DisplayNameStep breathingOwner(UserData breathingOwner) {
        Objects.requireNonNull(breathingOwner);
        this.breathingOwner = breathingOwner;
        return this;
    }
    
    @Override
     public BuildStep displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.display_name = displayName;
        return this;
    }
    
    @Override
     public BuildStep breathingOwnerId(String breathingOwnerId) {
        this.breathingOwnerId = breathingOwnerId;
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
    private CopyOfBuilder(String id, UserData breathingOwner, String breathingOwnerId, String displayName) {
      super.id(id);
      super.breathingOwner(breathingOwner)
        .displayName(displayName)
        .breathingOwnerId(breathingOwnerId);
    }
    
    @Override
     public CopyOfBuilder breathingOwner(UserData breathingOwner) {
      return (CopyOfBuilder) super.breathingOwner(breathingOwner);
    }
    
    @Override
     public CopyOfBuilder displayName(String displayName) {
      return (CopyOfBuilder) super.displayName(displayName);
    }
    
    @Override
     public CopyOfBuilder breathingOwnerId(String breathingOwnerId) {
      return (CopyOfBuilder) super.breathingOwnerId(breathingOwnerId);
    }
  }
  
}
