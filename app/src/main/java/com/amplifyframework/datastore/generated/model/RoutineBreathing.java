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

/** This is an auto generated class representing the RoutineBreathing type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RoutineBreathings")
@Index(name = "byRoutineData", fields = {"routineDataID"})
@Index(name = "byBreathingData", fields = {"breathingDataID"})
public final class RoutineBreathing implements Model {
  public static final QueryField ID = field("RoutineBreathing", "id");
  public static final QueryField ROUTINE_DATA = field("RoutineBreathing", "routineDataID");
  public static final QueryField BREATHING_DATA = field("RoutineBreathing", "breathingDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private final @ModelField(targetType="BreathingData", isRequired = true) @BelongsTo(targetName = "breathingDataID", type = BreathingData.class) BreathingData breathingData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public RoutineData getRoutineData() {
      return routineData;
  }
  
  public BreathingData getBreathingData() {
      return breathingData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private RoutineBreathing(String id, RoutineData routineData, BreathingData breathingData) {
    this.id = id;
    this.routineData = routineData;
    this.breathingData = breathingData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RoutineBreathing routineBreathing = (RoutineBreathing) obj;
      return ObjectsCompat.equals(getId(), routineBreathing.getId()) &&
              ObjectsCompat.equals(getRoutineData(), routineBreathing.getRoutineData()) &&
              ObjectsCompat.equals(getBreathingData(), routineBreathing.getBreathingData()) &&
              ObjectsCompat.equals(getCreatedAt(), routineBreathing.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routineBreathing.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRoutineData())
      .append(getBreathingData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RoutineBreathing {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("breathingData=" + String.valueOf(getBreathingData()) + ", ")
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
  public static RoutineBreathing justId(String id) {
    return new RoutineBreathing(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      routineData,
      breathingData);
  }
  public interface RoutineDataStep {
    BreathingDataStep routineData(RoutineData routineData);
  }
  

  public interface BreathingDataStep {
    BuildStep breathingData(BreathingData breathingData);
  }
  

  public interface BuildStep {
    RoutineBreathing build();
    BuildStep id(String id);
  }
  

  public static class Builder implements RoutineDataStep, BreathingDataStep, BuildStep {
    private String id;
    private RoutineData routineData;
    private BreathingData breathingData;
    @Override
     public RoutineBreathing build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutineBreathing(
          id,
          routineData,
          breathingData);
    }
    
    @Override
     public BreathingDataStep routineData(RoutineData routineData) {
        Objects.requireNonNull(routineData);
        this.routineData = routineData;
        return this;
    }
    
    @Override
     public BuildStep breathingData(BreathingData breathingData) {
        Objects.requireNonNull(breathingData);
        this.breathingData = breathingData;
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
    private CopyOfBuilder(String id, RoutineData routineData, BreathingData breathingData) {
      super.id(id);
      super.routineData(routineData)
        .breathingData(breathingData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder breathingData(BreathingData breathingData) {
      return (CopyOfBuilder) super.breathingData(breathingData);
    }
  }
  
}
