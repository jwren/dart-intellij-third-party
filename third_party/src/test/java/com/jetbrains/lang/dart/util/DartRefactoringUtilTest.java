package com.jetbrains.lang.dart.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.lang.dart.DartCodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.psi.DartExpression;
import java.util.Set;

public class DartRefactoringUtilTest extends DartCodeInsightFixtureTestCase {

  public void testCollectUsedNames() {
    myFixture.configureByText("foo.dart", "main() { var a = 1; var b = 2; print(a + b); }");
    int offset = myFixture.getFile().getText().indexOf("print");
    PsiElement element = myFixture.getFile().findElementAt(offset);
    Set<String> names = DartRefactoringUtil.collectUsedNames(element);
    assertTrue(names.contains("a"));
    assertTrue(names.contains("b"));
  }

  public void testFindExpressionInRange() {
    myFixture.configureByText("foo.dart", "main() { var a = 1 + 2; }");
    PsiFile file = myFixture.getFile();
    String text = file.getText();
    int start = text.indexOf("1 + 2");
    int end = start + "1 + 2".length();
    
    DartExpression expr = DartRefactoringUtil.findExpressionInRange(file, start, end);
    assertNotNull(expr);
    assertEquals("1 + 2", expr.getText());
  }
}
