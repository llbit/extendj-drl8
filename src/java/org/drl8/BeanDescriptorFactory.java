/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drl8;

import org.extendj.ast.BodyDecl;
import org.extendj.ast.ClassDecl;
import org.extendj.ast.CompilationUnit;
import org.extendj.ast.MemberClassDecl;
import org.extendj.ast.TypeDecl;

import java.util.HashMap;
import java.util.Map;

public class BeanDescriptorFactory {

    private final CompilationUnit unit;

    private final Map<String, BeanDescriptor> cache = new HashMap<>();

    public BeanDescriptorFactory( CompilationUnit unit ) {
        this.unit = unit;
    }

    public BeanDescriptor findBean(String name) {
        return cache.computeIfAbsent( name, this::createDescriptor );
    }

    public BeanDescriptor findBean(TypeDecl type ) {
        return cache.computeIfAbsent( type.getID(), name -> new TypeDeclBeanDescriptor( type ) );
    }

    private BeanDescriptor createDescriptor(String name) {
        for (BodyDecl body : unit.getTypeDecl( 0 ).getBodyDecls()) {
            if (body instanceof MemberClassDecl) {
                ClassDecl classDecl = ( (MemberClassDecl) body ).getClassDecl();
                if (classDecl.getID().equals( name )) {
                    return new TypeDeclBeanDescriptor( classDecl );
                }
            }
        }
        throw new RuntimeException( "TODO - type lookup out of the compilation unit not supported yet" );
    }
}
