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

/** This is an auto generated class representing the RoutineSelfLoves type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RoutineSelfLoves")
@Index(name = "byRoutineData", fields = {"routineDataID"})
@Index(name = "bySelfLoveData", fields = {"selfLoveDataID"})
public final class RoutineSelfLoves implements Model {
  public static final QueryField ID = field("RoutineSelfLoves", "id");
  public static final QueryField ROUTINE_DATA = field("RoutineSelfLoves", "routineDataID");
  public static final QueryField SELF_LOVE_DATA = field("RoutineSelfLoves", "selfLoveDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "routineDataID", type = RoutineData.class) RoutineData routineData;
  private final @ModelField(targetType="SelfLoveData", isRequired = true) @BelongsTo(targetName = "selfLoveDataID", type = SelfLoveData.class) SelfLoveData selfLoveData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public RoutineData getRoutineData() {
      return routineData;
  }
  
  public SelfLoveData getSelfLoveData() {
      return selfLoveData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private RoutineSelfLoves(String id, RoutineData routineData, SelfLoveData selfLoveData) {
    this.id = id;
    this.routineData = routineData;
    this.selfLoveData = selfLoveData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RoutineSelfLoves routineSelfLoves = (RoutineSelfLoves) obj;
      return ObjectsCompat.equals(getId(), routineSelfLoves.getId()) &&
              ObjectsCompat.equals(getRoutineData(), routineSelfLoves.getRoutineData()) &&
              ObjectsCompat.equals(getSelfLoveData(), routineSelfLoves.getSelfLoveData()) &&
              ObjectsCompat.equals(getCreatedAt(), routineSelfLoves.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routineSelfLoves.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRoutineData())
      .append(getSelfLoveData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RoutineSelfLoves {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("routineData=" + String.valueOf(getRoutineData()) + ", ")
      .append("selfLoveData=" + String.valueOf(getSelfLoveData()) + ", ")
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
  public static RoutineSelfLoves justId(String id) {
    return new RoutineSelfLoves(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      routineData,
      selfLoveData);
  }
  public interface RoutineDataStep {
    SelfLoveDataStep routineData(RoutineData routineData);
  }
  

  public interface SelfLoveDataStep {
    BuildStep selfLoveData(SelfLoveData selfLoveData);
  }
  

  public interface BuildStep {
    RoutineSelfLoves build();
    BuildStep id(String id);
  }
  

  public static class Builder implements RoutineDataStep, SelfLoveDataStep, BuildStep {
    private String id;
    private RoutineData routineData;
    private SelfLoveData selfLoveData;
    @Override
     public RoutineSelfLoves build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutineSelfLoves(
          id,
          routineData,
          selfLoveData);
    }
    
    @Override
     public SelfLoveDataStep routineData(RoutineData routineData) {
        Objects.requireNonNull(routineData);
        this.routineData = routineData;
        return this;
    }
    
    @Override
     public BuildStep selfLoveData(SelfLoveData selfLoveData) {
        Objects.requireNonNull(selfLoveData);
        this.selfLoveData = selfLoveData;
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
    private CopyOfBuilder(String id, RoutineData routineData, SelfLoveData selfLoveData) {
      super.id(id);
      super.routineData(routineData)
        .selfLoveData(selfLoveData);
    }
    
    @Override
     public CopyOfBuilder routineData(RoutineData routineData) {
      return (CopyOfBuilder) super.routineData(routineData);
    }
    
    @Override
     public CopyOfBuilder selfLoveData(SelfLoveData selfLoveData) {
      return (CopyOfBuilder) super.selfLoveData(selfLoveData);
    }
  }
  
}
