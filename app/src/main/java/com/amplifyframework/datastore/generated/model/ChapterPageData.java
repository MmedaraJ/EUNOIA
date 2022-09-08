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

/** This is an auto generated class representing the ChapterPageData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "ChapterPageData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "ChapterPages", fields = {"bedtimeStoryInfoChapterDataID","pageNumber"})
public final class ChapterPageData implements Model {
  public static final QueryField ID = field("ChapterPageData", "id");
  public static final QueryField DISPLAY_NAME = field("ChapterPageData", "display_name");
  public static final QueryField PAGE_NUMBER = field("ChapterPageData", "pageNumber");
  public static final QueryField AUDIO_KEYS_S3 = field("ChapterPageData", "audioKeysS3");
  public static final QueryField AUDIO_NAMES = field("ChapterPageData", "audioNames");
  public static final QueryField BEDTIME_STORY_INFO_CHAPTER = field("ChapterPageData", "bedtimeStoryInfoChapterDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="Int", isRequired = true) Integer pageNumber;
  private final @ModelField(targetType="String", isRequired = true) List<String> audioKeysS3;
  private final @ModelField(targetType="String", isRequired = true) List<String> audioNames;
  private final @ModelField(targetType="BedtimeStoryInfoChapterData", isRequired = true) @BelongsTo(targetName = "bedtimeStoryInfoChapterDataID", type = BedtimeStoryInfoChapterData.class) BedtimeStoryInfoChapterData bedtimeStoryInfoChapter;
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
  
  public BedtimeStoryInfoChapterData getBedtimeStoryInfoChapter() {
      return bedtimeStoryInfoChapter;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private ChapterPageData(String id, String display_name, Integer pageNumber, List<String> audioKeysS3, List<String> audioNames, BedtimeStoryInfoChapterData bedtimeStoryInfoChapter) {
    this.id = id;
    this.display_name = display_name;
    this.pageNumber = pageNumber;
    this.audioKeysS3 = audioKeysS3;
    this.audioNames = audioNames;
    this.bedtimeStoryInfoChapter = bedtimeStoryInfoChapter;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      ChapterPageData chapterPageData = (ChapterPageData) obj;
      return ObjectsCompat.equals(getId(), chapterPageData.getId()) &&
              ObjectsCompat.equals(getDisplayName(), chapterPageData.getDisplayName()) &&
              ObjectsCompat.equals(getPageNumber(), chapterPageData.getPageNumber()) &&
              ObjectsCompat.equals(getAudioKeysS3(), chapterPageData.getAudioKeysS3()) &&
              ObjectsCompat.equals(getAudioNames(), chapterPageData.getAudioNames()) &&
              ObjectsCompat.equals(getBedtimeStoryInfoChapter(), chapterPageData.getBedtimeStoryInfoChapter()) &&
              ObjectsCompat.equals(getCreatedAt(), chapterPageData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), chapterPageData.getUpdatedAt());
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
      .append(getBedtimeStoryInfoChapter())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("ChapterPageData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("pageNumber=" + String.valueOf(getPageNumber()) + ", ")
      .append("audioKeysS3=" + String.valueOf(getAudioKeysS3()) + ", ")
      .append("audioNames=" + String.valueOf(getAudioNames()) + ", ")
      .append("bedtimeStoryInfoChapter=" + String.valueOf(getBedtimeStoryInfoChapter()) + ", ")
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
  public static ChapterPageData justId(String id) {
    return new ChapterPageData(
      id,
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
      bedtimeStoryInfoChapter);
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
    BedtimeStoryInfoChapterStep audioNames(List<String> audioNames);
  }
  

  public interface BedtimeStoryInfoChapterStep {
    BuildStep bedtimeStoryInfoChapter(BedtimeStoryInfoChapterData bedtimeStoryInfoChapter);
  }
  

  public interface BuildStep {
    ChapterPageData build();
    BuildStep id(String id);
  }
  

  public static class Builder implements DisplayNameStep, PageNumberStep, AudioKeysS3Step, AudioNamesStep, BedtimeStoryInfoChapterStep, BuildStep {
    private String id;
    private String display_name;
    private Integer pageNumber;
    private List<String> audioKeysS3;
    private List<String> audioNames;
    private BedtimeStoryInfoChapterData bedtimeStoryInfoChapter;
    @Override
     public ChapterPageData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new ChapterPageData(
          id,
          display_name,
          pageNumber,
          audioKeysS3,
          audioNames,
          bedtimeStoryInfoChapter);
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
     public BedtimeStoryInfoChapterStep audioNames(List<String> audioNames) {
        Objects.requireNonNull(audioNames);
        this.audioNames = audioNames;
        return this;
    }
    
    @Override
     public BuildStep bedtimeStoryInfoChapter(BedtimeStoryInfoChapterData bedtimeStoryInfoChapter) {
        Objects.requireNonNull(bedtimeStoryInfoChapter);
        this.bedtimeStoryInfoChapter = bedtimeStoryInfoChapter;
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
    private CopyOfBuilder(String id, String displayName, Integer pageNumber, List<String> audioKeysS3, List<String> audioNames, BedtimeStoryInfoChapterData bedtimeStoryInfoChapter) {
      super.id(id);
      super.displayName(displayName)
        .pageNumber(pageNumber)
        .audioKeysS3(audioKeysS3)
        .audioNames(audioNames)
        .bedtimeStoryInfoChapter(bedtimeStoryInfoChapter);
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
     public CopyOfBuilder bedtimeStoryInfoChapter(BedtimeStoryInfoChapterData bedtimeStoryInfoChapter) {
      return (CopyOfBuilder) super.bedtimeStoryInfoChapter(bedtimeStoryInfoChapter);
    }
  }
  
}
