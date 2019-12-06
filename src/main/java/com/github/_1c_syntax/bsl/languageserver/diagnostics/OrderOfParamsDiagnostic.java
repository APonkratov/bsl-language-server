/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2019
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
package com.github._1c_syntax.bsl.languageserver.diagnostics;

import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticInfo;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticMetadata;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticSeverity;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticTag;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticType;
import com.github._1c_syntax.bsl.parser.BSLParser.ParamContext;
import com.github._1c_syntax.bsl.parser.BSLParser.ParamListContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Objects;

@DiagnosticMetadata(
  type = DiagnosticType.CODE_SMELL,
  severity = DiagnosticSeverity.MAJOR,
  minutesToFix = 30,
  tags = {
    DiagnosticTag.STANDARD,
    DiagnosticTag.DESIGN
  }
)
public class OrderOfParamsDiagnostic extends AbstractVisitorDiagnostic {

  public OrderOfParamsDiagnostic(DiagnosticInfo info) {
    super(info);
  }

  @Override
  public ParseTree visitParamList(ParamListContext ctx) {

    boolean hasDefaultBetweenRequired = ctx.param().stream()
      .map(ParamContext::defaultValue)
      .dropWhile(Objects::isNull)
      .anyMatch(Objects::isNull);
    if (hasDefaultBetweenRequired) {
      diagnosticStorage.addDiagnostic(ctx);
    }
    return ctx;
  }

}
