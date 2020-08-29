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
package com.github._1c_syntax.bsl.languageserver;

import com.github._1c_syntax.bsl.languageserver.configuration.LanguageServerConfiguration;
import com.github._1c_syntax.bsl.languageserver.context.ServerContext;
import com.github._1c_syntax.bsl.languageserver.diagnostics.metadata.DiagnosticInfo;
import lombok.Getter;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

@SpringBootApplication(
  scanBasePackages = {"com.github._1c_syntax.bsl.languageserver"}
)
public class BSLLSBinding {

  @Getter(lazy = true)
  private static final ApplicationContext applicationContext = createContext();

  public BSLLSBinding() {
    // public constructor is needed for spring initialization
  }

  @SuppressWarnings("unchecked")
  public static Collection<DiagnosticInfo> getDiagnosticInfos() {
    return (Collection<DiagnosticInfo>) getApplicationContext().getBean("diagnosticInfos", Collection.class);
  }

  public static LanguageServerConfiguration getLanguageServerConfiguration() {
    return getApplicationContext().getBean(LanguageServerConfiguration.class);
  }

  public static ServerContext getServerContext() {
    return getApplicationContext().getBean(ServerContext.class);
  }

  private static ApplicationContext createContext() {
    return new SpringApplicationBuilder(BSLLSBinding.class)
      .bannerMode(Banner.Mode.OFF)
      .web(WebApplicationType.NONE)
      .logStartupInfo(false)
      .resourceLoader(new DefaultResourceLoader(BSLLSBinding.class.getClassLoader()))
      .lazyInitialization(true)
      .properties(Map.of(
        "app.command.line.runner.enabled", "false",
        "app.scheduling.enabled", "false"
      ))
      .build()
      .run();
  }
}
