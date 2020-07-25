package flash.shockwaveflashobjects.impl;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

import flash.shockwaveflashobjects.IFlashFactory;

/**
 * Represents COM interface IFlashFactory.
 */
public class IFlashFactoryImpl extends IUnknownImpl
    implements IFlashFactory
{
    public static final String INTERFACE_IDENTIFIER = IFlashFactory.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IFlashFactoryImpl()
    {
    }

    protected IFlashFactoryImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IFlashFactoryImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IFlashFactoryImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IFlashFactoryImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IFlashFactoryImpl(this);
    }
}
