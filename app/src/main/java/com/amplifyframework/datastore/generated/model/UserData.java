package com.amplifyframework.datastore.generated.model;

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

/** This is an auto generated class representing the UserData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserData", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class UserData implements Model {
  public static final QueryField ID = field("UserData", "id");
  public static final QueryField USERNAME = field("UserData", "username");
  public static final QueryField AMPLIFY_AUTH_USER_ID = field("UserData", "amplifyAuthUserId");
  public static final QueryField GIVEN_NAME = field("UserData", "givenName");
  public static final QueryField FAMILY_NAME = field("UserData", "familyName");
  public static final QueryField MIDDLE_NAME = field("UserData", "middleName");
  public static final QueryField EMAIL = field("UserData", "email");
  public static final QueryField PROFILE_PICTURE_KEY = field("UserData", "profile_picture_key");
  public static final QueryField ADDRESS = field("UserData", "address");
  public static final QueryField BIRTHDATE = field("UserData", "birthdate");
  public static final QueryField GENDER = field("UserData", "gender");
  public static final QueryField NICKNAME = field("UserData", "nickname");
  public static final QueryField PHONE_NUMBER = field("UserData", "phoneNumber");
  public static final QueryField AUTHENTICATED = field("UserData", "authenticated");
  public static final QueryField SUBSCRIPTION = field("UserData", "subscription");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String username;
  private final @ModelField(targetType="String", isRequired = true) String amplifyAuthUserId;
  private final @ModelField(targetType="String", isRequired = true) String givenName;
  private final @ModelField(targetType="String", isRequired = true) String familyName;
  private final @ModelField(targetType="String", isRequired = true) String middleName;
  private final @ModelField(targetType="String", isRequired = true) String email;
  private final @ModelField(targetType="String", isRequired = true) String profile_picture_key;
  private final @ModelField(targetType="String", isRequired = true) String address;
  private final @ModelField(targetType="String", isRequired = true) String birthdate;
  private final @ModelField(targetType="String", isRequired = true) String gender;
  private final @ModelField(targetType="String", isRequired = true) String nickname;
  private final @ModelField(targetType="String", isRequired = true) String phoneNumber;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean authenticated;
  private final @ModelField(targetType="String", isRequired = true) String subscription;
  private final @ModelField(targetType="UserSoundRelationship") @HasMany(associatedWith = "userSoundRelationshipOwner", type = UserSoundRelationship.class) List<UserSoundRelationship> userSoundRelationshipsOwnedByUser = null;
  private final @ModelField(targetType="UserBedtimeStoryInfoRelationship") @HasMany(associatedWith = "userBedtimeStoryInfoRelationshipOwner", type = UserBedtimeStoryInfoRelationship.class) List<UserBedtimeStoryInfoRelationship> userBedtimeStoryInfoRelationshipsOwnedByUser = null;
  private final @ModelField(targetType="UserPrayerRelationship") @HasMany(associatedWith = "userPrayerRelationshipOwner", type = UserPrayerRelationship.class) List<UserPrayerRelationship> userPrayerRelationshipsOwnedByUser = null;
  private final @ModelField(targetType="UserSelfLoveRelationship") @HasMany(associatedWith = "userSelfLoveRelationshipOwner", type = UserSelfLoveRelationship.class) List<UserSelfLoveRelationship> userSelfLoveRelationshipsOwnedByUser = null;
  private final @ModelField(targetType="UserRoutineRelationship") @HasMany(associatedWith = "userRoutineRelationshipOwner", type = UserRoutineRelationship.class) List<UserRoutineRelationship> userRoutineRelationshipsOwnedByUser = null;
  private final @ModelField(targetType="UserPresetRelationship") @HasMany(associatedWith = "userPresetRelationshipOwner", type = UserPresetRelationship.class) List<UserPresetRelationship> userPresetRelationshipsOwnedByUser = null;
  private final @ModelField(targetType="SoundData") @HasMany(associatedWith = "soundOwner", type = SoundData.class) List<SoundData> soundsOwnedByUser = null;
  private final @ModelField(targetType="UserSound") @HasMany(associatedWith = "userData", type = UserSound.class) List<UserSound> sounds = null;
  private final @ModelField(targetType="PresetData") @HasMany(associatedWith = "presetOwner", type = PresetData.class) List<PresetData> presetsOwnedByUser = null;
  private final @ModelField(targetType="UserPreset") @HasMany(associatedWith = "userData", type = UserPreset.class) List<UserPreset> presets = null;
  private final @ModelField(targetType="CommentData") @HasMany(associatedWith = "commentOwner", type = CommentData.class) List<CommentData> commentsOwnedByUser = null;
  private final @ModelField(targetType="RoutineData") @HasMany(associatedWith = "routineOwner", type = RoutineData.class) List<RoutineData> routinesOwnedByUser = null;
  private final @ModelField(targetType="UserRoutine") @HasMany(associatedWith = "userData", type = UserRoutine.class) List<UserRoutine> routines = null;
  private final @ModelField(targetType="StretchData") @HasMany(associatedWith = "stretchOwner", type = StretchData.class) List<StretchData> stretchesOwnedByUser = null;
  private final @ModelField(targetType="UserStretch") @HasMany(associatedWith = "userData", type = UserStretch.class) List<UserStretch> stretches = null;
  private final @ModelField(targetType="PrayerData") @HasMany(associatedWith = "prayerOwner", type = PrayerData.class) List<PrayerData> prayersOwnedByUser = null;
  private final @ModelField(targetType="UserPrayer") @HasMany(associatedWith = "userData", type = UserPrayer.class) List<UserPrayer> prayers = null;
  private final @ModelField(targetType="BreathingData") @HasMany(associatedWith = "breathingOwner", type = BreathingData.class) List<BreathingData> breathingsOwnedByUser = null;
  private final @ModelField(targetType="UserBreathing") @HasMany(associatedWith = "userData", type = UserBreathing.class) List<UserBreathing> breathings = null;
  private final @ModelField(targetType="SelfLoveData") @HasMany(associatedWith = "selfLoveOwner", type = SelfLoveData.class) List<SelfLoveData> selfLovesOwnedByUser = null;
  private final @ModelField(targetType="UserSelfLove") @HasMany(associatedWith = "userData", type = UserSelfLove.class) List<UserSelfLove> selfLoves = null;
  private final @ModelField(targetType="BedtimeStoryInfoData") @HasMany(associatedWith = "bedtimeStoryOwner", type = BedtimeStoryInfoData.class) List<BedtimeStoryInfoData> bedtimeStoriesOwnedByUser = null;
  private final @ModelField(targetType="UserBedtimeStoryInfo") @HasMany(associatedWith = "userData", type = UserBedtimeStoryInfo.class) List<UserBedtimeStoryInfo> bedtimeStories = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getUsername() {
      return username;
  }
  
  public String getAmplifyAuthUserId() {
      return amplifyAuthUserId;
  }
  
  public String getGivenName() {
      return givenName;
  }
  
  public String getFamilyName() {
      return familyName;
  }
  
  public String getMiddleName() {
      return middleName;
  }
  
  public String getEmail() {
      return email;
  }
  
  public String getProfilePictureKey() {
      return profile_picture_key;
  }
  
  public String getAddress() {
      return address;
  }
  
  public String getBirthdate() {
      return birthdate;
  }
  
  public String getGender() {
      return gender;
  }
  
  public String getNickname() {
      return nickname;
  }
  
  public String getPhoneNumber() {
      return phoneNumber;
  }
  
  public Boolean getAuthenticated() {
      return authenticated;
  }
  
  public String getSubscription() {
      return subscription;
  }
  
  public List<UserSoundRelationship> getUserSoundRelationshipsOwnedByUser() {
      return userSoundRelationshipsOwnedByUser;
  }
  
  public List<UserBedtimeStoryInfoRelationship> getUserBedtimeStoryInfoRelationshipsOwnedByUser() {
      return userBedtimeStoryInfoRelationshipsOwnedByUser;
  }
  
  public List<UserPrayerRelationship> getUserPrayerRelationshipsOwnedByUser() {
      return userPrayerRelationshipsOwnedByUser;
  }
  
  public List<UserSelfLoveRelationship> getUserSelfLoveRelationshipsOwnedByUser() {
      return userSelfLoveRelationshipsOwnedByUser;
  }
  
  public List<UserRoutineRelationship> getUserRoutineRelationshipsOwnedByUser() {
      return userRoutineRelationshipsOwnedByUser;
  }
  
  public List<UserPresetRelationship> getUserPresetRelationshipsOwnedByUser() {
      return userPresetRelationshipsOwnedByUser;
  }
  
  public List<SoundData> getSoundsOwnedByUser() {
      return soundsOwnedByUser;
  }
  
  public List<UserSound> getSounds() {
      return sounds;
  }
  
  public List<PresetData> getPresetsOwnedByUser() {
      return presetsOwnedByUser;
  }
  
  public List<UserPreset> getPresets() {
      return presets;
  }
  
  public List<CommentData> getCommentsOwnedByUser() {
      return commentsOwnedByUser;
  }
  
  public List<RoutineData> getRoutinesOwnedByUser() {
      return routinesOwnedByUser;
  }
  
  public List<UserRoutine> getRoutines() {
      return routines;
  }
  
  public List<StretchData> getStretchesOwnedByUser() {
      return stretchesOwnedByUser;
  }
  
  public List<UserStretch> getStretches() {
      return stretches;
  }
  
  public List<PrayerData> getPrayersOwnedByUser() {
      return prayersOwnedByUser;
  }
  
  public List<UserPrayer> getPrayers() {
      return prayers;
  }
  
  public List<BreathingData> getBreathingsOwnedByUser() {
      return breathingsOwnedByUser;
  }
  
  public List<UserBreathing> getBreathings() {
      return breathings;
  }
  
  public List<SelfLoveData> getSelfLovesOwnedByUser() {
      return selfLovesOwnedByUser;
  }
  
  public List<UserSelfLove> getSelfLoves() {
      return selfLoves;
  }
  
  public List<BedtimeStoryInfoData> getBedtimeStoriesOwnedByUser() {
      return bedtimeStoriesOwnedByUser;
  }
  
  public List<UserBedtimeStoryInfo> getBedtimeStories() {
      return bedtimeStories;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserData(String id, String username, String amplifyAuthUserId, String givenName, String familyName, String middleName, String email, String profile_picture_key, String address, String birthdate, String gender, String nickname, String phoneNumber, Boolean authenticated, String subscription) {
    this.id = id;
    this.username = username;
    this.amplifyAuthUserId = amplifyAuthUserId;
    this.givenName = givenName;
    this.familyName = familyName;
    this.middleName = middleName;
    this.email = email;
    this.profile_picture_key = profile_picture_key;
    this.address = address;
    this.birthdate = birthdate;
    this.gender = gender;
    this.nickname = nickname;
    this.phoneNumber = phoneNumber;
    this.authenticated = authenticated;
    this.subscription = subscription;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserData userData = (UserData) obj;
      return ObjectsCompat.equals(getId(), userData.getId()) &&
              ObjectsCompat.equals(getUsername(), userData.getUsername()) &&
              ObjectsCompat.equals(getAmplifyAuthUserId(), userData.getAmplifyAuthUserId()) &&
              ObjectsCompat.equals(getGivenName(), userData.getGivenName()) &&
              ObjectsCompat.equals(getFamilyName(), userData.getFamilyName()) &&
              ObjectsCompat.equals(getMiddleName(), userData.getMiddleName()) &&
              ObjectsCompat.equals(getEmail(), userData.getEmail()) &&
              ObjectsCompat.equals(getProfilePictureKey(), userData.getProfilePictureKey()) &&
              ObjectsCompat.equals(getAddress(), userData.getAddress()) &&
              ObjectsCompat.equals(getBirthdate(), userData.getBirthdate()) &&
              ObjectsCompat.equals(getGender(), userData.getGender()) &&
              ObjectsCompat.equals(getNickname(), userData.getNickname()) &&
              ObjectsCompat.equals(getPhoneNumber(), userData.getPhoneNumber()) &&
              ObjectsCompat.equals(getAuthenticated(), userData.getAuthenticated()) &&
              ObjectsCompat.equals(getSubscription(), userData.getSubscription()) &&
              ObjectsCompat.equals(getCreatedAt(), userData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUsername())
      .append(getAmplifyAuthUserId())
      .append(getGivenName())
      .append(getFamilyName())
      .append(getMiddleName())
      .append(getEmail())
      .append(getProfilePictureKey())
      .append(getAddress())
      .append(getBirthdate())
      .append(getGender())
      .append(getNickname())
      .append(getPhoneNumber())
      .append(getAuthenticated())
      .append(getSubscription())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserData {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("username=" + String.valueOf(getUsername()) + ", ")
      .append("amplifyAuthUserId=" + String.valueOf(getAmplifyAuthUserId()) + ", ")
      .append("givenName=" + String.valueOf(getGivenName()) + ", ")
      .append("familyName=" + String.valueOf(getFamilyName()) + ", ")
      .append("middleName=" + String.valueOf(getMiddleName()) + ", ")
      .append("email=" + String.valueOf(getEmail()) + ", ")
      .append("profile_picture_key=" + String.valueOf(getProfilePictureKey()) + ", ")
      .append("address=" + String.valueOf(getAddress()) + ", ")
      .append("birthdate=" + String.valueOf(getBirthdate()) + ", ")
      .append("gender=" + String.valueOf(getGender()) + ", ")
      .append("nickname=" + String.valueOf(getNickname()) + ", ")
      .append("phoneNumber=" + String.valueOf(getPhoneNumber()) + ", ")
      .append("authenticated=" + String.valueOf(getAuthenticated()) + ", ")
      .append("subscription=" + String.valueOf(getSubscription()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UsernameStep builder() {
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
  public static UserData justId(String id) {
    return new UserData(
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
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      username,
      amplifyAuthUserId,
      givenName,
      familyName,
      middleName,
      email,
      profile_picture_key,
      address,
      birthdate,
      gender,
      nickname,
      phoneNumber,
      authenticated,
      subscription);
  }
  public interface UsernameStep {
    AmplifyAuthUserIdStep username(String username);
  }
  

  public interface AmplifyAuthUserIdStep {
    GivenNameStep amplifyAuthUserId(String amplifyAuthUserId);
  }
  

  public interface GivenNameStep {
    FamilyNameStep givenName(String givenName);
  }
  

  public interface FamilyNameStep {
    MiddleNameStep familyName(String familyName);
  }
  

  public interface MiddleNameStep {
    EmailStep middleName(String middleName);
  }
  

  public interface EmailStep {
    ProfilePictureKeyStep email(String email);
  }
  

  public interface ProfilePictureKeyStep {
    AddressStep profilePictureKey(String profilePictureKey);
  }
  

  public interface AddressStep {
    BirthdateStep address(String address);
  }
  

  public interface BirthdateStep {
    GenderStep birthdate(String birthdate);
  }
  

  public interface GenderStep {
    NicknameStep gender(String gender);
  }
  

  public interface NicknameStep {
    PhoneNumberStep nickname(String nickname);
  }
  

  public interface PhoneNumberStep {
    AuthenticatedStep phoneNumber(String phoneNumber);
  }
  

  public interface AuthenticatedStep {
    SubscriptionStep authenticated(Boolean authenticated);
  }
  

  public interface SubscriptionStep {
    BuildStep subscription(String subscription);
  }
  

  public interface BuildStep {
    UserData build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UsernameStep, AmplifyAuthUserIdStep, GivenNameStep, FamilyNameStep, MiddleNameStep, EmailStep, ProfilePictureKeyStep, AddressStep, BirthdateStep, GenderStep, NicknameStep, PhoneNumberStep, AuthenticatedStep, SubscriptionStep, BuildStep {
    private String id;
    private String username;
    private String amplifyAuthUserId;
    private String givenName;
    private String familyName;
    private String middleName;
    private String email;
    private String profile_picture_key;
    private String address;
    private String birthdate;
    private String gender;
    private String nickname;
    private String phoneNumber;
    private Boolean authenticated;
    private String subscription;
    @Override
     public UserData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserData(
          id,
          username,
          amplifyAuthUserId,
          givenName,
          familyName,
          middleName,
          email,
          profile_picture_key,
          address,
          birthdate,
          gender,
          nickname,
          phoneNumber,
          authenticated,
          subscription);
    }
    
    @Override
     public AmplifyAuthUserIdStep username(String username) {
        Objects.requireNonNull(username);
        this.username = username;
        return this;
    }
    
    @Override
     public GivenNameStep amplifyAuthUserId(String amplifyAuthUserId) {
        Objects.requireNonNull(amplifyAuthUserId);
        this.amplifyAuthUserId = amplifyAuthUserId;
        return this;
    }
    
    @Override
     public FamilyNameStep givenName(String givenName) {
        Objects.requireNonNull(givenName);
        this.givenName = givenName;
        return this;
    }
    
    @Override
     public MiddleNameStep familyName(String familyName) {
        Objects.requireNonNull(familyName);
        this.familyName = familyName;
        return this;
    }
    
    @Override
     public EmailStep middleName(String middleName) {
        Objects.requireNonNull(middleName);
        this.middleName = middleName;
        return this;
    }
    
    @Override
     public ProfilePictureKeyStep email(String email) {
        Objects.requireNonNull(email);
        this.email = email;
        return this;
    }
    
    @Override
     public AddressStep profilePictureKey(String profilePictureKey) {
        Objects.requireNonNull(profilePictureKey);
        this.profile_picture_key = profilePictureKey;
        return this;
    }
    
    @Override
     public BirthdateStep address(String address) {
        Objects.requireNonNull(address);
        this.address = address;
        return this;
    }
    
    @Override
     public GenderStep birthdate(String birthdate) {
        Objects.requireNonNull(birthdate);
        this.birthdate = birthdate;
        return this;
    }
    
    @Override
     public NicknameStep gender(String gender) {
        Objects.requireNonNull(gender);
        this.gender = gender;
        return this;
    }
    
    @Override
     public PhoneNumberStep nickname(String nickname) {
        Objects.requireNonNull(nickname);
        this.nickname = nickname;
        return this;
    }
    
    @Override
     public AuthenticatedStep phoneNumber(String phoneNumber) {
        Objects.requireNonNull(phoneNumber);
        this.phoneNumber = phoneNumber;
        return this;
    }
    
    @Override
     public SubscriptionStep authenticated(Boolean authenticated) {
        Objects.requireNonNull(authenticated);
        this.authenticated = authenticated;
        return this;
    }
    
    @Override
     public BuildStep subscription(String subscription) {
        Objects.requireNonNull(subscription);
        this.subscription = subscription;
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
    private CopyOfBuilder(String id, String username, String amplifyAuthUserId, String givenName, String familyName, String middleName, String email, String profilePictureKey, String address, String birthdate, String gender, String nickname, String phoneNumber, Boolean authenticated, String subscription) {
      super.id(id);
      super.username(username)
        .amplifyAuthUserId(amplifyAuthUserId)
        .givenName(givenName)
        .familyName(familyName)
        .middleName(middleName)
        .email(email)
        .profilePictureKey(profilePictureKey)
        .address(address)
        .birthdate(birthdate)
        .gender(gender)
        .nickname(nickname)
        .phoneNumber(phoneNumber)
        .authenticated(authenticated)
        .subscription(subscription);
    }
    
    @Override
     public CopyOfBuilder username(String username) {
      return (CopyOfBuilder) super.username(username);
    }
    
    @Override
     public CopyOfBuilder amplifyAuthUserId(String amplifyAuthUserId) {
      return (CopyOfBuilder) super.amplifyAuthUserId(amplifyAuthUserId);
    }
    
    @Override
     public CopyOfBuilder givenName(String givenName) {
      return (CopyOfBuilder) super.givenName(givenName);
    }
    
    @Override
     public CopyOfBuilder familyName(String familyName) {
      return (CopyOfBuilder) super.familyName(familyName);
    }
    
    @Override
     public CopyOfBuilder middleName(String middleName) {
      return (CopyOfBuilder) super.middleName(middleName);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder profilePictureKey(String profilePictureKey) {
      return (CopyOfBuilder) super.profilePictureKey(profilePictureKey);
    }
    
    @Override
     public CopyOfBuilder address(String address) {
      return (CopyOfBuilder) super.address(address);
    }
    
    @Override
     public CopyOfBuilder birthdate(String birthdate) {
      return (CopyOfBuilder) super.birthdate(birthdate);
    }
    
    @Override
     public CopyOfBuilder gender(String gender) {
      return (CopyOfBuilder) super.gender(gender);
    }
    
    @Override
     public CopyOfBuilder nickname(String nickname) {
      return (CopyOfBuilder) super.nickname(nickname);
    }
    
    @Override
     public CopyOfBuilder phoneNumber(String phoneNumber) {
      return (CopyOfBuilder) super.phoneNumber(phoneNumber);
    }
    
    @Override
     public CopyOfBuilder authenticated(Boolean authenticated) {
      return (CopyOfBuilder) super.authenticated(authenticated);
    }
    
    @Override
     public CopyOfBuilder subscription(String subscription) {
      return (CopyOfBuilder) super.subscription(subscription);
    }
  }
  
}
