package org.extendj;

import org.drl8.CompilationUnitProcessor;
import org.extendj.ast.CompilationUnit;

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
        CompilationUnitProcessor.processCompilationUnit( unit );
        try {
            unit.prettyPrint( new PrintStream( System.out, false, "UTF-8" ) );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException( e );
        }
        unit.generateClassfile();
    }



}
