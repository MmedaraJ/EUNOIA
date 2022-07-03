package com.amplifyframework.datastore.generated.model;

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
public final class SoundData implements Model {
  public static final QueryField ID = field("SoundData", "id");
  public static final QueryField OWNER_USERNAME = field("SoundData", "owner_username");
  public static final QueryField ORIGINAL_NAME = field("SoundData", "original_name");
  public static final QueryField DISPLAY_NAME = field("SoundData", "display_name");
  public static final QueryField SHORT_DESCRIPTION = field("SoundData", "short_description");
  public static final QueryField LONG_DESCRIPTION = field("SoundData", "long_description");
  public static final QueryField AUDIO_KEY = field("SoundData", "audio_key");
  public static final QueryField COMMENT = field("SoundData", "comment");
  public static final QueryField ICON = field("SoundData", "icon");
  public static final QueryField FULL_PLAY_TIME = field("SoundData", "fullPlayTime");
  public static final QueryField VISIBLE_TO_OTHERS = field("SoundData", "visible_to_others");
  public static final QueryField ORIGINAL_VOLUMES = field("SoundData", "original_volumes");
  public static final QueryField CURRENT_VOLUMES = field("SoundData", "current_volumes");
  public static final QueryField AUDIO_NAMES = field("SoundData", "audio_names");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String owner_username;
  private final @ModelField(targetType="String", isRequired = true) String original_name;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="String", isRequired = true) String short_description;
  private final @ModelField(targetType="String", isRequired = true) String long_description;
  private final @ModelField(targetType="String", isRequired = true) String audio_key;
  private final @ModelField(targetType="String", isRequired = true) String comment;
  private final @ModelField(targetType="Int", isRequired = true) Integer icon;
  private final @ModelField(targetType="Int", isRequired = true) Integer fullPlayTime;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean visible_to_others;
  private final @ModelField(targetType="Int", isRequired = true) List<Integer> original_volumes;
  private final @ModelField(targetType="Int", isRequired = true) List<Integer> current_volumes;
  private final @ModelField(targetType="String", isRequired = true) List<String> audio_names;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getOwnerUsername() {
      return owner_username;
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
  
  public String getAudioKey() {
      return audio_key;
  }
  
  public String getComment() {
      return comment;
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
  
  public List<Integer> getOriginalVolumes() {
      return original_volumes;
  }
  
  public List<Integer> getCurrentVolumes() {
      return current_volumes;
  }
  
  public List<String> getAudioNames() {
      return audio_names;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private SoundData(String id, String owner_username, String original_name, String display_name, String short_description, String long_description, String audio_key, String comment, Integer icon, Integer fullPlayTime, Boolean visible_to_others, List<Integer> original_volumes, List<Integer> current_volumes, List<String> audio_names) {
    this.id = id;
    this.owner_username = owner_username;
    this.original_name = original_name;
    this.display_name = display_name;
    this.short_description = short_description;
    this.long_description = long_description;
    this.audio_key = audio_key;
    this.comment = comment;
    this.icon = icon;
    this.fullPlayTime = fullPlayTime;
    this.visible_to_others = visible_to_others;
    this.original_volumes = original_volumes;
    this.current_volumes = current_volumes;
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
              ObjectsCompat.equals(getOwnerUsername(), soundData.getOwnerUsername()) &&
              ObjectsCompat.equals(getOriginalName(), soundData.getOriginalName()) &&
              ObjectsCompat.equals(getDisplayName(), soundData.getDisplayName()) &&
              ObjectsCompat.equals(getShortDescription(), soundData.getShortDescription()) &&
              ObjectsCompat.equals(getLongDescription(), soundData.getLongDescription()) &&
              ObjectsCompat.equals(getAudioKey(), soundData.getAudioKey()) &&
              ObjectsCompat.equals(getComment(), soundData.getComment()) &&
              ObjectsCompat.equals(getIcon(), soundData.getIcon()) &&
              ObjectsCompat.equals(getFullPlayTime(), soundData.getFullPlayTime()) &&
              ObjectsCompat.equals(getVisibleToOthers(), soundData.getVisibleToOthers()) &&
              ObjectsCompat.equals(getOriginalVolumes(), soundData.getOriginalVolumes()) &&
              ObjectsCompat.equals(getCurrentVolumes(), soundData.getCurrentVolumes()) &&
              ObjectsCompat.equals(getAudioNames(), soundData.getAudioNames()) &&
              ObjectsCompat.equals(getCreatedAt(), soundData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), soundData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getOwnerUsername())
      .append(getOriginalName())
      .append(getDisplayName())
      .append(getShortDescription())
      .append(getLongDescription())
      .append(getAudioKey())
      .append(getComment())
      .append(getIcon())
      .append(getFullPlayTime())
      .append(getVisibleToOthers())
      .append(getOriginalVolumes())
      .append(getCurrentVolumes())
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
      .append("owner_username=" + String.valueOf(getOwnerUsername()) + ", ")
      .append("original_name=" + String.valueOf(getOriginalName()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("short_description=" + String.valueOf(getShortDescription()) + ", ")
      .append("long_description=" + String.valueOf(getLongDescription()) + ", ")
      .append("audio_key=" + String.valueOf(getAudioKey()) + ", ")
      .append("comment=" + String.valueOf(getComment()) + ", ")
      .append("icon=" + String.valueOf(getIcon()) + ", ")
      .append("fullPlayTime=" + String.valueOf(getFullPlayTime()) + ", ")
      .append("visible_to_others=" + String.valueOf(getVisibleToOthers()) + ", ")
      .append("original_volumes=" + String.valueOf(getOriginalVolumes()) + ", ")
      .append("current_volumes=" + String.valueOf(getCurrentVolumes()) + ", ")
      .append("audio_names=" + String.valueOf(getAudioNames()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static OwnerUsernameStep builder() {
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
      owner_username,
      original_name,
      display_name,
      short_description,
      long_description,
      audio_key,
      comment,
      icon,
      fullPlayTime,
      visible_to_others,
      original_volumes,
      current_volumes,
      audio_names);
  }
  public interface OwnerUsernameStep {
    OriginalNameStep ownerUsername(String ownerUsername);
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
    AudioKeyStep longDescription(String longDescription);
  }
  

  public interface AudioKeyStep {
    CommentStep audioKey(String audioKey);
  }
  

  public interface CommentStep {
    IconStep comment(String comment);
  }
  

  public interface IconStep {
    FullPlayTimeStep icon(Integer icon);
  }
  

  public interface FullPlayTimeStep {
    VisibleToOthersStep fullPlayTime(Integer fullPlayTime);
  }
  

  public interface VisibleToOthersStep {
    OriginalVolumesStep visibleToOthers(Boolean visibleToOthers);
  }
  

  public interface OriginalVolumesStep {
    CurrentVolumesStep originalVolumes(List<Integer> originalVolumes);
  }
  

  public interface CurrentVolumesStep {
    AudioNamesStep currentVolumes(List<Integer> currentVolumes);
  }
  

  public interface AudioNamesStep {
    BuildStep audioNames(List<String> audioNames);
  }
  

  public interface BuildStep {
    SoundData build();
    BuildStep id(String id);
  }
  

  public static class Builder implements OwnerUsernameStep, OriginalNameStep, DisplayNameStep, ShortDescriptionStep, LongDescriptionStep, AudioKeyStep, CommentStep, IconStep, FullPlayTimeStep, VisibleToOthersStep, OriginalVolumesStep, CurrentVolumesStep, AudioNamesStep, BuildStep {
    private String id;
    private String owner_username;
    private String original_name;
    private String display_name;
    private String short_description;
    private String long_description;
    private String audio_key;
    private String comment;
    private Integer icon;
    private Integer fullPlayTime;
    private Boolean visible_to_others;
    private List<Integer> original_volumes;
    private List<Integer> current_volumes;
    private List<String> audio_names;
    @Override
     public SoundData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new SoundData(
          id,
          owner_username,
          original_name,
          display_name,
          short_description,
          long_description,
          audio_key,
          comment,
          icon,
          fullPlayTime,
          visible_to_others,
          original_volumes,
          current_volumes,
          audio_names);
    }
    
    @Override
     public OriginalNameStep ownerUsername(String ownerUsername) {
        Objects.requireNonNull(ownerUsername);
        this.owner_username = ownerUsername;
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
     public AudioKeyStep longDescription(String longDescription) {
        Objects.requireNonNull(longDescription);
        this.long_description = longDescription;
        return this;
    }
    
    @Override
     public CommentStep audioKey(String audioKey) {
        Objects.requireNonNull(audioKey);
        this.audio_key = audioKey;
        return this;
    }
    
    @Override
     public IconStep comment(String comment) {
        Objects.requireNonNull(comment);
        this.comment = comment;
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
     public OriginalVolumesStep visibleToOthers(Boolean visibleToOthers) {
        Objects.requireNonNull(visibleToOthers);
        this.visible_to_others = visibleToOthers;
        return this;
    }
    
    @Override
     public CurrentVolumesStep originalVolumes(List<Integer> originalVolumes) {
        Objects.requireNonNull(originalVolumes);
        this.original_volumes = originalVolumes;
        return this;
    }
    
    @Override
     public AudioNamesStep currentVolumes(List<Integer> currentVolumes) {
        Objects.requireNonNull(currentVolumes);
        this.current_volumes = currentVolumes;
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
    private CopyOfBuilder(String id, String ownerUsername, String originalName, String displayName, String shortDescription, String longDescription, String audioKey, String comment, Integer icon, Integer fullPlayTime, Boolean visibleToOthers, List<Integer> originalVolumes, List<Integer> currentVolumes, List<String> audioNames) {
      super.id(id);
      super.ownerUsername(ownerUsername)
        .originalName(originalName)
        .displayName(displayName)
        .shortDescription(shortDescription)
        .longDescription(longDescription)
        .audioKey(audioKey)
        .comment(comment)
        .icon(icon)
        .fullPlayTime(fullPlayTime)
        .visibleToOthers(visibleToOthers)
        .originalVolumes(originalVolumes)
        .currentVolumes(currentVolumes)
        .audioNames(audioNames);
    }
    
    @Override
     public CopyOfBuilder ownerUsername(String ownerUsername) {
      return (CopyOfBuilder) super.ownerUsername(ownerUsername);
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
     public CopyOfBuilder audioKey(String audioKey) {
      return (CopyOfBuilder) super.audioKey(audioKey);
    }
    
    @Override
     public CopyOfBuilder comment(String comment) {
      return (CopyOfBuilder) super.comment(comment);
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
     public CopyOfBuilder originalVolumes(List<Integer> originalVolumes) {
      return (CopyOfBuilder) super.originalVolumes(originalVolumes);
    }
    
    @Override
     public CopyOfBuilder currentVolumes(List<Integer> currentVolumes) {
      return (CopyOfBuilder) super.currentVolumes(currentVolumes);
    }
    
    @Override
     public CopyOfBuilder audioNames(List<String> audioNames) {
      return (CopyOfBuilder) super.audioNames(audioNames);
    }
  }
  
}
