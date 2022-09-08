package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
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

/** This is an auto generated class representing the PresetNameAndVolumesMapData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "PresetNameAndVolumesMapData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byPresetData", fields = {"presetID","key"})
public final class PresetNameAndVolumesMapData implements Model {
  public static final QueryField ID = field("PresetNameAndVolumesMapData", "id");
  public static final QueryField KEY = field("PresetNameAndVolumesMapData", "key");
  public static final QueryField VOLUMES = field("PresetNameAndVolumesMapData", "volumes");
  public static final QueryField PRESET = field("PresetNameAndVolumesMapData", "presetID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String key;
  private final @ModelField(targetType="Int", isRequired = true) List<Integer> volumes;
  private final @ModelField(targetType="PresetData", isRequired = true) @BelongsTo(targetName = "presetID", type = PresetData.class) PresetData preset;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getKey() {
      return key;
  }
  
  public List<Integer> getVolumes() {
      return volumes;
  }
  
  public PresetData getPreset() {
      return preset;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private PresetNameAndVolumesMapData(String id, String key, List<Integer> volumes, PresetData preset) {
    this.id = id;
    this.key = key;
    this.volumes = volumes;
    this.preset = preset;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      PresetNameAndVolumesMapData presetNameAndVolumesMapData = (PresetNameAndVolumesMapData) obj;
      return ObjectsCompat.equals(getId(), presetNameAndVolumesMapData.getId()) &&
              ObjectsCompat.equals(getKey(), presetNameAndVolumesMapData.getKey()) &&
              ObjectsCompat.equals(getVolumes(), presetNameAndVolumesMapData.getVolumes()) &&
              ObjectsCompat.equals(getPreset(), presetNameAndVolumesMapData.getPreset()) &&
              ObjectsCompat.equals(getCreatedAt(), presetNameAndVolumesMapData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), presetNameAndVolumesMapData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getKey())
      .append(getVolumes())
      .append(getPreset())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("PresetNameAndVolumesMapData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("key=" + String.valueOf(getKey()) + ", ")
      .append("volumes=" + String.valueOf(getVolumes()) + ", ")
      .append("preset=" + String.valueOf(getPreset()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static KeyStep builder() {
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
  public static PresetNameAndVolumesMapData justId(String id) {
    return new PresetNameAndVolumesMapData(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      key,
      volumes,
      preset);
  }
  public interface KeyStep {
    VolumesStep key(String key);
  }
  

  public interface VolumesStep {
    PresetStep volumes(List<Integer> volumes);
  }
  

  public interface PresetStep {
    BuildStep preset(PresetData preset);
  }
  

  public interface BuildStep {
    PresetNameAndVolumesMapData build();
    BuildStep id(String id);
  }
  

  public static class Builder implements KeyStep, VolumesStep, PresetStep, BuildStep {
    private String id;
    private String key;
    private List<Integer> volumes;
    private PresetData preset;
    @Override
     public PresetNameAndVolumesMapData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new PresetNameAndVolumesMapData(
          id,
          key,
          volumes,
          preset);
    }
    
    @Override
     public VolumesStep key(String key) {
        Objects.requireNonNull(key);
        this.key = key;
        return this;
    }
    
    @Override
     public PresetStep volumes(List<Integer> volumes) {
        Objects.requireNonNull(volumes);
        this.volumes = volumes;
        return this;
    }
    
    @Override
     public BuildStep preset(PresetData preset) {
        Objects.requireNonNull(preset);
        this.preset = preset;
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
    private CopyOfBuilder(String id, String key, List<Integer> volumes, PresetData preset) {
      super.id(id);
      super.key(key)
        .volumes(volumes)
        .preset(preset);
    }
    
    @Override
     public CopyOfBuilder key(String key) {
      return (CopyOfBuilder) super.key(key);
    }
    
    @Override
     public CopyOfBuilder volumes(List<Integer> volumes) {
      return (CopyOfBuilder) super.volumes(volumes);
    }
    
    @Override
     public CopyOfBuilder preset(PresetData preset) {
      return (CopyOfBuilder) super.preset(preset);
    }
  }
  
}
