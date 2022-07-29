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

/** This is an auto generated class representing the RoutineStretch type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RoutineStretches")
@Index(name = "byRoutineData", fields = {"routineDataID"})
@Index(name = "byStretchData", fields = {"stretchDataID"})
public final class RoutineStretch implements Model {
  public static final QueryField ID = field("RoutineStretch", "id");
  public static final QueryField ROUTINE_DATA = field("RoutineStretch", "routineDataID");
  public static final QueryField STRETCH_DATA = field("RoutineStretch", "stretchDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private final @ModelField(targetType="StretchData", isRequired = true) @BelongsTo(targetName = "stretchDataID", type = StretchData.class) StretchData stretchData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public RoutineData getRoutineData() {
      return routineData;
  }
  
  public StretchData getStretchData() {
      return stretchData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private RoutineStretch(String id, RoutineData routineData, StretchData stretchData) {
    this.id = id;
    this.routineData = routineData;
    this.stretchData = stretchData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RoutineStretch routineStretch = (RoutineStretch) obj;
      return ObjectsCompat.equals(getId(), routineStretch.getId()) &&
              ObjectsCompat.equals(getRoutineData(), routineStretch.getRoutineData()) &&
              ObjectsCompat.equals(getStretchData(), routineStretch.getStretchData()) &&
              ObjectsCompat.equals(getCreatedAt(), routineStretch.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routineStretch.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRoutineData())
      .append(getStretchData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RoutineStretch {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("stretchData=" + String.valueOf(getStretchData()) + ", ")
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
  public static RoutineStretch justId(String id) {
    return new RoutineStretch(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      routineData,
      stretchData);
  }
  public interface RoutineDataStep {
    StretchDataStep routineData(RoutineData routineData);
  }
  

  public interface StretchDataStep {
    BuildStep stretchData(StretchData stretchData);
  }
  

  public interface BuildStep {
    RoutineStretch build();
    BuildStep id(String id);
  }
  

  public static class Builder implements RoutineDataStep, StretchDataStep, BuildStep {
    private String id;
    private RoutineData routineData;
    private StretchData stretchData;
    @Override
     public RoutineStretch build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutineStretch(
          id,
          routineData,
          stretchData);
    }
    
    @Override
     public StretchDataStep routineData(RoutineData routineData) {
        Objects.requireNonNull(routineData);
        this.routineData = routineData;
        return this;
    }
    
    @Override
     public BuildStep stretchData(StretchData stretchData) {
        Objects.requireNonNull(stretchData);
        this.stretchData = stretchData;
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
    private CopyOfBuilder(String id, RoutineData routineData, StretchData stretchData) {
      super.id(id);
      super.routineData(routineData)
        .stretchData(stretchData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder stretchData(StretchData stretchData) {
      return (CopyOfBuilder) super.stretchData(stretchData);
    }
  }
  
}
