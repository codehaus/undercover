package org.codehaus.undercover.popup.actions;

import java.util.Iterator;

import org.codehaus.undercover.QuiltRun;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class FindTestsAction implements IObjectActionDelegate, IWorkbenchWindowActionDelegate
{

    private ICompilationUnit resource;

    /**
     * Constructor for Action1.
     */
    public FindTestsAction( )
    {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart( IAction action, IWorkbenchPart targetPart )
    {
        //System.out.println( "Action:     " + action );
        //System.out.println( "TargetPart: " + targetPart );
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run( IAction action )
    {
      
	    try
        {
	        //IMarker marker = resource.getCorrespondingResource().createMarker("org.eclipse.core.resources.problemmarker");
	        //Once we have a marker object, we can set its attributes
            //marker.setAttribute("coolFactor", "ULTRA");
            //marker.setAttribute(IMarker.LINE_NUMBER, 16);
            //marker.setAttribute(IMarker.TRANSIENT, true);
    	    //marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
    	    //marker.setAttribute(IMarker.MESSAGE, resource.toString() + " is a piece of shite");
	        QuiltRun qr = new QuiltRun();
	        String className = resource.getElementName();
	        System.out.println("Class Name:" + className);
	        qr.testInvokeTestData(className);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
	 }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged( IAction action, ISelection selection )
    {
        if ( selection instanceof StructuredSelection )
        {
            StructuredSelection ss = (StructuredSelection) selection;
            for ( Iterator iter = ss.iterator(); iter.hasNext(); )
            {

                Object sel = (Object) iter.next();
                System.out.println( "sel: " + sel.getClass() );
                if (sel instanceof ICompilationUnit) {
                    this.resource = (ICompilationUnit) sel;
                    
                }
            }
        }
        else
        {
            System.out.println( "Unknown selection: " + selection.getClass() );
        }
    }

    public void dispose()
    {
        // TODO Auto-generated method stub

    }

    public void init( IWorkbenchWindow window )
    {

    }

}
