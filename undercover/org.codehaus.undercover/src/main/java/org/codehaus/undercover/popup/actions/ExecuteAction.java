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

public class ExecuteAction implements IObjectActionDelegate, IWorkbenchWindowActionDelegate
{

    private ICompilationUnit resource;

    public void setActivePart( IAction action, IWorkbenchPart targetPart )
    {
    }

    public void run( IAction action )
    {
        try
        {
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
    }

    public void init( IWorkbenchWindow window )
    {

    }

}
