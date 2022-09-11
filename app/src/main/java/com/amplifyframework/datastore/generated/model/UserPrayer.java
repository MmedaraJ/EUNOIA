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

/** This is an auto generated class representing the UserPrayer type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserPrayers")
@Index(name = "byUserData", fields = {"userDataID"})
@Index(name = "byPrayerData", fields = {"prayerDataID"})
public final class UserPrayer implements Model {
  public static final QueryField ID = field("UserPrayer", "id");
  public static final QueryField USER_DATA = field("UserPrayer", "userDataID");
  public static final QueryField PRAYER_DATA = field("UserPrayer", "prayerDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserData", isRequired = true) @BelongsTo(targetName = "userDataID", type = UserData.class) UserData userData;
  private final @ModelField(targetType="PrayerData", isRequired = true) @BelongsTo(targetName = "prayerDataID", type = PrayerData.class) PrayerData prayerData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserData getUserData() {
      return userData;
  }
  
  public PrayerData getPrayerData() {
      return prayerData;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserPrayer(String id, UserData userData, PrayerData prayerData) {
    this.id = id;
    this.userData = userData;
    this.prayerData = prayerData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserPrayer userPrayer = (UserPrayer) obj;
      return ObjectsCompat.equals(getId(), userPrayer.getId()) &&
              ObjectsCompat.equals(getUserData(), userPrayer.getUserData()) &&
              ObjectsCompat.equals(getPrayerData(), userPrayer.getPrayerData()) &&
              ObjectsCompat.equals(getCreatedAt(), userPrayer.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userPrayer.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserData())
      .append(getPrayerData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserPrayer {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userData=" + String.valueOf(getUserData()) + ", ")
      .append("prayerData=" + String.valueOf(getPrayerData()) + ", ")
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
  public static UserPrayer justId(String id) {
    return new UserPrayer(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userData,
      prayerData);
  }
  public interface UserDataStep {
    PrayerDataStep userData(UserData userData);
  }
  

  public interface PrayerDataStep {
    BuildStep prayerData(PrayerData prayerData);
  }
  

  public interface BuildStep {
    UserPrayer build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserDataStep, PrayerDataStep, BuildStep {
    private String id;
    private UserData userData;
    private PrayerData prayerData;
    @Override
     public UserPrayer build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserPrayer(
          id,
          userData,
          prayerData);
    }
    
    @Override
     public PrayerDataStep userData(UserData userData) {
        Objects.requireNonNull(userData);
        this.userData = userData;
        return this;
    }
    
    @Override
     public BuildStep prayerData(PrayerData prayerData) {
        Objects.requireNonNull(prayerData);
        this.prayerData = prayerData;
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
    private CopyOfBuilder(String id, UserData userData, PrayerData prayerData) {
      super.id(id);
      super.userData(userData)
        .prayerData(prayerData);
    }
    
    @Override
     public CopyOfBuilder userData(UserData userData) {
      return (CopyOfBuilder) super.userData(userData);
    }
    
    @Override
     public CopyOfBuilder prayerData(PrayerData prayerData) {
      return (CopyOfBuilder) super.prayerData(prayerData);
    }
  }
  
}
