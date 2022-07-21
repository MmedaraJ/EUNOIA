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

/** This is an auto generated class representing the RoutineBedtimeStories type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RoutineBedtimeStories")
@Index(name = "byRoutineData", fields = {"routineDataID"})
@Index(name = "byBedtimeStoryData", fields = {"bedtimeStoryDataID"})
public final class RoutineBedtimeStories implements Model {
  public static final QueryField ID = field("RoutineBedtimeStories", "id");
  public static final QueryField ROUTINE_DATA = field("RoutineBedtimeStories", "routineDataID");
  public static final QueryField BEDTIME_STORY_DATA = field("RoutineBedtimeStories", "bedtimeStoryDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private final @ModelField(targetType="BedtimeStoryData", isRequired = true) @BelongsTo(targetName = "bedtimeStoryDataID", type = BedtimeStoryData.class) BedtimeStoryData bedtimeStoryData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public RoutineData getRoutineData() {
      return routineData;
  }
  
  public BedtimeStoryData getBedtimeStoryData() {
      return bedtimeStoryData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private RoutineBedtimeStories(String id, RoutineData routineData, BedtimeStoryData bedtimeStoryData) {
    this.id = id;
    this.routineData = routineData;
    this.bedtimeStoryData = bedtimeStoryData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RoutineBedtimeStories routineBedtimeStories = (RoutineBedtimeStories) obj;
      return ObjectsCompat.equals(getId(), routineBedtimeStories.getId()) &&
              ObjectsCompat.equals(getRoutineData(), routineBedtimeStories.getRoutineData()) &&
              ObjectsCompat.equals(getBedtimeStoryData(), routineBedtimeStories.getBedtimeStoryData()) &&
              ObjectsCompat.equals(getCreatedAt(), routineBedtimeStories.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routineBedtimeStories.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRoutineData())
      .append(getBedtimeStoryData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RoutineBedtimeStories {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("bedtimeStoryData=" + String.valueOf(getBedtimeStoryData()) + ", ")
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
  public static RoutineBedtimeStories justId(String id) {
    return new RoutineBedtimeStories(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      routineData,
      bedtimeStoryData);
  }
  public interface RoutineDataStep {
    BedtimeStoryDataStep routineData(RoutineData routineData);
  }
  

  public interface BedtimeStoryDataStep {
    BuildStep bedtimeStoryData(BedtimeStoryData bedtimeStoryData);
  }
  

  public interface BuildStep {
    RoutineBedtimeStories build();
    BuildStep id(String id);
  }
  

  public static class Builder implements RoutineDataStep, BedtimeStoryDataStep, BuildStep {
    private String id;
    private RoutineData routineData;
    private BedtimeStoryData bedtimeStoryData;
    @Override
     public RoutineBedtimeStories build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutineBedtimeStories(
          id,
          routineData,
          bedtimeStoryData);
    }
    
    @Override
     public BedtimeStoryDataStep routineData(RoutineData routineData) {
        Objects.requireNonNull(routineData);
        this.routineData = routineData;
        return this;
    }
    
    @Override
     public BuildStep bedtimeStoryData(BedtimeStoryData bedtimeStoryData) {
        Objects.requireNonNull(bedtimeStoryData);
        this.bedtimeStoryData = bedtimeStoryData;
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
    private CopyOfBuilder(String id, RoutineData routineData, BedtimeStoryData bedtimeStoryData) {
      super.id(id);
      super.routineData(routineData)
        .bedtimeStoryData(bedtimeStoryData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryData(BedtimeStoryData bedtimeStoryData) {
      return (CopyOfBuilder) super.bedtimeStoryData(bedtimeStoryData);
    }
  }
  
}
