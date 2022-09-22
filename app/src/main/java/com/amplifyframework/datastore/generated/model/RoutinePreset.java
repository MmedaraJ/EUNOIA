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

/** This is an auto generated class representing the RoutinePreset type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RoutinePresets")
@Index(name = "byPresetData", fields = {"presetDataID"})
@Index(name = "byRoutineData", fields = {"routineDataID"})
public final class RoutinePreset implements Model {
  public static final QueryField ID = field("RoutinePreset", "id");
  public static final QueryField PRESET_DATA = field("RoutinePreset", "presetDataID");
  public static final QueryField ROUTINE_DATA = field("RoutinePreset", "routineDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="PresetData", isRequired = true) @BelongsTo(targetName = "presetDataID", type = PresetData.class) PresetData presetData;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public PresetData getPresetData() {
      return presetData;
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
  
  private RoutinePreset(String id, PresetData presetData, RoutineData routineData) {
    this.id = id;
    this.presetData = presetData;
    this.routineData = routineData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RoutinePreset routinePreset = (RoutinePreset) obj;
      return ObjectsCompat.equals(getId(), routinePreset.getId()) &&
              ObjectsCompat.equals(getPresetData(), routinePreset.getPresetData()) &&
              ObjectsCompat.equals(getRoutineData(), routinePreset.getRoutineData()) &&
              ObjectsCompat.equals(getCreatedAt(), routinePreset.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routinePreset.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getPresetData())
      .append(getRoutineData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RoutinePreset {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("presetData=" + String.valueOf(getPresetData()) + ", ")
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static PresetDataStep builder() {
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
  public static RoutinePreset justId(String id) {
    return new RoutinePreset(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      presetData,
      routineData);
  }
  public interface PresetDataStep {
    RoutineDataStep presetData(PresetData presetData);
  }
  

  public interface RoutineDataStep {
    BuildStep routineData(RoutineData routineData);
  }
  

  public interface BuildStep {
    RoutinePreset build();
    BuildStep id(String id);
  }
  

  public static class Builder implements PresetDataStep, RoutineDataStep, BuildStep {
    private String id;
    private PresetData presetData;
    private RoutineData routineData;
    @Override
     public RoutinePreset build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutinePreset(
          id,
          presetData,
          routineData);
    }
    
    @Override
     public RoutineDataStep presetData(PresetData presetData) {
        Objects.requireNonNull(presetData);
        this.presetData = presetData;
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
    private CopyOfBuilder(String id, PresetData presetData, RoutineData routineData) {
      super.id(id);
      super.presetData(presetData)
        .routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder presetData(PresetData presetData) {
      return (CopyOfBuilder) super.presetData(presetData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
  }
  
}
