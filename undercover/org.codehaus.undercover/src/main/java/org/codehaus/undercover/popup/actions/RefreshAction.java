package org.codehaus.undercover.popup.actions;

import org.codehaus.undercover.decorators.UndercoverDecorator;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class RefreshAction implements IObjectActionDelegate, IWorkbenchWindowActionDelegate {

	/**
	 * Constructor for Action1.
	 */
	public RefreshAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		Display.getDefault().beep();
		
		try
        {
		    System.out.println(UndercoverDecorator.lastResource);
            createMarkerForResource(UndercoverDecorator.lastResource);
        }
        catch ( CoreException e )
        {
             e.printStackTrace();
        }
	}
	
	public void createMarkerForResource(IResource resource) throws CoreException {
	    IMarker marker = resource.createMarker("org.eclipse.core.resources.problemmarker");

	    //Once we have a marker object, we can set its attributes
	    //marker.setAttribute("coolFactor", "ULTRA");
	    marker.setAttribute(IMarker.LINE_NUMBER, 16);
	    marker.setAttribute(IMarker.TRANSIENT, true);
	    marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
	    marker.setAttribute(IMarker.MESSAGE, resource.toString() + " is a piece of shite");
	 }

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
     */
    public void init( IWorkbenchWindow window )
    {
        // TODO Auto-generated method stub
        
    }

}
