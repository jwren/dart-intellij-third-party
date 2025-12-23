package com.jetbrains.lang.dart.ide.index;

import com.intellij.lexer.Lexer;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.search.IndexPatternBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.jetbrains.lang.dart.DartParserDefinition;
import com.jetbrains.lang.dart.DartTokenTypesSets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DartIndexPatternBuilder implements IndexPatternBuilder {
  @Override
  public @Nullable Lexer getIndexingLexer(@NotNull PsiFile file) {
    return new DartParserDefinition().createLexer(file.getProject());
  }

  @Override
  public @Nullable TokenSet getCommentTokenSet(@NotNull PsiFile file) {
    return DartTokenTypesSets.COMMENTS;
  }

  @Override
  public int getCommentStartDelta(IElementType tokenType) {
    if (tokenType == DartTokenTypesSets.MULTI_LINE_DOC_COMMENT) return 3;
    if (tokenType == DartTokenTypesSets.MULTI_LINE_COMMENT) return 2;
    if (tokenType == DartTokenTypesSets.SINGLE_LINE_DOC_COMMENT) return 3;
    if (tokenType == DartTokenTypesSets.SINGLE_LINE_COMMENT) return 2;
    return 0;
  }

  @Override
  public int getCommentEndDelta(IElementType tokenType) {
    if (tokenType == DartTokenTypesSets.MULTI_LINE_DOC_COMMENT || tokenType == DartTokenTypesSets.MULTI_LINE_COMMENT) {
      return 2;
    }
    return 0;
  }
}
