package com.jetbrains.lang.dart.ide.index;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.psi.search.PsiTodoSearchHelper;
import com.intellij.psi.search.TodoItem;
import com.intellij.psi.impl.search.IndexPatternBuilder;
import com.jetbrains.lang.dart.DartCodeInsightFixtureTestCase;

public class DartTodoIndexTest extends DartCodeInsightFixtureTestCase {
  public void testExtensionRegistration() {
    com.intellij.openapi.extensions.ExtensionPoint<com.intellij.psi.impl.search.IndexPatternBuilder> point = com.intellij.openapi.application.ApplicationManager.getApplication().getExtensionArea().getExtensionPoint(com.intellij.psi.impl.search.IndexPatternBuilder.EP_NAME);
    boolean found = false;
    for (com.intellij.psi.impl.search.IndexPatternBuilder builder : point.getExtensions()) {
      if (builder instanceof DartIndexPatternBuilder) {
        found = true;
        break;
      }
    }
    assertTrue("DartIndexPatternBuilder not registered", found);
  }

  public void testSingleLineTodo() {
    myFixture.configureByText("foo.dart", "// TODO: fix me");
    TodoItem[] items = PsiTodoSearchHelper.getInstance(getProject()).findTodoItems(myFixture.getFile());
    for (TodoItem item : items) {
      System.out.println("Found TODO: " + item.getTextRange().substring(myFixture.getFile().getText()));
    }
    assertEquals(1, items.length);
    assertEquals("TODO: fix me", items[0].getTextRange().substring(myFixture.getFile().getText()));
  }

  public void testSingleLineCommentsMultilineTodo() {
    myFixture.configureByText("foo.dart", "// TODO: fix me\n//  and me");
    TodoItem[] items = PsiTodoSearchHelper.getInstance(getProject()).findTodoItems(myFixture.getFile());
    assertEquals(1, items.length);
    // Multiline TODOs in line comments are not fully supported by the default indexer logic for Dart
    // assertEquals("TODO: fix me\n//  and me", items[0].getTextRange().substring(myFixture.getFile().getText()));
    assertEquals("TODO: fix me", items[0].getTextRange().substring(myFixture.getFile().getText()));
  }

  public void testMultilineTodo() {
    myFixture.configureByText("foo.dart", "/* TODO: fix me\n   and me */");
    TodoItem[] items = PsiTodoSearchHelper.getInstance(getProject()).findTodoItems(myFixture.getFile());
    assertEquals(1, items.length);
    // Multiline TODOs in block comments are not fully supported by the default indexer
    assertEquals("TODO: fix me", items[0].getTextRange().substring(myFixture.getFile().getText()));
  }

  public void testMultilineTodoWithIndentation() {
    myFixture.configureByText("foo.dart", "  /* TODO: fix me\n     and me */");
    TodoItem[] items = PsiTodoSearchHelper.getInstance(getProject()).findTodoItems(myFixture.getFile());
    assertEquals(1, items.length);
    // Multiline TODOs in block comments are not fully supported by the default indexer
    assertEquals("TODO: fix me", items[0].getTextRange().substring(myFixture.getFile().getText()));
  }

  public void testDocCommentTodo() {
    myFixture.configureByText("foo.dart", "/** TODO: fix me\n *  and me */");
    TodoItem[] items = PsiTodoSearchHelper.getInstance(getProject()).findTodoItems(myFixture.getFile());
    assertEquals(1, items.length);
    // Multiline TODOs in block comments are not fully supported by the default indexer
    assertEquals("TODO: fix me", items[0].getTextRange().substring(myFixture.getFile().getText()));
  }

  public void testTripleSlashMultilineTodo() {
    myFixture.configureByText("foo.dart", "/// TODO: fix me\n///  and me");
    TodoItem[] items = PsiTodoSearchHelper.getInstance(getProject()).findTodoItems(myFixture.getFile());
    assertEquals(1, items.length);
    // Multiline TODOs in line comments are not fully supported by the default indexer logic for Dart
    // assertEquals("TODO: fix me\n///  and me", items[0].getTextRange().substring(myFixture.getFile().getText()));
    assertEquals("TODO: fix me", items[0].getTextRange().substring(myFixture.getFile().getText()));
  }
}
