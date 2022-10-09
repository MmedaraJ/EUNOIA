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

/** This is an auto generated class representing the BedtimeStoryInfoData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "BedtimeStoryInfoData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "BedtimeStoriesOwnedByUser", fields = {"bedtimeStoryUserDataID","displayName"})
public final class BedtimeStoryInfoData implements Model {
  public static final QueryField ID = field("BedtimeStoryInfoData", "id");
  public static final QueryField BEDTIME_STORY_OWNER = field("BedtimeStoryInfoData", "bedtimeStoryUserDataID");
  public static final QueryField BEDTIME_STORY_OWNER_ID = field("BedtimeStoryInfoData", "bedtimeStoryOwnerId");
  public static final QueryField DISPLAY_NAME = field("BedtimeStoryInfoData", "displayName");
  public static final QueryField SHORT_DESCRIPTION = field("BedtimeStoryInfoData", "shortDescription");
  public static final QueryField LONG_DESCRIPTION = field("BedtimeStoryInfoData", "longDescription");
  public static final QueryField AUDIO_KEY_S3 = field("BedtimeStoryInfoData", "audioKeyS3");
  public static final QueryField ICON = field("BedtimeStoryInfoData", "icon");
  public static final QueryField FULL_PLAY_TIME = field("BedtimeStoryInfoData", "fullPlayTime");
  public static final QueryField VISIBLE_TO_OTHERS = field("BedtimeStoryInfoData", "visibleToOthers");
  public static final QueryField TAGS = field("BedtimeStoryInfoData", "tags");
  public static final QueryField AUDIO_SOURCE = field("BedtimeStoryInfoData", "audioSource");
  public static final QueryField APPROVAL_STATUS = field("BedtimeStoryInfoData", "approvalStatus");
  public static final QueryField CREATION_STATUS = field("BedtimeStoryInfoData", "creationStatus");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "bedtimeStoryUserDataID", type = UserData.class) UserData bedtimeStoryOwner;
  private final @ModelField(targetType="String") String bedtimeStoryOwnerId;
  private final @ModelField(targetType="String", isRequired = true) String displayName;
  private final @ModelField(targetType="String") String shortDescription;
  private final @ModelField(targetType="String") String longDescription;
  private final @ModelField(targetType="String", isRequired = true) String audioKeyS3;
  private final @ModelField(targetType="Int", isRequired = true) Integer icon;
  private final @ModelField(targetType="Int", isRequired = true) Integer fullPlayTime;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean visibleToOthers;
  private final @ModelField(targetType="String") List<String> tags;
  private final @ModelField(targetType="BedtimeStoryAudioSource") BedtimeStoryAudioSource audioSource;
  private final @ModelField(targetType="BedtimeStoryApprovalStatus") BedtimeStoryApprovalStatus approvalStatus;
  private final @ModelField(targetType="BedtimeStoryCreationStatus") BedtimeStoryCreationStatus creationStatus;
  private final @ModelField(targetType="UserBedtimeStoryInfoRelationship") @HasMany(associatedWith = "userBedtimeStoryInfoRelationshipBedtimeStoryInfo", type = UserBedtimeStoryInfoRelationship.class) List<UserBedtimeStoryInfoRelationship> userBedtimeStoryInfoRelationshipsOwnedByBedtimeStoryInfo = null;
  private final @ModelField(targetType="RoutineBedtimeStoryInfo") @HasMany(associatedWith = "bedtimeStoryInfoData", type = RoutineBedtimeStoryInfo.class) List<RoutineBedtimeStoryInfo> routines = null;
  private final @ModelField(targetType="UserRoutineRelationshipBedtimeStoryInfo") @HasMany(associatedWith = "bedtimeStoryInfoData", type = UserRoutineRelationshipBedtimeStoryInfo.class) List<UserRoutineRelationshipBedtimeStoryInfo> userRoutineRelationships = null;
  private final @ModelField(targetType="UserBedtimeStoryInfo") @HasMany(associatedWith = "bedtimeStoryInfoData", type = UserBedtimeStoryInfo.class) List<UserBedtimeStoryInfo> users = null;
  private final @ModelField(targetType="BedtimeStoryInfoChapterData") @HasMany(associatedWith = "bedtimeStoryInfo", type = BedtimeStoryInfoChapterData.class) List<BedtimeStoryInfoChapterData> chapters = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getBedtimeStoryOwner() {
      return bedtimeStoryOwner;
  }
  
  public String getBedtimeStoryOwnerId() {
      return bedtimeStoryOwnerId;
  }
  
  public String getDisplayName() {
      return displayName;
  }
  
  public String getShortDescription() {
      return shortDescription;
  }
  
  public String getLongDescription() {
      return longDescription;
  }
  
  public String getAudioKeyS3() {
      return audioKeyS3;
  }
  
  public Integer getIcon() {
      return icon;
  }
  
  public Integer getFullPlayTime() {
      return fullPlayTime;
  }
  
  public Boolean getVisibleToOthers() {
      return visibleToOthers;
  }
  
  public List<String> getTags() {
      return tags;
  }
  
  public BedtimeStoryAudioSource getAudioSource() {
      return audioSource;
  }
  
  public BedtimeStoryApprovalStatus getApprovalStatus() {
      return approvalStatus;
  }
  
  public BedtimeStoryCreationStatus getCreationStatus() {
      return creationStatus;
  }
  
  public List<UserBedtimeStoryInfoRelationship> getUserBedtimeStoryInfoRelationshipsOwnedByBedtimeStoryInfo() {
      return userBedtimeStoryInfoRelationshipsOwnedByBedtimeStoryInfo;
  }
  
  public List<RoutineBedtimeStoryInfo> getRoutines() {
      return routines;
  }
  
  public List<UserRoutineRelationshipBedtimeStoryInfo> getUserRoutineRelationships() {
      return userRoutineRelationships;
  }
  
  public List<UserBedtimeStoryInfo> getUsers() {
      return users;
  }
  
  public List<BedtimeStoryInfoChapterData> getChapters() {
      return chapters;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private BedtimeStoryInfoData(String id, UserData bedtimeStoryOwner, String bedtimeStoryOwnerId, String displayName, String shortDescription, String longDescription, String audioKeyS3, Integer icon, Integer fullPlayTime, Boolean visibleToOthers, List<String> tags, BedtimeStoryAudioSource audioSource, BedtimeStoryApprovalStatus approvalStatus, BedtimeStoryCreationStatus creationStatus) {
    this.id = id;
    this.bedtimeStoryOwner = bedtimeStoryOwner;
    this.bedtimeStoryOwnerId = bedtimeStoryOwnerId;
    this.displayName = displayName;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.audioKeyS3 = audioKeyS3;
    this.icon = icon;
    this.fullPlayTime = fullPlayTime;
    this.visibleToOthers = visibleToOthers;
    this.tags = tags;
    this.audioSource = audioSource;
    this.approvalStatus = approvalStatus;
    this.creationStatus = creationStatus;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      BedtimeStoryInfoData bedtimeStoryInfoData = (BedtimeStoryInfoData) obj;
      return ObjectsCompat.equals(getId(), bedtimeStoryInfoData.getId()) &&
              ObjectsCompat.equals(getBedtimeStoryOwner(), bedtimeStoryInfoData.getBedtimeStoryOwner()) &&
              ObjectsCompat.equals(getBedtimeStoryOwnerId(), bedtimeStoryInfoData.getBedtimeStoryOwnerId()) &&
              ObjectsCompat.equals(getDisplayName(), bedtimeStoryInfoData.getDisplayName()) &&
              ObjectsCompat.equals(getShortDescription(), bedtimeStoryInfoData.getShortDescription()) &&
              ObjectsCompat.equals(getLongDescription(), bedtimeStoryInfoData.getLongDescription()) &&
              ObjectsCompat.equals(getAudioKeyS3(), bedtimeStoryInfoData.getAudioKeyS3()) &&
              ObjectsCompat.equals(getIcon(), bedtimeStoryInfoData.getIcon()) &&
              ObjectsCompat.equals(getFullPlayTime(), bedtimeStoryInfoData.getFullPlayTime()) &&
              ObjectsCompat.equals(getVisibleToOthers(), bedtimeStoryInfoData.getVisibleToOthers()) &&
              ObjectsCompat.equals(getTags(), bedtimeStoryInfoData.getTags()) &&
              ObjectsCompat.equals(getAudioSource(), bedtimeStoryInfoData.getAudioSource()) &&
              ObjectsCompat.equals(getApprovalStatus(), bedtimeStoryInfoData.getApprovalStatus()) &&
              ObjectsCompat.equals(getCreationStatus(), bedtimeStoryInfoData.getCreationStatus()) &&
              ObjectsCompat.equals(getCreatedAt(), bedtimeStoryInfoData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), bedtimeStoryInfoData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getBedtimeStoryOwner())
      .append(getBedtimeStoryOwnerId())
      .append(getDisplayName())
      .append(getShortDescription())
      .append(getLongDescription())
      .append(getAudioKeyS3())
      .append(getIcon())
      .append(getFullPlayTime())
      .append(getVisibleToOthers())
      .append(getTags())
      .append(getAudioSource())
      .append(getApprovalStatus())
      .append(getCreationStatus())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("BedtimeStoryInfoData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("bedtimeStoryOwner=" + String.valueOf(getBedtimeStoryOwner()) + ", ")
      .append("bedtimeStoryOwnerId=" + String.valueOf(getBedtimeStoryOwnerId()) + ", ")
      .append("displayName=" + String.valueOf(getDisplayName()) + ", ")
      .append("shortDescription=" + String.valueOf(getShortDescription()) + ", ")
      .append("longDescription=" + String.valueOf(getLongDescription()) + ", ")
      .append("audioKeyS3=" + String.valueOf(getAudioKeyS3()) + ", ")
      .append("icon=" + String.valueOf(getIcon()) + ", ")
      .append("fullPlayTime=" + String.valueOf(getFullPlayTime()) + ", ")
      .append("visibleToOthers=" + String.valueOf(getVisibleToOthers()) + ", ")
      .append("tags=" + String.valueOf(getTags()) + ", ")
      .append("audioSource=" + String.valueOf(getAudioSource()) + ", ")
      .append("approvalStatus=" + String.valueOf(getApprovalStatus()) + ", ")
      .append("creationStatus=" + String.valueOf(getCreationStatus()) + ", ")
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
  public static BedtimeStoryInfoData justId(String id) {
    return new BedtimeStoryInfoData(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      bedtimeStoryOwner,
      bedtimeStoryOwnerId,
      displayName,
      shortDescription,
      longDescription,
      audioKeyS3,
      icon,
      fullPlayTime,
      visibleToOthers,
      tags,
      audioSource,
      approvalStatus,
      creationStatus);
  }
  public interface BedtimeStoryOwnerStep {
    DisplayNameStep bedtimeStoryOwner(UserData bedtimeStoryOwner);
  }
  

  public interface DisplayNameStep {
    AudioKeyS3Step displayName(String displayName);
  }
  

  public interface AudioKeyS3Step {
    IconStep audioKeyS3(String audioKeyS3);
  }
  

  public interface IconStep {
    FullPlayTimeStep icon(Integer icon);
  }
  

  public interface FullPlayTimeStep {
    VisibleToOthersStep fullPlayTime(Integer fullPlayTime);
  }
  

  public interface VisibleToOthersStep {
    BuildStep visibleToOthers(Boolean visibleToOthers);
  }
  

  public interface BuildStep {
    BedtimeStoryInfoData build();
    BuildStep id(String id);
    BuildStep bedtimeStoryOwnerId(String bedtimeStoryOwnerId);
    BuildStep shortDescription(String shortDescription);
    BuildStep longDescription(String longDescription);
    BuildStep tags(List<String> tags);
    BuildStep audioSource(BedtimeStoryAudioSource audioSource);
    BuildStep approvalStatus(BedtimeStoryApprovalStatus approvalStatus);
    BuildStep creationStatus(BedtimeStoryCreationStatus creationStatus);
  }
  

  public static class Builder implements BedtimeStoryOwnerStep, DisplayNameStep, AudioKeyS3Step, IconStep, FullPlayTimeStep, VisibleToOthersStep, BuildStep {
    private String id;
    private UserData bedtimeStoryOwner;
    private String displayName;
    private String audioKeyS3;
    private Integer icon;
    private Integer fullPlayTime;
    private Boolean visibleToOthers;
    private String bedtimeStoryOwnerId;
    private String shortDescription;
    private String longDescription;
    private List<String> tags;
    private BedtimeStoryAudioSource audioSource;
    private BedtimeStoryApprovalStatus approvalStatus;
    private BedtimeStoryCreationStatus creationStatus;
    @Override
     public BedtimeStoryInfoData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new BedtimeStoryInfoData(
          id,
          bedtimeStoryOwner,
          bedtimeStoryOwnerId,
          displayName,
          shortDescription,
          longDescription,
          audioKeyS3,
          icon,
          fullPlayTime,
          visibleToOthers,
          tags,
          audioSource,
          approvalStatus,
          creationStatus);
    }
    
    @Override
     public DisplayNameStep bedtimeStoryOwner(UserData bedtimeStoryOwner) {
        Objects.requireNonNull(bedtimeStoryOwner);
        this.bedtimeStoryOwner = bedtimeStoryOwner;
        return this;
    }
    
    @Override
     public AudioKeyS3Step displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.displayName = displayName;
        return this;
    }
    
    @Override
     public IconStep audioKeyS3(String audioKeyS3) {
        Objects.requireNonNull(audioKeyS3);
        this.audioKeyS3 = audioKeyS3;
        return this;
    }
    
    @Override
     public FullPlayTimeStep icon(Integer icon) {
        Objects.requireNonNull(icon);
        this.icon = icon;
        return this;
    }
    
    @Override
     public VisibleToOthersStep fullPlayTime(Integer fullPlayTime) {
        Objects.requireNonNull(fullPlayTime);
        this.fullPlayTime = fullPlayTime;
        return this;
    }
    
    @Override
     public BuildStep visibleToOthers(Boolean visibleToOthers) {
        Objects.requireNonNull(visibleToOthers);
        this.visibleToOthers = visibleToOthers;
        return this;
    }
    
    @Override
     public BuildStep bedtimeStoryOwnerId(String bedtimeStoryOwnerId) {
        this.bedtimeStoryOwnerId = bedtimeStoryOwnerId;
        return this;
    }
    
    @Override
     public BuildStep shortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }
    
    @Override
     public BuildStep longDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }
    
    @Override
     public BuildStep tags(List<String> tags) {
        this.tags = tags;
        return this;
    }
    
    @Override
     public BuildStep audioSource(BedtimeStoryAudioSource audioSource) {
        this.audioSource = audioSource;
        return this;
    }
    
    @Override
     public BuildStep approvalStatus(BedtimeStoryApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
        return this;
    }
    
    @Override
     public BuildStep creationStatus(BedtimeStoryCreationStatus creationStatus) {
        this.creationStatus = creationStatus;
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
    private CopyOfBuilder(String id, UserData bedtimeStoryOwner, String bedtimeStoryOwnerId, String displayName, String shortDescription, String longDescription, String audioKeyS3, Integer icon, Integer fullPlayTime, Boolean visibleToOthers, List<String> tags, BedtimeStoryAudioSource audioSource, BedtimeStoryApprovalStatus approvalStatus, BedtimeStoryCreationStatus creationStatus) {
      super.id(id);
      super.bedtimeStoryOwner(bedtimeStoryOwner)
        .displayName(displayName)
        .audioKeyS3(audioKeyS3)
        .icon(icon)
        .fullPlayTime(fullPlayTime)
        .visibleToOthers(visibleToOthers)
        .bedtimeStoryOwnerId(bedtimeStoryOwnerId)
        .shortDescription(shortDescription)
        .longDescription(longDescription)
        .tags(tags)
        .audioSource(audioSource)
        .approvalStatus(approvalStatus)
        .creationStatus(creationStatus);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryOwner(UserData bedtimeStoryOwner) {
      return (CopyOfBuilder) super.bedtimeStoryOwner(bedtimeStoryOwner);
    }
    
    @Override
     public CopyOfBuilder displayName(String displayName) {
      return (CopyOfBuilder) super.displayName(displayName);
    }
    
    @Override
     public CopyOfBuilder audioKeyS3(String audioKeyS3) {
      return (CopyOfBuilder) super.audioKeyS3(audioKeyS3);
    }
    
    @Override
     public CopyOfBuilder icon(Integer icon) {
      return (CopyOfBuilder) super.icon(icon);
    }
    
    @Override
     public CopyOfBuilder fullPlayTime(Integer fullPlayTime) {
      return (CopyOfBuilder) super.fullPlayTime(fullPlayTime);
    }
    
    @Override
     public CopyOfBuilder visibleToOthers(Boolean visibleToOthers) {
      return (CopyOfBuilder) super.visibleToOthers(visibleToOthers);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryOwnerId(String bedtimeStoryOwnerId) {
      return (CopyOfBuilder) super.bedtimeStoryOwnerId(bedtimeStoryOwnerId);
    }
    
    @Override
     public CopyOfBuilder shortDescription(String shortDescription) {
      return (CopyOfBuilder) super.shortDescription(shortDescription);
    }
    
    @Override
     public CopyOfBuilder longDescription(String longDescription) {
      return (CopyOfBuilder) super.longDescription(longDescription);
    }
    
    @Override
     public CopyOfBuilder tags(List<String> tags) {
      return (CopyOfBuilder) super.tags(tags);
    }
    
    @Override
     public CopyOfBuilder audioSource(BedtimeStoryAudioSource audioSource) {
      return (CopyOfBuilder) super.audioSource(audioSource);
    }
    
    @Override
     public CopyOfBuilder approvalStatus(BedtimeStoryApprovalStatus approvalStatus) {
      return (CopyOfBuilder) super.approvalStatus(approvalStatus);
    }
    
    @Override
     public CopyOfBuilder creationStatus(BedtimeStoryCreationStatus creationStatus) {
      return (CopyOfBuilder) super.creationStatus(creationStatus);
    }
  }
  
}
