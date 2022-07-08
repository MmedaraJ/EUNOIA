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

/** This is an auto generated class representing the PresetData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "PresetData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "bySoundData", fields = {"soundID"})
public final class PresetData implements Model {
  public static final QueryField ID = field("PresetData", "id");
  public static final QueryField SOUND = field("PresetData", "soundID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="SoundData", isRequired = true) @BelongsTo(targetName = "soundID", type = SoundData.class) SoundData sound;
  private final @ModelField(targetType="PresetNameAndVolumesMapData", isRequired = true) @HasMany(associatedWith = "preset", type = PresetNameAndVolumesMapData.class) List<PresetNameAndVolumesMapData> presets = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public SoundData getSound() {
      return sound;
  }
  
  public List<PresetNameAndVolumesMapData> getPresets() {
      return presets;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private PresetData(String id, SoundData sound) {
    this.id = id;
    this.sound = sound;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      PresetData presetData = (PresetData) obj;
      return ObjectsCompat.equals(getId(), presetData.getId()) &&
              ObjectsCompat.equals(getSound(), presetData.getSound()) &&
              ObjectsCompat.equals(getCreatedAt(), presetData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), presetData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getSound())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("PresetData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("sound=" + String.valueOf(getSound()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static SoundStep builder() {
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
  public static PresetData justId(String id) {
    return new PresetData(
      id,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      sound);
  }
  public interface SoundStep {
    BuildStep sound(SoundData sound);
  }
  

  public interface BuildStep {
    PresetData build();
    BuildStep id(String id);
  }
  

  public static class Builder implements SoundStep, BuildStep {
    private String id;
    private SoundData sound;
    @Override
     public PresetData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new PresetData(
          id,
          sound);
    }
    
    @Override
     public BuildStep sound(SoundData sound) {
        Objects.requireNonNull(sound);
        this.sound = sound;
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
    private CopyOfBuilder(String id, SoundData sound) {
      super.id(id);
      super.sound(sound);
    }
    
    @Override
     public CopyOfBuilder sound(SoundData sound) {
      return (CopyOfBuilder) super.sound(sound);
    }
  }
  
}
