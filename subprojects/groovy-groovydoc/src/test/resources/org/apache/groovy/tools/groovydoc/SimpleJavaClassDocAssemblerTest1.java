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

/**
 * This a class doc
 */
public class SimpleJavaClassDocAssemblerTest1 {
    /**
     * The greeting word
     */
    public static final String HELLO = "hello";

    /**
     * constructor of SimpleJavaClassDocAssemblerTest1
     */
    public SimpleJavaClassDocAssemblerTest1() {
    }

    /**
     * This an inner class doc
     */
    public static class SomeInnerClass {
        /**
         * The greeting word of inner class
         */
        public static final String INNER_HELLO = "hello";

        /**
         * constructor of SomeInnerClass
         */
        public SomeInnerClass() {}

        /**
         * say hello from inner class
         *
         * @param name some name
         * @return the greeting words
         */
        public String innerHello(String name) {
            return HELLO + "," + name;
        }
    }

    /**
     * say hello
     *
     * @param name some name
     * @return the greeting words
     */
    public String hello(String name) {
        return HELLO + "," + name;
    }
}

/**
 * some class
 */
class SomeClass {

}

/**
 * some enum
 */
enum SomeEnum {

}
