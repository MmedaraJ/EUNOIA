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

/** This is an auto generated class representing the SelfLoveData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "SelfLoveData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "SelfLovesOwnedByUser", fields = {"userDataID","display_name"})
public final class SelfLoveData implements Model {
  public static final QueryField ID = field("SelfLoveData", "id");
  public static final QueryField SELF_LOVE_OWNER = field("SelfLoveData", "userDataID");
  public static final QueryField SELF_LOVE_OWNER_ID = field("SelfLoveData", "selfLoveOwnerId");
  public static final QueryField DISPLAY_NAME = field("SelfLoveData", "display_name");
  public static final QueryField SHORT_DESCRIPTION = field("SelfLoveData", "shortDescription");
  public static final QueryField LONG_DESCRIPTION = field("SelfLoveData", "longDescription");
  public static final QueryField AUDIO_KEY_S3 = field("SelfLoveData", "audioKeyS3");
  public static final QueryField ICON = field("SelfLoveData", "icon");
  public static final QueryField FULL_PLAY_TIME = field("SelfLoveData", "fullPlayTime");
  public static final QueryField VISIBLE_TO_OTHERS = field("SelfLoveData", "visibleToOthers");
  public static final QueryField LYRICS = field("SelfLoveData", "lyrics");
  public static final QueryField TAGS = field("SelfLoveData", "tags");
  public static final QueryField AUDIO_KEYS_S3 = field("SelfLoveData", "audioKeysS3");
  public static final QueryField AUDIO_NAMES = field("SelfLoveData", "audioNames");
  public static final QueryField AUDIO_LENGTHS = field("SelfLoveData", "audioLengths");
  public static final QueryField AUDIO_SOURCE = field("SelfLoveData", "audioSource");
  public static final QueryField APPROVAL_STATUS = field("SelfLoveData", "approvalStatus");
  public static final QueryField CREATION_STATUS = field("SelfLoveData", "creationStatus");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData selfLoveOwner;
  private final @ModelField(targetType="String") String selfLoveOwnerId;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="String") String shortDescription;
  private final @ModelField(targetType="String") String longDescription;
  private final @ModelField(targetType="String", isRequired = true) String audioKeyS3;
  private final @ModelField(targetType="Int", isRequired = true) Integer icon;
  private final @ModelField(targetType="Int", isRequired = true) Integer fullPlayTime;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean visibleToOthers;
  private final @ModelField(targetType="String") List<String> lyrics;
  private final @ModelField(targetType="String") List<String> tags;
  private final @ModelField(targetType="String") List<String> audioKeysS3;
  private final @ModelField(targetType="String") List<String> audioNames;
  private final @ModelField(targetType="Int") List<Integer> audioLengths;
  private final @ModelField(targetType="SelfLoveAudioSource") SelfLoveAudioSource audioSource;
  private final @ModelField(targetType="SelfLoveApprovalStatus") SelfLoveApprovalStatus approvalStatus;
  private final @ModelField(targetType="SelfLoveCreationStatus") SelfLoveCreationStatus creationStatus;
  private final @ModelField(targetType="UserSelfLoveRelationship") @HasMany(associatedWith = "userSelfLoveRelationshipSelfLove", type = UserSelfLoveRelationship.class) List<UserSelfLoveRelationship> userSelfLoveRelationshipsOwnedBySelfLove = null;
  private final @ModelField(targetType="RoutineSelfLove") @HasMany(associatedWith = "selfLoveData", type = RoutineSelfLove.class) List<RoutineSelfLove> routines = null;
  private final @ModelField(targetType="UserRoutineRelationshipSelfLove") @HasMany(associatedWith = "selfLoveData", type = UserRoutineRelationshipSelfLove.class) List<UserRoutineRelationshipSelfLove> userRoutineRelationships = null;
  private final @ModelField(targetType="UserSelfLove") @HasMany(associatedWith = "selfLoveData", type = UserSelfLove.class) List<UserSelfLove> users = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getSelfLoveOwner() {
      return selfLoveOwner;
  }
  
  public String getSelfLoveOwnerId() {
      return selfLoveOwnerId;
  }
  
  public String getDisplayName() {
      return display_name;
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
  
  public List<String> getLyrics() {
      return lyrics;
  }
  
  public List<String> getTags() {
      return tags;
  }
  
  public List<String> getAudioKeysS3() {
      return audioKeysS3;
  }
  
  public List<String> getAudioNames() {
      return audioNames;
  }
  
  public List<Integer> getAudioLengths() {
      return audioLengths;
  }
  
  public SelfLoveAudioSource getAudioSource() {
      return audioSource;
  }
  
  public SelfLoveApprovalStatus getApprovalStatus() {
      return approvalStatus;
  }
  
  public SelfLoveCreationStatus getCreationStatus() {
      return creationStatus;
  }
  
  public List<UserSelfLoveRelationship> getUserSelfLoveRelationshipsOwnedBySelfLove() {
      return userSelfLoveRelationshipsOwnedBySelfLove;
  }
  
  public List<RoutineSelfLove> getRoutines() {
      return routines;
  }
  
  public List<UserRoutineRelationshipSelfLove> getUserRoutineRelationships() {
      return userRoutineRelationships;
  }
  
  public List<UserSelfLove> getUsers() {
      return users;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private SelfLoveData(String id, UserData selfLoveOwner, String selfLoveOwnerId, String display_name, String shortDescription, String longDescription, String audioKeyS3, Integer icon, Integer fullPlayTime, Boolean visibleToOthers, List<String> lyrics, List<String> tags, List<String> audioKeysS3, List<String> audioNames, List<Integer> audioLengths, SelfLoveAudioSource audioSource, SelfLoveApprovalStatus approvalStatus, SelfLoveCreationStatus creationStatus) {
    this.id = id;
    this.selfLoveOwner = selfLoveOwner;
    this.selfLoveOwnerId = selfLoveOwnerId;
    this.display_name = display_name;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.audioKeyS3 = audioKeyS3;
    this.icon = icon;
    this.fullPlayTime = fullPlayTime;
    this.visibleToOthers = visibleToOthers;
    this.lyrics = lyrics;
    this.tags = tags;
    this.audioKeysS3 = audioKeysS3;
    this.audioNames = audioNames;
    this.audioLengths = audioLengths;
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
      SelfLoveData selfLoveData = (SelfLoveData) obj;
      return ObjectsCompat.equals(getId(), selfLoveData.getId()) &&
              ObjectsCompat.equals(getSelfLoveOwner(), selfLoveData.getSelfLoveOwner()) &&
              ObjectsCompat.equals(getSelfLoveOwnerId(), selfLoveData.getSelfLoveOwnerId()) &&
              ObjectsCompat.equals(getDisplayName(), selfLoveData.getDisplayName()) &&
              ObjectsCompat.equals(getShortDescription(), selfLoveData.getShortDescription()) &&
              ObjectsCompat.equals(getLongDescription(), selfLoveData.getLongDescription()) &&
              ObjectsCompat.equals(getAudioKeyS3(), selfLoveData.getAudioKeyS3()) &&
              ObjectsCompat.equals(getIcon(), selfLoveData.getIcon()) &&
              ObjectsCompat.equals(getFullPlayTime(), selfLoveData.getFullPlayTime()) &&
              ObjectsCompat.equals(getVisibleToOthers(), selfLoveData.getVisibleToOthers()) &&
              ObjectsCompat.equals(getLyrics(), selfLoveData.getLyrics()) &&
              ObjectsCompat.equals(getTags(), selfLoveData.getTags()) &&
              ObjectsCompat.equals(getAudioKeysS3(), selfLoveData.getAudioKeysS3()) &&
              ObjectsCompat.equals(getAudioNames(), selfLoveData.getAudioNames()) &&
              ObjectsCompat.equals(getAudioLengths(), selfLoveData.getAudioLengths()) &&
              ObjectsCompat.equals(getAudioSource(), selfLoveData.getAudioSource()) &&
              ObjectsCompat.equals(getApprovalStatus(), selfLoveData.getApprovalStatus()) &&
              ObjectsCompat.equals(getCreationStatus(), selfLoveData.getCreationStatus()) &&
              ObjectsCompat.equals(getCreatedAt(), selfLoveData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), selfLoveData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getSelfLoveOwner())
      .append(getSelfLoveOwnerId())
      .append(getDisplayName())
      .append(getShortDescription())
      .append(getLongDescription())
      .append(getAudioKeyS3())
      .append(getIcon())
      .append(getFullPlayTime())
      .append(getVisibleToOthers())
      .append(getLyrics())
      .append(getTags())
      .append(getAudioKeysS3())
      .append(getAudioNames())
      .append(getAudioLengths())
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
      .append("SelfLoveData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("selfLoveOwner=" + String.valueOf(getSelfLoveOwner()) + ", ")
      .append("selfLoveOwnerId=" + String.valueOf(getSelfLoveOwnerId()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("shortDescription=" + String.valueOf(getShortDescription()) + ", ")
      .append("longDescription=" + String.valueOf(getLongDescription()) + ", ")
      .append("audioKeyS3=" + String.valueOf(getAudioKeyS3()) + ", ")
      .append("icon=" + String.valueOf(getIcon()) + ", ")
      .append("fullPlayTime=" + String.valueOf(getFullPlayTime()) + ", ")
      .append("visibleToOthers=" + String.valueOf(getVisibleToOthers()) + ", ")
      .append("lyrics=" + String.valueOf(getLyrics()) + ", ")
      .append("tags=" + String.valueOf(getTags()) + ", ")
      .append("audioKeysS3=" + String.valueOf(getAudioKeysS3()) + ", ")
      .append("audioNames=" + String.valueOf(getAudioNames()) + ", ")
      .append("audioLengths=" + String.valueOf(getAudioLengths()) + ", ")
      .append("audioSource=" + String.valueOf(getAudioSource()) + ", ")
      .append("approvalStatus=" + String.valueOf(getApprovalStatus()) + ", ")
      .append("creationStatus=" + String.valueOf(getCreationStatus()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static SelfLoveOwnerStep builder() {
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
  public static SelfLoveData justId(String id) {
    return new SelfLoveData(
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
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      selfLoveOwner,
      selfLoveOwnerId,
      display_name,
      shortDescription,
      longDescription,
      audioKeyS3,
      icon,
      fullPlayTime,
      visibleToOthers,
      lyrics,
      tags,
      audioKeysS3,
      audioNames,
      audioLengths,
      audioSource,
      approvalStatus,
      creationStatus);
  }
  public interface SelfLoveOwnerStep {
    DisplayNameStep selfLoveOwner(UserData selfLoveOwner);
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
    SelfLoveData build();
    BuildStep id(String id);
    BuildStep selfLoveOwnerId(String selfLoveOwnerId);
    BuildStep shortDescription(String shortDescription);
    BuildStep longDescription(String longDescription);
    BuildStep lyrics(List<String> lyrics);
    BuildStep tags(List<String> tags);
    BuildStep audioKeysS3(List<String> audioKeysS3);
    BuildStep audioNames(List<String> audioNames);
    BuildStep audioLengths(List<Integer> audioLengths);
    BuildStep audioSource(SelfLoveAudioSource audioSource);
    BuildStep approvalStatus(SelfLoveApprovalStatus approvalStatus);
    BuildStep creationStatus(SelfLoveCreationStatus creationStatus);
  }
  

  public static class Builder implements SelfLoveOwnerStep, DisplayNameStep, AudioKeyS3Step, IconStep, FullPlayTimeStep, VisibleToOthersStep, BuildStep {
    private String id;
    private UserData selfLoveOwner;
    private String display_name;
    private String audioKeyS3;
    private Integer icon;
    private Integer fullPlayTime;
    private Boolean visibleToOthers;
    private String selfLoveOwnerId;
    private String shortDescription;
    private String longDescription;
    private List<String> lyrics;
    private List<String> tags;
    private List<String> audioKeysS3;
    private List<String> audioNames;
    private List<Integer> audioLengths;
    private SelfLoveAudioSource audioSource;
    private SelfLoveApprovalStatus approvalStatus;
    private SelfLoveCreationStatus creationStatus;
    @Override
     public SelfLoveData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new SelfLoveData(
          id,
          selfLoveOwner,
          selfLoveOwnerId,
          display_name,
          shortDescription,
          longDescription,
          audioKeyS3,
          icon,
          fullPlayTime,
          visibleToOthers,
          lyrics,
          tags,
          audioKeysS3,
          audioNames,
          audioLengths,
          audioSource,
          approvalStatus,
          creationStatus);
    }
    
    @Override
     public DisplayNameStep selfLoveOwner(UserData selfLoveOwner) {
        Objects.requireNonNull(selfLoveOwner);
        this.selfLoveOwner = selfLoveOwner;
        return this;
    }
    
    @Override
     public AudioKeyS3Step displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.display_name = displayName;
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
     public BuildStep selfLoveOwnerId(String selfLoveOwnerId) {
        this.selfLoveOwnerId = selfLoveOwnerId;
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
     public BuildStep lyrics(List<String> lyrics) {
        this.lyrics = lyrics;
        return this;
    }
    
    @Override
     public BuildStep tags(List<String> tags) {
        this.tags = tags;
        return this;
    }
    
    @Override
     public BuildStep audioKeysS3(List<String> audioKeysS3) {
        this.audioKeysS3 = audioKeysS3;
        return this;
    }
    
    @Override
     public BuildStep audioNames(List<String> audioNames) {
        this.audioNames = audioNames;
        return this;
    }
    
    @Override
     public BuildStep audioLengths(List<Integer> audioLengths) {
        this.audioLengths = audioLengths;
        return this;
    }
    
    @Override
     public BuildStep audioSource(SelfLoveAudioSource audioSource) {
        this.audioSource = audioSource;
        return this;
    }
    
    @Override
     public BuildStep approvalStatus(SelfLoveApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
        return this;
    }
    
    @Override
     public BuildStep creationStatus(SelfLoveCreationStatus creationStatus) {
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
    private CopyOfBuilder(String id, UserData selfLoveOwner, String selfLoveOwnerId, String displayName, String shortDescription, String longDescription, String audioKeyS3, Integer icon, Integer fullPlayTime, Boolean visibleToOthers, List<String> lyrics, List<String> tags, List<String> audioKeysS3, List<String> audioNames, List<Integer> audioLengths, SelfLoveAudioSource audioSource, SelfLoveApprovalStatus approvalStatus, SelfLoveCreationStatus creationStatus) {
      super.id(id);
      super.selfLoveOwner(selfLoveOwner)
        .displayName(displayName)
        .audioKeyS3(audioKeyS3)
        .icon(icon)
        .fullPlayTime(fullPlayTime)
        .visibleToOthers(visibleToOthers)
        .selfLoveOwnerId(selfLoveOwnerId)
        .shortDescription(shortDescription)
        .longDescription(longDescription)
        .lyrics(lyrics)
        .tags(tags)
        .audioKeysS3(audioKeysS3)
        .audioNames(audioNames)
        .audioLengths(audioLengths)
        .audioSource(audioSource)
        .approvalStatus(approvalStatus)
        .creationStatus(creationStatus);
    }
    
    @Override
     public CopyOfBuilder selfLoveOwner(UserData selfLoveOwner) {
      return (CopyOfBuilder) super.selfLoveOwner(selfLoveOwner);
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
     public CopyOfBuilder selfLoveOwnerId(String selfLoveOwnerId) {
      return (CopyOfBuilder) super.selfLoveOwnerId(selfLoveOwnerId);
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
     public CopyOfBuilder lyrics(List<String> lyrics) {
      return (CopyOfBuilder) super.lyrics(lyrics);
    }
    
    @Override
     public CopyOfBuilder tags(List<String> tags) {
      return (CopyOfBuilder) super.tags(tags);
    }
    
    @Override
     public CopyOfBuilder audioKeysS3(List<String> audioKeysS3) {
      return (CopyOfBuilder) super.audioKeysS3(audioKeysS3);
    }
    
    @Override
     public CopyOfBuilder audioNames(List<String> audioNames) {
      return (CopyOfBuilder) super.audioNames(audioNames);
    }
    
    @Override
     public CopyOfBuilder audioLengths(List<Integer> audioLengths) {
      return (CopyOfBuilder) super.audioLengths(audioLengths);
    }
    
    @Override
     public CopyOfBuilder audioSource(SelfLoveAudioSource audioSource) {
      return (CopyOfBuilder) super.audioSource(audioSource);
    }
    
    @Override
     public CopyOfBuilder approvalStatus(SelfLoveApprovalStatus approvalStatus) {
      return (CopyOfBuilder) super.approvalStatus(approvalStatus);
    }
    
    @Override
     public CopyOfBuilder creationStatus(SelfLoveCreationStatus creationStatus) {
      return (CopyOfBuilder) super.creationStatus(creationStatus);
    }
  }
  
}
