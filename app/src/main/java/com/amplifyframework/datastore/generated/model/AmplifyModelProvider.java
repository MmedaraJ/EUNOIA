package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.util.Immutable;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/**
 *  Contains the set of model classes that implement {@link Model}
 * interface.
 */

public final class AmplifyModelProvider implements ModelProvider {
  private static final String AMPLIFY_MODEL_VERSION = "f0d3f0cfd7f40217a0d164970900d01d";
  private static AmplifyModelProvider amplifyGeneratedModelInstance;
  private AmplifyModelProvider() {
    
  }
  
  public static AmplifyModelProvider getInstance() {
    if (amplifyGeneratedModelInstance == null) {
      amplifyGeneratedModelInstance = new AmplifyModelProvider();
    }
    return amplifyGeneratedModelInstance;
  }
  
  /**
   * Get a set of the model classes.
   *
   * @return a set of the model classes.
   */
  @Override
   public Set<Class<? extends Model>> models() {
    final Set<Class<? extends Model>> modifiableSet = new HashSet<>(
          Arrays.<Class<? extends Model>>asList(NoteData.class, SoundData.class, UserData.class, CommentData.class, RoutineData.class, StretchData.class, BreathingData.class, BedtimeStoryInfoData.class, BedtimeStoryInfoChapterData.class, ChapterPageData.class, SelfLoveData.class, PresetData.class, PresetNameAndVolumesMapData.class, UserSound.class, RoutineSound.class, UserRoutine.class, UserStretch.class, UserBreathing.class, UserSelfLove.class, UserBedtimeStoryInfo.class, RoutineStretch.class, RoutineBreathing.class, RoutineBedtimeStoryInfo.class, RoutineSelfLove.class)
        );
    
        return Immutable.of(modifiableSet);
        
  }
  
  /**
   * Get the version of the models.
   *
   * @return the version string of the models.
   */
  @Override
   public String version() {
    return AMPLIFY_MODEL_VERSION;
  }
}
