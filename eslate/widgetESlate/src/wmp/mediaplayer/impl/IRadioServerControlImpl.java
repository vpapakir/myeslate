package wmp.mediaplayer.impl;

import wmp.mediaplayer.IRadioServerControl;

import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IRadioServerControl.
 */
public class IRadioServerControlImpl extends IDispatchImpl
    implements IRadioServerControl
{
    public static final String INTERFACE_IDENTIFIER = IRadioServerControl.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IRadioServerControlImpl()
    {
    }

    protected IRadioServerControlImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IRadioServerControlImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IRadioServerControlImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IRadioServerControlImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IRadioServerControlImpl(this);
    }
}
