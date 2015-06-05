/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.spring.util;

/**
 * Utility methods for working with classes and class members.
 *
 * @author Petter Holmström (petter@vaadin.com)
 */
public final class ClassUtils {

    private ClassUtils() {
    }

    /**
     * Visitor interface used with {@link org.vaadin.spring.util.ClassUtils#visitClassHierarchy(org.vaadin.spring.util.ClassUtils.ClassVisitor, Class)}.
     */
    public interface ClassVisitor {
        void visit(Class<?> clazz);
    }

    /**
     * Visits the entire class hierarchy from {@code subClass} to {@code Object}.
     *
     * @param visitor  the visitor to use, must not be {@code null}.
     * @param subClass the class whose hierarchy is to be visited, must not be {@code null}.
     */
    public static void visitClassHierarchy(ClassVisitor visitor, Class<?> subClass) {
        Class<?> visitedClass = subClass;
        while (visitedClass.getSuperclass() != null) {
            visitor.visit(visitedClass);
            visitedClass = visitedClass.getSuperclass();
        }
    }
}
