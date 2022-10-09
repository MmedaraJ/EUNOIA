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

/** This is an auto generated class representing the UserRoutineRelationshipBedtimeStoryInfo type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRoutineRelationshipBedtimeStoryInfos")
@Index(name = "byBedtimeStoryInfoData", fields = {"bedtimeStoryInfoDataID"})
@Index(name = "byUserRoutineRelationship", fields = {"userRoutineRelationshipID"})
public final class UserRoutineRelationshipBedtimeStoryInfo implements Model {
  public static final QueryField ID = field("UserRoutineRelationshipBedtimeStoryInfo", "id");
  public static final QueryField BEDTIME_STORY_INFO_DATA = field("UserRoutineRelationshipBedtimeStoryInfo", "bedtimeStoryInfoDataID");
  public static final QueryField USER_ROUTINE_RELATIONSHIP = field("UserRoutineRelationshipBedtimeStoryInfo", "userRoutineRelationshipID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="BedtimeStoryInfoData", isRequired = true) @BelongsTo(targetName = "bedtimeStoryInfoDataID", type = BedtimeStoryInfoData.class) BedtimeStoryInfoData bedtimeStoryInfoData;
  private final @ModelField(targetType="UserRoutineRelationship", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipID", type = UserRoutineRelationship.class) UserRoutineRelationship userRoutineRelationship;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public BedtimeStoryInfoData getBedtimeStoryInfoData() {
      return bedtimeStoryInfoData;
  }
  
  public UserRoutineRelationship getUserRoutineRelationship() {
      return userRoutineRelationship;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserRoutineRelationshipBedtimeStoryInfo(String id, BedtimeStoryInfoData bedtimeStoryInfoData, UserRoutineRelationship userRoutineRelationship) {
    this.id = id;
    this.bedtimeStoryInfoData = bedtimeStoryInfoData;
    this.userRoutineRelationship = userRoutineRelationship;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserRoutineRelationshipBedtimeStoryInfo userRoutineRelationshipBedtimeStoryInfo = (UserRoutineRelationshipBedtimeStoryInfo) obj;
      return ObjectsCompat.equals(getId(), userRoutineRelationshipBedtimeStoryInfo.getId()) &&
              ObjectsCompat.equals(getBedtimeStoryInfoData(), userRoutineRelationshipBedtimeStoryInfo.getBedtimeStoryInfoData()) &&
              ObjectsCompat.equals(getUserRoutineRelationship(), userRoutineRelationshipBedtimeStoryInfo.getUserRoutineRelationship()) &&
              ObjectsCompat.equals(getCreatedAt(), userRoutineRelationshipBedtimeStoryInfo.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRoutineRelationshipBedtimeStoryInfo.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getBedtimeStoryInfoData())
      .append(getUserRoutineRelationship())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserRoutineRelationshipBedtimeStoryInfo {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("bedtimeStoryInfoData=" + String.valueOf(getBedtimeStoryInfoData()) + ", ")
      .append("userRoutineRelationship=" + String.valueOf(getUserRoutineRelationship()) + ", ")
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
  public static UserRoutineRelationshipBedtimeStoryInfo justId(String id) {
    return new UserRoutineRelationshipBedtimeStoryInfo(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      bedtimeStoryInfoData,
      userRoutineRelationship);
  }
  public interface BedtimeStoryInfoDataStep {
    UserRoutineRelationshipStep bedtimeStoryInfoData(BedtimeStoryInfoData bedtimeStoryInfoData);
  }
  

  public interface UserRoutineRelationshipStep {
    BuildStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship);
  }
  

  public interface BuildStep {
    UserRoutineRelationshipBedtimeStoryInfo build();
    BuildStep id(String id);
  }
  

  public static class Builder implements BedtimeStoryInfoDataStep, UserRoutineRelationshipStep, BuildStep {
    private String id;
    private BedtimeStoryInfoData bedtimeStoryInfoData;
    private UserRoutineRelationship userRoutineRelationship;
    @Override
     public UserRoutineRelationshipBedtimeStoryInfo build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRoutineRelationshipBedtimeStoryInfo(
          id,
          bedtimeStoryInfoData,
          userRoutineRelationship);
    }
    
    @Override
     public UserRoutineRelationshipStep bedtimeStoryInfoData(BedtimeStoryInfoData bedtimeStoryInfoData) {
        Objects.requireNonNull(bedtimeStoryInfoData);
        this.bedtimeStoryInfoData = bedtimeStoryInfoData;
        return this;
    }
    
    @Override
     public BuildStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
        Objects.requireNonNull(userRoutineRelationship);
        this.userRoutineRelationship = userRoutineRelationship;
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
    private CopyOfBuilder(String id, BedtimeStoryInfoData bedtimeStoryInfoData, UserRoutineRelationship userRoutineRelationship) {
      super.id(id);
      super.bedtimeStoryInfoData(bedtimeStoryInfoData)
        .userRoutineRelationship(userRoutineRelationship);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryInfoData(BedtimeStoryInfoData bedtimeStoryInfoData) {
      return (CopyOfBuilder) super.bedtimeStoryInfoData(bedtimeStoryInfoData);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
      return (CopyOfBuilder) super.userRoutineRelationship(userRoutineRelationship);
    }
  }
  
}
