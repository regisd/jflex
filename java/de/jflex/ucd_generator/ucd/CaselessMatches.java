package de.jflex.ucd_generator.ucd;

import static de.jflex.ucd_generator.util.HexaUtils.intFromHexa;
import static java.lang.Math.max;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import de.jflex.util.collect.IntSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CaselessMatches {
  /**
   * A set of code point space partitions, each containing at least two caselessly equivalent code
   * points.
   */
  public final Map<Integer, IntSet> caselessMatchPartitions = new HashMap<>();

  public int maxCaselessMatchPartitionSize() {
    int maxSize = 0;
    for (IntSet partition : caselessMatchPartitions.values()) {
      maxSize = max(maxSize, partition.size());
    }
    return maxSize;
  }

  /**
   * Returns the {@link #caselessMatchPartitions} where the key is the first element from the
   * partition.
   */
  public ImmutableCollection<IntSet> uniqueCaselessMatchPartitions() {
    ArrayList<IntSet> partitions = new ArrayList<>();
    for (Map.Entry<Integer, IntSet> entry : caselessMatchPartitions.entrySet()) {
      IntSet intset = entry.getValue();
      if (entry.getKey() == intset.smallestValue()) {
        intset.sort();
        partitions.add(intset);
      }
    }
    Comparator<IntSet> comparator = Comparator.comparingInt(IntSet::smallestValue);
    return ImmutableList.sortedCopyOf(comparator, partitions);
  }

  /**
   * Grows the partition containing the given codePoint and its caseless equivalents, if any, to
   * include all of them.
   *
   * @param codePoint The code point to include in a caselessly equivalent partition
   * @param uppercaseMapping A hex String representation of the uppercase mapping of codePoint, or
   *     {@code null} if there isn't one
   * @param lowercaseMapping A hex String representation of the lowercase mapping of codePoint, or
   *     {@code null} if there isn't one
   * @param titlecaseMapping A hex String representation of the titlecase mapping of codePoint, or
   *     {@code null} if there isn't one
   */
  public void addCaselessMatches(
      int codePoint, String uppercaseMapping, String lowercaseMapping, String titlecaseMapping) {
    if (Strings.isNullOrEmpty(uppercaseMapping)
        && Strings.isNullOrEmpty(lowercaseMapping)
        && Strings.isNullOrEmpty(titlecaseMapping)) {
      return;
    }

    List<Integer> codepoints =
        Arrays.asList(
            codePoint,
            intFromHexa(uppercaseMapping),
            intFromHexa(lowercaseMapping),
            intFromHexa(titlecaseMapping));
    IntSet partition =
        codepoints.stream()
            .filter(Objects::nonNull)
            .map(caselessMatchPartitions::get)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(new IntSet());
    for (Integer cp : codepoints) {
      if (cp != null) {
        partition.put(cp);
        caselessMatchPartitions.put(cp, partition);
      }
    }
  }
}
