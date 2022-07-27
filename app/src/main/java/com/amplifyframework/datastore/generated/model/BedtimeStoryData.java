package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.annotations.HasMany;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the BedtimeStoryData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "BedtimeStoryData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "BedtimeStoriesOwnedByUser", fields = {"userDataID","display_name"})
public final class BedtimeStoryData implements Model {
  public static final QueryField ID = field("BedtimeStoryData", "id");
  public static final QueryField BEDTIME_STORY_OWNER = field("BedtimeStoryData", "userDataID");
  public static final QueryField DISPLAY_NAME = field("BedtimeStoryData", "display_name");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData bedtimeStoryOwner;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="RoutineBedtimeStory") @HasMany(associatedWith = "bedtimeStoryData", type = RoutineBedtimeStory.class) List<RoutineBedtimeStory> routines = null;
  private final @ModelField(targetType="UserBedtimeStoryModel") @HasMany(associatedWith = "bedtimeStoryData", type = UserBedtimeStory.class) List<UserBedtimeStory> users = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getBedtimeStoryOwner() {
      return bedtimeStoryOwner;
  }
  
  public String getDisplayName() {
      return display_name;
  }
  
  public List<RoutineBedtimeStory> getRoutines() {
      return routines;
  }
  
  public List<UserBedtimeStory> getUsers() {
      return users;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private BedtimeStoryData(String id, UserData bedtimeStoryOwner, String display_name) {
    this.id = id;
    this.bedtimeStoryOwner = bedtimeStoryOwner;
    this.display_name = display_name;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      BedtimeStoryData bedtimeStoryData = (BedtimeStoryData) obj;
      return ObjectsCompat.equals(getId(), bedtimeStoryData.getId()) &&
              ObjectsCompat.equals(getBedtimeStoryOwner(), bedtimeStoryData.getBedtimeStoryOwner()) &&
              ObjectsCompat.equals(getDisplayName(), bedtimeStoryData.getDisplayName()) &&
              ObjectsCompat.equals(getCreatedAt(), bedtimeStoryData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), bedtimeStoryData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getBedtimeStoryOwner())
      .append(getDisplayName())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("BedtimeStoryData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("bedtimeStoryOwner=" + String.valueOf(getBedtimeStoryOwner()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BedtimeStoryOwnerStep builder() {
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
  public static BedtimeStoryData justId(String id) {
    return new BedtimeStoryData(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      bedtimeStoryOwner,
      display_name);
  }
  public interface BedtimeStoryOwnerStep {
    DisplayNameStep bedtimeStoryOwner(UserData bedtimeStoryOwner);
  }
  

  public interface DisplayNameStep {
    BuildStep displayName(String displayName);
  }
  

  public interface BuildStep {
    BedtimeStoryData build();
    BuildStep id(String id);
  }
  

  public static class Builder implements BedtimeStoryOwnerStep, DisplayNameStep, BuildStep {
    private String id;
    private UserData bedtimeStoryOwner;
    private String display_name;
    @Override
     public BedtimeStoryData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new BedtimeStoryData(
          id,
          bedtimeStoryOwner,
          display_name);
    }
    
    @Override
     public DisplayNameStep bedtimeStoryOwner(UserData bedtimeStoryOwner) {
        Objects.requireNonNull(bedtimeStoryOwner);
        this.bedtimeStoryOwner = bedtimeStoryOwner;
        return this;
    }
    
    @Override
     public BuildStep displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.display_name = displayName;
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
    private CopyOfBuilder(String id, UserData bedtimeStoryOwner, String displayName) {
      super.id(id);
      super.bedtimeStoryOwner(bedtimeStoryOwner)
        .displayName(displayName);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryOwner(UserData bedtimeStoryOwner) {
      return (CopyOfBuilder) super.bedtimeStoryOwner(bedtimeStoryOwner);
    }
    
    @Override
     public CopyOfBuilder displayName(String displayName) {
      return (CopyOfBuilder) super.displayName(displayName);
    }
  }
  
}
