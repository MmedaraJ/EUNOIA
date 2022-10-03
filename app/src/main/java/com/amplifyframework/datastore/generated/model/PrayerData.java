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

/** This is an auto generated class representing the PrayerData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "PrayerData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "PrayersOwnedByUser", fields = {"userDataID","displayName"})
public final class PrayerData implements Model {
  public static final QueryField ID = field("PrayerData", "id");
  public static final QueryField PRAYER_OWNER = field("PrayerData", "userDataID");
  public static final QueryField DISPLAY_NAME = field("PrayerData", "displayName");
  public static final QueryField SHORT_DESCRIPTION = field("PrayerData", "shortDescription");
  public static final QueryField LONG_DESCRIPTION = field("PrayerData", "longDescription");
  public static final QueryField AUDIO_KEY_S3 = field("PrayerData", "audioKeyS3");
  public static final QueryField ICON = field("PrayerData", "icon");
  public static final QueryField FULL_PLAY_TIME = field("PrayerData", "fullPlayTime");
  public static final QueryField VISIBLE_TO_OTHERS = field("PrayerData", "visibleToOthers");
  public static final QueryField RELIGION = field("PrayerData", "religion");
  public static final QueryField COUNTRY = field("PrayerData", "country");
  public static final QueryField TAGS = field("PrayerData", "tags");
  public static final QueryField AUDIO_SOURCE = field("PrayerData", "audioSource");
  public static final QueryField APPROVAL_STATUS = field("PrayerData", "approvalStatus");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData prayerOwner;
  private final @ModelField(targetType="String", isRequired = true) String displayName;
  private final @ModelField(targetType="String") String shortDescription;
  private final @ModelField(targetType="String") String longDescription;
  private final @ModelField(targetType="String", isRequired = true) String audioKeyS3;
  private final @ModelField(targetType="Int", isRequired = true) Integer icon;
  private final @ModelField(targetType="Int", isRequired = true) Integer fullPlayTime;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean visibleToOthers;
  private final @ModelField(targetType="String") String religion;
  private final @ModelField(targetType="String") String country;
  private final @ModelField(targetType="String") List<String> tags;
  private final @ModelField(targetType="PrayerAudioSource") PrayerAudioSource audioSource;
  private final @ModelField(targetType="PrayerApprovalStatus") PrayerApprovalStatus approvalStatus;
  private final @ModelField(targetType="UserPrayerRelationship") @HasMany(associatedWith = "userPrayerRelationshipPrayer", type = UserPrayerRelationship.class) List<UserPrayerRelationship> userPrayerRelationshipsOwnedByPrayer = null;
  private final @ModelField(targetType="RoutinePrayer") @HasMany(associatedWith = "prayerData", type = RoutinePrayer.class) List<RoutinePrayer> routines = null;
  private final @ModelField(targetType="UserPrayer") @HasMany(associatedWith = "prayerData", type = UserPrayer.class) List<UserPrayer> users = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getPrayerOwner() {
      return prayerOwner;
  }
  
  public String getDisplayName() {
      return displayName;
  }
  
  public String getShortDescription() {
      return shortDescription;
  }
  
  public String getLongDescription() {
      return longDescription;
  }
  
  public String getAudioKeyS3() {
      return audioKeyS3;
  }
  
  public Integer getIcon() {
      return icon;
  }
  
  public Integer getFullPlayTime() {
      return fullPlayTime;
  }
  
  public Boolean getVisibleToOthers() {
      return visibleToOthers;
  }
  
  public String getReligion() {
      return religion;
  }
  
  public String getCountry() {
      return country;
  }
  
  public List<String> getTags() {
      return tags;
  }
  
  public PrayerAudioSource getAudioSource() {
      return audioSource;
  }
  
  public PrayerApprovalStatus getApprovalStatus() {
      return approvalStatus;
  }
  
  public List<UserPrayerRelationship> getUserPrayerRelationshipsOwnedByPrayer() {
      return userPrayerRelationshipsOwnedByPrayer;
  }
  
  public List<RoutinePrayer> getRoutines() {
      return routines;
  }
  
  public List<UserPrayer> getUsers() {
      return users;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private PrayerData(String id, UserData prayerOwner, String displayName, String shortDescription, String longDescription, String audioKeyS3, Integer icon, Integer fullPlayTime, Boolean visibleToOthers, String religion, String country, List<String> tags, PrayerAudioSource audioSource, PrayerApprovalStatus approvalStatus) {
    this.id = id;
    this.prayerOwner = prayerOwner;
    this.displayName = displayName;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.audioKeyS3 = audioKeyS3;
    this.icon = icon;
    this.fullPlayTime = fullPlayTime;
    this.visibleToOthers = visibleToOthers;
    this.religion = religion;
    this.country = country;
    this.tags = tags;
    this.audioSource = audioSource;
    this.approvalStatus = approvalStatus;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      PrayerData prayerData = (PrayerData) obj;
      return ObjectsCompat.equals(getId(), prayerData.getId()) &&
              ObjectsCompat.equals(getPrayerOwner(), prayerData.getPrayerOwner()) &&
              ObjectsCompat.equals(getDisplayName(), prayerData.getDisplayName()) &&
              ObjectsCompat.equals(getShortDescription(), prayerData.getShortDescription()) &&
              ObjectsCompat.equals(getLongDescription(), prayerData.getLongDescription()) &&
              ObjectsCompat.equals(getAudioKeyS3(), prayerData.getAudioKeyS3()) &&
              ObjectsCompat.equals(getIcon(), prayerData.getIcon()) &&
              ObjectsCompat.equals(getFullPlayTime(), prayerData.getFullPlayTime()) &&
              ObjectsCompat.equals(getVisibleToOthers(), prayerData.getVisibleToOthers()) &&
              ObjectsCompat.equals(getReligion(), prayerData.getReligion()) &&
              ObjectsCompat.equals(getCountry(), prayerData.getCountry()) &&
              ObjectsCompat.equals(getTags(), prayerData.getTags()) &&
              ObjectsCompat.equals(getAudioSource(), prayerData.getAudioSource()) &&
              ObjectsCompat.equals(getApprovalStatus(), prayerData.getApprovalStatus()) &&
              ObjectsCompat.equals(getCreatedAt(), prayerData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), prayerData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getPrayerOwner())
      .append(getDisplayName())
      .append(getShortDescription())
      .append(getLongDescription())
      .append(getAudioKeyS3())
      .append(getIcon())
      .append(getFullPlayTime())
      .append(getVisibleToOthers())
      .append(getReligion())
      .append(getCountry())
      .append(getTags())
      .append(getAudioSource())
      .append(getApprovalStatus())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("PrayerData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("prayerOwner=" + String.valueOf(getPrayerOwner()) + ", ")
      .append("displayName=" + String.valueOf(getDisplayName()) + ", ")
      .append("shortDescription=" + String.valueOf(getShortDescription()) + ", ")
      .append("longDescription=" + String.valueOf(getLongDescription()) + ", ")
      .append("audioKeyS3=" + String.valueOf(getAudioKeyS3()) + ", ")
      .append("icon=" + String.valueOf(getIcon()) + ", ")
      .append("fullPlayTime=" + String.valueOf(getFullPlayTime()) + ", ")
      .append("visibleToOthers=" + String.valueOf(getVisibleToOthers()) + ", ")
      .append("religion=" + String.valueOf(getReligion()) + ", ")
      .append("country=" + String.valueOf(getCountry()) + ", ")
      .append("tags=" + String.valueOf(getTags()) + ", ")
      .append("audioSource=" + String.valueOf(getAudioSource()) + ", ")
      .append("approvalStatus=" + String.valueOf(getApprovalStatus()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static PrayerOwnerStep builder() {
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
  public static PrayerData justId(String id) {
    return new PrayerData(
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
      prayerOwner,
      displayName,
      shortDescription,
      longDescription,
      audioKeyS3,
      icon,
      fullPlayTime,
      visibleToOthers,
      religion,
      country,
      tags,
      audioSource,
      approvalStatus);
  }
  public interface PrayerOwnerStep {
    DisplayNameStep prayerOwner(UserData prayerOwner);
  }
  

  public interface DisplayNameStep {
    AudioKeyS3Step displayName(String displayName);
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
    BuildStep visibleToOthers(Boolean visibleToOthers);
  }
  

  public interface BuildStep {
    PrayerData build();
    BuildStep id(String id);
    BuildStep shortDescription(String shortDescription);
    BuildStep longDescription(String longDescription);
    BuildStep religion(String religion);
    BuildStep country(String country);
    BuildStep tags(List<String> tags);
    BuildStep audioSource(PrayerAudioSource audioSource);
    BuildStep approvalStatus(PrayerApprovalStatus approvalStatus);
  }
  

  public static class Builder implements PrayerOwnerStep, DisplayNameStep, AudioKeyS3Step, IconStep, FullPlayTimeStep, VisibleToOthersStep, BuildStep {
    private String id;
    private UserData prayerOwner;
    private String displayName;
    private String audioKeyS3;
    private Integer icon;
    private Integer fullPlayTime;
    private Boolean visibleToOthers;
    private String shortDescription;
    private String longDescription;
    private String religion;
    private String country;
    private List<String> tags;
    private PrayerAudioSource audioSource;
    private PrayerApprovalStatus approvalStatus;
    @Override
     public PrayerData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new PrayerData(
          id,
          prayerOwner,
          displayName,
          shortDescription,
          longDescription,
          audioKeyS3,
          icon,
          fullPlayTime,
          visibleToOthers,
          religion,
          country,
          tags,
          audioSource,
          approvalStatus);
    }
    
    @Override
     public DisplayNameStep prayerOwner(UserData prayerOwner) {
        Objects.requireNonNull(prayerOwner);
        this.prayerOwner = prayerOwner;
        return this;
    }
    
    @Override
     public AudioKeyS3Step displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.displayName = displayName;
        return this;
    }
    
    @Override
     public IconStep audioKeyS3(String audioKeyS3) {
        Objects.requireNonNull(audioKeyS3);
        this.audioKeyS3 = audioKeyS3;
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
     public BuildStep visibleToOthers(Boolean visibleToOthers) {
        Objects.requireNonNull(visibleToOthers);
        this.visibleToOthers = visibleToOthers;
        return this;
    }
    
    @Override
     public BuildStep shortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }
    
    @Override
     public BuildStep longDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }
    
    @Override
     public BuildStep religion(String religion) {
        this.religion = religion;
        return this;
    }
    
    @Override
     public BuildStep country(String country) {
        this.country = country;
        return this;
    }
    
    @Override
     public BuildStep tags(List<String> tags) {
        this.tags = tags;
        return this;
    }
    
    @Override
     public BuildStep audioSource(PrayerAudioSource audioSource) {
        this.audioSource = audioSource;
        return this;
    }
    
    @Override
     public BuildStep approvalStatus(PrayerApprovalStatus approvalStatus) {
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
    private CopyOfBuilder(String id, UserData prayerOwner, String displayName, String shortDescription, String longDescription, String audioKeyS3, Integer icon, Integer fullPlayTime, Boolean visibleToOthers, String religion, String country, List<String> tags, PrayerAudioSource audioSource, PrayerApprovalStatus approvalStatus) {
      super.id(id);
      super.prayerOwner(prayerOwner)
        .displayName(displayName)
        .audioKeyS3(audioKeyS3)
        .icon(icon)
        .fullPlayTime(fullPlayTime)
        .visibleToOthers(visibleToOthers)
        .shortDescription(shortDescription)
        .longDescription(longDescription)
        .religion(religion)
        .country(country)
        .tags(tags)
        .audioSource(audioSource)
        .approvalStatus(approvalStatus);
    }
    
    @Override
     public CopyOfBuilder prayerOwner(UserData prayerOwner) {
      return (CopyOfBuilder) super.prayerOwner(prayerOwner);
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
     public CopyOfBuilder fullPlayTime(Integer fullPlayTime) {
      return (CopyOfBuilder) super.fullPlayTime(fullPlayTime);
    }
    
    @Override
     public CopyOfBuilder visibleToOthers(Boolean visibleToOthers) {
      return (CopyOfBuilder) super.visibleToOthers(visibleToOthers);
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
     public CopyOfBuilder religion(String religion) {
      return (CopyOfBuilder) super.religion(religion);
    }
    
    @Override
     public CopyOfBuilder country(String country) {
      return (CopyOfBuilder) super.country(country);
    }
    
    @Override
     public CopyOfBuilder tags(List<String> tags) {
      return (CopyOfBuilder) super.tags(tags);
    }
    
    @Override
     public CopyOfBuilder audioSource(PrayerAudioSource audioSource) {
      return (CopyOfBuilder) super.audioSource(audioSource);
    }
    
    @Override
     public CopyOfBuilder approvalStatus(PrayerApprovalStatus approvalStatus) {
      return (CopyOfBuilder) super.approvalStatus(approvalStatus);
    }
  }
  
}
