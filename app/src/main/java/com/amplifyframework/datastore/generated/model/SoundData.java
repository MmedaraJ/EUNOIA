package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.annotations.HasOne;
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

import kotlinx.parcelize.Parcelize;

/** This is an auto generated class representing the SoundData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "SoundData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byUserData", fields = {"userDataID","display_name"})
public final class SoundData implements Model {
  public static final QueryField ID = field("SoundData", "id");
  public static final QueryField ORIGINAL_OWNER = field("SoundData", "userDataID");
  public static final QueryField CURRENT_OWNER = field("SoundData", "userDataID");
  public static final QueryField ORIGINAL_NAME = field("SoundData", "original_name");
  public static final QueryField DISPLAY_NAME = field("SoundData", "display_name");
  public static final QueryField SHORT_DESCRIPTION = field("SoundData", "short_description");
  public static final QueryField LONG_DESCRIPTION = field("SoundData", "long_description");
  public static final QueryField AUDIO_KEY_S3 = field("SoundData", "audio_key_s3");
  public static final QueryField ICON = field("SoundData", "icon");
  public static final QueryField FULL_PLAY_TIME = field("SoundData", "fullPlayTime");
  public static final QueryField VISIBLE_TO_OTHERS = field("SoundData", "visible_to_others");
  public static final QueryField AUDIO_NAMES = field("SoundData", "audio_names");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData original_owner;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData current_owner;
  private final @ModelField(targetType="String", isRequired = true) String original_name;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="String", isRequired = true) String short_description;
  private final @ModelField(targetType="String", isRequired = true) String long_description;
  private final @ModelField(targetType="String", isRequired = true) String audio_key_s3;
  private final @ModelField(targetType="Int", isRequired = true) Integer icon;
  private final @ModelField(targetType="Int", isRequired = true) Integer fullPlayTime;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean visible_to_others;
  private final @ModelField(targetType="String", isRequired = true) List<String> audio_names;
  private final @ModelField(targetType="CommentData") @HasOne(associatedWith = "sound", type = CommentData.class) CommentData comment = null;
  private final @ModelField(targetType="PresetData") @HasOne(associatedWith = "sound", type = PresetData.class) PresetData preset = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getOriginalOwner() {
      return original_owner;
  }
  
  public UserData getCurrentOwner() {
      return current_owner;
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
  
  public Integer getFullPlayTime() {
      return fullPlayTime;
  }
  
  public Boolean getVisibleToOthers() {
      return visible_to_others;
  }
  
  public List<String> getAudioNames() {
      return audio_names;
  }
  
  public CommentData getComment() {
      return comment;
  }
  
  public PresetData getPreset() {
      return preset;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private SoundData(String id, UserData original_owner, UserData current_owner, String original_name, String display_name, String short_description, String long_description, String audio_key_s3, Integer icon, Integer fullPlayTime, Boolean visible_to_others, List<String> audio_names) {
    this.id = id;
    this.original_owner = original_owner;
    this.current_owner = current_owner;
    this.original_name = original_name;
    this.display_name = display_name;
    this.short_description = short_description;
    this.long_description = long_description;
    this.audio_key_s3 = audio_key_s3;
    this.icon = icon;
    this.fullPlayTime = fullPlayTime;
    this.visible_to_others = visible_to_others;
    this.audio_names = audio_names;
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
              ObjectsCompat.equals(getOriginalOwner(), soundData.getOriginalOwner()) &&
              ObjectsCompat.equals(getCurrentOwner(), soundData.getCurrentOwner()) &&
              ObjectsCompat.equals(getOriginalName(), soundData.getOriginalName()) &&
              ObjectsCompat.equals(getDisplayName(), soundData.getDisplayName()) &&
              ObjectsCompat.equals(getShortDescription(), soundData.getShortDescription()) &&
              ObjectsCompat.equals(getLongDescription(), soundData.getLongDescription()) &&
              ObjectsCompat.equals(getAudioKeyS3(), soundData.getAudioKeyS3()) &&
              ObjectsCompat.equals(getIcon(), soundData.getIcon()) &&
              ObjectsCompat.equals(getFullPlayTime(), soundData.getFullPlayTime()) &&
              ObjectsCompat.equals(getVisibleToOthers(), soundData.getVisibleToOthers()) &&
              ObjectsCompat.equals(getAudioNames(), soundData.getAudioNames()) &&
              ObjectsCompat.equals(getCreatedAt(), soundData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), soundData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getOriginalOwner())
      .append(getCurrentOwner())
      .append(getOriginalName())
      .append(getDisplayName())
      .append(getShortDescription())
      .append(getLongDescription())
      .append(getAudioKeyS3())
      .append(getIcon())
      .append(getFullPlayTime())
      .append(getVisibleToOthers())
      .append(getAudioNames())
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
      .append("original_owner=" + String.valueOf(getOriginalOwner()) + ", ")
      .append("current_owner=" + String.valueOf(getCurrentOwner()) + ", ")
      .append("original_name=" + String.valueOf(getOriginalName()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("short_description=" + String.valueOf(getShortDescription()) + ", ")
      .append("long_description=" + String.valueOf(getLongDescription()) + ", ")
      .append("audio_key_s3=" + String.valueOf(getAudioKeyS3()) + ", ")
      .append("icon=" + String.valueOf(getIcon()) + ", ")
      .append("fullPlayTime=" + String.valueOf(getFullPlayTime()) + ", ")
      .append("visible_to_others=" + String.valueOf(getVisibleToOthers()) + ", ")
      .append("audio_names=" + String.valueOf(getAudioNames()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static OriginalOwnerStep builder() {
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
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      original_owner,
      current_owner,
      original_name,
      display_name,
      short_description,
      long_description,
      audio_key_s3,
      icon,
      fullPlayTime,
      visible_to_others,
      audio_names);
  }
  public interface OriginalOwnerStep {
    CurrentOwnerStep originalOwner(UserData originalOwner);
  }
  

  public interface CurrentOwnerStep {
    OriginalNameStep currentOwner(UserData currentOwner);
  }
  

  public interface OriginalNameStep {
    DisplayNameStep originalName(String originalName);
  }
  

  public interface DisplayNameStep {
    ShortDescriptionStep displayName(String displayName);
  }
  

  public interface ShortDescriptionStep {
    LongDescriptionStep shortDescription(String shortDescription);
  }
  

  public interface LongDescriptionStep {
    AudioKeyS3Step longDescription(String longDescription);
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
    AudioNamesStep visibleToOthers(Boolean visibleToOthers);
  }
  

  public interface AudioNamesStep {
    BuildStep audioNames(List<String> audioNames);
  }
  

  public interface BuildStep {
    SoundData build();
    BuildStep id(String id);
  }
  

  public static class Builder implements OriginalOwnerStep, CurrentOwnerStep, OriginalNameStep, DisplayNameStep, ShortDescriptionStep, LongDescriptionStep, AudioKeyS3Step, IconStep, FullPlayTimeStep, VisibleToOthersStep, AudioNamesStep, BuildStep {
    private String id;
    private UserData original_owner;
    private UserData current_owner;
    private String original_name;
    private String display_name;
    private String short_description;
    private String long_description;
    private String audio_key_s3;
    private Integer icon;
    private Integer fullPlayTime;
    private Boolean visible_to_others;
    private List<String> audio_names;
    @Override
     public SoundData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new SoundData(
          id,
          original_owner,
          current_owner,
          original_name,
          display_name,
          short_description,
          long_description,
          audio_key_s3,
          icon,
          fullPlayTime,
          visible_to_others,
          audio_names);
    }
    
    @Override
     public CurrentOwnerStep originalOwner(UserData originalOwner) {
        Objects.requireNonNull(originalOwner);
        this.original_owner = originalOwner;
        return this;
    }
    
    @Override
     public OriginalNameStep currentOwner(UserData currentOwner) {
        Objects.requireNonNull(currentOwner);
        this.current_owner = currentOwner;
        return this;
    }
    
    @Override
     public DisplayNameStep originalName(String originalName) {
        Objects.requireNonNull(originalName);
        this.original_name = originalName;
        return this;
    }
    
    @Override
     public ShortDescriptionStep displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.display_name = displayName;
        return this;
    }
    
    @Override
     public LongDescriptionStep shortDescription(String shortDescription) {
        Objects.requireNonNull(shortDescription);
        this.short_description = shortDescription;
        return this;
    }
    
    @Override
     public AudioKeyS3Step longDescription(String longDescription) {
        Objects.requireNonNull(longDescription);
        this.long_description = longDescription;
        return this;
    }
    
    @Override
     public IconStep audioKeyS3(String audioKeyS3) {
        Objects.requireNonNull(audioKeyS3);
        this.audio_key_s3 = audioKeyS3;
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
     public AudioNamesStep visibleToOthers(Boolean visibleToOthers) {
        Objects.requireNonNull(visibleToOthers);
        this.visible_to_others = visibleToOthers;
        return this;
    }
    
    @Override
     public BuildStep audioNames(List<String> audioNames) {
        Objects.requireNonNull(audioNames);
        this.audio_names = audioNames;
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
    private CopyOfBuilder(String id, UserData originalOwner, UserData currentOwner, String originalName, String displayName, String shortDescription, String longDescription, String audioKeyS3, Integer icon, Integer fullPlayTime, Boolean visibleToOthers, List<String> audioNames) {
      super.id(id);
      super.originalOwner(originalOwner)
        .currentOwner(currentOwner)
        .originalName(originalName)
        .displayName(displayName)
        .shortDescription(shortDescription)
        .longDescription(longDescription)
        .audioKeyS3(audioKeyS3)
        .icon(icon)
        .fullPlayTime(fullPlayTime)
        .visibleToOthers(visibleToOthers)
        .audioNames(audioNames);
    }
    
    @Override
     public CopyOfBuilder originalOwner(UserData originalOwner) {
      return (CopyOfBuilder) super.originalOwner(originalOwner);
    }
    
    @Override
     public CopyOfBuilder currentOwner(UserData currentOwner) {
      return (CopyOfBuilder) super.currentOwner(currentOwner);
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
     public CopyOfBuilder shortDescription(String shortDescription) {
      return (CopyOfBuilder) super.shortDescription(shortDescription);
    }
    
    @Override
     public CopyOfBuilder longDescription(String longDescription) {
      return (CopyOfBuilder) super.longDescription(longDescription);
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
     public CopyOfBuilder audioNames(List<String> audioNames) {
      return (CopyOfBuilder) super.audioNames(audioNames);
    }
  }
  
}
