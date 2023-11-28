package me.av306.xenon.util;

public class TypeWrapper<T>
{
  private T value;

  public TypeWrapper( T value )
  {
    this.value = value;
  }

  public void set( T value )
  {
    this.value = value;
  }

  public T get()
  {
    return this.value;
  }

  @Override
  public boolean equals( Object other )
  {
    if ( other instanceof TypeWrapper )
      // Compare the contained objects if possible
      return this.get().equals( other.get() );
    // Otherwise compare our contained object with the other object directly
    // e.g. comparing a TypeWrapper<Integer> with an int
    else return this.get().equals( other );
  }

  // TODO: IDK how to implement hashCode()

  @Override
  public String toString()
  {
    return this.get().toString();
  }
}
