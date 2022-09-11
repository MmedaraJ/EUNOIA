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

/** This is an auto generated class representing the RoutinePrayer type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RoutinePrayers")
@Index(name = "byRoutineData", fields = {"routineDataID"})
@Index(name = "byPrayerData", fields = {"prayerDataID"})
public final class RoutinePrayer implements Model {
  public static final QueryField ID = field("RoutinePrayer", "id");
  public static final QueryField ROUTINE_DATA = field("RoutinePrayer", "routineDataID");
  public static final QueryField PRAYER_DATA = field("RoutinePrayer", "prayerDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private final @ModelField(targetType="PrayerData", isRequired = true) @BelongsTo(targetName = "prayerDataID", type = PrayerData.class) PrayerData prayerData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public RoutineData getRoutineData() {
      return routineData;
  }
  
  public PrayerData getPrayerData() {
      return prayerData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private RoutinePrayer(String id, RoutineData routineData, PrayerData prayerData) {
    this.id = id;
    this.routineData = routineData;
    this.prayerData = prayerData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RoutinePrayer routinePrayer = (RoutinePrayer) obj;
      return ObjectsCompat.equals(getId(), routinePrayer.getId()) &&
              ObjectsCompat.equals(getRoutineData(), routinePrayer.getRoutineData()) &&
              ObjectsCompat.equals(getPrayerData(), routinePrayer.getPrayerData()) &&
              ObjectsCompat.equals(getCreatedAt(), routinePrayer.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routinePrayer.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRoutineData())
      .append(getPrayerData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RoutinePrayer {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("prayerData=" + String.valueOf(getPrayerData()) + ", ")
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
  public static RoutinePrayer justId(String id) {
    return new RoutinePrayer(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      routineData,
      prayerData);
  }
  public interface RoutineDataStep {
    PrayerDataStep routineData(RoutineData routineData);
  }
  

  public interface PrayerDataStep {
    BuildStep prayerData(PrayerData prayerData);
  }
  

  public interface BuildStep {
    RoutinePrayer build();
    BuildStep id(String id);
  }
  

  public static class Builder implements RoutineDataStep, PrayerDataStep, BuildStep {
    private String id;
    private RoutineData routineData;
    private PrayerData prayerData;
    @Override
     public RoutinePrayer build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutinePrayer(
          id,
          routineData,
          prayerData);
    }
    
    @Override
     public PrayerDataStep routineData(RoutineData routineData) {
        Objects.requireNonNull(routineData);
        this.routineData = routineData;
        return this;
    }
    
    @Override
     public BuildStep prayerData(PrayerData prayerData) {
        Objects.requireNonNull(prayerData);
        this.prayerData = prayerData;
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
    private CopyOfBuilder(String id, RoutineData routineData, PrayerData prayerData) {
      super.id(id);
      super.routineData(routineData)
        .prayerData(prayerData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder prayerData(PrayerData prayerData) {
      return (CopyOfBuilder) super.prayerData(prayerData);
    }
  }
  
}
