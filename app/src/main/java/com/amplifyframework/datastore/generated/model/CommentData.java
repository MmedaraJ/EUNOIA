package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
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

/** This is an auto generated class representing the CommentData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "CommentData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "CommentsOwnedByUser", fields = {"userDataID","comment"})
@Index(name = "CommentsOwnedBySound", fields = {"soundID","comment"})
@Index(name = "CommentsOwnedBySoundPreset", fields = {"presetID","comment"})
public final class CommentData implements Model {
  public static final QueryField ID = field("CommentData", "id");
  public static final QueryField COMMENT_OWNER = field("CommentData", "userDataID");
  public static final QueryField COMMENT_OWNER_ID = field("CommentData", "commentOwnerId");
  public static final QueryField COMMENT = field("CommentData", "comment");
  public static final QueryField SOUND = field("CommentData", "soundID");
  public static final QueryField SOUND_ID = field("CommentData", "soundId");
  public static final QueryField PRESET = field("CommentData", "presetID");
  public static final QueryField PRESET_ID = field("CommentData", "presetId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData commentOwner;
  private final @ModelField(targetType="String") String commentOwnerId;
  private final @ModelField(targetType="String", isRequired = true) String comment;
  private final @ModelField(targetType="SoundData", isRequired = true) @BelongsTo(targetName = "soundID", type = SoundData.class) SoundData sound;
  private final @ModelField(targetType="String") String soundId;
  private final @ModelField(targetType="SoundPresetData", isRequired = true) @BelongsTo(targetName = "presetID", type = SoundPresetData.class) SoundPresetData preset;
  private final @ModelField(targetType="String") String presetId;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getCommentOwner() {
      return commentOwner;
  }
  
  public String getCommentOwnerId() {
      return commentOwnerId;
  }
  
  public String getComment() {
      return comment;
  }
  
  public SoundData getSound() {
      return sound;
  }
  
  public String getSoundId() {
      return soundId;
  }
  
  public SoundPresetData getPreset() {
      return preset;
  }
  
  public String getPresetId() {
      return presetId;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private CommentData(String id, UserData commentOwner, String commentOwnerId, String comment, SoundData sound, String soundId, SoundPresetData preset, String presetId) {
    this.id = id;
    this.commentOwner = commentOwner;
    this.commentOwnerId = commentOwnerId;
    this.comment = comment;
    this.sound = sound;
    this.soundId = soundId;
    this.preset = preset;
    this.presetId = presetId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      CommentData commentData = (CommentData) obj;
      return ObjectsCompat.equals(getId(), commentData.getId()) &&
              ObjectsCompat.equals(getCommentOwner(), commentData.getCommentOwner()) &&
              ObjectsCompat.equals(getCommentOwnerId(), commentData.getCommentOwnerId()) &&
              ObjectsCompat.equals(getComment(), commentData.getComment()) &&
              ObjectsCompat.equals(getSound(), commentData.getSound()) &&
              ObjectsCompat.equals(getSoundId(), commentData.getSoundId()) &&
              ObjectsCompat.equals(getPreset(), commentData.getPreset()) &&
              ObjectsCompat.equals(getPresetId(), commentData.getPresetId()) &&
              ObjectsCompat.equals(getCreatedAt(), commentData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), commentData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getCommentOwner())
      .append(getCommentOwnerId())
      .append(getComment())
      .append(getSound())
      .append(getSoundId())
      .append(getPreset())
      .append(getPresetId())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("CommentData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("commentOwner=" + String.valueOf(getCommentOwner()) + ", ")
      .append("commentOwnerId=" + String.valueOf(getCommentOwnerId()) + ", ")
      .append("comment=" + String.valueOf(getComment()) + ", ")
      .append("sound=" + String.valueOf(getSound()) + ", ")
      .append("soundId=" + String.valueOf(getSoundId()) + ", ")
      .append("preset=" + String.valueOf(getPreset()) + ", ")
      .append("presetId=" + String.valueOf(getPresetId()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static CommentOwnerStep builder() {
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
  public static CommentData justId(String id) {
    return new CommentData(
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
      commentOwner,
      commentOwnerId,
      comment,
      sound,
      soundId,
      preset,
      presetId);
  }
  public interface CommentOwnerStep {
    CommentStep commentOwner(UserData commentOwner);
  }
  

  public interface CommentStep {
    SoundStep comment(String comment);
  }
  

  public interface SoundStep {
    PresetStep sound(SoundData sound);
  }
  

  public interface PresetStep {
    BuildStep preset(SoundPresetData preset);
  }
  

  public interface BuildStep {
    CommentData build();
    BuildStep id(String id);
    BuildStep commentOwnerId(String commentOwnerId);
    BuildStep soundId(String soundId);
    BuildStep presetId(String presetId);
  }
  

  public static class Builder implements CommentOwnerStep, CommentStep, SoundStep, PresetStep, BuildStep {
    private String id;
    private UserData commentOwner;
    private String comment;
    private SoundData sound;
    private SoundPresetData preset;
    private String commentOwnerId;
    private String soundId;
    private String presetId;
    @Override
     public CommentData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new CommentData(
          id,
          commentOwner,
          commentOwnerId,
          comment,
          sound,
          soundId,
          preset,
          presetId);
    }
    
    @Override
     public CommentStep commentOwner(UserData commentOwner) {
        Objects.requireNonNull(commentOwner);
        this.commentOwner = commentOwner;
        return this;
    }
    
    @Override
     public SoundStep comment(String comment) {
        Objects.requireNonNull(comment);
        this.comment = comment;
        return this;
    }
    
    @Override
     public PresetStep sound(SoundData sound) {
        Objects.requireNonNull(sound);
        this.sound = sound;
        return this;
    }
    
    @Override
     public BuildStep preset(SoundPresetData preset) {
        Objects.requireNonNull(preset);
        this.preset = preset;
        return this;
    }
    
    @Override
     public BuildStep commentOwnerId(String commentOwnerId) {
        this.commentOwnerId = commentOwnerId;
        return this;
    }
    
    @Override
     public BuildStep soundId(String soundId) {
        this.soundId = soundId;
        return this;
    }
    
    @Override
     public BuildStep presetId(String presetId) {
        this.presetId = presetId;
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
    private CopyOfBuilder(String id, UserData commentOwner, String commentOwnerId, String comment, SoundData sound, String soundId, SoundPresetData preset, String presetId) {
      super.id(id);
      super.commentOwner(commentOwner)
        .comment(comment)
        .sound(sound)
        .preset(preset)
        .commentOwnerId(commentOwnerId)
        .soundId(soundId)
        .presetId(presetId);
    }
    
    @Override
     public CopyOfBuilder commentOwner(UserData commentOwner) {
      return (CopyOfBuilder) super.commentOwner(commentOwner);
    }
    
    @Override
     public CopyOfBuilder comment(String comment) {
      return (CopyOfBuilder) super.comment(comment);
    }
    
    @Override
     public CopyOfBuilder sound(SoundData sound) {
      return (CopyOfBuilder) super.sound(sound);
    }
    
    @Override
     public CopyOfBuilder preset(SoundPresetData preset) {
      return (CopyOfBuilder) super.preset(preset);
    }
    
    @Override
     public CopyOfBuilder commentOwnerId(String commentOwnerId) {
      return (CopyOfBuilder) super.commentOwnerId(commentOwnerId);
    }
    
    @Override
     public CopyOfBuilder soundId(String soundId) {
      return (CopyOfBuilder) super.soundId(soundId);
    }
    
    @Override
     public CopyOfBuilder presetId(String presetId) {
      return (CopyOfBuilder) super.presetId(presetId);
    }
  }
  
}
