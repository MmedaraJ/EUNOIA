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

/** This is an auto generated class representing the SoundData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "SoundData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "SoundsOwnedByUser", fields = {"userDataID","display_name"})
public final class SoundData implements Model {
  public static final QueryField ID = field("SoundData", "id");
  public static final QueryField SOUND_OWNER = field("SoundData", "userDataID");
  public static final QueryField ORIGINAL_NAME = field("SoundData", "original_name");
  public static final QueryField DISPLAY_NAME = field("SoundData", "display_name");
  public static final QueryField SHORT_DESCRIPTION = field("SoundData", "short_description");
  public static final QueryField LONG_DESCRIPTION = field("SoundData", "long_description");
  public static final QueryField AUDIO_KEY_S3 = field("SoundData", "audio_key_s3");
  public static final QueryField ICON = field("SoundData", "icon");
  public static final QueryField COLOR_HEX = field("SoundData", "colorHEX");
  public static final QueryField FULL_PLAY_TIME = field("SoundData", "fullPlayTime");
  public static final QueryField VISIBLE_TO_OTHERS = field("SoundData", "visible_to_others");
  public static final QueryField TAGS = field("SoundData", "tags");
  public static final QueryField AUDIO_NAMES = field("SoundData", "audio_names");
  public static final QueryField APPROVAL_STATUS = field("SoundData", "approvalStatus");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData soundOwner;
  private final @ModelField(targetType="String", isRequired = true) String original_name;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="String") String short_description;
  private final @ModelField(targetType="String") String long_description;
  private final @ModelField(targetType="String", isRequired = true) String audio_key_s3;
  private final @ModelField(targetType="Int", isRequired = true) Integer icon;
  private final @ModelField(targetType="Int", isRequired = true) Integer colorHEX;
  private final @ModelField(targetType="Int", isRequired = true) Integer fullPlayTime;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean visible_to_others;
  private final @ModelField(targetType="String", isRequired = true) List<String> tags;
  private final @ModelField(targetType="String", isRequired = true) List<String> audio_names;
  private final @ModelField(targetType="SoundApprovalStatus") SoundApprovalStatus approvalStatus;
  private final @ModelField(targetType="CommentData") @HasMany(associatedWith = "sound", type = CommentData.class) List<CommentData> commentsOwnedBySound = null;
  private final @ModelField(targetType="PresetData") @HasMany(associatedWith = "sound", type = PresetData.class) List<PresetData> presetsOwnedBySound = null;
  private final @ModelField(targetType="UserSound") @HasMany(associatedWith = "soundData", type = UserSound.class) List<UserSound> users = null;
  private final @ModelField(targetType="RoutineSound") @HasMany(associatedWith = "soundData", type = RoutineSound.class) List<RoutineSound> routines = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getSoundOwner() {
      return soundOwner;
  }
  
  public String getOriginalName() {
      return original_name;
  }
  
  public String getDisplayName() {
      return display_name;
  }
  
  public String getShortDescription() {
      return short_description;
  }
  
  public String getLongDescription() {
      return long_description;
  }
  
  public String getAudioKeyS3() {
      return audio_key_s3;
  }
  
  public Integer getIcon() {
      return icon;
  }
  
  public Integer getColorHex() {
      return colorHEX;
  }
  
  public Integer getFullPlayTime() {
      return fullPlayTime;
  }
  
  public Boolean getVisibleToOthers() {
      return visible_to_others;
  }
  
  public List<String> getTags() {
      return tags;
  }
  
  public List<String> getAudioNames() {
      return audio_names;
  }
  
  public SoundApprovalStatus getApprovalStatus() {
      return approvalStatus;
  }
  
  public List<CommentData> getCommentsOwnedBySound() {
      return commentsOwnedBySound;
  }
  
  public List<PresetData> getPresetsOwnedBySound() {
      return presetsOwnedBySound;
  }
  
  public List<UserSound> getUsers() {
      return users;
  }
  
  public List<RoutineSound> getRoutines() {
      return routines;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private SoundData(String id, UserData soundOwner, String original_name, String display_name, String short_description, String long_description, String audio_key_s3, Integer icon, Integer colorHEX, Integer fullPlayTime, Boolean visible_to_others, List<String> tags, List<String> audio_names, SoundApprovalStatus approvalStatus) {
    this.id = id;
    this.soundOwner = soundOwner;
    this.original_name = original_name;
    this.display_name = display_name;
    this.short_description = short_description;
    this.long_description = long_description;
    this.audio_key_s3 = audio_key_s3;
    this.icon = icon;
    this.colorHEX = colorHEX;
    this.fullPlayTime = fullPlayTime;
    this.visible_to_others = visible_to_others;
    this.tags = tags;
    this.audio_names = audio_names;
    this.approvalStatus = approvalStatus;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      SoundData soundData = (SoundData) obj;
      return ObjectsCompat.equals(getId(), soundData.getId()) &&
              ObjectsCompat.equals(getSoundOwner(), soundData.getSoundOwner()) &&
              ObjectsCompat.equals(getOriginalName(), soundData.getOriginalName()) &&
              ObjectsCompat.equals(getDisplayName(), soundData.getDisplayName()) &&
              ObjectsCompat.equals(getShortDescription(), soundData.getShortDescription()) &&
              ObjectsCompat.equals(getLongDescription(), soundData.getLongDescription()) &&
              ObjectsCompat.equals(getAudioKeyS3(), soundData.getAudioKeyS3()) &&
              ObjectsCompat.equals(getIcon(), soundData.getIcon()) &&
              ObjectsCompat.equals(getColorHex(), soundData.getColorHex()) &&
              ObjectsCompat.equals(getFullPlayTime(), soundData.getFullPlayTime()) &&
              ObjectsCompat.equals(getVisibleToOthers(), soundData.getVisibleToOthers()) &&
              ObjectsCompat.equals(getTags(), soundData.getTags()) &&
              ObjectsCompat.equals(getAudioNames(), soundData.getAudioNames()) &&
              ObjectsCompat.equals(getApprovalStatus(), soundData.getApprovalStatus()) &&
              ObjectsCompat.equals(getCreatedAt(), soundData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), soundData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getSoundOwner())
      .append(getOriginalName())
      .append(getDisplayName())
      .append(getShortDescription())
      .append(getLongDescription())
      .append(getAudioKeyS3())
      .append(getIcon())
      .append(getColorHex())
      .append(getFullPlayTime())
      .append(getVisibleToOthers())
      .append(getTags())
      .append(getAudioNames())
      .append(getApprovalStatus())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("SoundData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("soundOwner=" + String.valueOf(getSoundOwner()) + ", ")
      .append("original_name=" + String.valueOf(getOriginalName()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("short_description=" + String.valueOf(getShortDescription()) + ", ")
      .append("long_description=" + String.valueOf(getLongDescription()) + ", ")
      .append("audio_key_s3=" + String.valueOf(getAudioKeyS3()) + ", ")
      .append("icon=" + String.valueOf(getIcon()) + ", ")
      .append("colorHEX=" + String.valueOf(getColorHex()) + ", ")
      .append("fullPlayTime=" + String.valueOf(getFullPlayTime()) + ", ")
      .append("visible_to_others=" + String.valueOf(getVisibleToOthers()) + ", ")
      .append("tags=" + String.valueOf(getTags()) + ", ")
      .append("audio_names=" + String.valueOf(getAudioNames()) + ", ")
      .append("approvalStatus=" + String.valueOf(getApprovalStatus()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static SoundOwnerStep builder() {
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
  public static SoundData justId(String id) {
    return new SoundData(
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
      soundOwner,
      original_name,
      display_name,
      short_description,
      long_description,
      audio_key_s3,
      icon,
      colorHEX,
      fullPlayTime,
      visible_to_others,
      tags,
      audio_names,
      approvalStatus);
  }
  public interface SoundOwnerStep {
    OriginalNameStep soundOwner(UserData soundOwner);
  }
  

  public interface OriginalNameStep {
    DisplayNameStep originalName(String originalName);
  }
  

  public interface DisplayNameStep {
    AudioKeyS3Step displayName(String displayName);
  }
  

  public interface AudioKeyS3Step {
    IconStep audioKeyS3(String audioKeyS3);
  }
  

  public interface IconStep {
    ColorHexStep icon(Integer icon);
  }
  

  public interface ColorHexStep {
    FullPlayTimeStep colorHex(Integer colorHex);
  }
  

  public interface FullPlayTimeStep {
    VisibleToOthersStep fullPlayTime(Integer fullPlayTime);
  }
  

  public interface VisibleToOthersStep {
    TagsStep visibleToOthers(Boolean visibleToOthers);
  }
  

  public interface TagsStep {
    AudioNamesStep tags(List<String> tags);
  }
  

  public interface AudioNamesStep {
    BuildStep audioNames(List<String> audioNames);
  }
  

  public interface BuildStep {
    SoundData build();
    BuildStep id(String id);
    BuildStep shortDescription(String shortDescription);
    BuildStep longDescription(String longDescription);
    BuildStep approvalStatus(SoundApprovalStatus approvalStatus);
  }
  

  public static class Builder implements SoundOwnerStep, OriginalNameStep, DisplayNameStep, AudioKeyS3Step, IconStep, ColorHexStep, FullPlayTimeStep, VisibleToOthersStep, TagsStep, AudioNamesStep, BuildStep {
    private String id;
    private UserData soundOwner;
    private String original_name;
    private String display_name;
    private String audio_key_s3;
    private Integer icon;
    private Integer colorHEX;
    private Integer fullPlayTime;
    private Boolean visible_to_others;
    private List<String> tags;
    private List<String> audio_names;
    private String short_description;
    private String long_description;
    private SoundApprovalStatus approvalStatus;
    @Override
     public SoundData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new SoundData(
          id,
          soundOwner,
          original_name,
          display_name,
          short_description,
          long_description,
          audio_key_s3,
          icon,
          colorHEX,
          fullPlayTime,
          visible_to_others,
          tags,
          audio_names,
          approvalStatus);
    }
    
    @Override
     public OriginalNameStep soundOwner(UserData soundOwner) {
        Objects.requireNonNull(soundOwner);
        this.soundOwner = soundOwner;
        return this;
    }
    
    @Override
     public DisplayNameStep originalName(String originalName) {
        Objects.requireNonNull(originalName);
        this.original_name = originalName;
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
        this.audio_key_s3 = audioKeyS3;
        return this;
    }
    
    @Override
     public ColorHexStep icon(Integer icon) {
        Objects.requireNonNull(icon);
        this.icon = icon;
        return this;
    }
    
    @Override
     public FullPlayTimeStep colorHex(Integer colorHex) {
        Objects.requireNonNull(colorHex);
        this.colorHEX = colorHex;
        return this;
    }
    
    @Override
     public VisibleToOthersStep fullPlayTime(Integer fullPlayTime) {
        Objects.requireNonNull(fullPlayTime);
        this.fullPlayTime = fullPlayTime;
        return this;
    }
    
    @Override
     public TagsStep visibleToOthers(Boolean visibleToOthers) {
        Objects.requireNonNull(visibleToOthers);
        this.visible_to_others = visibleToOthers;
        return this;
    }
    
    @Override
     public AudioNamesStep tags(List<String> tags) {
        Objects.requireNonNull(tags);
        this.tags = tags;
        return this;
    }
    
    @Override
     public BuildStep audioNames(List<String> audioNames) {
        Objects.requireNonNull(audioNames);
        this.audio_names = audioNames;
        return this;
    }
    
    @Override
     public BuildStep shortDescription(String shortDescription) {
        this.short_description = shortDescription;
        return this;
    }
    
    @Override
     public BuildStep longDescription(String longDescription) {
        this.long_description = longDescription;
        return this;
    }
    
    @Override
     public BuildStep approvalStatus(SoundApprovalStatus approvalStatus) {
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
    private CopyOfBuilder(String id, UserData soundOwner, String originalName, String displayName, String shortDescription, String longDescription, String audioKeyS3, Integer icon, Integer colorHex, Integer fullPlayTime, Boolean visibleToOthers, List<String> tags, List<String> audioNames, SoundApprovalStatus approvalStatus) {
      super.id(id);
      super.soundOwner(soundOwner)
        .originalName(originalName)
        .displayName(displayName)
        .audioKeyS3(audioKeyS3)
        .icon(icon)
        .colorHex(colorHex)
        .fullPlayTime(fullPlayTime)
        .visibleToOthers(visibleToOthers)
        .tags(tags)
        .audioNames(audioNames)
        .shortDescription(shortDescription)
        .longDescription(longDescription)
        .approvalStatus(approvalStatus);
    }
    
    @Override
     public CopyOfBuilder soundOwner(UserData soundOwner) {
      return (CopyOfBuilder) super.soundOwner(soundOwner);
    }
    
    @Override
     public CopyOfBuilder originalName(String originalName) {
      return (CopyOfBuilder) super.originalName(originalName);
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
     public CopyOfBuilder colorHex(Integer colorHex) {
      return (CopyOfBuilder) super.colorHex(colorHex);
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
     public CopyOfBuilder tags(List<String> tags) {
      return (CopyOfBuilder) super.tags(tags);
    }
    
    @Override
     public CopyOfBuilder audioNames(List<String> audioNames) {
      return (CopyOfBuilder) super.audioNames(audioNames);
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
     public CopyOfBuilder approvalStatus(SoundApprovalStatus approvalStatus) {
      return (CopyOfBuilder) super.approvalStatus(approvalStatus);
    }
  }
  
}
