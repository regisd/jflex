package de.jflex.util.collect;

import com.google.common.primitives.Ints;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * A set of primitive integer.
 *
 * <p>Efficient if the set is small. Prefer {@code HashSet<Integer>} otherwise.
 */
public final class IntSet {
  int[] data = new int[3];
  int size = 0;
  boolean sorted = false;

  public void put(int value) {
    if (contains(value)) {
      return;
    }
    size++;
    data = Ints.ensureCapacity(data, size, 4);
    data[ /*actualSize*/size - 1] = value;
    sorted = false;
  }

  private boolean contains(int value) {
    return stream().anyMatch(v -> v == value);
  }

  public int size() {
    return size;
  }

  public int smallestValue() {
    if (size == 0) {
      throw new IllegalStateException("Set has no data");
    }
    if (!sorted) {
      sort();
    }
    return data[0];
  }

  public void sort() {
    Arrays.sort(data, 0, size);
    sorted = true;
  }

  public IntStream stream() {
    return Arrays.stream(data, 0, size);
  }
}
