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

/** This is an auto generated class representing the RoutineSoundModel type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RoutineSounds")
@Index(name = "bySoundData", fields = {"soundDataID"})
@Index(name = "byRoutineData", fields = {"routineDataID"})
public final class RoutineSound implements Model {
  public static final QueryField ID = field("RoutineSoundModel", "id");
  public static final QueryField SOUND_DATA = field("RoutineSoundModel", "soundDataID");
  public static final QueryField ROUTINE_DATA = field("RoutineSoundModel", "routineDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="SoundData", isRequired = true) @BelongsTo(targetName = "soundDataID", type = SoundData.class) SoundData soundData;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public SoundData getSoundData() {
      return soundData;
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
  
  private RoutineSound(String id, SoundData soundData, RoutineData routineData) {
    this.id = id;
    this.soundData = soundData;
    this.routineData = routineData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RoutineSound routineSound = (RoutineSound) obj;
      return ObjectsCompat.equals(getId(), routineSound.getId()) &&
              ObjectsCompat.equals(getSoundData(), routineSound.getSoundData()) &&
              ObjectsCompat.equals(getRoutineData(), routineSound.getRoutineData()) &&
              ObjectsCompat.equals(getCreatedAt(), routineSound.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routineSound.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getSoundData())
      .append(getRoutineData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RoutineSoundModel {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("soundData=" + String.valueOf(getSoundData()) + ", ")
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static SoundDataStep builder() {
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
  public static RoutineSound justId(String id) {
    return new RoutineSound(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      soundData,
      routineData);
  }
  public interface SoundDataStep {
    RoutineDataStep soundData(SoundData soundData);
  }
  

  public interface RoutineDataStep {
    BuildStep routineData(RoutineData routineData);
  }
  

  public interface BuildStep {
    RoutineSound build();
    BuildStep id(String id);
  }
  

  public static class Builder implements SoundDataStep, RoutineDataStep, BuildStep {
    private String id;
    private SoundData soundData;
    private RoutineData routineData;
    @Override
     public RoutineSound build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutineSound(
          id,
          soundData,
          routineData);
    }
    
    @Override
     public RoutineDataStep soundData(SoundData soundData) {
        Objects.requireNonNull(soundData);
        this.soundData = soundData;
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
    private CopyOfBuilder(String id, SoundData soundData, RoutineData routineData) {
      super.id(id);
      super.soundData(soundData)
        .routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder soundData(SoundData soundData) {
      return (CopyOfBuilder) super.soundData(soundData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
  }
  
}
