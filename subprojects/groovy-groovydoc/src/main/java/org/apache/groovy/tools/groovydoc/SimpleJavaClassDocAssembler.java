/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.groovy.tools.groovydoc;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.tools.groovydoc.LinkArgument;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class SimpleJavaClassDocAssembler extends VoidVisitorAdapter<Object> {
    private final String packagePath;
    private final String file;
    private final String javaSourceContent;
    private final String className;
    private final List<LinkArgument> links;
    private final Properties properties;
    private final Map<String, GroovyClassDoc> classDocs = new LinkedHashMap<>();

    public SimpleJavaClassDocAssembler(String packagePath, String file, String javaSourceContent, List<LinkArgument> links, Properties properties) {
        this.packagePath = packagePath;
        this.file = file;
        this.javaSourceContent = javaSourceContent;
        this.links = links;
        this.properties = properties;

        if (file != null && file.contains(".")) {
            // todo: replace this simple idea of default class name
            int idx = file.lastIndexOf(".");
            className = file.substring(0, idx);
        } else {
            className = file;
        }
    }

    public Map<String, GroovyClassDoc> assemble() {
        this.visit(JavaParser.parse(javaSourceContent), null);
        return classDocs;
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration n, final Object arg) {
        processJavadoc(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(final MethodDeclaration n, final Object arg) {
        processJavadoc(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(final ConstructorDeclaration n, final Object arg) {
        processJavadoc(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(final FieldDeclaration n, final Object arg) {
        processJavadoc(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(final EnumDeclaration n, final Object arg) {
        processJavadoc(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(final EnumConstantDeclaration n, final Object arg) {
        processJavadoc(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(final AnnotationDeclaration n, final Object arg) {
        processJavadoc(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(final AnnotationMemberDeclaration n, final Object arg) {
        processJavadoc(n);
        super.visit(n, arg);
    }

    private void processJavadoc(NodeWithJavadoc n) {
        Optional<Javadoc> optionalJavadoc = n.getJavadoc();

        if (!optionalJavadoc.isPresent()) {
            return;
        }

        System.out.println(optionalJavadoc.get().getDescription().toText());
    }
}