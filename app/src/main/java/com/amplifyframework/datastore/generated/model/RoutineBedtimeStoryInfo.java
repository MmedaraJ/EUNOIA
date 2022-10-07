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

/** This is an auto generated class representing the RoutineBedtimeStoryInfo type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RoutineBedtimeStoryInfos")
@Index(name = "byBedtimeStoryInfoData", fields = {"bedtimeStoryInfoDataID"})
@Index(name = "byRoutineData", fields = {"routineDataID"})
public final class RoutineBedtimeStoryInfo implements Model {
  public static final QueryField ID = field("RoutineBedtimeStoryInfo", "id");
  public static final QueryField BEDTIME_STORY_INFO_DATA = field("RoutineBedtimeStoryInfo", "bedtimeStoryInfoDataID");
  public static final QueryField ROUTINE_DATA = field("RoutineBedtimeStoryInfo", "routineDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="BedtimeStoryInfoData", isRequired = true) @BelongsTo(targetName = "bedtimeStoryInfoDataID", type = BedtimeStoryInfoData.class) BedtimeStoryInfoData bedtimeStoryInfoData;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public BedtimeStoryInfoData getBedtimeStoryInfoData() {
      return bedtimeStoryInfoData;
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
  
  private RoutineBedtimeStoryInfo(String id, BedtimeStoryInfoData bedtimeStoryInfoData, RoutineData routineData) {
    this.id = id;
    this.bedtimeStoryInfoData = bedtimeStoryInfoData;
    this.routineData = routineData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RoutineBedtimeStoryInfo routineBedtimeStoryInfo = (RoutineBedtimeStoryInfo) obj;
      return ObjectsCompat.equals(getId(), routineBedtimeStoryInfo.getId()) &&
              ObjectsCompat.equals(getBedtimeStoryInfoData(), routineBedtimeStoryInfo.getBedtimeStoryInfoData()) &&
              ObjectsCompat.equals(getRoutineData(), routineBedtimeStoryInfo.getRoutineData()) &&
              ObjectsCompat.equals(getCreatedAt(), routineBedtimeStoryInfo.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routineBedtimeStoryInfo.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getBedtimeStoryInfoData())
      .append(getRoutineData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RoutineBedtimeStoryInfo {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("bedtimeStoryInfoData=" + String.valueOf(getBedtimeStoryInfoData()) + ", ")
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BedtimeStoryInfoDataStep builder() {
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
  public static RoutineBedtimeStoryInfo justId(String id) {
    return new RoutineBedtimeStoryInfo(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      bedtimeStoryInfoData,
      routineData);
  }
  public interface BedtimeStoryInfoDataStep {
    RoutineDataStep bedtimeStoryInfoData(BedtimeStoryInfoData bedtimeStoryInfoData);
  }
  

  public interface RoutineDataStep {
    BuildStep routineData(RoutineData routineData);
  }
  

  public interface BuildStep {
    RoutineBedtimeStoryInfo build();
    BuildStep id(String id);
  }
  

  public static class Builder implements BedtimeStoryInfoDataStep, RoutineDataStep, BuildStep {
    private String id;
    private BedtimeStoryInfoData bedtimeStoryInfoData;
    private RoutineData routineData;
    @Override
     public RoutineBedtimeStoryInfo build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutineBedtimeStoryInfo(
          id,
          bedtimeStoryInfoData,
          routineData);
    }
    
    @Override
     public RoutineDataStep bedtimeStoryInfoData(BedtimeStoryInfoData bedtimeStoryInfoData) {
        Objects.requireNonNull(bedtimeStoryInfoData);
        this.bedtimeStoryInfoData = bedtimeStoryInfoData;
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
    private CopyOfBuilder(String id, BedtimeStoryInfoData bedtimeStoryInfoData, RoutineData routineData) {
      super.id(id);
      super.bedtimeStoryInfoData(bedtimeStoryInfoData)
        .routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryInfoData(BedtimeStoryInfoData bedtimeStoryInfoData) {
      return (CopyOfBuilder) super.bedtimeStoryInfoData(bedtimeStoryInfoData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
  }
  
}
