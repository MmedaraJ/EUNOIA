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

/** This is an auto generated class representing the UserRoutineRelationshipPrayer type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserRoutineRelationshipPrayers")
@Index(name = "byUserRoutineRelationship", fields = {"userRoutineRelationshipID"})
@Index(name = "byPrayerData", fields = {"prayerDataID"})
public final class UserRoutineRelationshipPrayer implements Model {
  public static final QueryField ID = field("UserRoutineRelationshipPrayer", "id");
  public static final QueryField USER_ROUTINE_RELATIONSHIP = field("UserRoutineRelationshipPrayer", "userRoutineRelationshipID");
  public static final QueryField PRAYER_DATA = field("UserRoutineRelationshipPrayer", "prayerDataID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="UserRoutineRelationship", isRequired = true) @BelongsTo(targetName = "userRoutineRelationshipID", type = UserRoutineRelationship.class) UserRoutineRelationship userRoutineRelationship;
  private final @ModelField(targetType="PrayerData", isRequired = true) @BelongsTo(targetName = "prayerDataID", type = PrayerData.class) PrayerData prayerData;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public UserRoutineRelationship getUserRoutineRelationship() {
      return userRoutineRelationship;
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
  
  private UserRoutineRelationshipPrayer(String id, UserRoutineRelationship userRoutineRelationship, PrayerData prayerData) {
    this.id = id;
    this.userRoutineRelationship = userRoutineRelationship;
    this.prayerData = prayerData;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserRoutineRelationshipPrayer userRoutineRelationshipPrayer = (UserRoutineRelationshipPrayer) obj;
      return ObjectsCompat.equals(getId(), userRoutineRelationshipPrayer.getId()) &&
              ObjectsCompat.equals(getUserRoutineRelationship(), userRoutineRelationshipPrayer.getUserRoutineRelationship()) &&
              ObjectsCompat.equals(getPrayerData(), userRoutineRelationshipPrayer.getPrayerData()) &&
              ObjectsCompat.equals(getCreatedAt(), userRoutineRelationshipPrayer.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userRoutineRelationshipPrayer.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserRoutineRelationship())
      .append(getPrayerData())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserRoutineRelationshipPrayer {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("userRoutineRelationship=" + String.valueOf(getUserRoutineRelationship()) + ", ")
      .append("prayerData=" + String.valueOf(getPrayerData()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserRoutineRelationshipStep builder() {
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
  public static UserRoutineRelationshipPrayer justId(String id) {
    return new UserRoutineRelationshipPrayer(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userRoutineRelationship,
      prayerData);
  }
  public interface UserRoutineRelationshipStep {
    PrayerDataStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship);
  }
  

  public interface PrayerDataStep {
    BuildStep prayerData(PrayerData prayerData);
  }
  

  public interface BuildStep {
    UserRoutineRelationshipPrayer build();
    BuildStep id(String id);
  }
  

  public static class Builder implements UserRoutineRelationshipStep, PrayerDataStep, BuildStep {
    private String id;
    private UserRoutineRelationship userRoutineRelationship;
    private PrayerData prayerData;
    @Override
     public UserRoutineRelationshipPrayer build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserRoutineRelationshipPrayer(
          id,
          userRoutineRelationship,
          prayerData);
    }
    
    @Override
     public PrayerDataStep userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
        Objects.requireNonNull(userRoutineRelationship);
        this.userRoutineRelationship = userRoutineRelationship;
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
    private CopyOfBuilder(String id, UserRoutineRelationship userRoutineRelationship, PrayerData prayerData) {
      super.id(id);
      super.userRoutineRelationship(userRoutineRelationship)
        .prayerData(prayerData);
    }
    
    @Override
     public CopyOfBuilder userRoutineRelationship(UserRoutineRelationship userRoutineRelationship) {
      return (CopyOfBuilder) super.userRoutineRelationship(userRoutineRelationship);
    }
    
    @Override
     public CopyOfBuilder prayerData(PrayerData prayerData) {
      return (CopyOfBuilder) super.prayerData(prayerData);
    }
  }
  
}
