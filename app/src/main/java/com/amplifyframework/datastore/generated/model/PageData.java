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

/** This is an auto generated class representing the PageData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "PageData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class PageData implements Model {
  public static final QueryField ID = field("PageData", "id");
  public static final QueryField DISPLAY_NAME = field("PageData", "display_name");
  public static final QueryField PAGE_NUMBER = field("PageData", "pageNumber");
  public static final QueryField AUDIO_KEYS_S3 = field("PageData", "audioKeysS3");
  public static final QueryField AUDIO_NAMES = field("PageData", "audioNames");
  public static final QueryField AUDIO_LENGTH = field("PageData", "audioLength");
  public static final QueryField BEDTIME_STORY_INFO_CHAPTER_ID = field("PageData", "bedtimeStoryInfoChapterId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="Int", isRequired = true) Integer pageNumber;
  private final @ModelField(targetType="String", isRequired = true) List<String> audioKeysS3;
  private final @ModelField(targetType="String", isRequired = true) List<String> audioNames;
  private final @ModelField(targetType="Int") List<Integer> audioLength;
  private final @ModelField(targetType="String") String bedtimeStoryInfoChapterId;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getDisplayName() {
      return display_name;
  }
  
  public Integer getPageNumber() {
      return pageNumber;
  }
  
  public List<String> getAudioKeysS3() {
      return audioKeysS3;
  }
  
  public List<String> getAudioNames() {
      return audioNames;
  }
  
  public List<Integer> getAudioLength() {
      return audioLength;
  }
  
  public String getBedtimeStoryInfoChapterId() {
      return bedtimeStoryInfoChapterId;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private PageData(String id, String display_name, Integer pageNumber, List<String> audioKeysS3, List<String> audioNames, List<Integer> audioLength, String bedtimeStoryInfoChapterId) {
    this.id = id;
    this.display_name = display_name;
    this.pageNumber = pageNumber;
    this.audioKeysS3 = audioKeysS3;
    this.audioNames = audioNames;
    this.audioLength = audioLength;
    this.bedtimeStoryInfoChapterId = bedtimeStoryInfoChapterId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      PageData pageData = (PageData) obj;
      return ObjectsCompat.equals(getId(), pageData.getId()) &&
              ObjectsCompat.equals(getDisplayName(), pageData.getDisplayName()) &&
              ObjectsCompat.equals(getPageNumber(), pageData.getPageNumber()) &&
              ObjectsCompat.equals(getAudioKeysS3(), pageData.getAudioKeysS3()) &&
              ObjectsCompat.equals(getAudioNames(), pageData.getAudioNames()) &&
              ObjectsCompat.equals(getAudioLength(), pageData.getAudioLength()) &&
              ObjectsCompat.equals(getBedtimeStoryInfoChapterId(), pageData.getBedtimeStoryInfoChapterId()) &&
              ObjectsCompat.equals(getCreatedAt(), pageData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), pageData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getDisplayName())
      .append(getPageNumber())
      .append(getAudioKeysS3())
      .append(getAudioNames())
      .append(getAudioLength())
      .append(getBedtimeStoryInfoChapterId())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("PageData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("pageNumber=" + String.valueOf(getPageNumber()) + ", ")
      .append("audioKeysS3=" + String.valueOf(getAudioKeysS3()) + ", ")
      .append("audioNames=" + String.valueOf(getAudioNames()) + ", ")
      .append("audioLength=" + String.valueOf(getAudioLength()) + ", ")
      .append("bedtimeStoryInfoChapterId=" + String.valueOf(getBedtimeStoryInfoChapterId()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static DisplayNameStep builder() {
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
  public static PageData justId(String id) {
    return new PageData(
      id,
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
      display_name,
      pageNumber,
      audioKeysS3,
      audioNames,
      audioLength,
      bedtimeStoryInfoChapterId);
  }
  public interface DisplayNameStep {
    PageNumberStep displayName(String displayName);
  }
  

  public interface PageNumberStep {
    AudioKeysS3Step pageNumber(Integer pageNumber);
  }
  

  public interface AudioKeysS3Step {
    AudioNamesStep audioKeysS3(List<String> audioKeysS3);
  }
  

  public interface AudioNamesStep {
    BuildStep audioNames(List<String> audioNames);
  }
  

  public interface BuildStep {
    PageData build();
    BuildStep id(String id);
    BuildStep audioLength(List<Integer> audioLength);
    BuildStep bedtimeStoryInfoChapterId(String bedtimeStoryInfoChapterId);
  }
  

  public static class Builder implements DisplayNameStep, PageNumberStep, AudioKeysS3Step, AudioNamesStep, BuildStep {
    private String id;
    private String display_name;
    private Integer pageNumber;
    private List<String> audioKeysS3;
    private List<String> audioNames;
    private List<Integer> audioLength;
    private String bedtimeStoryInfoChapterId;
    @Override
     public PageData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new PageData(
          id,
          display_name,
          pageNumber,
          audioKeysS3,
          audioNames,
          audioLength,
          bedtimeStoryInfoChapterId);
    }
    
    @Override
     public PageNumberStep displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.display_name = displayName;
        return this;
    }
    
    @Override
     public AudioKeysS3Step pageNumber(Integer pageNumber) {
        Objects.requireNonNull(pageNumber);
        this.pageNumber = pageNumber;
        return this;
    }
    
    @Override
     public AudioNamesStep audioKeysS3(List<String> audioKeysS3) {
        Objects.requireNonNull(audioKeysS3);
        this.audioKeysS3 = audioKeysS3;
        return this;
    }
    
    @Override
     public BuildStep audioNames(List<String> audioNames) {
        Objects.requireNonNull(audioNames);
        this.audioNames = audioNames;
        return this;
    }
    
    @Override
     public BuildStep audioLength(List<Integer> audioLength) {
        this.audioLength = audioLength;
        return this;
    }
    
    @Override
     public BuildStep bedtimeStoryInfoChapterId(String bedtimeStoryInfoChapterId) {
        this.bedtimeStoryInfoChapterId = bedtimeStoryInfoChapterId;
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
    private CopyOfBuilder(String id, String displayName, Integer pageNumber, List<String> audioKeysS3, List<String> audioNames, List<Integer> audioLength, String bedtimeStoryInfoChapterId) {
      super.id(id);
      super.displayName(displayName)
        .pageNumber(pageNumber)
        .audioKeysS3(audioKeysS3)
        .audioNames(audioNames)
        .audioLength(audioLength)
        .bedtimeStoryInfoChapterId(bedtimeStoryInfoChapterId);
    }
    
    @Override
     public CopyOfBuilder displayName(String displayName) {
      return (CopyOfBuilder) super.displayName(displayName);
    }
    
    @Override
     public CopyOfBuilder pageNumber(Integer pageNumber) {
      return (CopyOfBuilder) super.pageNumber(pageNumber);
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
     public CopyOfBuilder audioLength(List<Integer> audioLength) {
      return (CopyOfBuilder) super.audioLength(audioLength);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryInfoChapterId(String bedtimeStoryInfoChapterId) {
      return (CopyOfBuilder) super.bedtimeStoryInfoChapterId(bedtimeStoryInfoChapterId);
    }
  }
  
}
