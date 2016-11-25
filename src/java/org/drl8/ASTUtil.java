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
import org.extendj.ast.FieldDecl;
import org.extendj.ast.FieldDeclarator;
import org.extendj.ast.ParTypeAccess;
import org.extendj.ast.TypeAccess;
import org.extendj.ast.TypeDecl;

public class ASTUtil {

    public static FieldDecl findField( TypeDecl type, String name ) {
        for ( BodyDecl body : type.getBodyDeclList() ) {
            if (body.isField()) {
                for ( FieldDeclarator fDec : ( (FieldDecl) body ).getDeclaratorList()) {
                    if (fDec.getID().equals( name )) {
                        return ( (FieldDecl) body );
                    }
                }
            }
        }
        return null;
    }

    public static TypeAccess getGenericType(FieldDecl field, int i) {
        return (TypeAccess) ((ParTypeAccess) field.getTypeAccess()).getTypeArgument( i );
    }
}
