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
package org.codehaus.groovy.tools.groovydoc;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import org.codehaus.groovy.antlr.AntlrASTProcessor;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.antlr.java.Groovifier;
import org.codehaus.groovy.antlr.java.Java2GroovyConverter;
import org.codehaus.groovy.antlr.java.JavaLexer;
import org.codehaus.groovy.antlr.java.JavaRecognizer;
import org.codehaus.groovy.antlr.treewalker.PreOrderTraversal;
import org.codehaus.groovy.antlr.treewalker.SourceCodeTraversal;
import org.codehaus.groovy.antlr.treewalker.Visitor;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SimpleJavaClassDocAssembler {
    private final String packagePath;
    private final String file;
    private final String javaSourceContent;
//    private final String className;
    private final List<LinkArgument> links;
    private final Properties properties;

    public SimpleJavaClassDocAssembler(String packagePath, String file, String javaSourceContent, List<LinkArgument> links, Properties properties) {
        this.packagePath = packagePath;
        this.file = file;
        this.javaSourceContent = javaSourceContent;
        this.links = links;
        this.properties = properties;
    }

    public Map<String, GroovyClassDoc> assemble() throws TokenStreamException, RecognitionException {
        SourceBuffer sourceBuffer = new SourceBuffer();
        JavaRecognizer parser = getJavaParser(javaSourceContent, sourceBuffer);
        String[] tokenNames = parser.getTokenNames();
        try {
            parser.compilationUnit();
        } catch (OutOfMemoryError e) {
            throw e;
        }
        AST ast = parser.getAST();

        // modify the Java AST into a Groovy AST (just token types)
        Visitor java2groovyConverter = new Java2GroovyConverter(tokenNames);
        AntlrASTProcessor java2groovyTraverser = new PreOrderTraversal(java2groovyConverter);
        java2groovyTraverser.process(ast);

        // now mutate (groovify) the ast into groovy
        Visitor groovifier = new Groovifier(tokenNames, false);
        AntlrASTProcessor groovifierTraverser = new PreOrderTraversal(groovifier);
        groovifierTraverser.process(ast);

        // now do the business
        Visitor visitor = new SimpleGroovyClassDocAssembler(packagePath, file, sourceBuffer, links, properties, false);
        AntlrASTProcessor traverser = new SourceCodeTraversal(visitor);

        traverser.process(ast);

        return ((SimpleGroovyClassDocAssembler) visitor).getGroovyClassDocs();
    }

    private static JavaRecognizer getJavaParser(String input, SourceBuffer sourceBuffer) {
        UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(new StringReader(input), sourceBuffer);
        JavaLexer lexer = new JavaLexer(unicodeReader);
        unicodeReader.setLexer(lexer);
        JavaRecognizer parser = JavaRecognizer.make(lexer);
        parser.setSourceBuffer(sourceBuffer);
        return parser;
    }
}
