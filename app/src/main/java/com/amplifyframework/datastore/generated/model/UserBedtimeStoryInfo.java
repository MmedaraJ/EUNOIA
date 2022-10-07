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

/** This is an auto generated class representing the UserBedtimeStoryInfo type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserBedtimeStoryInfos")
@Index(name = "byUserData", fields = {"userDataID"})
@Index(name = "byBedtimeStoryInfoData", fields = {"bedtimeStoryInfoDataID"})
public final class UserBedtimeStoryInfo implements Model {
  public static final QueryField ID = field("UserBedtimeStoryInfo", "id");
  public static final QueryField USER_DATA = field("UserBedtimeStoryInfo", "userDataID");
  public static final QueryField BEDTIME_STORY_INFO_DATA = field("UserBedtimeStoryInfo", "bedtimeStoryInfoDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private final @ModelField(targetType="BedtimeStoryInfoData", isRequired = true) @BelongsTo(targetName = "bedtimeStoryInfoDataID", type = BedtimeStoryInfoData.class) BedtimeStoryInfoData bedtimeStoryInfoData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserData() {
      return userData;
  }
  
  public BedtimeStoryInfoData getBedtimeStoryInfoData() {
      return bedtimeStoryInfoData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserBedtimeStoryInfo(String id, UserData userData, BedtimeStoryInfoData bedtimeStoryInfoData) {
    this.id = id;
    this.userData = userData;
    this.bedtimeStoryInfoData = bedtimeStoryInfoData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserBedtimeStoryInfo userBedtimeStoryInfo = (UserBedtimeStoryInfo) obj;
      return ObjectsCompat.equals(getId(), userBedtimeStoryInfo.getId()) &&
              ObjectsCompat.equals(getUserData(), userBedtimeStoryInfo.getUserData()) &&
              ObjectsCompat.equals(getBedtimeStoryInfoData(), userBedtimeStoryInfo.getBedtimeStoryInfoData()) &&
              ObjectsCompat.equals(getCreatedAt(), userBedtimeStoryInfo.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userBedtimeStoryInfo.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserData())
      .append(getBedtimeStoryInfoData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserBedtimeStoryInfo {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("bedtimeStoryInfoData=" + String.valueOf(getBedtimeStoryInfoData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserDataStep builder() {
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
  public static UserBedtimeStoryInfo justId(String id) {
    return new UserBedtimeStoryInfo(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userData,
      bedtimeStoryInfoData);
  }
  public interface UserDataStep {
    BedtimeStoryInfoDataStep userData(UserData userData);
  }
  

  public interface BedtimeStoryInfoDataStep {
    BuildStep bedtimeStoryInfoData(BedtimeStoryInfoData bedtimeStoryInfoData);
  }
  

  public interface BuildStep {
    UserBedtimeStoryInfo build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserDataStep, BedtimeStoryInfoDataStep, BuildStep {
    private String id;
    private UserData userData;
    private BedtimeStoryInfoData bedtimeStoryInfoData;
    @Override
     public UserBedtimeStoryInfo build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserBedtimeStoryInfo(
          id,
          userData,
          bedtimeStoryInfoData);
    }
    
    @Override
     public BedtimeStoryInfoDataStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
        return this;
    }
    
    @Override
     public BuildStep bedtimeStoryInfoData(BedtimeStoryInfoData bedtimeStoryInfoData) {
        Objects.requireNonNull(bedtimeStoryInfoData);
        this.bedtimeStoryInfoData = bedtimeStoryInfoData;
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
    private CopyOfBuilder(String id, UserData userData, BedtimeStoryInfoData bedtimeStoryInfoData) {
      super.id(id);
      super.userData(userData)
        .bedtimeStoryInfoData(bedtimeStoryInfoData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryInfoData(BedtimeStoryInfoData bedtimeStoryInfoData) {
      return (CopyOfBuilder) super.bedtimeStoryInfoData(bedtimeStoryInfoData);
    }
  }
  
}
