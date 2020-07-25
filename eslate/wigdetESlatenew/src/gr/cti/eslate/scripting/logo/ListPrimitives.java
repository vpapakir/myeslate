//20-8-1998: Created by G.Birbilis with code from G.Tsironis
//20-8-1998: G.Birbilis renamed some primitives so that LIST is their name prefix
//31-5-2000: moved to "gr.cti.eslate.scripting.logo" package

package gr.cti.eslate.scripting.logo;

import virtuoso.logo.*;

public class ListPrimitives extends PrimitiveGroup
{

   protected void setup(Machine machine, Console console)
        throws SetupException
    {   //maybe "LIST" should be prepended to all the following primitives
        registerPrimitive("LISTSUM", "pLISTSUM", 1);
        registerPrimitive("LISTPRODUCT", "pLISTPRODUCT", 1);
        registerPrimitive("AVERAGE", "pAVERAGE", 1);
        registerPrimitive("GEOMEAN", "pGEOMEAN", 1);
        registerPrimitive("VARIANCE", "pVARIANCE", 1);
        registerPrimitive("STDEV", "pSTDEV", 1);
        registerPrimitive("LISTMIN", "pLISTMIN", 1);
        registerPrimitive("LISTMAX", "pLISTMAX", 1);
        registerPrimitive("MEDIAN", "pMEDIAN", 1);

        if(console != null)
            console.putStatusMessage("Loaded ESlate's List Primitives");
    }

/////////////////

    public final LogoObject pLISTSUM(InterpEnviron interpenviron, LogoObject[]
aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("LISTSUM accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        if (ll.length() == 0)
            throw new LanguageException("Empty list");

        double sum = 0;
        for (int i=1; i<=ll.length(); i++)
            sum = sum + ll.pick(i).toNumber();

        return new LogoWord(sum);
    }


    public final LogoObject pLISTPRODUCT(InterpEnviron interpenviron, LogoObject[]
aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("LISTPRODUCT accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        if (ll.length() == 0)
            throw new LanguageException("Empty list");

        double product = 1;
        for (int i=1; i<=ll.length(); i++)
            product = product * ll.pick(i).toNumber();

        return new LogoWord(product);
    }


    public final LogoObject pAVERAGE(InterpEnviron interpenviron, LogoObject[]
aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("AVERAGE accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        double sum = 0;
        for (int i=1; i<=ll.length(); i++)
            sum = sum + ll.pick(i).toNumber();

        return new LogoWord(sum/ll.length());
    }


    public final LogoObject pGEOMEAN(InterpEnviron interpenviron, LogoObject[]
aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("GEOMEAN accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        if (ll.length() == 0)
            throw new LanguageException("Empty list");

        double product = 1;
        for (int i=1; i<=ll.length(); i++)
            product = product * ll.pick(i).toNumber();

        return new LogoWord(Math.pow(product, (1d/ll.length())));
    }


    public final LogoObject pVARIANCE(InterpEnviron interpenviron, LogoObject[]
aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("VARIANCE accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        int elementCount = ll.length();
        if (elementCount == 0)
            throw new LanguageException("Empty list");

        double sum = 0;
        double squareSum = 0;
        double tmp;
        for (int i=1; i<=elementCount; i++) {
            tmp = ll.pick(i).toNumber();
            sum = sum + tmp;
            squareSum = squareSum + (tmp * tmp);
        }

        double elementCountd = new Integer(elementCount).doubleValue();
        return new LogoWord( ( ( elementCountd*squareSum ) - ( sum*sum ) ) / ( (
elementCountd * ( elementCountd - 1d ) ) ) );
    }


    public final LogoObject pLISTMIN(InterpEnviron interpenviron, LogoObject[]
aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("LISTMIN accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        int elementCount = ll.length();
        if (elementCount == 0)
            throw new LanguageException("Empty list");

        try{
            double min = ll.pick(1).toNumber();
            for (int i=2; i<=elementCount; i++) {
                if (min > ll.pick(i).toNumber())
                    min = ll.pick(i).toNumber();
            }
            return new LogoWord(min);
        }catch (LanguageException exc) {
            String min = ll.pick(1).toString();
            for (int i=2; i<=elementCount; i++) {
                if (min.compareTo(ll.pick(i).toString()) > 0)
                    min = ll.pick(i).toString();
            }
            return new LogoWord(min);
        }
    }


    public final LogoObject pLISTMAX(InterpEnviron interpenviron, LogoObject[]
aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("LISTMAX accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        int elementCount = ll.length();
        if (elementCount == 0)
            throw new LanguageException("Empty list");

        try{
            double max = ll.pick(1).toNumber();
            for (int i=2; i<=elementCount; i++) {
                if (max < ll.pick(i).toNumber())
                    max = ll.pick(i).toNumber();
            }
            return new LogoWord(max);
        }catch (LanguageException exc) {
            String max = ll.pick(1).toString();
            for (int i=2; i<=elementCount; i++) {
                if (max.compareTo(ll.pick(i).toString()) < 0)
                    max = ll.pick(i).toString();
            }
            return new LogoWord(max);
        }
    }


    public final LogoObject pMEDIAN(InterpEnviron interpenviron, LogoObject[]
aLogoObject) throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("MEDIAN accepts only lists as argument");

        LogoList ll = (LogoList) aLogoObject[0];
        int elementCount = ll.length();
        if (elementCount == 0)
            throw new LanguageException("Empty list");

        if (elementCount % 2 == 1)
            return ll.pick(elementCount/2 + 1);
        else
            return new LogoWord(( ll.pick(elementCount/2).toNumber() +
ll.pick(elementCount/2 + 1).toNumber() ) / 2);
    }


    public final LogoObject pSTDEV(InterpEnviron interpenviron, LogoObject[] aLogoObject)
throws LanguageException {
        testNumParams(aLogoObject, 1);

        if (!(aLogoObject[0] instanceof LogoList))
            throw new LanguageException("STDEV accepts only lists as argument");

        LogoWord lw = (LogoWord) pVARIANCE(interpenviron, aLogoObject);
        return new LogoWord(Math.sqrt(lw.toNumber()));
    }

}
