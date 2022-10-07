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

/** This is an auto generated class representing the BedtimeStoryInfoChapterData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "BedtimeStoryInfoChapterData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "BedtimeStoryInfoChapters", fields = {"bedtimeStoryInfoDataID","id"})
public final class BedtimeStoryInfoChapterData implements Model {
  public static final QueryField ID = field("BedtimeStoryInfoChapterData", "id");
  public static final QueryField DISPLAY_NAME = field("BedtimeStoryInfoChapterData", "display_name");
  public static final QueryField CHAPTER_NUMBER = field("BedtimeStoryInfoChapterData", "chapterNumber");
  public static final QueryField BEDTIME_STORY_INFO = field("BedtimeStoryInfoChapterData", "bedtimeStoryInfoDataID");
  public static final QueryField BEDTIME_STORY_INFO_ID = field("BedtimeStoryInfoChapterData", "bedtimeStoryInfoId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="Int", isRequired = true) Integer chapterNumber;
  private final @ModelField(targetType="BedtimeStoryInfoData", isRequired = true) @BelongsTo(targetName = "bedtimeStoryInfoDataID", type = BedtimeStoryInfoData.class) BedtimeStoryInfoData bedtimeStoryInfo;
  private final @ModelField(targetType="String") String bedtimeStoryInfoId;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getDisplayName() {
      return display_name;
  }
  
  public Integer getChapterNumber() {
      return chapterNumber;
  }
  
  public BedtimeStoryInfoData getBedtimeStoryInfo() {
      return bedtimeStoryInfo;
  }
  
  public String getBedtimeStoryInfoId() {
      return bedtimeStoryInfoId;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private BedtimeStoryInfoChapterData(String id, String display_name, Integer chapterNumber, BedtimeStoryInfoData bedtimeStoryInfo, String bedtimeStoryInfoId) {
    this.id = id;
    this.display_name = display_name;
    this.chapterNumber = chapterNumber;
    this.bedtimeStoryInfo = bedtimeStoryInfo;
    this.bedtimeStoryInfoId = bedtimeStoryInfoId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      BedtimeStoryInfoChapterData bedtimeStoryInfoChapterData = (BedtimeStoryInfoChapterData) obj;
      return ObjectsCompat.equals(getId(), bedtimeStoryInfoChapterData.getId()) &&
              ObjectsCompat.equals(getDisplayName(), bedtimeStoryInfoChapterData.getDisplayName()) &&
              ObjectsCompat.equals(getChapterNumber(), bedtimeStoryInfoChapterData.getChapterNumber()) &&
              ObjectsCompat.equals(getBedtimeStoryInfo(), bedtimeStoryInfoChapterData.getBedtimeStoryInfo()) &&
              ObjectsCompat.equals(getBedtimeStoryInfoId(), bedtimeStoryInfoChapterData.getBedtimeStoryInfoId()) &&
              ObjectsCompat.equals(getCreatedAt(), bedtimeStoryInfoChapterData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), bedtimeStoryInfoChapterData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getDisplayName())
      .append(getChapterNumber())
      .append(getBedtimeStoryInfo())
      .append(getBedtimeStoryInfoId())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("BedtimeStoryInfoChapterData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("chapterNumber=" + String.valueOf(getChapterNumber()) + ", ")
      .append("bedtimeStoryInfo=" + String.valueOf(getBedtimeStoryInfo()) + ", ")
      .append("bedtimeStoryInfoId=" + String.valueOf(getBedtimeStoryInfoId()) + ", ")
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
  public static BedtimeStoryInfoChapterData justId(String id) {
    return new BedtimeStoryInfoChapterData(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      display_name,
      chapterNumber,
      bedtimeStoryInfo,
      bedtimeStoryInfoId);
  }
  public interface DisplayNameStep {
    ChapterNumberStep displayName(String displayName);
  }
  

  public interface ChapterNumberStep {
    BedtimeStoryInfoStep chapterNumber(Integer chapterNumber);
  }
  

  public interface BedtimeStoryInfoStep {
    BuildStep bedtimeStoryInfo(BedtimeStoryInfoData bedtimeStoryInfo);
  }
  

  public interface BuildStep {
    BedtimeStoryInfoChapterData build();
    BuildStep id(String id);
    BuildStep bedtimeStoryInfoId(String bedtimeStoryInfoId);
  }
  

  public static class Builder implements DisplayNameStep, ChapterNumberStep, BedtimeStoryInfoStep, BuildStep {
    private String id;
    private String display_name;
    private Integer chapterNumber;
    private BedtimeStoryInfoData bedtimeStoryInfo;
    private String bedtimeStoryInfoId;
    @Override
     public BedtimeStoryInfoChapterData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new BedtimeStoryInfoChapterData(
          id,
          display_name,
          chapterNumber,
          bedtimeStoryInfo,
          bedtimeStoryInfoId);
    }
    
    @Override
     public ChapterNumberStep displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.display_name = displayName;
        return this;
    }
    
    @Override
     public BedtimeStoryInfoStep chapterNumber(Integer chapterNumber) {
        Objects.requireNonNull(chapterNumber);
        this.chapterNumber = chapterNumber;
        return this;
    }
    
    @Override
     public BuildStep bedtimeStoryInfo(BedtimeStoryInfoData bedtimeStoryInfo) {
        Objects.requireNonNull(bedtimeStoryInfo);
        this.bedtimeStoryInfo = bedtimeStoryInfo;
        return this;
    }
    
    @Override
     public BuildStep bedtimeStoryInfoId(String bedtimeStoryInfoId) {
        this.bedtimeStoryInfoId = bedtimeStoryInfoId;
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
    private CopyOfBuilder(String id, String displayName, Integer chapterNumber, BedtimeStoryInfoData bedtimeStoryInfo, String bedtimeStoryInfoId) {
      super.id(id);
      super.displayName(displayName)
        .chapterNumber(chapterNumber)
        .bedtimeStoryInfo(bedtimeStoryInfo)
        .bedtimeStoryInfoId(bedtimeStoryInfoId);
    }
    
    @Override
     public CopyOfBuilder displayName(String displayName) {
      return (CopyOfBuilder) super.displayName(displayName);
    }
    
    @Override
     public CopyOfBuilder chapterNumber(Integer chapterNumber) {
      return (CopyOfBuilder) super.chapterNumber(chapterNumber);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryInfo(BedtimeStoryInfoData bedtimeStoryInfo) {
      return (CopyOfBuilder) super.bedtimeStoryInfo(bedtimeStoryInfo);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryInfoId(String bedtimeStoryInfoId) {
      return (CopyOfBuilder) super.bedtimeStoryInfoId(bedtimeStoryInfoId);
    }
  }
  
}
