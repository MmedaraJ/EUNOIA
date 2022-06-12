package com.amplifyframework.datastore.generated.model;


import androidx.core.util.ObjectsCompat;

import java.util.Objects;
import java.util.List;

/** This is an auto generated class representing the ModelSoundDataConnection type in your schema. */
public final class ModelSoundDataConnection {
  private final List<SoundData> items;
  private final String nextToken;
  public List<SoundData> getItems() {
      return items;
  }
  
  public String getNextToken() {
      return nextToken;
  }
  
  private ModelSoundDataConnection(List<SoundData> items, String nextToken) {
    this.items = items;
    this.nextToken = nextToken;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      ModelSoundDataConnection modelSoundDataConnection = (ModelSoundDataConnection) obj;
      return ObjectsCompat.equals(getItems(), modelSoundDataConnection.getItems()) &&
              ObjectsCompat.equals(getNextToken(), modelSoundDataConnection.getNextToken());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getItems())
      .append(getNextToken())
      .toString()
      .hashCode();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(items,
      nextToken);
  }
  public interface ItemsStep {
    BuildStep items(List<SoundData> items);
  }
  

  public interface BuildStep {
    ModelSoundDataConnection build();
    BuildStep nextToken(String nextToken);
  }
  

  public static class Builder implements ItemsStep, BuildStep {
    private List<SoundData> items;
    private String nextToken;
    @Override
     public ModelSoundDataConnection build() {
        
        return new ModelSoundDataConnection(
          items,
          nextToken);
    }
    
    @Override
     public BuildStep items(List<SoundData> items) {
        Objects.requireNonNull(items);
        this.items = items;
        return this;
    }
    
    @Override
     public BuildStep nextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(List<SoundData> items, String nextToken) {
      super.items(items)
        .nextToken(nextToken);
    }
    
    @Override
     public CopyOfBuilder items(List<SoundData> items) {
      return (CopyOfBuilder) super.items(items);
    }
    
    @Override
     public CopyOfBuilder nextToken(String nextToken) {
      return (CopyOfBuilder) super.nextToken(nextToken);
    }
  }
  
}
