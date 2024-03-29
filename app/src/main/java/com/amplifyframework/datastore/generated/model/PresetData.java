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

/** This is an auto generated class representing the PresetData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "PresetData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "PresetsOwnedByUser", fields = {"presetUserDataID","id"})
@Index(name = "PresetsOwnedBySound", fields = {"soundID","id"})
public final class PresetData implements Model {
  public static final QueryField ID = field("PresetData", "id");
  public static final QueryField PRESET_OWNER = field("PresetData", "presetUserDataID");
  public static final QueryField PRESET_OWNER_ID = field("PresetData", "presetOwnerId");
  public static final QueryField KEY = field("PresetData", "key");
  public static final QueryField VOLUMES = field("PresetData", "volumes");
  public static final QueryField SOUND = field("PresetData", "soundID");
  public static final QueryField SOUND_ID = field("PresetData", "soundId");
  public static final QueryField PUBLICITY_STATUS = field("PresetData", "publicityStatus");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "presetUserDataID", type = UserData.class) UserData presetOwner;
  private final @ModelField(targetType="String") String presetOwnerId;
  private final @ModelField(targetType="String", isRequired = true) String key;
  private final @ModelField(targetType="Int", isRequired = true) List<Integer> volumes;
  private final @ModelField(targetType="SoundData", isRequired = true) @BelongsTo(targetName = "soundID", type = SoundData.class) SoundData sound;
  private final @ModelField(targetType="String") String soundId;
  private final @ModelField(targetType="PresetPublicityStatus") PresetPublicityStatus publicityStatus;
  private final @ModelField(targetType="CommentData") @HasMany(associatedWith = "preset", type = CommentData.class) List<CommentData> commentsOwnedByPreset = null;
  private final @ModelField(targetType="UserPresetRelationship") @HasMany(associatedWith = "userPresetRelationshipPreset", type = UserPresetRelationship.class) List<UserPresetRelationship> userPresetRelationshipsOwnedByPreset = null;
  private final @ModelField(targetType="UserPreset") @HasMany(associatedWith = "presetData", type = UserPreset.class) List<UserPreset> users = null;
  private final @ModelField(targetType="RoutinePreset") @HasMany(associatedWith = "presetData", type = RoutinePreset.class) List<RoutinePreset> routines = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getPresetOwner() {
      return presetOwner;
  }
  
  public String getPresetOwnerId() {
      return presetOwnerId;
  }
  
  public String getKey() {
      return key;
  }
  
  public List<Integer> getVolumes() {
      return volumes;
  }
  
  public SoundData getSound() {
      return sound;
  }
  
  public String getSoundId() {
      return soundId;
  }
  
  public PresetPublicityStatus getPublicityStatus() {
      return publicityStatus;
  }
  
  public List<CommentData> getCommentsOwnedByPreset() {
      return commentsOwnedByPreset;
  }
  
  public List<UserPresetRelationship> getUserPresetRelationshipsOwnedByPreset() {
      return userPresetRelationshipsOwnedByPreset;
  }
  
  public List<UserPreset> getUsers() {
      return users;
  }
  
  public List<RoutinePreset> getRoutines() {
      return routines;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private PresetData(String id, UserData presetOwner, String presetOwnerId, String key, List<Integer> volumes, SoundData sound, String soundId, PresetPublicityStatus publicityStatus) {
    this.id = id;
    this.presetOwner = presetOwner;
    this.presetOwnerId = presetOwnerId;
    this.key = key;
    this.volumes = volumes;
    this.sound = sound;
    this.soundId = soundId;
    this.publicityStatus = publicityStatus;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      PresetData presetData = (PresetData) obj;
      return ObjectsCompat.equals(getId(), presetData.getId()) &&
              ObjectsCompat.equals(getPresetOwner(), presetData.getPresetOwner()) &&
              ObjectsCompat.equals(getPresetOwnerId(), presetData.getPresetOwnerId()) &&
              ObjectsCompat.equals(getKey(), presetData.getKey()) &&
              ObjectsCompat.equals(getVolumes(), presetData.getVolumes()) &&
              ObjectsCompat.equals(getSound(), presetData.getSound()) &&
              ObjectsCompat.equals(getSoundId(), presetData.getSoundId()) &&
              ObjectsCompat.equals(getPublicityStatus(), presetData.getPublicityStatus()) &&
              ObjectsCompat.equals(getCreatedAt(), presetData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), presetData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getPresetOwner())
      .append(getPresetOwnerId())
      .append(getKey())
      .append(getVolumes())
      .append(getSound())
      .append(getSoundId())
      .append(getPublicityStatus())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("PresetData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("presetOwner=" + String.valueOf(getPresetOwner()) + ", ")
      .append("presetOwnerId=" + String.valueOf(getPresetOwnerId()) + ", ")
      .append("key=" + String.valueOf(getKey()) + ", ")
      .append("volumes=" + String.valueOf(getVolumes()) + ", ")
      .append("sound=" + String.valueOf(getSound()) + ", ")
      .append("soundId=" + String.valueOf(getSoundId()) + ", ")
      .append("publicityStatus=" + String.valueOf(getPublicityStatus()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static PresetOwnerStep builder() {
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
  public static PresetData justId(String id) {
    return new PresetData(
      id,
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
      presetOwner,
      presetOwnerId,
      key,
      volumes,
      sound,
      soundId,
      publicityStatus);
  }
  public interface PresetOwnerStep {
    KeyStep presetOwner(UserData presetOwner);
  }
  

  public interface KeyStep {
    VolumesStep key(String key);
  }
  

  public interface VolumesStep {
    SoundStep volumes(List<Integer> volumes);
  }
  

  public interface SoundStep {
    BuildStep sound(SoundData sound);
  }
  

  public interface BuildStep {
    PresetData build();
    BuildStep id(String id);
    BuildStep presetOwnerId(String presetOwnerId);
    BuildStep soundId(String soundId);
    BuildStep publicityStatus(PresetPublicityStatus publicityStatus);
  }
  

  public static class Builder implements PresetOwnerStep, KeyStep, VolumesStep, SoundStep, BuildStep {
    private String id;
    private UserData presetOwner;
    private String key;
    private List<Integer> volumes;
    private SoundData sound;
    private String presetOwnerId;
    private String soundId;
    private PresetPublicityStatus publicityStatus;
    @Override
     public PresetData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new PresetData(
          id,
          presetOwner,
          presetOwnerId,
          key,
          volumes,
          sound,
          soundId,
          publicityStatus);
    }
    
    @Override
     public KeyStep presetOwner(UserData presetOwner) {
        Objects.requireNonNull(presetOwner);
        this.presetOwner = presetOwner;
        return this;
    }
    
    @Override
     public VolumesStep key(String key) {
        Objects.requireNonNull(key);
        this.key = key;
        return this;
    }
    
    @Override
     public SoundStep volumes(List<Integer> volumes) {
        Objects.requireNonNull(volumes);
        this.volumes = volumes;
        return this;
    }
    
    @Override
     public BuildStep sound(SoundData sound) {
        Objects.requireNonNull(sound);
        this.sound = sound;
        return this;
    }
    
    @Override
     public BuildStep presetOwnerId(String presetOwnerId) {
        this.presetOwnerId = presetOwnerId;
        return this;
    }
    
    @Override
     public BuildStep soundId(String soundId) {
        this.soundId = soundId;
        return this;
    }
    
    @Override
     public BuildStep publicityStatus(PresetPublicityStatus publicityStatus) {
        this.publicityStatus = publicityStatus;
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
    private CopyOfBuilder(String id, UserData presetOwner, String presetOwnerId, String key, List<Integer> volumes, SoundData sound, String soundId, PresetPublicityStatus publicityStatus) {
      super.id(id);
      super.presetOwner(presetOwner)
        .key(key)
        .volumes(volumes)
        .sound(sound)
        .presetOwnerId(presetOwnerId)
        .soundId(soundId)
        .publicityStatus(publicityStatus);
    }
    
    @Override
     public CopyOfBuilder presetOwner(UserData presetOwner) {
      return (CopyOfBuilder) super.presetOwner(presetOwner);
    }
    
    @Override
     public CopyOfBuilder key(String key) {
      return (CopyOfBuilder) super.key(key);
    }
    
    @Override
     public CopyOfBuilder volumes(List<Integer> volumes) {
      return (CopyOfBuilder) super.volumes(volumes);
    }
    
    @Override
     public CopyOfBuilder sound(SoundData sound) {
      return (CopyOfBuilder) super.sound(sound);
    }
    
    @Override
     public CopyOfBuilder presetOwnerId(String presetOwnerId) {
      return (CopyOfBuilder) super.presetOwnerId(presetOwnerId);
    }
    
    @Override
     public CopyOfBuilder soundId(String soundId) {
      return (CopyOfBuilder) super.soundId(soundId);
    }
    
    @Override
     public CopyOfBuilder publicityStatus(PresetPublicityStatus publicityStatus) {
      return (CopyOfBuilder) super.publicityStatus(publicityStatus);
    }
  }
  
}
