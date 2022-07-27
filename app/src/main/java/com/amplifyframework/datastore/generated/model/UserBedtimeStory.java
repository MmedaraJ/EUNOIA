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

/** This is an auto generated class representing the UserBedtimeStoryModel type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserBedtimeStories")
@Index(name = "byUserData", fields = {"userDataID"})
@Index(name = "byBedtimeStoryData", fields = {"bedtimeStoryDataID"})
public final class UserBedtimeStory implements Model {
  public static final QueryField ID = field("UserBedtimeStoryModel", "id");
  public static final QueryField USER_DATA = field("UserBedtimeStoryModel", "userDataID");
  public static final QueryField BEDTIME_STORY_DATA = field("UserBedtimeStoryModel", "bedtimeStoryDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private final @ModelField(targetType="BedtimeStoryData", isRequired = true) @BelongsTo(targetName = "bedtimeStoryDataID", type = BedtimeStoryData.class) BedtimeStoryData bedtimeStoryData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserData() {
      return userData;
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
  
  private UserBedtimeStory(String id, UserData userData, BedtimeStoryData bedtimeStoryData) {
    this.id = id;
    this.userData = userData;
    this.bedtimeStoryData = bedtimeStoryData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserBedtimeStory userBedtimeStory = (UserBedtimeStory) obj;
      return ObjectsCompat.equals(getId(), userBedtimeStory.getId()) &&
              ObjectsCompat.equals(getUserData(), userBedtimeStory.getUserData()) &&
              ObjectsCompat.equals(getBedtimeStoryData(), userBedtimeStory.getBedtimeStoryData()) &&
              ObjectsCompat.equals(getCreatedAt(), userBedtimeStory.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userBedtimeStory.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserData())
      .append(getBedtimeStoryData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserBedtimeStoryModel {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("bedtimeStoryData=" + String.valueOf(getBedtimeStoryData()) + ", ")
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
  public static UserBedtimeStory justId(String id) {
    return new UserBedtimeStory(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userData,
      bedtimeStoryData);
  }
  public interface UserDataStep {
    BedtimeStoryDataStep userData(UserData userData);
  }
  

  public interface BedtimeStoryDataStep {
    BuildStep bedtimeStoryData(BedtimeStoryData bedtimeStoryData);
  }
  

  public interface BuildStep {
    UserBedtimeStory build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserDataStep, BedtimeStoryDataStep, BuildStep {
    private String id;
    private UserData userData;
    private BedtimeStoryData bedtimeStoryData;
    @Override
     public UserBedtimeStory build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserBedtimeStory(
          id,
          userData,
          bedtimeStoryData);
    }
    
    @Override
     public BedtimeStoryDataStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
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
    private CopyOfBuilder(String id, UserData userData, BedtimeStoryData bedtimeStoryData) {
      super.id(id);
      super.userData(userData)
        .bedtimeStoryData(bedtimeStoryData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryData(BedtimeStoryData bedtimeStoryData) {
      return (CopyOfBuilder) super.bedtimeStoryData(bedtimeStoryData);
    }
  }
  
}
