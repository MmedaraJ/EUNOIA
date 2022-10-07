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

/** This is an auto generated class representing the RoutineSoundPreset type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RoutineSoundPresets")
@Index(name = "byRoutineData", fields = {"routineDataID"})
@Index(name = "bySoundPresetData", fields = {"soundPresetDataID"})
public final class RoutineSoundPreset implements Model {
  public static final QueryField ID = field("RoutineSoundPreset", "id");
  public static final QueryField ROUTINE_DATA = field("RoutineSoundPreset", "routineDataID");
  public static final QueryField SOUND_PRESET_DATA = field("RoutineSoundPreset", "soundPresetDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private final @ModelField(targetType="SoundPresetData", isRequired = true) @BelongsTo(targetName = "soundPresetDataID", type = SoundPresetData.class) SoundPresetData soundPresetData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public RoutineData getRoutineData() {
      return routineData;
  }
  
  public SoundPresetData getSoundPresetData() {
      return soundPresetData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private RoutineSoundPreset(String id, RoutineData routineData, SoundPresetData soundPresetData) {
    this.id = id;
    this.routineData = routineData;
    this.soundPresetData = soundPresetData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RoutineSoundPreset routineSoundPreset = (RoutineSoundPreset) obj;
      return ObjectsCompat.equals(getId(), routineSoundPreset.getId()) &&
              ObjectsCompat.equals(getRoutineData(), routineSoundPreset.getRoutineData()) &&
              ObjectsCompat.equals(getSoundPresetData(), routineSoundPreset.getSoundPresetData()) &&
              ObjectsCompat.equals(getCreatedAt(), routineSoundPreset.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routineSoundPreset.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRoutineData())
      .append(getSoundPresetData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RoutineSoundPreset {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("soundPresetData=" + String.valueOf(getSoundPresetData()) + ", ")
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
  public static RoutineSoundPreset justId(String id) {
    return new RoutineSoundPreset(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      routineData,
      soundPresetData);
  }
  public interface RoutineDataStep {
    SoundPresetDataStep routineData(RoutineData routineData);
  }
  

  public interface SoundPresetDataStep {
    BuildStep soundPresetData(SoundPresetData soundPresetData);
  }
  

  public interface BuildStep {
    RoutineSoundPreset build();
    BuildStep id(String id);
  }
  

  public static class Builder implements RoutineDataStep, SoundPresetDataStep, BuildStep {
    private String id;
    private RoutineData routineData;
    private SoundPresetData soundPresetData;
    @Override
     public RoutineSoundPreset build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutineSoundPreset(
          id,
          routineData,
          soundPresetData);
    }
    
    @Override
     public SoundPresetDataStep routineData(RoutineData routineData) {
        Objects.requireNonNull(routineData);
        this.routineData = routineData;
        return this;
    }
    
    @Override
     public BuildStep soundPresetData(SoundPresetData soundPresetData) {
        Objects.requireNonNull(soundPresetData);
        this.soundPresetData = soundPresetData;
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
    private CopyOfBuilder(String id, RoutineData routineData, SoundPresetData soundPresetData) {
      super.id(id);
      super.routineData(routineData)
        .soundPresetData(soundPresetData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder soundPresetData(SoundPresetData soundPresetData) {
      return (CopyOfBuilder) super.soundPresetData(soundPresetData);
    }
  }
  
}
