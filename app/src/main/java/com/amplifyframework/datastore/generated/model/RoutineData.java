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

/** This is an auto generated class representing the RoutineData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "RoutineData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "RoutinesOwnedByUser", fields = {"userDataID","displayName"})
public final class RoutineData implements Model {
  public static final QueryField ID = field("RoutineData", "id");
  public static final QueryField ROUTINE_OWNER = field("RoutineData", "userDataID");
  public static final QueryField ORIGINAL_NAME = field("RoutineData", "originalName");
  public static final QueryField DISPLAY_NAME = field("RoutineData", "displayName");
  public static final QueryField NUMBER_OF_STEPS = field("RoutineData", "numberOfSteps");
  public static final QueryField NUMBER_OF_TIMES_USED = field("RoutineData", "numberOfTimesUsed");
  public static final QueryField FULL_PLAY_TIME = field("RoutineData", "fullPlayTime");
  public static final QueryField ICON = field("RoutineData", "icon");
  public static final QueryField VISIBLE_TO_OTHERS = field("RoutineData", "visibleToOthers");
  public static final QueryField COLOR_HEX = field("RoutineData", "colorHEX");
  public static final QueryField PLAY_SOUND_DURING_STRETCH = field("RoutineData", "playSoundDuringStretch");
  public static final QueryField PLAY_SOUND_DURING_PRAYER = field("RoutineData", "playSoundDuringPrayer");
  public static final QueryField PLAY_SOUND_DURING_BREATHING = field("RoutineData", "playSoundDuringBreathing");
  public static final QueryField PLAY_SOUND_DURING_SELF_LOVE = field("RoutineData", "playSoundDuringSelfLove");
  public static final QueryField PLAY_SOUND_DURING_BEDTIME_STORY = field("RoutineData", "playSoundDuringBedtimeStory");
  public static final QueryField PLAY_SOUND_DURING_SLEEP = field("RoutineData", "playSoundDuringSleep");
  public static final QueryField EACH_SOUND_PLAY_TIME = field("RoutineData", "eachSoundPlayTime");
  public static final QueryField PRAYER_PLAY_TIME = field("RoutineData", "prayerPlayTime");
  public static final QueryField BEDTIME_STORY_PLAY_TIME = field("RoutineData", "bedtimeStoryPlayTime");
  public static final QueryField SELF_LOVE_PLAY_TIME = field("RoutineData", "selfLovePlayTime");
  public static final QueryField STRETCH_TIME = field("RoutineData", "stretchTime");
  public static final QueryField BREATHING_TIME = field("RoutineData", "breathingTime");
  public static final QueryField CURRENT_BEDTIME_STORY_PLAYING_INDEX = field("RoutineData", "currentBedtimeStoryPlayingIndex");
  public static final QueryField CURRENT_BEDTIME_STORY_CONTINUE_PLAYING_TIME = field("RoutineData", "currentBedtimeStoryContinuePlayingTime");
  public static final QueryField CURRENT_SELF_LOVE_PLAYING_INDEX = field("RoutineData", "currentSelfLovePlayingIndex");
  public static final QueryField CURRENT_SELF_LOVE_CONTINUE_PLAYING_TIME = field("RoutineData", "currentSelfLoveContinuePlayingTime");
  public static final QueryField CURRENT_PRAYER_PLAYING_INDEX = field("RoutineData", "currentPrayerPlayingIndex");
  public static final QueryField CURRENT_PRAYER_CONTINUE_PLAYING_TIME = field("RoutineData", "currentPrayerContinuePlayingTime");
  public static final QueryField PLAYING_ORDER = field("RoutineData", "playingOrder");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData routineOwner;
  private final @ModelField(targetType="String") String originalName;
  private final @ModelField(targetType="String") String displayName;
  private final @ModelField(targetType="Int") Integer numberOfSteps;
  private final @ModelField(targetType="Int") Integer numberOfTimesUsed;
  private final @ModelField(targetType="Int") Integer fullPlayTime;
  private final @ModelField(targetType="Int") Integer icon;
  private final @ModelField(targetType="Boolean") Boolean visibleToOthers;
  private final @ModelField(targetType="Int") Integer colorHEX;
  private final @ModelField(targetType="Boolean") Boolean playSoundDuringStretch;
  private final @ModelField(targetType="Boolean") Boolean playSoundDuringPrayer;
  private final @ModelField(targetType="Boolean") Boolean playSoundDuringBreathing;
  private final @ModelField(targetType="Boolean") Boolean playSoundDuringSelfLove;
  private final @ModelField(targetType="Boolean") Boolean playSoundDuringBedtimeStory;
  private final @ModelField(targetType="Boolean") Boolean playSoundDuringSleep;
  private final @ModelField(targetType="Int") Integer eachSoundPlayTime;
  private final @ModelField(targetType="Int") Integer prayerPlayTime;
  private final @ModelField(targetType="Int") Integer bedtimeStoryPlayTime;
  private final @ModelField(targetType="Int") Integer selfLovePlayTime;
  private final @ModelField(targetType="Int") Integer stretchTime;
  private final @ModelField(targetType="Int") Integer breathingTime;
  private final @ModelField(targetType="Int") Integer currentBedtimeStoryPlayingIndex;
  private final @ModelField(targetType="Int") Integer currentBedtimeStoryContinuePlayingTime;
  private final @ModelField(targetType="Int") Integer currentSelfLovePlayingIndex;
  private final @ModelField(targetType="Int") Integer currentSelfLoveContinuePlayingTime;
  private final @ModelField(targetType="Int") Integer currentPrayerPlayingIndex;
  private final @ModelField(targetType="Int") Integer currentPrayerContinuePlayingTime;
  private final @ModelField(targetType="RoutineSound") @HasMany(associatedWith = "routineData", type = RoutineSound.class) List<RoutineSound> sounds = null;
  private final @ModelField(targetType="RoutinePreset") @HasMany(associatedWith = "routineData", type = RoutinePreset.class) List<RoutinePreset> presets = null;
  private final @ModelField(targetType="RoutinePrayer") @HasMany(associatedWith = "routineData", type = RoutinePrayer.class) List<RoutinePrayer> prayers = null;
  private final @ModelField(targetType="RoutineStretch") @HasMany(associatedWith = "routineData", type = RoutineStretch.class) List<RoutineStretch> stretches = null;
  private final @ModelField(targetType="RoutineBreathing") @HasMany(associatedWith = "routineData", type = RoutineBreathing.class) List<RoutineBreathing> breathing = null;
  private final @ModelField(targetType="RoutineBedtimeStoryInfo") @HasMany(associatedWith = "routineData", type = RoutineBedtimeStoryInfo.class) List<RoutineBedtimeStoryInfo> bedtimeStories = null;
  private final @ModelField(targetType="RoutineSelfLove") @HasMany(associatedWith = "routineData", type = RoutineSelfLove.class) List<RoutineSelfLove> selfLoves = null;
  private final @ModelField(targetType="UserRoutine") @HasMany(associatedWith = "routineData", type = UserRoutine.class) List<UserRoutine> users = null;
  private final @ModelField(targetType="String") List<String> playingOrder;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getRoutineOwner() {
      return routineOwner;
  }
  
  public String getOriginalName() {
      return originalName;
  }
  
  public String getDisplayName() {
      return displayName;
  }
  
  public Integer getNumberOfSteps() {
      return numberOfSteps;
  }
  
  public Integer getNumberOfTimesUsed() {
      return numberOfTimesUsed;
  }
  
  public Integer getFullPlayTime() {
      return fullPlayTime;
  }
  
  public Integer getIcon() {
      return icon;
  }
  
  public Boolean getVisibleToOthers() {
      return visibleToOthers;
  }
  
  public Integer getColorHex() {
      return colorHEX;
  }
  
  public Boolean getPlaySoundDuringStretch() {
      return playSoundDuringStretch;
  }
  
  public Boolean getPlaySoundDuringPrayer() {
      return playSoundDuringPrayer;
  }
  
  public Boolean getPlaySoundDuringBreathing() {
      return playSoundDuringBreathing;
  }
  
  public Boolean getPlaySoundDuringSelfLove() {
      return playSoundDuringSelfLove;
  }
  
  public Boolean getPlaySoundDuringBedtimeStory() {
      return playSoundDuringBedtimeStory;
  }
  
  public Boolean getPlaySoundDuringSleep() {
      return playSoundDuringSleep;
  }
  
  public Integer getEachSoundPlayTime() {
      return eachSoundPlayTime;
  }
  
  public Integer getPrayerPlayTime() {
      return prayerPlayTime;
  }
  
  public Integer getBedtimeStoryPlayTime() {
      return bedtimeStoryPlayTime;
  }
  
  public Integer getSelfLovePlayTime() {
      return selfLovePlayTime;
  }
  
  public Integer getStretchTime() {
      return stretchTime;
  }
  
  public Integer getBreathingTime() {
      return breathingTime;
  }
  
  public Integer getCurrentBedtimeStoryPlayingIndex() {
      return currentBedtimeStoryPlayingIndex;
  }
  
  public Integer getCurrentBedtimeStoryContinuePlayingTime() {
      return currentBedtimeStoryContinuePlayingTime;
  }
  
  public Integer getCurrentSelfLovePlayingIndex() {
      return currentSelfLovePlayingIndex;
  }
  
  public Integer getCurrentSelfLoveContinuePlayingTime() {
      return currentSelfLoveContinuePlayingTime;
  }
  
  public Integer getCurrentPrayerPlayingIndex() {
      return currentPrayerPlayingIndex;
  }
  
  public Integer getCurrentPrayerContinuePlayingTime() {
      return currentPrayerContinuePlayingTime;
  }
  
  public List<RoutineSound> getSounds() {
      return sounds;
  }
  
  public List<RoutinePreset> getPresets() {
      return presets;
  }
  
  public List<RoutinePrayer> getPrayers() {
      return prayers;
  }
  
  public List<RoutineStretch> getStretches() {
      return stretches;
  }
  
  public List<RoutineBreathing> getBreathing() {
      return breathing;
  }
  
  public List<RoutineBedtimeStoryInfo> getBedtimeStories() {
      return bedtimeStories;
  }
  
  public List<RoutineSelfLove> getSelfLoves() {
      return selfLoves;
  }
  
  public List<UserRoutine> getUsers() {
      return users;
  }
  
  public List<String> getPlayingOrder() {
      return playingOrder;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private RoutineData(String id, UserData routineOwner, String originalName, String displayName, Integer numberOfSteps, Integer numberOfTimesUsed, Integer fullPlayTime, Integer icon, Boolean visibleToOthers, Integer colorHEX, Boolean playSoundDuringStretch, Boolean playSoundDuringPrayer, Boolean playSoundDuringBreathing, Boolean playSoundDuringSelfLove, Boolean playSoundDuringBedtimeStory, Boolean playSoundDuringSleep, Integer eachSoundPlayTime, Integer prayerPlayTime, Integer bedtimeStoryPlayTime, Integer selfLovePlayTime, Integer stretchTime, Integer breathingTime, Integer currentBedtimeStoryPlayingIndex, Integer currentBedtimeStoryContinuePlayingTime, Integer currentSelfLovePlayingIndex, Integer currentSelfLoveContinuePlayingTime, Integer currentPrayerPlayingIndex, Integer currentPrayerContinuePlayingTime, List<String> playingOrder) {
    this.id = id;
    this.routineOwner = routineOwner;
    this.originalName = originalName;
    this.displayName = displayName;
    this.numberOfSteps = numberOfSteps;
    this.numberOfTimesUsed = numberOfTimesUsed;
    this.fullPlayTime = fullPlayTime;
    this.icon = icon;
    this.visibleToOthers = visibleToOthers;
    this.colorHEX = colorHEX;
    this.playSoundDuringStretch = playSoundDuringStretch;
    this.playSoundDuringPrayer = playSoundDuringPrayer;
    this.playSoundDuringBreathing = playSoundDuringBreathing;
    this.playSoundDuringSelfLove = playSoundDuringSelfLove;
    this.playSoundDuringBedtimeStory = playSoundDuringBedtimeStory;
    this.playSoundDuringSleep = playSoundDuringSleep;
    this.eachSoundPlayTime = eachSoundPlayTime;
    this.prayerPlayTime = prayerPlayTime;
    this.bedtimeStoryPlayTime = bedtimeStoryPlayTime;
    this.selfLovePlayTime = selfLovePlayTime;
    this.stretchTime = stretchTime;
    this.breathingTime = breathingTime;
    this.currentBedtimeStoryPlayingIndex = currentBedtimeStoryPlayingIndex;
    this.currentBedtimeStoryContinuePlayingTime = currentBedtimeStoryContinuePlayingTime;
    this.currentSelfLovePlayingIndex = currentSelfLovePlayingIndex;
    this.currentSelfLoveContinuePlayingTime = currentSelfLoveContinuePlayingTime;
    this.currentPrayerPlayingIndex = currentPrayerPlayingIndex;
    this.currentPrayerContinuePlayingTime = currentPrayerContinuePlayingTime;
    this.playingOrder = playingOrder;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      RoutineData routineData = (RoutineData) obj;
      return ObjectsCompat.equals(getId(), routineData.getId()) &&
              ObjectsCompat.equals(getRoutineOwner(), routineData.getRoutineOwner()) &&
              ObjectsCompat.equals(getOriginalName(), routineData.getOriginalName()) &&
              ObjectsCompat.equals(getDisplayName(), routineData.getDisplayName()) &&
              ObjectsCompat.equals(getNumberOfSteps(), routineData.getNumberOfSteps()) &&
              ObjectsCompat.equals(getNumberOfTimesUsed(), routineData.getNumberOfTimesUsed()) &&
              ObjectsCompat.equals(getFullPlayTime(), routineData.getFullPlayTime()) &&
              ObjectsCompat.equals(getIcon(), routineData.getIcon()) &&
              ObjectsCompat.equals(getVisibleToOthers(), routineData.getVisibleToOthers()) &&
              ObjectsCompat.equals(getColorHex(), routineData.getColorHex()) &&
              ObjectsCompat.equals(getPlaySoundDuringStretch(), routineData.getPlaySoundDuringStretch()) &&
              ObjectsCompat.equals(getPlaySoundDuringPrayer(), routineData.getPlaySoundDuringPrayer()) &&
              ObjectsCompat.equals(getPlaySoundDuringBreathing(), routineData.getPlaySoundDuringBreathing()) &&
              ObjectsCompat.equals(getPlaySoundDuringSelfLove(), routineData.getPlaySoundDuringSelfLove()) &&
              ObjectsCompat.equals(getPlaySoundDuringBedtimeStory(), routineData.getPlaySoundDuringBedtimeStory()) &&
              ObjectsCompat.equals(getPlaySoundDuringSleep(), routineData.getPlaySoundDuringSleep()) &&
              ObjectsCompat.equals(getEachSoundPlayTime(), routineData.getEachSoundPlayTime()) &&
              ObjectsCompat.equals(getPrayerPlayTime(), routineData.getPrayerPlayTime()) &&
              ObjectsCompat.equals(getBedtimeStoryPlayTime(), routineData.getBedtimeStoryPlayTime()) &&
              ObjectsCompat.equals(getSelfLovePlayTime(), routineData.getSelfLovePlayTime()) &&
              ObjectsCompat.equals(getStretchTime(), routineData.getStretchTime()) &&
              ObjectsCompat.equals(getBreathingTime(), routineData.getBreathingTime()) &&
              ObjectsCompat.equals(getCurrentBedtimeStoryPlayingIndex(), routineData.getCurrentBedtimeStoryPlayingIndex()) &&
              ObjectsCompat.equals(getCurrentBedtimeStoryContinuePlayingTime(), routineData.getCurrentBedtimeStoryContinuePlayingTime()) &&
              ObjectsCompat.equals(getCurrentSelfLovePlayingIndex(), routineData.getCurrentSelfLovePlayingIndex()) &&
              ObjectsCompat.equals(getCurrentSelfLoveContinuePlayingTime(), routineData.getCurrentSelfLoveContinuePlayingTime()) &&
              ObjectsCompat.equals(getCurrentPrayerPlayingIndex(), routineData.getCurrentPrayerPlayingIndex()) &&
              ObjectsCompat.equals(getCurrentPrayerContinuePlayingTime(), routineData.getCurrentPrayerContinuePlayingTime()) &&
              ObjectsCompat.equals(getPlayingOrder(), routineData.getPlayingOrder()) &&
              ObjectsCompat.equals(getCreatedAt(), routineData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), routineData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRoutineOwner())
      .append(getOriginalName())
      .append(getDisplayName())
      .append(getNumberOfSteps())
      .append(getNumberOfTimesUsed())
      .append(getFullPlayTime())
      .append(getIcon())
      .append(getVisibleToOthers())
      .append(getColorHex())
      .append(getPlaySoundDuringStretch())
      .append(getPlaySoundDuringPrayer())
      .append(getPlaySoundDuringBreathing())
      .append(getPlaySoundDuringSelfLove())
      .append(getPlaySoundDuringBedtimeStory())
      .append(getPlaySoundDuringSleep())
      .append(getEachSoundPlayTime())
      .append(getPrayerPlayTime())
      .append(getBedtimeStoryPlayTime())
      .append(getSelfLovePlayTime())
      .append(getStretchTime())
      .append(getBreathingTime())
      .append(getCurrentBedtimeStoryPlayingIndex())
      .append(getCurrentBedtimeStoryContinuePlayingTime())
      .append(getCurrentSelfLovePlayingIndex())
      .append(getCurrentSelfLoveContinuePlayingTime())
      .append(getCurrentPrayerPlayingIndex())
      .append(getCurrentPrayerContinuePlayingTime())
      .append(getPlayingOrder())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("RoutineData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("routineOwner=" + String.valueOf(getRoutineOwner()) + ", ")
      .append("originalName=" + String.valueOf(getOriginalName()) + ", ")
      .append("displayName=" + String.valueOf(getDisplayName()) + ", ")
      .append("numberOfSteps=" + String.valueOf(getNumberOfSteps()) + ", ")
      .append("numberOfTimesUsed=" + String.valueOf(getNumberOfTimesUsed()) + ", ")
      .append("fullPlayTime=" + String.valueOf(getFullPlayTime()) + ", ")
      .append("icon=" + String.valueOf(getIcon()) + ", ")
      .append("visibleToOthers=" + String.valueOf(getVisibleToOthers()) + ", ")
      .append("colorHEX=" + String.valueOf(getColorHex()) + ", ")
      .append("playSoundDuringStretch=" + String.valueOf(getPlaySoundDuringStretch()) + ", ")
      .append("playSoundDuringPrayer=" + String.valueOf(getPlaySoundDuringPrayer()) + ", ")
      .append("playSoundDuringBreathing=" + String.valueOf(getPlaySoundDuringBreathing()) + ", ")
      .append("playSoundDuringSelfLove=" + String.valueOf(getPlaySoundDuringSelfLove()) + ", ")
      .append("playSoundDuringBedtimeStory=" + String.valueOf(getPlaySoundDuringBedtimeStory()) + ", ")
      .append("playSoundDuringSleep=" + String.valueOf(getPlaySoundDuringSleep()) + ", ")
      .append("eachSoundPlayTime=" + String.valueOf(getEachSoundPlayTime()) + ", ")
      .append("prayerPlayTime=" + String.valueOf(getPrayerPlayTime()) + ", ")
      .append("bedtimeStoryPlayTime=" + String.valueOf(getBedtimeStoryPlayTime()) + ", ")
      .append("selfLovePlayTime=" + String.valueOf(getSelfLovePlayTime()) + ", ")
      .append("stretchTime=" + String.valueOf(getStretchTime()) + ", ")
      .append("breathingTime=" + String.valueOf(getBreathingTime()) + ", ")
      .append("currentBedtimeStoryPlayingIndex=" + String.valueOf(getCurrentBedtimeStoryPlayingIndex()) + ", ")
      .append("currentBedtimeStoryContinuePlayingTime=" + String.valueOf(getCurrentBedtimeStoryContinuePlayingTime()) + ", ")
      .append("currentSelfLovePlayingIndex=" + String.valueOf(getCurrentSelfLovePlayingIndex()) + ", ")
      .append("currentSelfLoveContinuePlayingTime=" + String.valueOf(getCurrentSelfLoveContinuePlayingTime()) + ", ")
      .append("currentPrayerPlayingIndex=" + String.valueOf(getCurrentPrayerPlayingIndex()) + ", ")
      .append("currentPrayerContinuePlayingTime=" + String.valueOf(getCurrentPrayerContinuePlayingTime()) + ", ")
      .append("playingOrder=" + String.valueOf(getPlayingOrder()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static RoutineOwnerStep builder() {
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
  public static RoutineData justId(String id) {
    return new RoutineData(
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
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      routineOwner,
      originalName,
      displayName,
      numberOfSteps,
      numberOfTimesUsed,
      fullPlayTime,
      icon,
      visibleToOthers,
      colorHEX,
      playSoundDuringStretch,
      playSoundDuringPrayer,
      playSoundDuringBreathing,
      playSoundDuringSelfLove,
      playSoundDuringBedtimeStory,
      playSoundDuringSleep,
      eachSoundPlayTime,
      prayerPlayTime,
      bedtimeStoryPlayTime,
      selfLovePlayTime,
      stretchTime,
      breathingTime,
      currentBedtimeStoryPlayingIndex,
      currentBedtimeStoryContinuePlayingTime,
      currentSelfLovePlayingIndex,
      currentSelfLoveContinuePlayingTime,
      currentPrayerPlayingIndex,
      currentPrayerContinuePlayingTime,
      playingOrder);
  }
  public interface RoutineOwnerStep {
    BuildStep routineOwner(UserData routineOwner);
  }
  

  public interface BuildStep {
    RoutineData build();
    BuildStep id(String id);
    BuildStep originalName(String originalName);
    BuildStep displayName(String displayName);
    BuildStep numberOfSteps(Integer numberOfSteps);
    BuildStep numberOfTimesUsed(Integer numberOfTimesUsed);
    BuildStep fullPlayTime(Integer fullPlayTime);
    BuildStep icon(Integer icon);
    BuildStep visibleToOthers(Boolean visibleToOthers);
    BuildStep colorHex(Integer colorHex);
    BuildStep playSoundDuringStretch(Boolean playSoundDuringStretch);
    BuildStep playSoundDuringPrayer(Boolean playSoundDuringPrayer);
    BuildStep playSoundDuringBreathing(Boolean playSoundDuringBreathing);
    BuildStep playSoundDuringSelfLove(Boolean playSoundDuringSelfLove);
    BuildStep playSoundDuringBedtimeStory(Boolean playSoundDuringBedtimeStory);
    BuildStep playSoundDuringSleep(Boolean playSoundDuringSleep);
    BuildStep eachSoundPlayTime(Integer eachSoundPlayTime);
    BuildStep prayerPlayTime(Integer prayerPlayTime);
    BuildStep bedtimeStoryPlayTime(Integer bedtimeStoryPlayTime);
    BuildStep selfLovePlayTime(Integer selfLovePlayTime);
    BuildStep stretchTime(Integer stretchTime);
    BuildStep breathingTime(Integer breathingTime);
    BuildStep currentBedtimeStoryPlayingIndex(Integer currentBedtimeStoryPlayingIndex);
    BuildStep currentBedtimeStoryContinuePlayingTime(Integer currentBedtimeStoryContinuePlayingTime);
    BuildStep currentSelfLovePlayingIndex(Integer currentSelfLovePlayingIndex);
    BuildStep currentSelfLoveContinuePlayingTime(Integer currentSelfLoveContinuePlayingTime);
    BuildStep currentPrayerPlayingIndex(Integer currentPrayerPlayingIndex);
    BuildStep currentPrayerContinuePlayingTime(Integer currentPrayerContinuePlayingTime);
    BuildStep playingOrder(List<String> playingOrder);
  }
  

  public static class Builder implements RoutineOwnerStep, BuildStep {
    private String id;
    private UserData routineOwner;
    private String originalName;
    private String displayName;
    private Integer numberOfSteps;
    private Integer numberOfTimesUsed;
    private Integer fullPlayTime;
    private Integer icon;
    private Boolean visibleToOthers;
    private Integer colorHEX;
    private Boolean playSoundDuringStretch;
    private Boolean playSoundDuringPrayer;
    private Boolean playSoundDuringBreathing;
    private Boolean playSoundDuringSelfLove;
    private Boolean playSoundDuringBedtimeStory;
    private Boolean playSoundDuringSleep;
    private Integer eachSoundPlayTime;
    private Integer prayerPlayTime;
    private Integer bedtimeStoryPlayTime;
    private Integer selfLovePlayTime;
    private Integer stretchTime;
    private Integer breathingTime;
    private Integer currentBedtimeStoryPlayingIndex;
    private Integer currentBedtimeStoryContinuePlayingTime;
    private Integer currentSelfLovePlayingIndex;
    private Integer currentSelfLoveContinuePlayingTime;
    private Integer currentPrayerPlayingIndex;
    private Integer currentPrayerContinuePlayingTime;
    private List<String> playingOrder;
    @Override
     public RoutineData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutineData(
          id,
          routineOwner,
          originalName,
          displayName,
          numberOfSteps,
          numberOfTimesUsed,
          fullPlayTime,
          icon,
          visibleToOthers,
          colorHEX,
          playSoundDuringStretch,
          playSoundDuringPrayer,
          playSoundDuringBreathing,
          playSoundDuringSelfLove,
          playSoundDuringBedtimeStory,
          playSoundDuringSleep,
          eachSoundPlayTime,
          prayerPlayTime,
          bedtimeStoryPlayTime,
          selfLovePlayTime,
          stretchTime,
          breathingTime,
          currentBedtimeStoryPlayingIndex,
          currentBedtimeStoryContinuePlayingTime,
          currentSelfLovePlayingIndex,
          currentSelfLoveContinuePlayingTime,
          currentPrayerPlayingIndex,
          currentPrayerContinuePlayingTime,
          playingOrder);
    }
    
    @Override
     public BuildStep routineOwner(UserData routineOwner) {
        Objects.requireNonNull(routineOwner);
        this.routineOwner = routineOwner;
        return this;
    }
    
    @Override
     public BuildStep originalName(String originalName) {
        this.originalName = originalName;
        return this;
    }
    
    @Override
     public BuildStep displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
    
    @Override
     public BuildStep numberOfSteps(Integer numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
        return this;
    }
    
    @Override
     public BuildStep numberOfTimesUsed(Integer numberOfTimesUsed) {
        this.numberOfTimesUsed = numberOfTimesUsed;
        return this;
    }
    
    @Override
     public BuildStep fullPlayTime(Integer fullPlayTime) {
        this.fullPlayTime = fullPlayTime;
        return this;
    }
    
    @Override
     public BuildStep icon(Integer icon) {
        this.icon = icon;
        return this;
    }
    
    @Override
     public BuildStep visibleToOthers(Boolean visibleToOthers) {
        this.visibleToOthers = visibleToOthers;
        return this;
    }
    
    @Override
     public BuildStep colorHex(Integer colorHex) {
        this.colorHEX = colorHex;
        return this;
    }
    
    @Override
     public BuildStep playSoundDuringStretch(Boolean playSoundDuringStretch) {
        this.playSoundDuringStretch = playSoundDuringStretch;
        return this;
    }
    
    @Override
     public BuildStep playSoundDuringPrayer(Boolean playSoundDuringPrayer) {
        this.playSoundDuringPrayer = playSoundDuringPrayer;
        return this;
    }
    
    @Override
     public BuildStep playSoundDuringBreathing(Boolean playSoundDuringBreathing) {
        this.playSoundDuringBreathing = playSoundDuringBreathing;
        return this;
    }
    
    @Override
     public BuildStep playSoundDuringSelfLove(Boolean playSoundDuringSelfLove) {
        this.playSoundDuringSelfLove = playSoundDuringSelfLove;
        return this;
    }
    
    @Override
     public BuildStep playSoundDuringBedtimeStory(Boolean playSoundDuringBedtimeStory) {
        this.playSoundDuringBedtimeStory = playSoundDuringBedtimeStory;
        return this;
    }
    
    @Override
     public BuildStep playSoundDuringSleep(Boolean playSoundDuringSleep) {
        this.playSoundDuringSleep = playSoundDuringSleep;
        return this;
    }
    
    @Override
     public BuildStep eachSoundPlayTime(Integer eachSoundPlayTime) {
        this.eachSoundPlayTime = eachSoundPlayTime;
        return this;
    }
    
    @Override
     public BuildStep prayerPlayTime(Integer prayerPlayTime) {
        this.prayerPlayTime = prayerPlayTime;
        return this;
    }
    
    @Override
     public BuildStep bedtimeStoryPlayTime(Integer bedtimeStoryPlayTime) {
        this.bedtimeStoryPlayTime = bedtimeStoryPlayTime;
        return this;
    }
    
    @Override
     public BuildStep selfLovePlayTime(Integer selfLovePlayTime) {
        this.selfLovePlayTime = selfLovePlayTime;
        return this;
    }
    
    @Override
     public BuildStep stretchTime(Integer stretchTime) {
        this.stretchTime = stretchTime;
        return this;
    }
    
    @Override
     public BuildStep breathingTime(Integer breathingTime) {
        this.breathingTime = breathingTime;
        return this;
    }
    
    @Override
     public BuildStep currentBedtimeStoryPlayingIndex(Integer currentBedtimeStoryPlayingIndex) {
        this.currentBedtimeStoryPlayingIndex = currentBedtimeStoryPlayingIndex;
        return this;
    }
    
    @Override
     public BuildStep currentBedtimeStoryContinuePlayingTime(Integer currentBedtimeStoryContinuePlayingTime) {
        this.currentBedtimeStoryContinuePlayingTime = currentBedtimeStoryContinuePlayingTime;
        return this;
    }
    
    @Override
     public BuildStep currentSelfLovePlayingIndex(Integer currentSelfLovePlayingIndex) {
        this.currentSelfLovePlayingIndex = currentSelfLovePlayingIndex;
        return this;
    }
    
    @Override
     public BuildStep currentSelfLoveContinuePlayingTime(Integer currentSelfLoveContinuePlayingTime) {
        this.currentSelfLoveContinuePlayingTime = currentSelfLoveContinuePlayingTime;
        return this;
    }
    
    @Override
     public BuildStep currentPrayerPlayingIndex(Integer currentPrayerPlayingIndex) {
        this.currentPrayerPlayingIndex = currentPrayerPlayingIndex;
        return this;
    }
    
    @Override
     public BuildStep currentPrayerContinuePlayingTime(Integer currentPrayerContinuePlayingTime) {
        this.currentPrayerContinuePlayingTime = currentPrayerContinuePlayingTime;
        return this;
    }
    
    @Override
     public BuildStep playingOrder(List<String> playingOrder) {
        this.playingOrder = playingOrder;
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
    private CopyOfBuilder(String id, UserData routineOwner, String originalName, String displayName, Integer numberOfSteps, Integer numberOfTimesUsed, Integer fullPlayTime, Integer icon, Boolean visibleToOthers, Integer colorHex, Boolean playSoundDuringStretch, Boolean playSoundDuringPrayer, Boolean playSoundDuringBreathing, Boolean playSoundDuringSelfLove, Boolean playSoundDuringBedtimeStory, Boolean playSoundDuringSleep, Integer eachSoundPlayTime, Integer prayerPlayTime, Integer bedtimeStoryPlayTime, Integer selfLovePlayTime, Integer stretchTime, Integer breathingTime, Integer currentBedtimeStoryPlayingIndex, Integer currentBedtimeStoryContinuePlayingTime, Integer currentSelfLovePlayingIndex, Integer currentSelfLoveContinuePlayingTime, Integer currentPrayerPlayingIndex, Integer currentPrayerContinuePlayingTime, List<String> playingOrder) {
      super.id(id);
      super.routineOwner(routineOwner)
        .originalName(originalName)
        .displayName(displayName)
        .numberOfSteps(numberOfSteps)
        .numberOfTimesUsed(numberOfTimesUsed)
        .fullPlayTime(fullPlayTime)
        .icon(icon)
        .visibleToOthers(visibleToOthers)
        .colorHex(colorHex)
        .playSoundDuringStretch(playSoundDuringStretch)
        .playSoundDuringPrayer(playSoundDuringPrayer)
        .playSoundDuringBreathing(playSoundDuringBreathing)
        .playSoundDuringSelfLove(playSoundDuringSelfLove)
        .playSoundDuringBedtimeStory(playSoundDuringBedtimeStory)
        .playSoundDuringSleep(playSoundDuringSleep)
        .eachSoundPlayTime(eachSoundPlayTime)
        .prayerPlayTime(prayerPlayTime)
        .bedtimeStoryPlayTime(bedtimeStoryPlayTime)
        .selfLovePlayTime(selfLovePlayTime)
        .stretchTime(stretchTime)
        .breathingTime(breathingTime)
        .currentBedtimeStoryPlayingIndex(currentBedtimeStoryPlayingIndex)
        .currentBedtimeStoryContinuePlayingTime(currentBedtimeStoryContinuePlayingTime)
        .currentSelfLovePlayingIndex(currentSelfLovePlayingIndex)
        .currentSelfLoveContinuePlayingTime(currentSelfLoveContinuePlayingTime)
        .currentPrayerPlayingIndex(currentPrayerPlayingIndex)
        .currentPrayerContinuePlayingTime(currentPrayerContinuePlayingTime)
        .playingOrder(playingOrder);
    }
    
    @Override
     public CopyOfBuilder routineOwner(UserData routineOwner) {
      return (CopyOfBuilder) super.routineOwner(routineOwner);
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
     public CopyOfBuilder numberOfSteps(Integer numberOfSteps) {
      return (CopyOfBuilder) super.numberOfSteps(numberOfSteps);
    }
    
    @Override
     public CopyOfBuilder numberOfTimesUsed(Integer numberOfTimesUsed) {
      return (CopyOfBuilder) super.numberOfTimesUsed(numberOfTimesUsed);
    }
    
    @Override
     public CopyOfBuilder fullPlayTime(Integer fullPlayTime) {
      return (CopyOfBuilder) super.fullPlayTime(fullPlayTime);
    }
    
    @Override
     public CopyOfBuilder icon(Integer icon) {
      return (CopyOfBuilder) super.icon(icon);
    }
    
    @Override
     public CopyOfBuilder visibleToOthers(Boolean visibleToOthers) {
      return (CopyOfBuilder) super.visibleToOthers(visibleToOthers);
    }
    
    @Override
     public CopyOfBuilder colorHex(Integer colorHex) {
      return (CopyOfBuilder) super.colorHex(colorHex);
    }
    
    @Override
     public CopyOfBuilder playSoundDuringStretch(Boolean playSoundDuringStretch) {
      return (CopyOfBuilder) super.playSoundDuringStretch(playSoundDuringStretch);
    }
    
    @Override
     public CopyOfBuilder playSoundDuringPrayer(Boolean playSoundDuringPrayer) {
      return (CopyOfBuilder) super.playSoundDuringPrayer(playSoundDuringPrayer);
    }
    
    @Override
     public CopyOfBuilder playSoundDuringBreathing(Boolean playSoundDuringBreathing) {
      return (CopyOfBuilder) super.playSoundDuringBreathing(playSoundDuringBreathing);
    }
    
    @Override
     public CopyOfBuilder playSoundDuringSelfLove(Boolean playSoundDuringSelfLove) {
      return (CopyOfBuilder) super.playSoundDuringSelfLove(playSoundDuringSelfLove);
    }
    
    @Override
     public CopyOfBuilder playSoundDuringBedtimeStory(Boolean playSoundDuringBedtimeStory) {
      return (CopyOfBuilder) super.playSoundDuringBedtimeStory(playSoundDuringBedtimeStory);
    }
    
    @Override
     public CopyOfBuilder playSoundDuringSleep(Boolean playSoundDuringSleep) {
      return (CopyOfBuilder) super.playSoundDuringSleep(playSoundDuringSleep);
    }
    
    @Override
     public CopyOfBuilder eachSoundPlayTime(Integer eachSoundPlayTime) {
      return (CopyOfBuilder) super.eachSoundPlayTime(eachSoundPlayTime);
    }
    
    @Override
     public CopyOfBuilder prayerPlayTime(Integer prayerPlayTime) {
      return (CopyOfBuilder) super.prayerPlayTime(prayerPlayTime);
    }
    
    @Override
     public CopyOfBuilder bedtimeStoryPlayTime(Integer bedtimeStoryPlayTime) {
      return (CopyOfBuilder) super.bedtimeStoryPlayTime(bedtimeStoryPlayTime);
    }
    
    @Override
     public CopyOfBuilder selfLovePlayTime(Integer selfLovePlayTime) {
      return (CopyOfBuilder) super.selfLovePlayTime(selfLovePlayTime);
    }
    
    @Override
     public CopyOfBuilder stretchTime(Integer stretchTime) {
      return (CopyOfBuilder) super.stretchTime(stretchTime);
    }
    
    @Override
     public CopyOfBuilder breathingTime(Integer breathingTime) {
      return (CopyOfBuilder) super.breathingTime(breathingTime);
    }
    
    @Override
     public CopyOfBuilder currentBedtimeStoryPlayingIndex(Integer currentBedtimeStoryPlayingIndex) {
      return (CopyOfBuilder) super.currentBedtimeStoryPlayingIndex(currentBedtimeStoryPlayingIndex);
    }
    
    @Override
     public CopyOfBuilder currentBedtimeStoryContinuePlayingTime(Integer currentBedtimeStoryContinuePlayingTime) {
      return (CopyOfBuilder) super.currentBedtimeStoryContinuePlayingTime(currentBedtimeStoryContinuePlayingTime);
    }
    
    @Override
     public CopyOfBuilder currentSelfLovePlayingIndex(Integer currentSelfLovePlayingIndex) {
      return (CopyOfBuilder) super.currentSelfLovePlayingIndex(currentSelfLovePlayingIndex);
    }
    
    @Override
     public CopyOfBuilder currentSelfLoveContinuePlayingTime(Integer currentSelfLoveContinuePlayingTime) {
      return (CopyOfBuilder) super.currentSelfLoveContinuePlayingTime(currentSelfLoveContinuePlayingTime);
    }
    
    @Override
     public CopyOfBuilder currentPrayerPlayingIndex(Integer currentPrayerPlayingIndex) {
      return (CopyOfBuilder) super.currentPrayerPlayingIndex(currentPrayerPlayingIndex);
    }
    
    @Override
     public CopyOfBuilder currentPrayerContinuePlayingTime(Integer currentPrayerContinuePlayingTime) {
      return (CopyOfBuilder) super.currentPrayerContinuePlayingTime(currentPrayerContinuePlayingTime);
    }
    
    @Override
     public CopyOfBuilder playingOrder(List<String> playingOrder) {
      return (CopyOfBuilder) super.playingOrder(playingOrder);
    }
  }
  
}
