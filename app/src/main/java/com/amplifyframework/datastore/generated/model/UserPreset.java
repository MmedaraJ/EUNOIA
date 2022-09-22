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

/** This is an auto generated class representing the UserPreset type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserPresets")
@Index(name = "byUserData", fields = {"userDataID"})
@Index(name = "byPresetData", fields = {"presetDataID"})
public final class UserPreset implements Model {
  public static final QueryField ID = field("UserPreset", "id");
  public static final QueryField USER_DATA = field("UserPreset", "userDataID");
  public static final QueryField PRESET_DATA = field("UserPreset", "presetDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private final @ModelField(targetType="PresetData", isRequired = true) @BelongsTo(targetName = "presetDataID", type = PresetData.class) PresetData presetData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserData() {
      return userData;
  }
  
  public PresetData getPresetData() {
      return presetData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserPreset(String id, UserData userData, PresetData presetData) {
    this.id = id;
    this.userData = userData;
    this.presetData = presetData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserPreset userPreset = (UserPreset) obj;
      return ObjectsCompat.equals(getId(), userPreset.getId()) &&
              ObjectsCompat.equals(getUserData(), userPreset.getUserData()) &&
              ObjectsCompat.equals(getPresetData(), userPreset.getPresetData()) &&
              ObjectsCompat.equals(getCreatedAt(), userPreset.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userPreset.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserData())
      .append(getPresetData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserPreset {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("presetData=" + String.valueOf(getPresetData()) + ", ")
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
  public static UserPreset justId(String id) {
    return new UserPreset(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userData,
      presetData);
  }
  public interface UserDataStep {
    PresetDataStep userData(UserData userData);
  }
  

  public interface PresetDataStep {
    BuildStep presetData(PresetData presetData);
  }
  

  public interface BuildStep {
    UserPreset build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserDataStep, PresetDataStep, BuildStep {
    private String id;
    private UserData userData;
    private PresetData presetData;
    @Override
     public UserPreset build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserPreset(
          id,
          userData,
          presetData);
    }
    
    @Override
     public PresetDataStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
        return this;
    }
    
    @Override
     public BuildStep presetData(PresetData presetData) {
        Objects.requireNonNull(presetData);
        this.presetData = presetData;
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
    private CopyOfBuilder(String id, UserData userData, PresetData presetData) {
      super.id(id);
      super.userData(userData)
        .presetData(presetData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
    
    @Override
     public CopyOfBuilder presetData(PresetData presetData) {
      return (CopyOfBuilder) super.presetData(presetData);
    }
  }
  
}
