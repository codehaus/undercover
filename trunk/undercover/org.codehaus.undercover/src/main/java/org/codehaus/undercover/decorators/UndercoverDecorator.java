package org.codehaus.undercover.decorators;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * @author Ben Walding
 */
public class UndercoverDecorator extends LabelProvider implements ILabelDecorator
{
    public static IResource lastResource;
    public UndercoverDecorator( )
    {
        super();
    }

    public Image decorateImage( Image image, Object object )
    {
        if (object instanceof IResource) {
            lastResource = (IResource) object;
        }
            
            
        if ( object instanceof File )
        {
            File f = (File) object;
            //String projectPath = f.getProjectRelativePath();
            Image i = PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJS_WARN_TSK );
            return i;
        }
        else
        {
            return null;
        }
    }

    public String decorateText( String label, Object object )
    {
        if ( object instanceof File )
        {
            File f = (File) object;
            String projectPath = f.getProjectRelativePath().toString();
            return label + "(" + projectPath + ")";
        }
        else
        {
            return label + ( object == null ? " [null]" : " [" + object.getClass().getName() + "]" );
        }
        
    }
    
    
}
