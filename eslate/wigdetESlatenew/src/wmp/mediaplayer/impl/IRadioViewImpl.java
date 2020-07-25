package wmp.mediaplayer.impl;

import wmp.mediaplayer.IRadioView;

import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;

/**
 * Represents COM interface IRadioView.
 */
public class IRadioViewImpl extends IDispatchImpl
    implements IRadioView
{
    public static final String INTERFACE_IDENTIFIER = IRadioView.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IRadioViewImpl()
    {
    }

    protected IRadioViewImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IRadioViewImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IRadioViewImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IRadioViewImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IRadioViewImpl(this);
    }
}
