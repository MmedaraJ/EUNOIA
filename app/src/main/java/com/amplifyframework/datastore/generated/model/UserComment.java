package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the UserComment type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserComments")
@Index(name = "byUserData", fields = {"userDataID"})
@Index(name = "byCommentData", fields = {"commentDataID"})
public final class UserComment implements Model {
  public static final QueryField ID = field("UserComment", "id");
  public static final QueryField USER_DATA = field("UserComment", "userDataID");
  public static final QueryField COMMENT_DATA = field("UserComment", "commentDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private final @ModelField(targetType="CommentData", isRequired = true) @BelongsTo(targetName = "commentDataID", type = CommentData.class) CommentData commentData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserData() {
      return userData;
  }
  
  public CommentData getCommentData() {
      return commentData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserComment(String id, UserData userData, CommentData commentData) {
    this.id = id;
    this.userData = userData;
    this.commentData = commentData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserComment userComment = (UserComment) obj;
      return ObjectsCompat.equals(getId(), userComment.getId()) &&
              ObjectsCompat.equals(getUserData(), userComment.getUserData()) &&
              ObjectsCompat.equals(getCommentData(), userComment.getCommentData()) &&
              ObjectsCompat.equals(getCreatedAt(), userComment.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userComment.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserData())
      .append(getCommentData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserComment {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("commentData=" + String.valueOf(getCommentData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserDataStep builder() {
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
  public static UserComment justId(String id) {
    return new UserComment(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userData,
      commentData);
  }
  public interface UserDataStep {
    CommentDataStep userData(UserData userData);
  }
  

  public interface CommentDataStep {
    BuildStep commentData(CommentData commentData);
  }
  

  public interface BuildStep {
    UserComment build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserDataStep, CommentDataStep, BuildStep {
    private String id;
    private UserData userData;
    private CommentData commentData;
    @Override
     public UserComment build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserComment(
          id,
          userData,
          commentData);
    }
    
    @Override
     public CommentDataStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
        return this;
    }
    
    @Override
     public BuildStep commentData(CommentData commentData) {
        Objects.requireNonNull(commentData);
        this.commentData = commentData;
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
    private CopyOfBuilder(String id, UserData userData, CommentData commentData) {
      super.id(id);
      super.userData(userData)
        .commentData(commentData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
    
    @Override
     public CopyOfBuilder commentData(CommentData commentData) {
      return (CopyOfBuilder) super.commentData(commentData);
    }
  }
  
}
