package com.amplifyframework.datastore.generated.model;


import androidx.core.util.ObjectsCompat;

import java.util.Objects;
import java.util.List;

/** This is an auto generated class representing the ModelNoteDataConnection type in your schema. */
public final class ModelNoteDataConnection {
  private final List<NoteData> items;
  private final String nextToken;
  public List<NoteData> getItems() {
      return items;
  }
  
  public String getNextToken() {
      return nextToken;
  }
  
  private ModelNoteDataConnection(List<NoteData> items, String nextToken) {
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
      ModelNoteDataConnection modelNoteDataConnection = (ModelNoteDataConnection) obj;
      return ObjectsCompat.equals(getItems(), modelNoteDataConnection.getItems()) &&
              ObjectsCompat.equals(getNextToken(), modelNoteDataConnection.getNextToken());
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
    BuildStep items(List<NoteData> items);
  }
  

  public interface BuildStep {
    ModelNoteDataConnection build();
    BuildStep nextToken(String nextToken);
  }
  

  public static class Builder implements ItemsStep, BuildStep {
    private List<NoteData> items;
    private String nextToken;
    @Override
     public ModelNoteDataConnection build() {
        
        return new ModelNoteDataConnection(
          items,
          nextToken);
    }
    
    @Override
     public BuildStep items(List<NoteData> items) {
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
    private CopyOfBuilder(List<NoteData> items, String nextToken) {
      super.items(items)
        .nextToken(nextToken);
    }
    
    @Override
     public CopyOfBuilder items(List<NoteData> items) {
      return (CopyOfBuilder) super.items(items);
    }
    
    @Override
     public CopyOfBuilder nextToken(String nextToken) {
      return (CopyOfBuilder) super.nextToken(nextToken);
    }
  }
  
}
