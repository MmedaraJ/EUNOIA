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
@Index(name = "byRoutineData", fields = {"routineDataID"})
@Index(name = "byPresetData", fields = {"presetDataID"})
public final class RoutinePreset implements Model {
  public static final QueryField ID = field("RoutinePreset", "id");
  public static final QueryField ROUTINE_DATA = field("RoutinePreset", "routineDataID");
  public static final QueryField PRESET_DATA = field("RoutinePreset", "presetDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private final @ModelField(targetType="PresetData", isRequired = true) @BelongsTo(targetName = "presetDataID", type = PresetData.class) PresetData presetData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public RoutineData getRoutineData() {
      return routineData;
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
  
  private RoutinePreset(String id, RoutineData routineData, PresetData presetData) {
    this.id = id;
    this.routineData = routineData;
    this.presetData = presetData;
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
              ObjectsCompat.equals(getRoutineData(), routinePreset.getRoutineData()) &&
              ObjectsCompat.equals(getPresetData(), routinePreset.getPresetData()) &&
              ObjectsCompat.equals(getCreatedAt(), routinePreset.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routinePreset.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRoutineData())
      .append(getPresetData())
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
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("presetData=" + String.valueOf(getPresetData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static RoutineDataStep builder() {
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
      routineData,
      presetData);
  }
  public interface RoutineDataStep {
    PresetDataStep routineData(RoutineData routineData);
  }
  

  public interface PresetDataStep {
    BuildStep presetData(PresetData presetData);
  }
  

  public interface BuildStep {
    RoutinePreset build();
    BuildStep id(String id);
  }
  

  public static class Builder implements RoutineDataStep, PresetDataStep, BuildStep {
    private String id;
    private RoutineData routineData;
    private PresetData presetData;
    @Override
     public RoutinePreset build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutinePreset(
          id,
          routineData,
          presetData);
    }
    
    @Override
     public PresetDataStep routineData(RoutineData routineData) {
        Objects.requireNonNull(routineData);
        this.routineData = routineData;
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
    private CopyOfBuilder(String id, RoutineData routineData, PresetData presetData) {
      super.id(id);
      super.routineData(routineData)
        .presetData(presetData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder presetData(PresetData presetData) {
      return (CopyOfBuilder) super.presetData(presetData);
    }
  }
  
}
