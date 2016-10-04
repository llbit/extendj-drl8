package org.extendj;

import org.extendj.ast.Access;
import org.extendj.ast.Block;
import org.extendj.ast.ClassDecl;
import org.extendj.ast.CompilationUnit;
import org.extendj.ast.List;
import org.extendj.ast.MethodDecl;
import org.extendj.ast.Modifier;
import org.extendj.ast.Modifiers;
import org.extendj.ast.Opt;
import org.extendj.ast.PrimitiveTypeAccess;
import org.extendj.ast.RuleDecl;
import org.extendj.ast.ThenDecl;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class ExtensionMain extends JavaChecker {

    public static void main( String args[] ) {
        int exitCode = new ExtensionMain().run( args );
        if ( exitCode != 0 ) {
            System.exit( exitCode );
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
        try {
            unit.prettyPrint( new PrintStream( System.out, false, "UTF-8" ) );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException( e );
        }
        unit.generateClassfile();
    }

    private static void processConsequences( CompilationUnit unit ) {
        for (RuleDecl rule : unit.getRules()) {
            if (rule.hasThenDecl()) {
                ThenDecl then = rule.getThenDecl();
                ClassDecl containingClass = (ClassDecl)rule.getParent().getParent();

                Modifiers modifiers = new Modifiers();
                modifiers.addModifier(new Modifier( "public" ));
                modifiers.addModifier(new Modifier( "static" ));

                Access access = new PrimitiveTypeAccess( "@primitive", "void" );

                String name = "rule_" + rule.getID() + "_Consequence";

                MethodDecl method = new MethodDecl( modifiers, access, name,
                                                    then.getParameterDeclarations(), new List<Access>(),
                                                    new Opt<Block>( then.getBlock()) );

                containingClass.getBodyDeclList().add( method );
            }
        }
    }
}
