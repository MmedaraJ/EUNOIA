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

/** This is an auto generated class representing the UserData type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserData", authRules = {
  @AuthRule(allow = AuthStrategy.OWNER, ownerField = "owner", identityClaim = "cognito:username", provider = "userPools", operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class UserData implements Model {
  public static final QueryField ID = field("UserData", "id");
  public static final QueryField USERNAME = field("UserData", "username");
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
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String username;
  private final @ModelField(targetType="String", isRequired = true) String givenName;
  private final @ModelField(targetType="String", isRequired = true) String familyName;
  private final @ModelField(targetType="String") String middleName;
  private final @ModelField(targetType="String", isRequired = true) String email;
  private final @ModelField(targetType="String") String profile_picture_key;
  private final @ModelField(targetType="String") String address;
  private final @ModelField(targetType="String") String birthdate;
  private final @ModelField(targetType="String") String gender;
  private final @ModelField(targetType="String") String nickname;
  private final @ModelField(targetType="String") String phoneNumber;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getUsername() {
      return username;
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
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserData(String id, String username, String givenName, String familyName, String middleName, String email, String profile_picture_key, String address, String birthdate, String gender, String nickname, String phoneNumber) {
    this.id = id;
    this.username = username;
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
              ObjectsCompat.equals(getCreatedAt(), userData.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userData.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUsername())
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
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      username,
      givenName,
      familyName,
      middleName,
      email,
      profile_picture_key,
      address,
      birthdate,
      gender,
      nickname,
      phoneNumber);
  }
  public interface UsernameStep {
    GivenNameStep username(String username);
  }
  

  public interface GivenNameStep {
    FamilyNameStep givenName(String givenName);
  }
  

  public interface FamilyNameStep {
    EmailStep familyName(String familyName);
  }
  

  public interface EmailStep {
    BuildStep email(String email);
  }
  

  public interface BuildStep {
    UserData build();
    BuildStep id(String id);
    BuildStep middleName(String middleName);
    BuildStep profilePictureKey(String profilePictureKey);
    BuildStep address(String address);
    BuildStep birthdate(String birthdate);
    BuildStep gender(String gender);
    BuildStep nickname(String nickname);
    BuildStep phoneNumber(String phoneNumber);
  }
  

  public static class Builder implements UsernameStep, GivenNameStep, FamilyNameStep, EmailStep, BuildStep {
    private String id;
    private String username;
    private String givenName;
    private String familyName;
    private String email;
    private String middleName;
    private String profile_picture_key;
    private String address;
    private String birthdate;
    private String gender;
    private String nickname;
    private String phoneNumber;
    @Override
     public UserData build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserData(
          id,
          username,
          givenName,
          familyName,
          middleName,
          email,
          profile_picture_key,
          address,
          birthdate,
          gender,
          nickname,
          phoneNumber);
    }
    
    @Override
     public GivenNameStep username(String username) {
        Objects.requireNonNull(username);
        this.username = username;
        return this;
    }
    
    @Override
     public FamilyNameStep givenName(String givenName) {
        Objects.requireNonNull(givenName);
        this.givenName = givenName;
        return this;
    }
    
    @Override
     public EmailStep familyName(String familyName) {
        Objects.requireNonNull(familyName);
        this.familyName = familyName;
        return this;
    }
    
    @Override
     public BuildStep email(String email) {
        Objects.requireNonNull(email);
        this.email = email;
        return this;
    }
    
    @Override
     public BuildStep middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }
    
    @Override
     public BuildStep profilePictureKey(String profilePictureKey) {
        this.profile_picture_key = profilePictureKey;
        return this;
    }
    
    @Override
     public BuildStep address(String address) {
        this.address = address;
        return this;
    }
    
    @Override
     public BuildStep birthdate(String birthdate) {
        this.birthdate = birthdate;
        return this;
    }
    
    @Override
     public BuildStep gender(String gender) {
        this.gender = gender;
        return this;
    }
    
    @Override
     public BuildStep nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }
    
    @Override
     public BuildStep phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
    private CopyOfBuilder(String id, String username, String givenName, String familyName, String middleName, String email, String profilePictureKey, String address, String birthdate, String gender, String nickname, String phoneNumber) {
      super.id(id);
      super.username(username)
        .givenName(givenName)
        .familyName(familyName)
        .email(email)
        .middleName(middleName)
        .profilePictureKey(profilePictureKey)
        .address(address)
        .birthdate(birthdate)
        .gender(gender)
        .nickname(nickname)
        .phoneNumber(phoneNumber);
    }
    
    @Override
     public CopyOfBuilder username(String username) {
      return (CopyOfBuilder) super.username(username);
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
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder middleName(String middleName) {
      return (CopyOfBuilder) super.middleName(middleName);
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
  }
  
}
