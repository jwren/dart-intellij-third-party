package com.jetbrains.lang.dart.util;

import com.jetbrains.lang.dart.DartCodeInsightFixtureTestCase;

import com.intellij.psi.PsiFile;
import com.jetbrains.lang.dart.psi.DartFile;
import java.util.Collections;

public class DartResolveUtilTest extends DartCodeInsightFixtureTestCase {

  public void testGetLibraryName() {
    myFixture.configureByText("lib.dart", "library my.lib;");
    assertEquals("my.lib", DartResolveUtil.getLibraryName(myFixture.getFile()));
    
    myFixture.configureByText("part.dart", "part of my.lib;");
    assertEquals("my.lib", DartResolveUtil.getLibraryName(myFixture.getFile()));
    
    myFixture.configureByText("file.dart", "");
    assertEquals("file.dart", DartResolveUtil.getLibraryName(myFixture.getFile()));
  }

  public void testIsLibraryRoot() {
    myFixture.configureByText("lib.dart", "library my.lib;");
    assertTrue(DartResolveUtil.isLibraryRoot((DartFile) myFixture.getFile()));

    myFixture.configureByText("part.dart", "part of my.lib;");
    assertFalse(DartResolveUtil.isLibraryRoot((DartFile) myFixture.getFile()));

    myFixture.configureByText("file.dart", "");
    assertTrue(DartResolveUtil.isLibraryRoot((DartFile) myFixture.getFile()));
  }

  public void testFindLibrary() {
    PsiFile lib = myFixture.configureByText("lib.dart", "library my.lib; part 'part.dart';");
    PsiFile part = myFixture.addFileToProject("part.dart", "part of my.lib;");

    assertEquals(Collections.singletonList(lib.getVirtualFile()), DartResolveUtil.findLibrary(lib));
    assertEquals(Collections.singletonList(lib.getVirtualFile()), DartResolveUtil.findLibrary(part));
  }

  public void testSameLibrary() {
    PsiFile lib = myFixture.configureByText("lib.dart", "library my.lib; part 'part.dart';");
    PsiFile part = myFixture.addFileToProject("part.dart", "part of my.lib;");
    PsiFile other = myFixture.addFileToProject("other.dart", "");

    assertTrue(DartResolveUtil.sameLibrary(lib, part));
    assertTrue(DartResolveUtil.sameLibrary(part, lib));
    assertFalse(DartResolveUtil.sameLibrary(lib, other));
  }
}
