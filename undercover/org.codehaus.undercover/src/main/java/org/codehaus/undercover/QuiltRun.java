/* TestStmtCoverage.java */

package org.codehaus.undercover;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.quilt.cl.GraphSpy;
import org.quilt.cl.GraphTalker;
import org.quilt.cl.GraphXformer;
import org.quilt.cl.QuiltClassLoader;
import org.quilt.cl.RunTest;
import org.quilt.cover.stmt.StmtRegistry;

public class QuiltRun
{
    /** Classpath. */
    private URL[] cp = null; // this is built up below

    private String[] delegating =
        { 
        // NOTHING beyond standard defaults
        };
    // we want everything to be instrumented
    private String[] include =
        {
            "AnonymousClass"
        };
    private String[] exclude =
        { 
        // NOTHING
        };

    private GraphXformer spy;
    private GraphXformer talker, talker2;

    private StmtRegistry stmtReg;

    private QuiltClassLoader qLoader = null;

    public QuiltRun( )
    {
        setUp();
    }

    public void setIncludes( String[] includes )
    {
        this.include = includes;
    }

    public void setUp()
    {
        File sam1 = new File( "target/test-data-classes/" );
        String fullPath1 = sam1.getAbsolutePath() + "/";
        File sam2 = new File( "target/classes" );
        String fullPath2 = sam2.getAbsolutePath() + "/";
        File sam3 = new File( "target/test-classes" );
        String fullPath3 = sam3.getAbsolutePath() + "/";
        try
        {
            // Terminating slash is required.  Relative paths don't
            // work.
            URL[] samples =
                {
                        new URL( "file:///" + fullPath1 ), new URL( "file:///" + fullPath2 ),
                        new URL( "file:///" + fullPath3 )
                };
            cp = samples;
        }
        catch ( MalformedURLException e )
        {
            e.printStackTrace();
            throw new RuntimeException( "problem creating class path" );
        }
        ClassLoader parent = ClassLoader.getSystemClassLoader();
        qLoader = new QuiltClassLoader( cp, parent, // parent
                        delegating, // delegated classes
                        include, // being instrumented
                        exclude ); // do NOT instrument

        // Graph Xformers /////////////////////////////////
        spy = new GraphSpy();
        qLoader.addGraphXformer( spy );

        // dumps graph BEFORE transformation
        talker = new GraphTalker();
        qLoader.addGraphXformer( talker );

        stmtReg = (StmtRegistry) qLoader.addQuiltRegistry( "org.quilt.cover.stmt.StmtRegistry" );

        talker2 = new GraphTalker();
        qLoader.addGraphXformer( talker2 );
    }

    // SUPPORT METHODS //////////////////////////////////////////////
    private Object loadClassThroughQuilt( String name ) throws Exception
    {
        Class clazz = null;
        try
        {
            clazz = qLoader.loadClass( name );
        }
        catch ( ClassNotFoundException e )
        {
            e.printStackTrace();
            throw new RuntimeException( "exception loading " + name + " using loadClass" );
        }

        Object o = clazz.newInstance();
        checkInstrumentation( o );

        return o;
    }

    /**
     * Check that the class has been properly instrumented by
     * org.quilt.cover.stmt.  At the moment this means only that the
     * hitcount array, public static int[] q$$q is one of the class's
     * fields.  We also check that it has NOT been initialized,
     * although we will eventually make sure that it has been.
     */
    private void checkInstrumentation( Object rt )
    {
        String name = rt.getClass().getName();
        System.out.println( "checkInstrumentation for class " + name );
        try
        {
            Field qField = rt.getClass().getField( "q$$q" );
            if ( qField == null )
            {
                System.out.println( name + " has no hit count array" );
                throw new RuntimeException( name + " has NO hit count array" );
            }
            else
                try
                {
                    int[] hitCounts = (int[]) qField.get( null );
                    if ( hitCounts == null )
                    {
                        throw new RuntimeException( "q$$q has not been initialized" );

                    }
                }
                catch ( IllegalAccessException e )
                {
                    e.printStackTrace();
                }
            // check version
            qField = rt.getClass().getField( "q$$qVer" );
            if ( qField == null )
            {
                System.out.println( name + " has no version field" );
                throw new RuntimeException( name + " has NO version field" );
            }
            else
                try
                {
                    int version = qField.getInt( qField );
                    if ( version != 0 )
                    {
                        throw new RuntimeException( "q$$q has wrong version number" );
                    }
                }
                catch ( IllegalAccessException e )
                {
                    e.printStackTrace();
                }
            // check StmtRegistry

        }
        catch ( NoSuchFieldException e )
        {
            throw new RuntimeException( name + " has no q$$q field" );
        }
    }

    public void testInvokeTestData( String className ) throws Exception
    {
        Object o = loadClassThroughQuilt( className );
        // AnonymousClass.runTest(x) returns x

        Method m = o.getClass().getMethod( "testSimple", new Class[0] );
        try
        {
            m.invoke( o, new Object[0] );
        }
        catch ( Exception e )
        {
            System.out.println( "Incidentally, you might be interested to know that the test failed!" );
            e.printStackTrace();
        }

        String runResults = stmtReg.getReport();
        System.out.println( "\nQuilt coverage report:\n" + runResults );
    }

}
