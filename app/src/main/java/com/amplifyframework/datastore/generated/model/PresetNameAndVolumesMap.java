package com.amplifyframework.datastore.generated.model;


import androidx.core.util.ObjectsCompat;

import java.util.Objects;
import java.util.List;

/** This is an auto generated class representing the PresetNameAndVolumesMap type in your schema. */
public final class PresetNameAndVolumesMap {
  private final String key;
  private final List<Integer> volumes;
  public String getKey() {
      return key;
  }
  
  public List<Integer> getVolumes() {
      return volumes;
  }
  
  private PresetNameAndVolumesMap(String key, List<Integer> volumes) {
    this.key = key;
    this.volumes = volumes;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      PresetNameAndVolumesMap presetNameAndVolumesMap = (PresetNameAndVolumesMap) obj;
      return ObjectsCompat.equals(getKey(), presetNameAndVolumesMap.getKey()) &&
              ObjectsCompat.equals(getVolumes(), presetNameAndVolumesMap.getVolumes());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getKey())
      .append(getVolumes())
      .toString()
      .hashCode();
  }
  
  public static KeyStep builder() {
      return new Builder();
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(key,
      volumes);
  }
  public interface KeyStep {
    VolumesStep key(String key);
  }
  

  public interface VolumesStep {
    BuildStep volumes(List<Integer> volumes);
  }
  

  public interface BuildStep {
    PresetNameAndVolumesMap build();
  }
  

  public static class Builder implements KeyStep, VolumesStep, BuildStep {
    private String key;
    private List<Integer> volumes;
    @Override
     public PresetNameAndVolumesMap build() {
        
        return new PresetNameAndVolumesMap(
          key,
          volumes);
    }
    
    @Override
     public VolumesStep key(String key) {
        Objects.requireNonNull(key);
        this.key = key;
        return this;
    }
    
    @Override
     public BuildStep volumes(List<Integer> volumes) {
        Objects.requireNonNull(volumes);
        this.volumes = volumes;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String key, List<Integer> volumes) {
      super.key(key)
        .volumes(volumes);
    }
    
    @Override
     public CopyOfBuilder key(String key) {
      return (CopyOfBuilder) super.key(key);
    }
    
    @Override
     public CopyOfBuilder volumes(List<Integer> volumes) {
      return (CopyOfBuilder) super.volumes(volumes);
    }
  }
  
}
