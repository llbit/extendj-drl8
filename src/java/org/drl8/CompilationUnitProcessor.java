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

import org.extendj.ast.Access;
import org.extendj.ast.Binary;
import org.extendj.ast.Block;
import org.extendj.ast.ClassDecl;
import org.extendj.ast.CompilationUnit;
import org.extendj.ast.Constraint;
import org.extendj.ast.Expr;
import org.extendj.ast.List;
import org.extendj.ast.MethodDecl;
import org.extendj.ast.Modifier;
import org.extendj.ast.Modifiers;
import org.extendj.ast.OOPathChunk;
import org.extendj.ast.Opt;
import org.extendj.ast.PatternDecl;
import org.extendj.ast.PrimitiveTypeAccess;
import org.extendj.ast.RuleDecl;
import org.extendj.ast.ThenDecl;
import org.extendj.ast.TypeDecl;
import org.extendj.ast.VarAccess;

public class CompilationUnitProcessor {
    private final CompilationUnit unit;
    private final BeanDescriptorFactory beanLookup;

    public CompilationUnitProcessor( CompilationUnit unit ) {
        this.unit = unit;
        this.beanLookup = new BeanDescriptorFactory( unit );
    }

    public static void processCompilationUnit( CompilationUnit unit ) {
        new CompilationUnitProcessor( unit ).process();
    }

    private void process() {
        ClassDecl unitClass = (ClassDecl)unit.getTypeDecl( 0 );
        for (RuleDecl rule : unit.getRules()) {
            processRule( unitClass, rule );
        }
    }

    private void processRule( ClassDecl unitClass, RuleDecl rule ) {
        processPatterns( unitClass, rule );
        processConsequence( rule );
    }

    private void processPatterns( ClassDecl unitClass, RuleDecl rule ) {
        if (!rule.hasWhenDecl()) {
            return;
        }

        int patternCounter = 0;
        for ( PatternDecl pattern : rule.getWhenDecl().getPatternDecls() ) {
            int chunkCounter = 0;
            for (OOPathChunk ooPathChunk : pattern.getPatternExpr().getOOPathExpr().getOOPathChunks()) {

//                String chunkVar = ooPathChunk.getVar(); // data
//                FieldDecl field = findField( unitClass, chunkVar ); // List<Person> data
//                TypeAccess itemType = getGenericType( field, 0 ); // Person
//                BeanDescriptor beanDescr = beanLookup.findBean( itemType.getID() );

                TypeDecl elementType = ooPathChunk.type().iterableElementType(); // Person
                BeanDescriptor beanDescr = beanLookup.findBean( elementType );

                int constraintCounter = 0;
                for (Constraint constraint : ooPathChunk.getConstraintList()) {
                    Binary expr = (Binary) constraint.getExpr();
                    Expr leftExpr = expr.getLeftOperand();
                    VarAccess var = (VarAccess) leftExpr;
                    var.typeName();
                }
            }

        }

//        public static boolean rule_Adult_Expr_0_0_0(Person p) {
//            return p.getAge() > 18;
//        }

    }

    private void processConsequence( RuleDecl rule ) {
        if (!rule.hasThenDecl()) {
            return;
        }

        ThenDecl then = rule.getThenDecl();
        ClassDecl containingClass = (ClassDecl)rule.getParent().getParent();

        Modifiers modifiers = new Modifiers();
        modifiers.addModifier(new Modifier( "public" ) );
        modifiers.addModifier(new Modifier( "static" ));

        Access returnType = new PrimitiveTypeAccess( "@primitive", "void" );

        String name = "rule_" + rule.getID() + "_Consequence";

        MethodDecl method = new MethodDecl( modifiers, returnType, name,
                                            then.getParameterDeclarations(), new List<Access>(),
                                            new Opt<Block>( then.getBlock()) );

        containingClass.getBodyDeclList().add( method );
    }
}
