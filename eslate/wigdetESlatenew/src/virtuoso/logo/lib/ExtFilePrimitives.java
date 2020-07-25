// Decompiled by Jad v1.5.7d. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExtFilePrimitives.java

//THIS HAS BEEN REMOVED FROM TURTLETRACKS1.0, BUT IS USEFUL, SO USING DECOMPILED CODE FROM THE OLDER TURTLETRACKS1.0a5

package virtuoso.logo.lib;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import virtuoso.logo.*;

// Referenced classes of package virtuoso.logo.lib:
//            FilePrimitives

public class ExtFilePrimitives extends PrimitiveGroup
{

    protected void setup(Machine machine, Console console)
        throws SetupException
    {
        registerPrimitive("BUILDPATH", "pBUILDPATH", 2);
        registerPrimitive("FILECANONICALPATH", "pFILECANONICALPATH", 1);
        registerPrimitive("FILEDELETE", "pFILEDELETE", 1);
        registerPrimitive("FILEISDIR?", "pFILEISDIRP", 1);
        registerPrimitive("FILEISDIRP", "pFILEISDIRP", 1);
        registerPrimitive("FILEEXISTS?", "pFILEEXISTSP", 1);
        registerPrimitive("FILEEXISTSP", "pFILEEXISTSP", 1);
        registerPrimitive("FILEISFILE?", "pFILEISFILEP", 1);
        registerPrimitive("FILEISFILEP", "pFILEISFILEP", 1);
        registerPrimitive("FILELENGTH", "pFILELENGTH", 1);
        registerPrimitive("FILENAME", "pFILENAME", 1);
        registerPrimitive("FILEPARENT", "pFILEPARENT", 1);
        registerPrimitive("FILEPATH", "pFILEPATH", 1);
        registerPrimitive("FILEREADABLE?", "pFILEREADABLEP", 1);
        registerPrimitive("FILEREADABLEP", "pFILEREADABLEP", 1);
        registerPrimitive("FILERENAME", "pFILERENAME", 2);
        registerPrimitive("FILEWRITEABLE?", "pFILEWRITEABLEP", 1);
        registerPrimitive("FILEWRITEABLEP", "pFILEWRITEABLEP", 1);
        registerPrimitive("LS", "pLS", 0);
        registerPrimitive("LSPATH", "pLSPATH", 0);
        registerPrimitive("MKDIR", "pMKDIR", 1);
        if(console != null)
            console.putSetupMessage("Loaded Turtle Tracks extended file system primitives 1.0a5");
    }

    public final LogoObject pBUILDPATH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 2)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 2)
            throw new LanguageException("Not enough arguments");
        if(!(alogoobject[0] instanceof LogoWord))
            throw new LanguageException("Path expected");
        if(!(alogoobject[1] instanceof LogoWord))
            throw new LanguageException("Filename expected");
        else
            return new LogoWord((new File(alogoobject[0].toString(), alogoobject[1].toString())).getPath());
    }

    public final LogoObject pFILECANONICALPATH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        try
        {
            return new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).getCanonicalPath());
        }
        catch(IOException ioexception)
        {
            throw new LanguageException("Couldn't get canonical path: " + ioexception.toString());
        }
    }

    public final LogoObject pFILEDELETE(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        if(!FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).delete())
            throw new LanguageException("Unable to delete file " + alogoobject[0].toString());
        else
            return LogoVoid.obj;
    }

    public final LogoObject pFILEISDIRP(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        else
            return new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).isDirectory());
    }

    public final LogoObject pFILEEXISTSP(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        else
            return new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).exists());
    }

    public final LogoObject pFILEISFILEP(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        else
            return new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).isFile());
    }

    public final LogoObject pFILELENGTH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        else
            return new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).length());
    }

    public final LogoObject pFILENAME(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        else
            return new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).getName());
    }

    public final LogoObject pFILEPARENT(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        else
            return new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).getParent());
    }

    public final LogoObject pFILEPATH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        else
            return new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).getPath());
    }

    public final LogoObject pFILEREADABLEP(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        else
            return new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).canRead());
    }

    public final LogoObject pFILERENAME(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 2)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 2)
            throw new LanguageException("Not enough arguments");
        File file = FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]);
        File file1 = FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[1]);
        if(!file.renameTo(file1))
            throw new LanguageException("Unable to rename file " + alogoobject[0].toString() + " to " + alogoobject[1].toString());
        else
            return LogoVoid.obj;
    }

    public final LogoObject pFILEWRITEABLEP(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 1)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 1)
            throw new LanguageException("Not enough arguments");
        else
            return new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), alogoobject[0]).canWrite());
    }

    public final LogoObject pLS(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 0)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 0)
            throw new LanguageException("Not enough arguments");
        String as[] = (new File((String)interpenviron.mach().getData(FilePrimitives.WDDATAID))).list();
        LogoWord alogoword[] = new LogoWord[as.length];
        for(int i = 0; i < as.length; i++)
            alogoword[i] = new LogoWord(as[i]);

        qsortHelper(alogoword, 0, alogoword.length);
        return new LogoList(alogoword);
    }

    public final LogoObject pLSPATH(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        if(alogoobject.length > 0)
            throw new LanguageException("Too many arguments");
        if(alogoobject.length < 0)
            throw new LanguageException("Not enough arguments");
        String as[] = (new File((String)interpenviron.mach().getData(FilePrimitives.WDDATAID))).list();
        LogoWord alogoword[] = new LogoWord[as.length];
        for(int i = 0; i < as.length; i++)
            alogoword[i] = new LogoWord(FilePrimitives.absolutePath(interpenviron.mach(), as[i]).getPath());

        qsortHelper(alogoword, 0, alogoword.length);
        return new LogoList(alogoword);
    }

    public final LogoObject pMKDIR(InterpEnviron interpenviron, LogoObject alogoobject[])
        throws LanguageException
    {
        return LogoVoid.obj;
    }

    protected static Random _rand = new Random(); //Birb

    private static final void qsortHelper(LogoWord alogoword[], int i, int j)
    {
        if(i > j - 2)
            return;
        int k = /*PrimitiveGroup*/_rand.nextInt(); //Birb: new TurtleTracks doesn't provide a PrimitiveGroup._rand field
        if(k < 0)
            k = -k;
        k = i + k % (j - i);
        LogoWord logoword = alogoword[k];
        alogoword[k] = alogoword[i];
        alogoword[i] = logoword;
        int l = i + 1;
        int i1 = j - 1;
        do
        {
            while(l <= i1) 
            {
                if(CaselessString.staticCompare(alogoword[i].toString(), alogoword[l].toString()) < 0)
                    break;
                l++;
            }
            for(; l <= i1; i1--)
                if(l != i1 && CaselessString.staticCompare(alogoword[i1].toString(), alogoword[i].toString()) < 0)
                    break;

            if(l < i1)
            {
                LogoWord logoword1 = alogoword[l];
                alogoword[l] = alogoword[i1];
                alogoword[i1] = logoword1;
            } else
            {
                LogoWord logoword2 = alogoword[i1];
                alogoword[i1] = alogoword[i];
                alogoword[i] = logoword2;
                qsortHelper(alogoword, i, i1);
                qsortHelper(alogoword, i1 + 1, j);
                return;
            }
        } while(true);
    }

    public ExtFilePrimitives()
    {
    }
}
