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
@Index(name = "byUserData", fields = {"userDataID","display_name"})
public final class RoutineData implements Model {
  public static final QueryField ID = field("RoutineData", "id");
  public static final QueryField ROUTINE_OWNER = field("RoutineData", "userDataID");
  public static final QueryField DISPLAY_NAME = field("RoutineData", "display_name");
  public static final QueryField NUMBER_OF_STEPS = field("RoutineData", "numberOfSteps");
  public static final QueryField NUMBER_OF_TIMES_USED = field("RoutineData", "numberOfTimesUsed");
  public static final QueryField FULL_PLAY_TIME = field("RoutineData", "fullPlayTime");
  public static final QueryField ICON = field("RoutineData", "icon");
  public static final QueryField VISIBLE_TO_OTHERS = field("RoutineData", "visible_to_others");
  public static final QueryField COLOR_HEX = field("RoutineData", "colorHEX");
  public static final QueryField PLAY_SOUND_DURING_STRETCH = field("RoutineData", "playSoundDuringStretch");
  public static final QueryField PLAY_SOUND_DURING_BREATHING = field("RoutineData", "playSoundDuringBreathing");
  public static final QueryField PLAY_SOUND_DURING_SELF_LOVE = field("RoutineData", "playSoundDuringSelfLove");
  public static final QueryField PLAY_SOUND_DURING_BEDTIME_STORY = field("RoutineData", "playSoundDuringBedtimeStory");
  public static final QueryField PLAY_SOUND_DURING_SLEEP = field("RoutineData", "playSoundDuringSleep");
  public static final QueryField EACH_SOUND_PLAY_TIME = field("RoutineData", "eachSoundPlayTime");
  public static final QueryField BEDTIME_STORY_PLAY_TIME = field("RoutineData", "bedtimeStoryPlayTime");
  public static final QueryField SELF_LOVE_PLAY_TIME = field("RoutineData", "selfLovePlayTime");
  public static final QueryField STRETCH_TIME = field("RoutineData", "stretchTime");
  public static final QueryField BREATHING_TIME = field("RoutineData", "breathingTime");
  public static final QueryField CURRENT_BEDTIME_STORY_PLAYING_INDEX = field("RoutineData", "currentBedtimeStoryPlayingIndex");
  public static final QueryField CURRENT_BEDTIME_STORY_CONTINUE_PLAYING_TIME = field("RoutineData", "currentBedtimeStoryContinuePlayingTime");
  public static final QueryField CURRENT_SELF_LOVE_PLAYING_INDEX = field("RoutineData", "currentSelfLovePlayingIndex");
  public static final QueryField CURRENT_SELF_LOVE_CONTINUE_PLAYING_TIME = field("RoutineData", "currentSelfLoveContinuePlayingTime");
  public static final QueryField PLAYING_ORDER = field("RoutineData", "playingOrder");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData routineOwner;
  private final @ModelField(targetType="String", isRequired = true) String display_name;
  private final @ModelField(targetType="Int", isRequired = true) Integer numberOfSteps;
  private final @ModelField(targetType="Int", isRequired = true) Integer numberOfTimesUsed;
  private final @ModelField(targetType="Int", isRequired = true) Integer fullPlayTime;
  private final @ModelField(targetType="Int", isRequired = true) Integer icon;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean visible_to_others;
  private final @ModelField(targetType="Int", isRequired = true) Integer colorHEX;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean playSoundDuringStretch;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean playSoundDuringBreathing;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean playSoundDuringSelfLove;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean playSoundDuringBedtimeStory;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean playSoundDuringSleep;
  private final @ModelField(targetType="Int", isRequired = true) Integer eachSoundPlayTime;
  private final @ModelField(targetType="Int", isRequired = true) Integer bedtimeStoryPlayTime;
  private final @ModelField(targetType="Int", isRequired = true) Integer selfLovePlayTime;
  private final @ModelField(targetType="Int", isRequired = true) Integer stretchTime;
  private final @ModelField(targetType="Int", isRequired = true) Integer breathingTime;
  private final @ModelField(targetType="Int", isRequired = true) Integer currentBedtimeStoryPlayingIndex;
  private final @ModelField(targetType="Int", isRequired = true) Integer currentBedtimeStoryContinuePlayingTime;
  private final @ModelField(targetType="Int", isRequired = true) Integer currentSelfLovePlayingIndex;
  private final @ModelField(targetType="Int", isRequired = true) Integer currentSelfLoveContinuePlayingTime;
  private final @ModelField(targetType="RoutineSounds") @HasMany(associatedWith = "routineData", type = RoutineSounds.class) List<RoutineSounds> sounds = null;
  private final @ModelField(targetType="RoutineStretches") @HasMany(associatedWith = "routineData", type = RoutineStretches.class) List<RoutineStretches> stretches = null;
  private final @ModelField(targetType="RoutineBreathings") @HasMany(associatedWith = "routineData", type = RoutineBreathings.class) List<RoutineBreathings> breathing = null;
  private final @ModelField(targetType="RoutineBedtimeStories") @HasMany(associatedWith = "routineData", type = RoutineBedtimeStories.class) List<RoutineBedtimeStories> bedtimeStories = null;
  private final @ModelField(targetType="RoutineSelfLoves") @HasMany(associatedWith = "routineData", type = RoutineSelfLoves.class) List<RoutineSelfLoves> selfLoves = null;
  private final @ModelField(targetType="String", isRequired = true) List<String> playingOrder;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getRoutineOwner() {
      return routineOwner;
  }
  
  public String getDisplayName() {
      return display_name;
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
      return visible_to_others;
  }
  
  public Integer getColorHex() {
      return colorHEX;
  }
  
  public Boolean getPlaySoundDuringStretch() {
      return playSoundDuringStretch;
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
  
  public List<RoutineSounds> getSounds() {
      return sounds;
  }
  
  public List<RoutineStretches> getStretches() {
      return stretches;
  }
  
  public List<RoutineBreathings> getBreathing() {
      return breathing;
  }
  
  public List<RoutineBedtimeStories> getBedtimeStories() {
      return bedtimeStories;
  }
  
  public List<RoutineSelfLoves> getSelfLoves() {
      return selfLoves;
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
  
  private RoutineData(String id, UserData routineOwner, String display_name, Integer numberOfSteps, Integer numberOfTimesUsed, Integer fullPlayTime, Integer icon, Boolean visible_to_others, Integer colorHEX, Boolean playSoundDuringStretch, Boolean playSoundDuringBreathing, Boolean playSoundDuringSelfLove, Boolean playSoundDuringBedtimeStory, Boolean playSoundDuringSleep, Integer eachSoundPlayTime, Integer bedtimeStoryPlayTime, Integer selfLovePlayTime, Integer stretchTime, Integer breathingTime, Integer currentBedtimeStoryPlayingIndex, Integer currentBedtimeStoryContinuePlayingTime, Integer currentSelfLovePlayingIndex, Integer currentSelfLoveContinuePlayingTime, List<String> playingOrder) {
    this.id = id;
    this.routineOwner = routineOwner;
    this.display_name = display_name;
    this.numberOfSteps = numberOfSteps;
    this.numberOfTimesUsed = numberOfTimesUsed;
    this.fullPlayTime = fullPlayTime;
    this.icon = icon;
    this.visible_to_others = visible_to_others;
    this.colorHEX = colorHEX;
    this.playSoundDuringStretch = playSoundDuringStretch;
    this.playSoundDuringBreathing = playSoundDuringBreathing;
    this.playSoundDuringSelfLove = playSoundDuringSelfLove;
    this.playSoundDuringBedtimeStory = playSoundDuringBedtimeStory;
    this.playSoundDuringSleep = playSoundDuringSleep;
    this.eachSoundPlayTime = eachSoundPlayTime;
    this.bedtimeStoryPlayTime = bedtimeStoryPlayTime;
    this.selfLovePlayTime = selfLovePlayTime;
    this.stretchTime = stretchTime;
    this.breathingTime = breathingTime;
    this.currentBedtimeStoryPlayingIndex = currentBedtimeStoryPlayingIndex;
    this.currentBedtimeStoryContinuePlayingTime = currentBedtimeStoryContinuePlayingTime;
    this.currentSelfLovePlayingIndex = currentSelfLovePlayingIndex;
    this.currentSelfLoveContinuePlayingTime = currentSelfLoveContinuePlayingTime;
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
              ObjectsCompat.equals(getDisplayName(), routineData.getDisplayName()) &&
              ObjectsCompat.equals(getNumberOfSteps(), routineData.getNumberOfSteps()) &&
              ObjectsCompat.equals(getNumberOfTimesUsed(), routineData.getNumberOfTimesUsed()) &&
              ObjectsCompat.equals(getFullPlayTime(), routineData.getFullPlayTime()) &&
              ObjectsCompat.equals(getIcon(), routineData.getIcon()) &&
              ObjectsCompat.equals(getVisibleToOthers(), routineData.getVisibleToOthers()) &&
              ObjectsCompat.equals(getColorHex(), routineData.getColorHex()) &&
              ObjectsCompat.equals(getPlaySoundDuringStretch(), routineData.getPlaySoundDuringStretch()) &&
              ObjectsCompat.equals(getPlaySoundDuringBreathing(), routineData.getPlaySoundDuringBreathing()) &&
              ObjectsCompat.equals(getPlaySoundDuringSelfLove(), routineData.getPlaySoundDuringSelfLove()) &&
              ObjectsCompat.equals(getPlaySoundDuringBedtimeStory(), routineData.getPlaySoundDuringBedtimeStory()) &&
              ObjectsCompat.equals(getPlaySoundDuringSleep(), routineData.getPlaySoundDuringSleep()) &&
              ObjectsCompat.equals(getEachSoundPlayTime(), routineData.getEachSoundPlayTime()) &&
              ObjectsCompat.equals(getBedtimeStoryPlayTime(), routineData.getBedtimeStoryPlayTime()) &&
              ObjectsCompat.equals(getSelfLovePlayTime(), routineData.getSelfLovePlayTime()) &&
              ObjectsCompat.equals(getStretchTime(), routineData.getStretchTime()) &&
              ObjectsCompat.equals(getBreathingTime(), routineData.getBreathingTime()) &&
              ObjectsCompat.equals(getCurrentBedtimeStoryPlayingIndex(), routineData.getCurrentBedtimeStoryPlayingIndex()) &&
              ObjectsCompat.equals(getCurrentBedtimeStoryContinuePlayingTime(), routineData.getCurrentBedtimeStoryContinuePlayingTime()) &&
              ObjectsCompat.equals(getCurrentSelfLovePlayingIndex(), routineData.getCurrentSelfLovePlayingIndex()) &&
              ObjectsCompat.equals(getCurrentSelfLoveContinuePlayingTime(), routineData.getCurrentSelfLoveContinuePlayingTime()) &&
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
      .append(getDisplayName())
      .append(getNumberOfSteps())
      .append(getNumberOfTimesUsed())
      .append(getFullPlayTime())
      .append(getIcon())
      .append(getVisibleToOthers())
      .append(getColorHex())
      .append(getPlaySoundDuringStretch())
      .append(getPlaySoundDuringBreathing())
      .append(getPlaySoundDuringSelfLove())
      .append(getPlaySoundDuringBedtimeStory())
      .append(getPlaySoundDuringSleep())
      .append(getEachSoundPlayTime())
      .append(getBedtimeStoryPlayTime())
      .append(getSelfLovePlayTime())
      .append(getStretchTime())
      .append(getBreathingTime())
      .append(getCurrentBedtimeStoryPlayingIndex())
      .append(getCurrentBedtimeStoryContinuePlayingTime())
      .append(getCurrentSelfLovePlayingIndex())
      .append(getCurrentSelfLoveContinuePlayingTime())
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
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("numberOfSteps=" + String.valueOf(getNumberOfSteps()) + ", ")
      .append("numberOfTimesUsed=" + String.valueOf(getNumberOfTimesUsed()) + ", ")
      .append("fullPlayTime=" + String.valueOf(getFullPlayTime()) + ", ")
      .append("icon=" + String.valueOf(getIcon()) + ", ")
      .append("visible_to_others=" + String.valueOf(getVisibleToOthers()) + ", ")
      .append("colorHEX=" + String.valueOf(getColorHex()) + ", ")
      .append("playSoundDuringStretch=" + String.valueOf(getPlaySoundDuringStretch()) + ", ")
      .append("playSoundDuringBreathing=" + String.valueOf(getPlaySoundDuringBreathing()) + ", ")
      .append("playSoundDuringSelfLove=" + String.valueOf(getPlaySoundDuringSelfLove()) + ", ")
      .append("playSoundDuringBedtimeStory=" + String.valueOf(getPlaySoundDuringBedtimeStory()) + ", ")
      .append("playSoundDuringSleep=" + String.valueOf(getPlaySoundDuringSleep()) + ", ")
      .append("eachSoundPlayTime=" + String.valueOf(getEachSoundPlayTime()) + ", ")
      .append("bedtimeStoryPlayTime=" + String.valueOf(getBedtimeStoryPlayTime()) + ", ")
      .append("selfLovePlayTime=" + String.valueOf(getSelfLovePlayTime()) + ", ")
      .append("stretchTime=" + String.valueOf(getStretchTime()) + ", ")
      .append("breathingTime=" + String.valueOf(getBreathingTime()) + ", ")
      .append("currentBedtimeStoryPlayingIndex=" + String.valueOf(getCurrentBedtimeStoryPlayingIndex()) + ", ")
      .append("currentBedtimeStoryContinuePlayingTime=" + String.valueOf(getCurrentBedtimeStoryContinuePlayingTime()) + ", ")
      .append("currentSelfLovePlayingIndex=" + String.valueOf(getCurrentSelfLovePlayingIndex()) + ", ")
      .append("currentSelfLoveContinuePlayingTime=" + String.valueOf(getCurrentSelfLoveContinuePlayingTime()) + ", ")
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
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      routineOwner,
      display_name,
      numberOfSteps,
      numberOfTimesUsed,
      fullPlayTime,
      icon,
      visible_to_others,
      colorHEX,
      playSoundDuringStretch,
      playSoundDuringBreathing,
      playSoundDuringSelfLove,
      playSoundDuringBedtimeStory,
      playSoundDuringSleep,
      eachSoundPlayTime,
      bedtimeStoryPlayTime,
      selfLovePlayTime,
      stretchTime,
      breathingTime,
      currentBedtimeStoryPlayingIndex,
      currentBedtimeStoryContinuePlayingTime,
      currentSelfLovePlayingIndex,
      currentSelfLoveContinuePlayingTime,
      playingOrder);
  }
  public interface RoutineOwnerStep {
    DisplayNameStep routineOwner(UserData routineOwner);
  }
  

  public interface DisplayNameStep {
    NumberOfStepsStep displayName(String displayName);
  }
  

  public interface NumberOfStepsStep {
    NumberOfTimesUsedStep numberOfSteps(Integer numberOfSteps);
  }
  

  public interface NumberOfTimesUsedStep {
    FullPlayTimeStep numberOfTimesUsed(Integer numberOfTimesUsed);
  }
  

  public interface FullPlayTimeStep {
    IconStep fullPlayTime(Integer fullPlayTime);
  }
  

  public interface IconStep {
    VisibleToOthersStep icon(Integer icon);
  }
  

  public interface VisibleToOthersStep {
    ColorHexStep visibleToOthers(Boolean visibleToOthers);
  }
  

  public interface ColorHexStep {
    PlaySoundDuringStretchStep colorHex(Integer colorHex);
  }
  

  public interface PlaySoundDuringStretchStep {
    PlaySoundDuringBreathingStep playSoundDuringStretch(Boolean playSoundDuringStretch);
  }
  

  public interface PlaySoundDuringBreathingStep {
    PlaySoundDuringSelfLoveStep playSoundDuringBreathing(Boolean playSoundDuringBreathing);
  }
  

  public interface PlaySoundDuringSelfLoveStep {
    PlaySoundDuringBedtimeStoryStep playSoundDuringSelfLove(Boolean playSoundDuringSelfLove);
  }
  

  public interface PlaySoundDuringBedtimeStoryStep {
    PlaySoundDuringSleepStep playSoundDuringBedtimeStory(Boolean playSoundDuringBedtimeStory);
  }
  

  public interface PlaySoundDuringSleepStep {
    EachSoundPlayTimeStep playSoundDuringSleep(Boolean playSoundDuringSleep);
  }
  

  public interface EachSoundPlayTimeStep {
    BedtimeStoryPlayTimeStep eachSoundPlayTime(Integer eachSoundPlayTime);
  }
  

  public interface BedtimeStoryPlayTimeStep {
    SelfLovePlayTimeStep bedtimeStoryPlayTime(Integer bedtimeStoryPlayTime);
  }
  

  public interface SelfLovePlayTimeStep {
    StretchTimeStep selfLovePlayTime(Integer selfLovePlayTime);
  }
  

  public interface StretchTimeStep {
    BreathingTimeStep stretchTime(Integer stretchTime);
  }
  

  public interface BreathingTimeStep {
    CurrentBedtimeStoryPlayingIndexStep breathingTime(Integer breathingTime);
  }
  

  public interface CurrentBedtimeStoryPlayingIndexStep {
    CurrentBedtimeStoryContinuePlayingTimeStep currentBedtimeStoryPlayingIndex(Integer currentBedtimeStoryPlayingIndex);
  }
  

  public interface CurrentBedtimeStoryContinuePlayingTimeStep {
    CurrentSelfLovePlayingIndexStep currentBedtimeStoryContinuePlayingTime(Integer currentBedtimeStoryContinuePlayingTime);
  }
  

  public interface CurrentSelfLovePlayingIndexStep {
    CurrentSelfLoveContinuePlayingTimeStep currentSelfLovePlayingIndex(Integer currentSelfLovePlayingIndex);
  }
  

  public interface CurrentSelfLoveContinuePlayingTimeStep {
    PlayingOrderStep currentSelfLoveContinuePlayingTime(Integer currentSelfLoveContinuePlayingTime);
  }
  

  public interface PlayingOrderStep {
    BuildStep playingOrder(List<String> playingOrder);
  }
  

  public interface BuildStep {
    RoutineData build();
    BuildStep id(String id);
  }
  

  public static class Builder implements RoutineOwnerStep, DisplayNameStep, NumberOfStepsStep, NumberOfTimesUsedStep, FullPlayTimeStep, IconStep, VisibleToOthersStep, ColorHexStep, PlaySoundDuringStretchStep, PlaySoundDuringBreathingStep, PlaySoundDuringSelfLoveStep, PlaySoundDuringBedtimeStoryStep, PlaySoundDuringSleepStep, EachSoundPlayTimeStep, BedtimeStoryPlayTimeStep, SelfLovePlayTimeStep, StretchTimeStep, BreathingTimeStep, CurrentBedtimeStoryPlayingIndexStep, CurrentBedtimeStoryContinuePlayingTimeStep, CurrentSelfLovePlayingIndexStep, CurrentSelfLoveContinuePlayingTimeStep, PlayingOrderStep, BuildStep {
    private String id;
    private UserData routineOwner;
    private String display_name;
    private Integer numberOfSteps;
    private Integer numberOfTimesUsed;
    private Integer fullPlayTime;
    private Integer icon;
    private Boolean visible_to_others;
    private Integer colorHEX;
    private Boolean playSoundDuringStretch;
    private Boolean playSoundDuringBreathing;
    private Boolean playSoundDuringSelfLove;
    private Boolean playSoundDuringBedtimeStory;
    private Boolean playSoundDuringSleep;
    private Integer eachSoundPlayTime;
    private Integer bedtimeStoryPlayTime;
    private Integer selfLovePlayTime;
    private Integer stretchTime;
    private Integer breathingTime;
    private Integer currentBedtimeStoryPlayingIndex;
    private Integer currentBedtimeStoryContinuePlayingTime;
    private Integer currentSelfLovePlayingIndex;
    private Integer currentSelfLoveContinuePlayingTime;
    private List<String> playingOrder;
    @Override
     public RoutineData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new RoutineData(
          id,
          routineOwner,
          display_name,
          numberOfSteps,
          numberOfTimesUsed,
          fullPlayTime,
          icon,
          visible_to_others,
          colorHEX,
          playSoundDuringStretch,
          playSoundDuringBreathing,
          playSoundDuringSelfLove,
          playSoundDuringBedtimeStory,
          playSoundDuringSleep,
          eachSoundPlayTime,
          bedtimeStoryPlayTime,
          selfLovePlayTime,
          stretchTime,
          breathingTime,
          currentBedtimeStoryPlayingIndex,
          currentBedtimeStoryContinuePlayingTime,
          currentSelfLovePlayingIndex,
          currentSelfLoveContinuePlayingTime,
          playingOrder);
    }
    
    @Override
     public DisplayNameStep routineOwner(UserData routineOwner) {
        Objects.requireNonNull(routineOwner);
        this.routineOwner = routineOwner;
        return this;
    }
    
    @Override
     public NumberOfStepsStep displayName(String displayName) {
        Objects.requireNonNull(displayName);
        this.display_name = displayName;
        return this;
    }
    
    @Override
     public NumberOfTimesUsedStep numberOfSteps(Integer numberOfSteps) {
        Objects.requireNonNull(numberOfSteps);
        this.numberOfSteps = numberOfSteps;
        return this;
    }
    
    @Override
     public FullPlayTimeStep numberOfTimesUsed(Integer numberOfTimesUsed) {
        Objects.requireNonNull(numberOfTimesUsed);
        this.numberOfTimesUsed = numberOfTimesUsed;
        return this;
    }
    
    @Override
     public IconStep fullPlayTime(Integer fullPlayTime) {
        Objects.requireNonNull(fullPlayTime);
        this.fullPlayTime = fullPlayTime;
        return this;
    }
    
    @Override
     public VisibleToOthersStep icon(Integer icon) {
        Objects.requireNonNull(icon);
        this.icon = icon;
        return this;
    }
    
    @Override
     public ColorHexStep visibleToOthers(Boolean visibleToOthers) {
        Objects.requireNonNull(visibleToOthers);
        this.visible_to_others = visibleToOthers;
        return this;
    }
    
    @Override
     public PlaySoundDuringStretchStep colorHex(Integer colorHex) {
        Objects.requireNonNull(colorHex);
        this.colorHEX = colorHex;
        return this;
    }
    
    @Override
     public PlaySoundDuringBreathingStep playSoundDuringStretch(Boolean playSoundDuringStretch) {
        Objects.requireNonNull(playSoundDuringStretch);
        this.playSoundDuringStretch = playSoundDuringStretch;
        return this;
    }
    
    @Override
     public PlaySoundDuringSelfLoveStep playSoundDuringBreathing(Boolean playSoundDuringBreathing) {
        Objects.requireNonNull(playSoundDuringBreathing);
        this.playSoundDuringBreathing = playSoundDuringBreathing;
        return this;
    }
    
    @Override
     public PlaySoundDuringBedtimeStoryStep playSoundDuringSelfLove(Boolean playSoundDuringSelfLove) {
        Objects.requireNonNull(playSoundDuringSelfLove);
        this.playSoundDuringSelfLove = playSoundDuringSelfLove;
        return this;
    }
    
    @Override
     public PlaySoundDuringSleepStep playSoundDuringBedtimeStory(Boolean playSoundDuringBedtimeStory) {
        Objects.requireNonNull(playSoundDuringBedtimeStory);
        this.playSoundDuringBedtimeStory = playSoundDuringBedtimeStory;
        return this;
    }
    
    @Override
     public EachSoundPlayTimeStep playSoundDuringSleep(Boolean playSoundDuringSleep) {
        Objects.requireNonNull(playSoundDuringSleep);
        this.playSoundDuringSleep = playSoundDuringSleep;
        return this;
    }
    
    @Override
     public BedtimeStoryPlayTimeStep eachSoundPlayTime(Integer eachSoundPlayTime) {
        Objects.requireNonNull(eachSoundPlayTime);
        this.eachSoundPlayTime = eachSoundPlayTime;
        return this;
    }
    
    @Override
     public SelfLovePlayTimeStep bedtimeStoryPlayTime(Integer bedtimeStoryPlayTime) {
        Objects.requireNonNull(bedtimeStoryPlayTime);
        this.bedtimeStoryPlayTime = bedtimeStoryPlayTime;
        return this;
    }
    
    @Override
     public StretchTimeStep selfLovePlayTime(Integer selfLovePlayTime) {
        Objects.requireNonNull(selfLovePlayTime);
        this.selfLovePlayTime = selfLovePlayTime;
        return this;
    }
    
    @Override
     public BreathingTimeStep stretchTime(Integer stretchTime) {
        Objects.requireNonNull(stretchTime);
        this.stretchTime = stretchTime;
        return this;
    }
    
    @Override
     public CurrentBedtimeStoryPlayingIndexStep breathingTime(Integer breathingTime) {
        Objects.requireNonNull(breathingTime);
        this.breathingTime = breathingTime;
        return this;
    }
    
    @Override
     public CurrentBedtimeStoryContinuePlayingTimeStep currentBedtimeStoryPlayingIndex(Integer currentBedtimeStoryPlayingIndex) {
        Objects.requireNonNull(currentBedtimeStoryPlayingIndex);
        this.currentBedtimeStoryPlayingIndex = currentBedtimeStoryPlayingIndex;
        return this;
    }
    
    @Override
     public CurrentSelfLovePlayingIndexStep currentBedtimeStoryContinuePlayingTime(Integer currentBedtimeStoryContinuePlayingTime) {
        Objects.requireNonNull(currentBedtimeStoryContinuePlayingTime);
        this.currentBedtimeStoryContinuePlayingTime = currentBedtimeStoryContinuePlayingTime;
        return this;
    }
    
    @Override
     public CurrentSelfLoveContinuePlayingTimeStep currentSelfLovePlayingIndex(Integer currentSelfLovePlayingIndex) {
        Objects.requireNonNull(currentSelfLovePlayingIndex);
        this.currentSelfLovePlayingIndex = currentSelfLovePlayingIndex;
        return this;
    }
    
    @Override
     public PlayingOrderStep currentSelfLoveContinuePlayingTime(Integer currentSelfLoveContinuePlayingTime) {
        Objects.requireNonNull(currentSelfLoveContinuePlayingTime);
        this.currentSelfLoveContinuePlayingTime = currentSelfLoveContinuePlayingTime;
        return this;
    }
    
    @Override
     public BuildStep playingOrder(List<String> playingOrder) {
        Objects.requireNonNull(playingOrder);
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
    private CopyOfBuilder(String id, UserData routineOwner, String displayName, Integer numberOfSteps, Integer numberOfTimesUsed, Integer fullPlayTime, Integer icon, Boolean visibleToOthers, Integer colorHex, Boolean playSoundDuringStretch, Boolean playSoundDuringBreathing, Boolean playSoundDuringSelfLove, Boolean playSoundDuringBedtimeStory, Boolean playSoundDuringSleep, Integer eachSoundPlayTime, Integer bedtimeStoryPlayTime, Integer selfLovePlayTime, Integer stretchTime, Integer breathingTime, Integer currentBedtimeStoryPlayingIndex, Integer currentBedtimeStoryContinuePlayingTime, Integer currentSelfLovePlayingIndex, Integer currentSelfLoveContinuePlayingTime, List<String> playingOrder) {
      super.id(id);
      super.routineOwner(routineOwner)
        .displayName(displayName)
        .numberOfSteps(numberOfSteps)
        .numberOfTimesUsed(numberOfTimesUsed)
        .fullPlayTime(fullPlayTime)
        .icon(icon)
        .visibleToOthers(visibleToOthers)
        .colorHex(colorHex)
        .playSoundDuringStretch(playSoundDuringStretch)
        .playSoundDuringBreathing(playSoundDuringBreathing)
        .playSoundDuringSelfLove(playSoundDuringSelfLove)
        .playSoundDuringBedtimeStory(playSoundDuringBedtimeStory)
        .playSoundDuringSleep(playSoundDuringSleep)
        .eachSoundPlayTime(eachSoundPlayTime)
        .bedtimeStoryPlayTime(bedtimeStoryPlayTime)
        .selfLovePlayTime(selfLovePlayTime)
        .stretchTime(stretchTime)
        .breathingTime(breathingTime)
        .currentBedtimeStoryPlayingIndex(currentBedtimeStoryPlayingIndex)
        .currentBedtimeStoryContinuePlayingTime(currentBedtimeStoryContinuePlayingTime)
        .currentSelfLovePlayingIndex(currentSelfLovePlayingIndex)
        .currentSelfLoveContinuePlayingTime(currentSelfLoveContinuePlayingTime)
        .playingOrder(playingOrder);
    }
    
    @Override
     public CopyOfBuilder routineOwner(UserData routineOwner) {
      return (CopyOfBuilder) super.routineOwner(routineOwner);
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
     public CopyOfBuilder playingOrder(List<String> playingOrder) {
      return (CopyOfBuilder) super.playingOrder(playingOrder);
    }
  }
  
}
