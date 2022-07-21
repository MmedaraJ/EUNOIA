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
@Index(name = "bySoundData", fields = {"soundID","comment"})
@Index(name = "byUserData", fields = {"userDataID","comment"})
public final class CommentData implements Model {
  public static final QueryField ID = field("CommentData", "id");
  public static final QueryField SOUND = field("CommentData", "soundID");
  public static final QueryField COMMENT_OWNER = field("CommentData", "userDataID");
  public static final QueryField COMMENT = field("CommentData", "comment");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="SoundData", isRequired = true) @BelongsTo(targetName = "soundID", type = SoundData.class) SoundData sound;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData commentOwner;
  private final @ModelField(targetType="String", isRequired = true) String comment;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public SoundData getSound() {
      return sound;
  }
  
  public UserData getCommentOwner() {
      return commentOwner;
  }
  
  public String getComment() {
      return comment;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private CommentData(String id, SoundData sound, UserData commentOwner, String comment) {
    this.id = id;
    this.sound = sound;
    this.commentOwner = commentOwner;
    this.comment = comment;
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
              ObjectsCompat.equals(getSound(), commentData.getSound()) &&
              ObjectsCompat.equals(getCommentOwner(), commentData.getCommentOwner()) &&
              ObjectsCompat.equals(getComment(), commentData.getComment()) &&
              ObjectsCompat.equals(getCreatedAt(), commentData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), commentData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getSound())
      .append(getCommentOwner())
      .append(getComment())
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
      .append("sound=" + String.valueOf(getSound()) + ", ")
      .append("commentOwner=" + String.valueOf(getCommentOwner()) + ", ")
      .append("comment=" + String.valueOf(getComment()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static SoundStep builder() {
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
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      sound,
      commentOwner,
      comment);
  }
  public interface SoundStep {
    CommentOwnerStep sound(SoundData sound);
  }
  

  public interface CommentOwnerStep {
    CommentStep commentOwner(UserData commentOwner);
  }
  

  public interface CommentStep {
    BuildStep comment(String comment);
  }
  

  public interface BuildStep {
    CommentData build();
    BuildStep id(String id);
  }
  

  public static class Builder implements SoundStep, CommentOwnerStep, CommentStep, BuildStep {
    private String id;
    private SoundData sound;
    private UserData commentOwner;
    private String comment;
    @Override
     public CommentData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new CommentData(
          id,
          sound,
          commentOwner,
          comment);
    }
    
    @Override
     public CommentOwnerStep sound(SoundData sound) {
        Objects.requireNonNull(sound);
        this.sound = sound;
        return this;
    }
    
    @Override
     public CommentStep commentOwner(UserData commentOwner) {
        Objects.requireNonNull(commentOwner);
        this.commentOwner = commentOwner;
        return this;
    }
    
    @Override
     public BuildStep comment(String comment) {
        Objects.requireNonNull(comment);
        this.comment = comment;
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
    private CopyOfBuilder(String id, SoundData sound, UserData commentOwner, String comment) {
      super.id(id);
      super.sound(sound)
        .commentOwner(commentOwner)
        .comment(comment);
    }
    
    @Override
     public CopyOfBuilder sound(SoundData sound) {
      return (CopyOfBuilder) super.sound(sound);
    }
    
    @Override
     public CopyOfBuilder commentOwner(UserData commentOwner) {
      return (CopyOfBuilder) super.commentOwner(commentOwner);
    }
    
    @Override
     public CopyOfBuilder comment(String comment) {
      return (CopyOfBuilder) super.comment(comment);
    }
  }
  
}
