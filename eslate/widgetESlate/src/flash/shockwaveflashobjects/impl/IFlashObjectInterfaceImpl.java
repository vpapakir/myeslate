package flash.shockwaveflashobjects.impl;

import com.jniwrapper.win32.automation.impl.IDispatchExImpl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

import flash.shockwaveflashobjects.IFlashObjectInterface;

/**
 * Represents COM interface IFlashObjectInterface.
 */
public class IFlashObjectInterfaceImpl extends IDispatchExImpl
    implements IFlashObjectInterface
{
    public static final String INTERFACE_IDENTIFIER = IFlashObjectInterface.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IFlashObjectInterfaceImpl()
    {
    }

    protected IFlashObjectInterfaceImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IFlashObjectInterfaceImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IFlashObjectInterfaceImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IFlashObjectInterfaceImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IFlashObjectInterfaceImpl(this);
    }
}
