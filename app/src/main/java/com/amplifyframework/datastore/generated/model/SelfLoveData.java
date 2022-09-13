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
  public static final QueryField DISPLAY_NAME = field("SelfLoveData", "display_name");
  public static final QueryField DESCRIPTION = field("SelfLoveData", "description");
  public static final QueryField AUDIO_KEY_S3 = field("SelfLoveData", "audioKeyS3");
  public static final QueryField ICON = field("SelfLoveData", "icon");
  public static final QueryField FULL_PLAY_TIME = field("SelfLoveData", "fullPlayTime");
  public static final QueryField VISIBLE_TO_OTHERS = field("SelfLoveData", "visibleToOthers");
  public static final QueryField LYRICS = field("SelfLoveData", "lyrics");
  public static final QueryField TAGS = field("SelfLoveData", "tags");
  public static final QueryField AUDIO_SOURCE = field("SelfLoveData", "audioSource");
  public static final QueryField APPROVAL_STATUS = field("SelfLoveData", "approvalStatus");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData selfLoveOwner;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="String", isRequired = true) String description;
  private final @ModelField(targetType="String", isRequired = true) String audioKeyS3;
  private final @ModelField(targetType="Int", isRequired = true) Integer icon;
  private final @ModelField(targetType="Int", isRequired = true) Integer fullPlayTime;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean visibleToOthers;
  private final @ModelField(targetType="String") List<String> lyrics;
  private final @ModelField(targetType="String") List<String> tags;
  private final @ModelField(targetType="SelfLoveAudioSource") SelfLoveAudioSource audioSource;
  private final @ModelField(targetType="SelfLoveApprovalStatus") SelfLoveApprovalStatus approvalStatus;
  private final @ModelField(targetType="RoutineSelfLove") @HasMany(associatedWith = "selfLoveData", type = RoutineSelfLove.class) List<RoutineSelfLove> routines = null;
  private final @ModelField(targetType="UserSelfLove") @HasMany(associatedWith = "selfLoveData", type = UserSelfLove.class) List<UserSelfLove> users = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getSelfLoveOwner() {
      return selfLoveOwner;
  }
  
  public String getDisplayName() {
      return display_name;
  }
  
  public String getDescription() {
      return description;
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
  
  public SelfLoveAudioSource getAudioSource() {
      return audioSource;
  }
  
  public SelfLoveApprovalStatus getApprovalStatus() {
      return approvalStatus;
  }
  
  public List<RoutineSelfLove> getRoutines() {
      return routines;
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
  
  private SelfLoveData(String id, UserData selfLoveOwner, String display_name, String description, String audioKeyS3, Integer icon, Integer fullPlayTime, Boolean visibleToOthers, List<String> lyrics, List<String> tags, SelfLoveAudioSource audioSource, SelfLoveApprovalStatus approvalStatus) {
    this.id = id;
    this.selfLoveOwner = selfLoveOwner;
    this.display_name = display_name;
    this.description = description;
    this.audioKeyS3 = audioKeyS3;
    this.icon = icon;
    this.fullPlayTime = fullPlayTime;
    this.visibleToOthers = visibleToOthers;
    this.lyrics = lyrics;
    this.tags = tags;
    this.audioSource = audioSource;
    this.approvalStatus = approvalStatus;
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
              ObjectsCompat.equals(getDisplayName(), selfLoveData.getDisplayName()) &&
              ObjectsCompat.equals(getDescription(), selfLoveData.getDescription()) &&
              ObjectsCompat.equals(getAudioKeyS3(), selfLoveData.getAudioKeyS3()) &&
              ObjectsCompat.equals(getIcon(), selfLoveData.getIcon()) &&
              ObjectsCompat.equals(getFullPlayTime(), selfLoveData.getFullPlayTime()) &&
              ObjectsCompat.equals(getVisibleToOthers(), selfLoveData.getVisibleToOthers()) &&
              ObjectsCompat.equals(getLyrics(), selfLoveData.getLyrics()) &&
              ObjectsCompat.equals(getTags(), selfLoveData.getTags()) &&
              ObjectsCompat.equals(getAudioSource(), selfLoveData.getAudioSource()) &&
              ObjectsCompat.equals(getApprovalStatus(), selfLoveData.getApprovalStatus()) &&
              ObjectsCompat.equals(getCreatedAt(), selfLoveData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), selfLoveData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getSelfLoveOwner())
      .append(getDisplayName())
      .append(getDescription())
      .append(getAudioKeyS3())
      .append(getIcon())
      .append(getFullPlayTime())
      .append(getVisibleToOthers())
      .append(getLyrics())
      .append(getTags())
      .append(getAudioSource())
      .append(getApprovalStatus())
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
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("audioKeyS3=" + String.valueOf(getAudioKeyS3()) + ", ")
      .append("icon=" + String.valueOf(getIcon()) + ", ")
      .append("fullPlayTime=" + String.valueOf(getFullPlayTime()) + ", ")
      .append("visibleToOthers=" + String.valueOf(getVisibleToOthers()) + ", ")
      .append("lyrics=" + String.valueOf(getLyrics()) + ", ")
      .append("tags=" + String.valueOf(getTags()) + ", ")
      .append("audioSource=" + String.valueOf(getAudioSource()) + ", ")
      .append("approvalStatus=" + String.valueOf(getApprovalStatus()) + ", ")
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
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      selfLoveOwner,
      display_name,
      description,
      audioKeyS3,
      icon,
      fullPlayTime,
      visibleToOthers,
      lyrics,
      tags,
      audioSource,
      approvalStatus);
  }
  public interface SelfLoveOwnerStep {
    DisplayNameStep selfLoveOwner(UserData selfLoveOwner);
  }
  

  public interface DisplayNameStep {
    DescriptionStep displayName(String displayName);
  }
  

  public interface DescriptionStep {
    AudioKeyS3Step description(String description);
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
    BuildStep lyrics(List<String> lyrics);
    BuildStep tags(List<String> tags);
    BuildStep audioSource(SelfLoveAudioSource audioSource);
    BuildStep approvalStatus(SelfLoveApprovalStatus approvalStatus);
  }
  

  public static class Builder implements SelfLoveOwnerStep, DisplayNameStep, DescriptionStep, AudioKeyS3Step, IconStep, FullPlayTimeStep, VisibleToOthersStep, BuildStep {
    private String id;
    private UserData selfLoveOwner;
    private String display_name;
    private String description;
    private String audioKeyS3;
    private Integer icon;
    private Integer fullPlayTime;
    private Boolean visibleToOthers;
    private List<String> lyrics;
    private List<String> tags;
    private SelfLoveAudioSource audioSource;
    private SelfLoveApprovalStatus approvalStatus;
    @Override
     public SelfLoveData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new SelfLoveData(
          id,
          selfLoveOwner,
          display_name,
          description,
          audioKeyS3,
          icon,
          fullPlayTime,
          visibleToOthers,
          lyrics,
          tags,
          audioSource,
          approvalStatus);
    }
    
    @Override
     public DisplayNameStep selfLoveOwner(UserData selfLoveOwner) {
        Objects.requireNonNull(selfLoveOwner);
        this.selfLoveOwner = selfLoveOwner;
        return this;
    }
    
    @Override
     public DescriptionStep displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.display_name = displayName;
        return this;
    }
    
    @Override
     public AudioKeyS3Step description(String description) {
        Objects.requireNonNull(description);
        this.description = description;
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
     public BuildStep audioSource(SelfLoveAudioSource audioSource) {
        this.audioSource = audioSource;
        return this;
    }
    
    @Override
     public BuildStep approvalStatus(SelfLoveApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
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
    private CopyOfBuilder(String id, UserData selfLoveOwner, String displayName, String description, String audioKeyS3, Integer icon, Integer fullPlayTime, Boolean visibleToOthers, List<String> lyrics, List<String> tags, SelfLoveAudioSource audioSource, SelfLoveApprovalStatus approvalStatus) {
      super.id(id);
      super.selfLoveOwner(selfLoveOwner)
        .displayName(displayName)
        .description(description)
        .audioKeyS3(audioKeyS3)
        .icon(icon)
        .fullPlayTime(fullPlayTime)
        .visibleToOthers(visibleToOthers)
        .lyrics(lyrics)
        .tags(tags)
        .audioSource(audioSource)
        .approvalStatus(approvalStatus);
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
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
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
     public CopyOfBuilder lyrics(List<String> lyrics) {
      return (CopyOfBuilder) super.lyrics(lyrics);
    }
    
    @Override
     public CopyOfBuilder tags(List<String> tags) {
      return (CopyOfBuilder) super.tags(tags);
    }
    
    @Override
     public CopyOfBuilder audioSource(SelfLoveAudioSource audioSource) {
      return (CopyOfBuilder) super.audioSource(audioSource);
    }
    
    @Override
     public CopyOfBuilder approvalStatus(SelfLoveApprovalStatus approvalStatus) {
      return (CopyOfBuilder) super.approvalStatus(approvalStatus);
    }
  }
  
}
