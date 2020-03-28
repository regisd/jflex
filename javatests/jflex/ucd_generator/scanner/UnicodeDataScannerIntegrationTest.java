package jflex.ucd_generator.scanner;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.ucd.UcdVersion;
import org.junit.Test;

/** Integration test for {@link UnicodeDataScanner}, generated by jflex. */
public class UnicodeDataScannerIntegrationTest {

  @Test
  public void scanUnicode_4_1() throws Exception {
    UnicodeData unicodeData = scan("ucd_4_1_0", "4.1.0");
    assertThat(unicodeData.maximumCodePoint()).isEqualTo(0x10ffff);
    assertThat(unicodeData.uniqueCaselessMatchPartitions()).isNotEmpty();
    assertThat(unicodeData.maxCaselessMatchPartitionSize()).isEqualTo(4);
  }

  @Test
  public void scanUnicode_10() throws Exception {
    UnicodeData unicodeData = scan("ucd_10", "10.0");
    assertThat(unicodeData.maximumCodePoint()).isEqualTo(0x10ffff);
    assertThat(unicodeData.uniqueCaselessMatchPartitions()).isNotEmpty();
    assertThat(unicodeData.maxCaselessMatchPartitionSize()).isEqualTo(4);
  }

  private static UnicodeData scan(final String ucdBazelTarget, final String versionName)
      throws IOException {
    File file = new File(new File("external/" + ucdBazelTarget), "UnicodeData.txt");
    if (!file.exists()) {
      throw new FileNotFoundException(
          "Missing test data (Unicode " + versionName + "): " + file.getAbsolutePath());
    }
    UcdVersion ucdVersion = UcdVersion.builder().setVersion(versionName).build();
    UnicodeData unicodeData = new UnicodeData(ucdVersion.version());
    UnicodeDataScanner scanner =
        new UnicodeDataScanner(Files.newReader(file, StandardCharsets.UTF_8), ucdVersion, unicodeData);
    scanner.scan();
    return unicodeData;
  }
}
