/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2020
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package com.github._1c_syntax.bsl.languageserver.context.computer;

import com.github._1c_syntax.bsl.languageserver.context.DocumentContext;
import com.github._1c_syntax.bsl.languageserver.context.symbol.MethodDescription;
import com.github._1c_syntax.bsl.languageserver.context.symbol.MethodSymbol;
import com.github._1c_syntax.bsl.languageserver.context.symbol.ParameterDefinition;
import com.github._1c_syntax.bsl.languageserver.context.symbol.annotations.Annotation;
import com.github._1c_syntax.bsl.languageserver.context.symbol.annotations.AnnotationKind;
import com.github._1c_syntax.bsl.languageserver.context.symbol.annotations.AnnotationParameterDefinition;
import com.github._1c_syntax.bsl.languageserver.context.symbol.annotations.CompilerDirectiveKind;
import com.github._1c_syntax.bsl.languageserver.utils.Ranges;
import com.github._1c_syntax.bsl.languageserver.utils.Trees;
import com.github._1c_syntax.bsl.parser.BSLParser;
import com.github._1c_syntax.bsl.parser.BSLParserBaseVisitor;
import com.github._1c_syntax.bsl.parser.BSLParserRuleContext;
import com.github._1c_syntax.mdclasses.mdo.MDObjectBase;
import com.github._1c_syntax.mdclasses.metadata.additional.MDOReference;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class MethodSymbolComputer
  extends BSLParserBaseVisitor<ParseTree>
  implements Computer<List<MethodSymbol>> {

  private static final Set<Integer> SPECIAL_COMPILER_DIRECTIVES_TOKEN_TYPES = Set.of(
    BSLParser.ANNOTATION_ATSERVERNOCONTEXT_SYMBOL,
    BSLParser.ANNOTATION_ATCLIENTATSERVER_SYMBOL);

  private final DocumentContext documentContext;
  private final List<MethodSymbol> methods = new ArrayList<>();

  public MethodSymbolComputer(DocumentContext documentContext) {
    this.documentContext = documentContext;
  }

  @Override
  public List<MethodSymbol> compute() {
    methods.clear();
    visitFile(documentContext.getAst());
    return new ArrayList<>(methods);
  }

  @Override
  public ParseTree visitFunction(BSLParser.FunctionContext ctx) {
    BSLParser.FuncDeclarationContext declaration = ctx.funcDeclaration();

    TerminalNode startNode = declaration.FUNCTION_KEYWORD();
    TerminalNode stopNode = ctx.ENDFUNCTION_KEYWORD();

    if (startNode == null
      || startNode instanceof ErrorNode
      || stopNode == null
      || stopNode instanceof ErrorNode
    ) {
      return ctx;
    }

    MethodSymbol methodSymbol = createMethodSymbol(
      startNode,
      stopNode,
      declaration.subName().getStart(),
      declaration.paramList(),
      true,
      declaration.EXPORT_KEYWORD() != null,
      getCompilerDirective(declaration.compilerDirective()),
      getAnnotations(declaration.annotation()));

    methods.add(methodSymbol);

    return ctx;
  }

  @Override
  public ParseTree visitProcedure(BSLParser.ProcedureContext ctx) {
    BSLParser.ProcDeclarationContext declaration = ctx.procDeclaration();

    TerminalNode startNode = declaration.PROCEDURE_KEYWORD();
    TerminalNode stopNode = ctx.ENDPROCEDURE_KEYWORD();

    if (startNode == null
      || startNode instanceof ErrorNode
      || stopNode == null
      || stopNode instanceof ErrorNode
    ) {
      return ctx;
    }

    MethodSymbol methodSymbol = createMethodSymbol(
      startNode,
      stopNode,
      declaration.subName().getStart(),
      declaration.paramList(),
      false,
      declaration.EXPORT_KEYWORD() != null,
      getCompilerDirective(declaration.compilerDirective()),
      getAnnotations(declaration.annotation())
    );

    methods.add(methodSymbol);

    return ctx;
  }

  // есть определенные предпочтения при использовании &НаКлиентеНаСервереБезКонтекста в модуле упр.формы
  // при ее использовании с другой директивой будет использоваться именно она
  // например, порядок 1
  //&НаКлиентеНаСервереБезКонтекста
  //&НаСервереБезКонтекста
  //показывает Сервер в отладчике и доступен серверный объект ТаблицаЗначений
  // или порядок 2
  //&НаСервереБезКонтекста
  //&НаКлиентеНаСервереБезКонтекста
  //аналогично
  //т.е. порядок этих 2х директив не важен, все равно используется &НаКлиентеНаСервереБезКонтекста.
  // проверял на 8.3.15

  // есть определенные предпочтения при использовании &НаКлиентеНаСервере в модуле команды
  // при ее использовании с другой директивой будет использоваться именно она
  //  проверял на 8.3.15
  //  порядок
  //  1
  //  &НаКлиентеНаСервере
  //  &НаКлиенте
  //  вызывает клиент при вызове метода с клиента
  //  вызывает сервер при вызове метода с сервера
  //  2
  //  &НаКлиенте
  //  &НаКлиентеНаСервере
  //  вызывает клиент при вызове метода с клиента
  //  вызывает сервер при вызове метода с сервера

  private static Optional<CompilerDirectiveKind> getCompilerDirective(
    List<? extends BSLParser.CompilerDirectiveContext> compilerDirectiveContexts
  ) {
    if (compilerDirectiveContexts.isEmpty()) {
      return Optional.empty();
    }
    var tokenType = compilerDirectiveContexts.stream()
      .map(compilerDirectiveContext -> compilerDirectiveContext.getStop().getType())
      .filter(SPECIAL_COMPILER_DIRECTIVES_TOKEN_TYPES::contains)
      .findAny()
      .orElseGet(() -> compilerDirectiveContexts.get(0).getStop().getType());

    return CompilerDirectiveKind.of(tokenType);

  }

  private MethodSymbol createMethodSymbol(
    TerminalNode startNode,
    TerminalNode stopNode,
    Token subName,
    BSLParser.ParamListContext paramList,
    boolean function,
    boolean export,
    Optional<CompilerDirectiveKind> compilerDirective,
    List<Annotation> annotations
  ) {
    Optional<MethodDescription> description = createDescription(startNode.getSymbol());
    boolean deprecated = description
      .map(MethodDescription::isDeprecated)
      .orElse(false);

    String mdoRef = documentContext.getMdObject()
      .map(MDObjectBase::getMdoReference)
      .map(MDOReference::getMdoRef)
      .orElse("");

    return MethodSymbol.builder()
      .name(subName.getText())
      .range(Ranges.create(startNode, stopNode))
      .subNameRange(Ranges.create(subName))
      .function(function)
      .export(export)
      .description(description)
      .deprecated(deprecated)
      .mdoRef(mdoRef)
      .parameters(createParameters(paramList))
      .compilerDirectiveKind(compilerDirective)
      .annotations(annotations)
      .build();
  }

  private Optional<MethodDescription> createDescription(Token token) {
    List<Token> comments = Trees.getComments(documentContext.getTokens(), token);
    if (comments.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(new MethodDescription(comments));
  }

  private static List<ParameterDefinition> createParameters(@Nullable BSLParser.ParamListContext paramList) {
    if (paramList == null) {
      return Collections.emptyList();
    }

    return paramList.param().stream()
      .map(param ->
        ParameterDefinition.builder()
          .name(getParameterName(param.IDENTIFIER()))
          .byValue(param.VAL_KEYWORD() != null)
          .optional(param.defaultValue() != null)
          .build()
      ).collect(Collectors.toList());
  }

  private static String getParameterName(TerminalNode identifier) {
    return Optional.ofNullable(identifier)
      .map(ParseTree::getText)
      .orElse("<UNKNOWN_IDENTIFIER>");
  }

  private static List<Annotation> getAnnotations(List<? extends BSLParser.AnnotationContext> annotationContext) {
    if (annotationContext.isEmpty()) {
      return Collections.emptyList();
    }
    return annotationContext.stream()
      .map(annotation -> createAnnotation(
        annotation.annotationName(),
        annotation.getStop().getType(),
        annotation.annotationParams()))
      .collect(Collectors.toList());
  }

  private static Annotation createAnnotation(BSLParser.AnnotationNameContext annotationNameContext, int type,
                                             BSLParser.AnnotationParamsContext annotationParamsContext) {
    final List<AnnotationParameterDefinition> params;
    if (annotationParamsContext == null) {
      params = Collections.emptyList();
    } else {
      params = annotationParamsContext.annotationParam().stream()
        .map(MethodSymbolComputer::getAnnotationParam)
        .collect(Collectors.toList());
    }

    return Annotation.builder()
      .name(annotationNameContext.getText())
      .kind(AnnotationKind.of(type))
      .parameters(params)
      .build();
  }

  private static AnnotationParameterDefinition getAnnotationParam(BSLParser.AnnotationParamContext o) {
    var name = Optional.ofNullable(o.annotationParamName())
      .map(BSLParserRuleContext::getText)
      .orElse("");
    var value = Optional.ofNullable(o.constValue())
      .map(BSLParserRuleContext::getText)
      .map(MethodSymbolComputer::excludeTrailingQuotes)
      .orElse("");
    var optional = o.constValue() != null;

    return new AnnotationParameterDefinition(name, value, optional);
  }

  private static String excludeTrailingQuotes(String text) {
    if (text.length() > 2 && text.charAt(0) == '\"') {
      return text.substring(1, text.length() - 1);
    }
    return text;
  }
}
