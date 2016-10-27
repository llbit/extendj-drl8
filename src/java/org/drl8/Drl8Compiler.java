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

import org.extendj.JavaChecker;
import org.extendj.ast.Access;
import org.extendj.ast.Block;
import org.extendj.ast.ClassDecl;
import org.extendj.ast.CompilationUnit;
import org.extendj.ast.MethodDecl;
import org.extendj.ast.Modifier;
import org.extendj.ast.Modifiers;
import org.extendj.ast.Opt;
import org.extendj.ast.PrimitiveTypeAccess;
import org.extendj.ast.RuleDecl;
import org.extendj.ast.ThenDecl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Drl8Compiler extends JavaChecker {

    private String srcDir = "src/main/drlx";
    private String destDir = "target/classes";

    private List<CompilationUnit> units = new ArrayList<>();

    public List<CompilationUnit> buildAll() {
        units.clear();
        String rootPath = new File(".").getAbsolutePath();
        List<String> sources = findAllSources( rootPath + "/" + srcDir );
        if (sources.isEmpty()) {
            return units;
        }
        sources.add( 0, "-d" );
        sources.add( 1, rootPath + "/" + destDir );
        run(sources.toArray( new String[sources.size()] ));
        return units;
    }

    private List<String> findAllSources( String rootPath ) {
        List<String> sources = new ArrayList<String>();
        findAllSources( new File( rootPath ), sources );
        return sources;
    }

    private void findAllSources( File dir, List<String> sources ) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                findAllSources( file, sources );
            } else if (file.getName().endsWith( ".drlx" )) {
                sources.add(file.getAbsolutePath());
            }
        }
    }

    @Override
    protected int processCompilationUnit( CompilationUnit unit ) throws Error {
        // Replace the following super call to skip semantic error checking in unit.
        return super.processCompilationUnit( unit );
    }

    /**
     * Called by processCompilationUnit when there are no errors in the argument unit.
     */
    @Override
    protected void processNoErrors( CompilationUnit unit ) {
        unit.process();
        processConsequences( unit );
        unit.generateClassfile();
        units.add(unit);
    }

    private static void processConsequences( CompilationUnit unit ) {
        for (RuleDecl rule : unit.getRules()) {
            if (rule.hasThenDecl()) {
                ThenDecl then = rule.getThenDecl();
                ClassDecl containingClass = (ClassDecl)rule.getParent().getParent();

                Modifiers modifiers = new Modifiers();
                modifiers.addModifier(new Modifier( "public" ) );
                modifiers.addModifier(new Modifier( "static" ));

                Access access = new PrimitiveTypeAccess( "@primitive", "void" );

                String name = "rule_" + rule.getID() + "_Consequence";

                MethodDecl method = new MethodDecl( modifiers, access, name,
                                                    then.getParameterDeclarations(), new org.extendj.ast.List<Access>(),
                                                    new Opt<Block>( then.getBlock()) );

                containingClass.getBodyDeclList().add( method );
            }
        }
    }
}
