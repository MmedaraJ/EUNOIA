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

/** This is an auto generated class representing the UserRoutineRelationship type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRoutineRelationships", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "UserRoutineRelationshipsOwnedByUser", fields = {"userRoutineRelationshipUserDataID","id"})
@Index(name = "UserRoutineRelationshipsOwnedByRoutine", fields = {"userRoutineRelationshipRoutineDataID","id"})
public final class UserRoutineRelationship implements Model {
  public static final QueryField ID = field("UserRoutineRelationship", "id");
  public static final QueryField USER_ROUTINE_RELATIONSHIP_OWNER = field("UserRoutineRelationship", "userRoutineRelationshipUserDataID");
  public static final QueryField USER_ROUTINE_RELATIONSHIP_ROUTINE = field("UserRoutineRelationship", "userRoutineRelationshipRoutineDataID");
  public static final QueryField NUMBER_OF_TIMES_PLAYED = field("UserRoutineRelationship", "numberOfTimesPlayed");
  public static final QueryField TOTAL_PLAY_TIME = field("UserRoutineRelationship", "totalPlayTime");
  public static final QueryField CURRENTLY_LISTENING = field("UserRoutineRelationship", "currentlyListening");
  public static final QueryField PLAY_SOUND_DURING_STRETCH = field("UserRoutineRelationship", "playSoundDuringStretch");
  public static final QueryField PLAY_SOUND_DURING_PRAYER = field("UserRoutineRelationship", "playSoundDuringPrayer");
  public static final QueryField PLAY_SOUND_DURING_BREATHING = field("UserRoutineRelationship", "playSoundDuringBreathing");
  public static final QueryField PLAY_SOUND_DURING_SELF_LOVE = field("UserRoutineRelationship", "playSoundDuringSelfLove");
  public static final QueryField PLAY_SOUND_DURING_BEDTIME_STORY = field("UserRoutineRelationship", "playSoundDuringBedtimeStory");
  public static final QueryField PLAY_SOUND_DURING_SLEEP = field("UserRoutineRelationship", "playSoundDuringSleep");
  public static final QueryField EACH_SOUND_PLAY_TIME = field("UserRoutineRelationship", "eachSoundPlayTime");
  public static final QueryField PRAYER_PLAY_TIME = field("UserRoutineRelationship", "prayerPlayTime");
  public static final QueryField BEDTIME_STORY_PLAY_TIME = field("UserRoutineRelationship", "bedtimeStoryPlayTime");
  public static final QueryField SELF_LOVE_PLAY_TIME = field("UserRoutineRelationship", "selfLovePlayTime");
  public static final QueryField STRETCH_TIME = field("UserRoutineRelationship", "stretchTime");
  public static final QueryField BREATHING_TIME = field("UserRoutineRelationship", "breathingTime");
  public static final QueryField CURRENT_BEDTIME_STORY_PLAYING_INDEX = field("UserRoutineRelationship", "currentBedtimeStoryPlayingIndex");
  public static final QueryField CURRENT_BEDTIME_STORY_CONTINUE_PLAYING_TIME = field("UserRoutineRelationship", "currentBedtimeStoryContinuePlayingTime");
  public static final QueryField CURRENT_SELF_LOVE_PLAYING_INDEX = field("UserRoutineRelationship", "currentSelfLovePlayingIndex");
  public static final QueryField CURRENT_SELF_LOVE_CONTINUE_PLAYING_TIME = field("UserRoutineRelationship", "currentSelfLoveContinuePlayingTime");
  public static final QueryField CURRENT_PRAYER_PLAYING_INDEX = field("UserRoutineRelationship", "currentPrayerPlayingIndex");
  public static final QueryField CURRENT_PRAYER_CONTINUE_PLAYING_TIME = field("UserRoutineRelationship", "currentPrayerContinuePlayingTime");
  public static final QueryField USAGE_TIMESTAMPS = field("UserRoutineRelationship", "usageTimestamps");
  public static final QueryField USAGE_PLAY_TIMES = field("UserRoutineRelationship", "usagePlayTimes");
  public static final QueryField PLAYING_ORDER = field("UserRoutineRelationship", "playingOrder");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipUserDataID", type = UserData.class) UserData userRoutineRelationshipOwner;
  private final @ModelField(targetType="RoutineData", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipRoutineDataID", type = RoutineData.class) RoutineData userRoutineRelationshipRoutine;
  private final @ModelField(targetType="Int") Integer numberOfTimesPlayed;
  private final @ModelField(targetType="Int") Integer totalPlayTime;
  private final @ModelField(targetType="Boolean") Boolean currentlyListening;
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
  private final @ModelField(targetType="AWSDateTime") List<Temporal.DateTime> usageTimestamps;
  private final @ModelField(targetType="Int") List<Integer> usagePlayTimes;
  private final @ModelField(targetType="String") List<String> playingOrder;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserRoutineRelationshipOwner() {
      return userRoutineRelationshipOwner;
  }
  
  public RoutineData getUserRoutineRelationshipRoutine() {
      return userRoutineRelationshipRoutine;
  }
  
  public Integer getNumberOfTimesPlayed() {
      return numberOfTimesPlayed;
  }
  
  public Integer getTotalPlayTime() {
      return totalPlayTime;
  }
  
  public Boolean getCurrentlyListening() {
      return currentlyListening;
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
  
  public List<Temporal.DateTime> getUsageTimestamps() {
      return usageTimestamps;
  }
  
  public List<Integer> getUsagePlayTimes() {
      return usagePlayTimes;
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
  
  private UserRoutineRelationship(String id, UserData userRoutineRelationshipOwner, RoutineData userRoutineRelationshipRoutine, Integer numberOfTimesPlayed, Integer totalPlayTime, Boolean currentlyListening, Boolean playSoundDuringStretch, Boolean playSoundDuringPrayer, Boolean playSoundDuringBreathing, Boolean playSoundDuringSelfLove, Boolean playSoundDuringBedtimeStory, Boolean playSoundDuringSleep, Integer eachSoundPlayTime, Integer prayerPlayTime, Integer bedtimeStoryPlayTime, Integer selfLovePlayTime, Integer stretchTime, Integer breathingTime, Integer currentBedtimeStoryPlayingIndex, Integer currentBedtimeStoryContinuePlayingTime, Integer currentSelfLovePlayingIndex, Integer currentSelfLoveContinuePlayingTime, Integer currentPrayerPlayingIndex, Integer currentPrayerContinuePlayingTime, List<Temporal.DateTime> usageTimestamps, List<Integer> usagePlayTimes, List<String> playingOrder) {
    this.id = id;
    this.userRoutineRelationshipOwner = userRoutineRelationshipOwner;
    this.userRoutineRelationshipRoutine = userRoutineRelationshipRoutine;
    this.numberOfTimesPlayed = numberOfTimesPlayed;
    this.totalPlayTime = totalPlayTime;
    this.currentlyListening = currentlyListening;
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
    this.usageTimestamps = usageTimestamps;
    this.usagePlayTimes = usagePlayTimes;
    this.playingOrder = playingOrder;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserRoutineRelationship userRoutineRelationship = (UserRoutineRelationship) obj;
      return ObjectsCompat.equals(getId(), userRoutineRelationship.getId()) &&
              ObjectsCompat.equals(getUserRoutineRelationshipOwner(), userRoutineRelationship.getUserRoutineRelationshipOwner()) &&
              ObjectsCompat.equals(getUserRoutineRelationshipRoutine(), userRoutineRelationship.getUserRoutineRelationshipRoutine()) &&
              ObjectsCompat.equals(getNumberOfTimesPlayed(), userRoutineRelationship.getNumberOfTimesPlayed()) &&
              ObjectsCompat.equals(getTotalPlayTime(), userRoutineRelationship.getTotalPlayTime()) &&
              ObjectsCompat.equals(getCurrentlyListening(), userRoutineRelationship.getCurrentlyListening()) &&
              ObjectsCompat.equals(getPlaySoundDuringStretch(), userRoutineRelationship.getPlaySoundDuringStretch()) &&
              ObjectsCompat.equals(getPlaySoundDuringPrayer(), userRoutineRelationship.getPlaySoundDuringPrayer()) &&
              ObjectsCompat.equals(getPlaySoundDuringBreathing(), userRoutineRelationship.getPlaySoundDuringBreathing()) &&
              ObjectsCompat.equals(getPlaySoundDuringSelfLove(), userRoutineRelationship.getPlaySoundDuringSelfLove()) &&
              ObjectsCompat.equals(getPlaySoundDuringBedtimeStory(), userRoutineRelationship.getPlaySoundDuringBedtimeStory()) &&
              ObjectsCompat.equals(getPlaySoundDuringSleep(), userRoutineRelationship.getPlaySoundDuringSleep()) &&
              ObjectsCompat.equals(getEachSoundPlayTime(), userRoutineRelationship.getEachSoundPlayTime()) &&
              ObjectsCompat.equals(getPrayerPlayTime(), userRoutineRelationship.getPrayerPlayTime()) &&
              ObjectsCompat.equals(getBedtimeStoryPlayTime(), userRoutineRelationship.getBedtimeStoryPlayTime()) &&
              ObjectsCompat.equals(getSelfLovePlayTime(), userRoutineRelationship.getSelfLovePlayTime()) &&
              ObjectsCompat.equals(getStretchTime(), userRoutineRelationship.getStretchTime()) &&
              ObjectsCompat.equals(getBreathingTime(), userRoutineRelationship.getBreathingTime()) &&
              ObjectsCompat.equals(getCurrentBedtimeStoryPlayingIndex(), userRoutineRelationship.getCurrentBedtimeStoryPlayingIndex()) &&
              ObjectsCompat.equals(getCurrentBedtimeStoryContinuePlayingTime(), userRoutineRelationship.getCurrentBedtimeStoryContinuePlayingTime()) &&
              ObjectsCompat.equals(getCurrentSelfLovePlayingIndex(), userRoutineRelationship.getCurrentSelfLovePlayingIndex()) &&
              ObjectsCompat.equals(getCurrentSelfLoveContinuePlayingTime(), userRoutineRelationship.getCurrentSelfLoveContinuePlayingTime()) &&
              ObjectsCompat.equals(getCurrentPrayerPlayingIndex(), userRoutineRelationship.getCurrentPrayerPlayingIndex()) &&
              ObjectsCompat.equals(getCurrentPrayerContinuePlayingTime(), userRoutineRelationship.getCurrentPrayerContinuePlayingTime()) &&
              ObjectsCompat.equals(getUsageTimestamps(), userRoutineRelationship.getUsageTimestamps()) &&
              ObjectsCompat.equals(getUsagePlayTimes(), userRoutineRelationship.getUsagePlayTimes()) &&
              ObjectsCompat.equals(getPlayingOrder(), userRoutineRelationship.getPlayingOrder()) &&
              ObjectsCompat.equals(getCreatedAt(), userRoutineRelationship.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRoutineRelationship.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserRoutineRelationshipOwner())
      .append(getUserRoutineRelationshipRoutine())
      .append(getNumberOfTimesPlayed())
      .append(getTotalPlayTime())
      .append(getCurrentlyListening())
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
      .append(getUsageTimestamps())
      .append(getUsagePlayTimes())
      .append(getPlayingOrder())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserRoutineRelationship {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userRoutineRelationshipOwner=" + String.valueOf(getUserRoutineRelationshipOwner()) + ", ")
      .append("userRoutineRelationshipRoutine=" + String.valueOf(getUserRoutineRelationshipRoutine()) + ", ")
      .append("numberOfTimesPlayed=" + String.valueOf(getNumberOfTimesPlayed()) + ", ")
      .append("totalPlayTime=" + String.valueOf(getTotalPlayTime()) + ", ")
      .append("currentlyListening=" + String.valueOf(getCurrentlyListening()) + ", ")
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
      .append("usageTimestamps=" + String.valueOf(getUsageTimestamps()) + ", ")
      .append("usagePlayTimes=" + String.valueOf(getUsagePlayTimes()) + ", ")
      .append("playingOrder=" + String.valueOf(getPlayingOrder()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserRoutineRelationshipOwnerStep builder() {
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
  public static UserRoutineRelationship justId(String id) {
    return new UserRoutineRelationship(
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
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userRoutineRelationshipOwner,
      userRoutineRelationshipRoutine,
      numberOfTimesPlayed,
      totalPlayTime,
      currentlyListening,
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
      usageTimestamps,
      usagePlayTimes,
      playingOrder);
  }
  public interface UserRoutineRelationshipOwnerStep {
    UserRoutineRelationshipRoutineStep userRoutineRelationshipOwner(UserData userRoutineRelationshipOwner);
  }
  

  public interface UserRoutineRelationshipRoutineStep {
    BuildStep userRoutineRelationshipRoutine(RoutineData userRoutineRelationshipRoutine);
  }
  

  public interface BuildStep {
    UserRoutineRelationship build();
    BuildStep id(String id);
    BuildStep numberOfTimesPlayed(Integer numberOfTimesPlayed);
    BuildStep totalPlayTime(Integer totalPlayTime);
    BuildStep currentlyListening(Boolean currentlyListening);
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
    BuildStep usageTimestamps(List<Temporal.DateTime> usageTimestamps);
    BuildStep usagePlayTimes(List<Integer> usagePlayTimes);
    BuildStep playingOrder(List<String> playingOrder);
  }
  

  public static class Builder implements UserRoutineRelationshipOwnerStep, UserRoutineRelationshipRoutineStep, BuildStep {
    private String id;
    private UserData userRoutineRelationshipOwner;
    private RoutineData userRoutineRelationshipRoutine;
    private Integer numberOfTimesPlayed;
    private Integer totalPlayTime;
    private Boolean currentlyListening;
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
    private List<Temporal.DateTime> usageTimestamps;
    private List<Integer> usagePlayTimes;
    private List<String> playingOrder;
    @Override
     public UserRoutineRelationship build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRoutineRelationship(
          id,
          userRoutineRelationshipOwner,
          userRoutineRelationshipRoutine,
          numberOfTimesPlayed,
          totalPlayTime,
          currentlyListening,
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
          usageTimestamps,
          usagePlayTimes,
          playingOrder);
    }
    
    @Override
     public UserRoutineRelationshipRoutineStep userRoutineRelationshipOwner(UserData userRoutineRelationshipOwner) {
        Objects.requireNonNull(userRoutineRelationshipOwner);
        this.userRoutineRelationshipOwner = userRoutineRelationshipOwner;
        return this;
    }
    
    @Override
     public BuildStep userRoutineRelationshipRoutine(RoutineData userRoutineRelationshipRoutine) {
        Objects.requireNonNull(userRoutineRelationshipRoutine);
        this.userRoutineRelationshipRoutine = userRoutineRelationshipRoutine;
        return this;
    }
    
    @Override
     public BuildStep numberOfTimesPlayed(Integer numberOfTimesPlayed) {
        this.numberOfTimesPlayed = numberOfTimesPlayed;
        return this;
    }
    
    @Override
     public BuildStep totalPlayTime(Integer totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
        return this;
    }
    
    @Override
     public BuildStep currentlyListening(Boolean currentlyListening) {
        this.currentlyListening = currentlyListening;
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
     public BuildStep usageTimestamps(List<Temporal.DateTime> usageTimestamps) {
        this.usageTimestamps = usageTimestamps;
        return this;
    }
    
    @Override
     public BuildStep usagePlayTimes(List<Integer> usagePlayTimes) {
        this.usagePlayTimes = usagePlayTimes;
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
    private CopyOfBuilder(String id, UserData userRoutineRelationshipOwner, RoutineData userRoutineRelationshipRoutine, Integer numberOfTimesPlayed, Integer totalPlayTime, Boolean currentlyListening, Boolean playSoundDuringStretch, Boolean playSoundDuringPrayer, Boolean playSoundDuringBreathing, Boolean playSoundDuringSelfLove, Boolean playSoundDuringBedtimeStory, Boolean playSoundDuringSleep, Integer eachSoundPlayTime, Integer prayerPlayTime, Integer bedtimeStoryPlayTime, Integer selfLovePlayTime, Integer stretchTime, Integer breathingTime, Integer currentBedtimeStoryPlayingIndex, Integer currentBedtimeStoryContinuePlayingTime, Integer currentSelfLovePlayingIndex, Integer currentSelfLoveContinuePlayingTime, Integer currentPrayerPlayingIndex, Integer currentPrayerContinuePlayingTime, List<Temporal.DateTime> usageTimestamps, List<Integer> usagePlayTimes, List<String> playingOrder) {
      super.id(id);
      super.userRoutineRelationshipOwner(userRoutineRelationshipOwner)
        .userRoutineRelationshipRoutine(userRoutineRelationshipRoutine)
        .numberOfTimesPlayed(numberOfTimesPlayed)
        .totalPlayTime(totalPlayTime)
        .currentlyListening(currentlyListening)
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
        .usageTimestamps(usageTimestamps)
        .usagePlayTimes(usagePlayTimes)
        .playingOrder(playingOrder);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationshipOwner(UserData userRoutineRelationshipOwner) {
      return (CopyOfBuilder) super.userRoutineRelationshipOwner(userRoutineRelationshipOwner);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationshipRoutine(RoutineData userRoutineRelationshipRoutine) {
      return (CopyOfBuilder) super.userRoutineRelationshipRoutine(userRoutineRelationshipRoutine);
    }
    
    @Override
     public CopyOfBuilder numberOfTimesPlayed(Integer numberOfTimesPlayed) {
      return (CopyOfBuilder) super.numberOfTimesPlayed(numberOfTimesPlayed);
    }
    
    @Override
     public CopyOfBuilder totalPlayTime(Integer totalPlayTime) {
      return (CopyOfBuilder) super.totalPlayTime(totalPlayTime);
    }
    
    @Override
     public CopyOfBuilder currentlyListening(Boolean currentlyListening) {
      return (CopyOfBuilder) super.currentlyListening(currentlyListening);
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
     public CopyOfBuilder usageTimestamps(List<Temporal.DateTime> usageTimestamps) {
      return (CopyOfBuilder) super.usageTimestamps(usageTimestamps);
    }
    
    @Override
     public CopyOfBuilder usagePlayTimes(List<Integer> usagePlayTimes) {
      return (CopyOfBuilder) super.usagePlayTimes(usagePlayTimes);
    }
    
    @Override
     public CopyOfBuilder playingOrder(List<String> playingOrder) {
      return (CopyOfBuilder) super.playingOrder(playingOrder);
    }
  }
  
}
