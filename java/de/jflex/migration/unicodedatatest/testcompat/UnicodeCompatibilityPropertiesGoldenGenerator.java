/*
 * Copyright (C) 2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.jflex.migration.unicodedatatest.testcompat;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.scanner.UcdScanner;
import de.jflex.ucd_generator.scanner.UcdScannerException;
import de.jflex.ucd_generator.ucd.UnicodeData;

public class UnicodeCompatibilityPropertiesGoldenGenerator
    extends AbstractGenerator<UnicodeCompatibilityPropertiesGoldenTemplateVars> {

  private static final String TEMPLATE_NAME = "UnicodeCompatibilityPropertiesGolden";

  private final UcdVersion ucdVersion;
  private final String propName;

  protected UnicodeCompatibilityPropertiesGoldenGenerator(
      UnicodeVersion unicodeVersion, UcdVersion ucdVersion, String propName) {
    super(TEMPLATE_NAME, unicodeVersion);
    this.ucdVersion = ucdVersion;
    this.propName = propName;
  }

  @Override
  protected UnicodeCompatibilityPropertiesGoldenTemplateVars createTemplateVars() {
    UnicodeCompatibilityPropertiesGoldenTemplateVars vars =
        new UnicodeCompatibilityPropertiesGoldenTemplateVars();
    vars.templateName = TEMPLATE_NAME;
    vars.className =
        "UnicodeCompatibilityProperties_" + propName + "_" + unicodeVersion.underscoreVersion();
    try {
      vars.ranges = findCompatibilyRanges();
    } catch (UcdScannerException e) {
      throw new IllegalArgumentException(e);
    }
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeCompatibilityPropertiesGoldenTemplateVars vars) {
    return vars.className + ".output";
  }

  private ImmutableList<CodepointRange> findCompatibilyRanges() throws UcdScannerException {
    UcdScanner scanner = new UcdScanner(ucdVersion);
    UnicodeData unicodeData = scanner.scan();
    return unicodeData.getPropertyValueIntervals(propName);
  }
}