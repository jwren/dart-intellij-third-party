package com.jetbrains.lang.dart.sdk;

import com.intellij.openapi.util.io.FileUtil;
import com.jetbrains.lang.dart.DartCodeInsightFixtureTestCase;
import java.io.File;
import java.io.IOException;

public class DartSdkUtilTest extends DartCodeInsightFixtureTestCase {

  public void testGetSdkVersion() throws IOException {
    File sdkHome = FileUtil.createTempDirectory("dart-sdk", null);
    File versionFile = new File(sdkHome, "version");
    FileUtil.writeToFile(versionFile, "1.2.3");
    
    assertEquals("1.2.3", DartSdkUtil.getSdkVersion(sdkHome.getAbsolutePath()));
    
    FileUtil.delete(sdkHome);
  }

  public void testIsDartSdkHome() throws IOException {
    File sdkHome = FileUtil.createTempDirectory("dart-sdk", null);
    File binDir = new File(sdkHome, "bin");
    binDir.mkdirs();
    File libCoreDir = new File(sdkHome, "lib/core");
    libCoreDir.mkdirs();
    new File(libCoreDir, "core.dart").createNewFile();

    assertTrue(DartSdkUtil.isDartSdkHome(sdkHome.getAbsolutePath()));
    assertFalse(DartSdkUtil.isDartSdkHome(new File(sdkHome, "bin").getAbsolutePath()));
    
    FileUtil.delete(sdkHome);
  }

  public void testGetErrorMessageIfWrongSdkRootPath() throws IOException {
    assertEquals("Error: a path to the Dart SDK is not specified.", DartSdkUtil.getErrorMessageIfWrongSdkRootPath(""));
    assertEquals("Error: the folder specified as the Dart SDK home does not exist.", DartSdkUtil.getErrorMessageIfWrongSdkRootPath("/non/existent/path"));
    
    File emptyDir = FileUtil.createTempDirectory("empty", null);
    assertEquals("Error: Dart SDK is not found in the specified location.", DartSdkUtil.getErrorMessageIfWrongSdkRootPath(emptyDir.getAbsolutePath()));
    FileUtil.delete(emptyDir);
  }
}
