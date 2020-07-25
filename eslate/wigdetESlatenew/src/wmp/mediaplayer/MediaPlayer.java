package wmp.mediaplayer;

import wmp.mediaplayer.impl.IMediaPlayer2Impl;

import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.CoClass;
import com.jniwrapper.win32.ole.OleFunctions;

/**
 * Represents COM coclass MediaPlayer.
 */
public class MediaPlayer extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{22D6F312-B0F6-11D0-94AB-0080C74C7E95}");

    public MediaPlayer()
    {
    }

    public MediaPlayer(MediaPlayer that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static IMediaPlayer2 create(ClsCtx dwClsContext) throws ComException
    {
        final IMediaPlayer2Impl intf = new IMediaPlayer2Impl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>IMediaPlayer2</code> interface from IUnknown instance.
     */
    public static IMediaPlayer2 queryInterface(IUnknown unknown) throws ComException
    {
        final IMediaPlayer2Impl result = new IMediaPlayer2Impl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new MediaPlayer(this);
    }
}