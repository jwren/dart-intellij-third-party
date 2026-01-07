package com.jetbrains.lang.dart.util;

import com.jetbrains.lang.dart.DartCodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.psi.DartExpression;
import java.util.Collection;

public class DartNameSuggesterUtilTest extends DartCodeInsightFixtureTestCase {

  public void testGenerateNames() {
    assertContainsElements(DartNameSuggesterUtil.generateNames("MyClass"), "myClass", "class");
    assertContainsElements(DartNameSuggesterUtil.generateNames("isGood"), "good");
    assertContainsElements(DartNameSuggesterUtil.generateNames("getFoo"), "foo");
    assertContainsElements(DartNameSuggesterUtil.generateNames("foo_bar"), "fooBar", "bar");
  }

  public void testGetSuggestedNames() {
    myFixture.configureByText("foo.dart", "main() { var a = 1; }");
    int offset = myFixture.getFile().getText().indexOf("1");
    // We assume findExpressionInRange works (tested previously)
    DartExpression expr = DartRefactoringUtil.findExpressionInRange(myFixture.getFile(), offset, offset + 1);
    assertNotNull(expr);
    Collection<String> names = DartNameSuggesterUtil.getSuggestedNames(expr);
    assertNotEmpty(names);
  }
}
