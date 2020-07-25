/*
===============================================================================

        FILE:  TurtlePrimitives
        
        PROJECT:
        
                Turtle Tracks
        
        CONTENTS:
        
                2D turtle graphics primitives
        
        PROGRAMMERS:
        
                Daniel Azuma (DA)  <dazuma@kagi.com>
        
        COPYRIGHT:
        
                Copyright (C) 1997-1999  Daniel Azuma  (dazuma@kagi.com)
                
                This program is free software; you can redistribute it and/or
                modify it under the terms of the GNU General Public License
                as published by the Free Software Foundation; either version 2
                of the License, or (at your option) any later version.
                
                This program is distributed in the hope that it will be useful,
                but WITHOUT ANY WARRANTY; without even the implied warranty of
                MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
                GNU General Public License for more details.
                
                You should have received a copy of the GNU General Public
                License along with this program. If not, you can obtain a copy
                by writing to:
                        Free Software Foundation, Inc.
                        59 Temple Place - Suite 330,
                        Boston, MA  02111-1307, USA.
        
        VERSION:
        
                Turtle Tracks 1.0  (13 November 1999)
        
        CHANGE HISTORY:
        
                13 November 1999 -- DA -- Released under GNU General Public License

===============================================================================
*/


package virtuoso.logo.lib;

import virtuoso.logo.*;
import java.awt.*;
import java.util.Hashtable;


/**
 * Names of primitives (keywords)
 */
@SuppressWarnings(value={"unchecked"})
public class TurtlePrimitives
extends PrimitiveGroup
{

        static final int TURTLE_HEIGHT = 16;
        static final int TURTLE_HALF_WIDTH = 6;
        static final int DEFAULT_WINDOW_WIDTH = 400;
        static final int DEFAULT_WINDOW_HEIGHT = 400;


        private Color _backColor;
        private Color _penColor;
        private byte _penState;
        private double _xpos;
        private double _ypos;
        int _xhalfsize;
        int _yhalfsize;
        private double _heading;
        private boolean _turtleVisible;
        private byte _wrapState;
        
        private TurtleWind _graphWind;
        Graphics _graphContext;
        
        private Hashtable _colorNames;
        
        Machine _mach;
        
        RedisplayThread _thread;
        int _redisplayInterval;
        
        private static final byte PS_DOWN = 1;
        private static final byte PS_UP = 2;
        private static final byte PS_ERASE = 3;
        private static final byte WS_WRAP = 1;
        private static final byte WS_FENCE = 2;
        private static final byte WS_WINDOW = 3;


        /**
         * Set up primitive group
         */
        protected void setup(
                Machine mach,
                Console console)
        throws
                SetupException
        {
                registerPrimitive("BACK", "pBACK", 1);
                registerPrimitive("BK", "pBACK", 1);
                registerPrimitive("CLEAN", "pCLEAN", 0);
                registerPrimitive("CLEARSCREEN", "pCLEARSCREEN", 0);
                registerPrimitive("CS", "pCLEARSCREEN", 0);
                registerPrimitive("DISTANCETO", "pDISTANCETO", 1);
                registerPrimitive("DISTANCETOXY", "pDISTANCETOXY", 2);
                registerPrimitive("DRAW", "pDRAW", 0);
                registerPrimitive("FD", "pFORWARD", 1);
                registerPrimitive("FENCE", "pFENCE", 0);
                registerPrimitive("FORWARD", "pFORWARD", 1);
                registerPrimitive("GETBACKGROUND", "pGETBACKGROUND", 0);
                registerPrimitive("GETBG", "pGETBACKGROUND", 0);
                registerPrimitive("GETPC", "pGETPENCOLOR", 0);
                registerPrimitive("GETPENCOLOR", "pGETPENCOLOR", 0);
                registerPrimitive("HEADING", "pHEADING", 0);
                registerPrimitive("HIDETURTLE", "pHIDETURTLE", 0);
                registerPrimitive("HOME", "pHOME", 0);
                registerPrimitive("HT", "pHIDETURTLE", 0);
                registerPrimitive("LABEL", "pLABEL", 1);
                registerPrimitive("LEFT", "pLEFT", 1);
                registerPrimitive("LT", "pLEFT", 1);
                registerPrimitive("ND", "pNODRAW", 0);
                registerPrimitive("NODRAW", "pNODRAW", 0);
                registerPrimitive("PALETTE", "pPALETTE", 1);
                registerPrimitive("PALETTE?", "pPALETTEP", 1);
                registerPrimitive("PALETTEP", "pPALETTEP", 1);
                registerPrimitive("PD", "pPENDOWN", 0);
                registerPrimitive("PE", "pPENERASE", 0);
                registerPrimitive("PENDOWN", "pPENDOWN", 0);
                registerPrimitive("PENERASE", "pPENERASE", 0);
                registerPrimitive("PENUP", "pPENUP", 0);
                registerPrimitive("POS", "pPOS", 0);
                registerPrimitive("PU", "pPENUP", 0);
                registerPrimitive("REFRESH", "pREFRESH", 0);
                registerPrimitive("REFRESHINTERVAL", "pREFRESHINTERVAL", 1);
                registerPrimitive("RESETPALETTE", "pRESETPALETTE", 1);
                registerPrimitive("RIGHT", "pRIGHT", 1);
                registerPrimitive("RT", "pRIGHT", 1);
                registerPrimitive("SETBACKGROUND", "pSETBACKGROUND", 1);
                registerPrimitive("SETBG", "pSETBACKGROUND", 1);
                registerPrimitive("SETH", "pSETHEADING", 1);
                registerPrimitive("SETHEADING", "pSETHEADING", 1);
                registerPrimitive("SETPALETTE", "pSETPALETTE", 1);
                registerPrimitive("SETPC", "pSETPENCOLOR", 1);
                registerPrimitive("SETPENCOLOR", "pSETPENCOLOR", 1);
                registerPrimitive("SETPOS", "pSETPOS", 1);
                registerPrimitive("SETX", "pSETX", 1);
                registerPrimitive("SETXY", "pSETXY", 2);
                registerPrimitive("SETY", "pSETY", 1);
                registerPrimitive("SHOWTURTLE", "pSHOWTURTLE", 0);
                registerPrimitive("ST", "pSHOWTURTLE", 0);
                registerPrimitive("TOWARDS", "pTOWARDS", 1);
                registerPrimitive("TOWARDSXY", "pTOWARDSXY", 2);
                registerPrimitive("UNSETPALETTE", "pUNSETPALETTE", 1);
                registerPrimitive("WINDOW", "pWINDOW", 0);
                registerPrimitive("WRAP", "pWRAP", 0);
                registerPrimitive("XCOR", "pXCOR", 0);
                registerPrimitive("XSIZE", "pXSIZE", 0);
                registerPrimitive("YCOR", "pYCOR", 0);
                registerPrimitive("YSIZE", "pYSIZE", 0);
                
                _mach = mach;
                _graphWind = null;
                _thread = null;
                _redisplayInterval = 0;
                resetPalette();
                
                console.putStatusMessage("Turtle Tracks turtle graphics primitives v1.0");
        }


        /**
         * VM is closing down
         */
        protected void exiting()
        {
                synchronized (this)
                {
                        if (_graphWind != null)
                        {
                                _graphWind.dispose();
                                _graphWind = null;
                                _thread.kill();
                                _thread = null;
                        }
                }
        }


        /**
         * Primitive implementation for BACK
         */
        public final LogoObject pBACK(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double dist = -params[0].toNumber();
                synchronized (this)
                {
                        double headingRad = (90.0-_heading)/180.0*Math.PI;
                        moveTo(_xpos+dist*Math.cos(headingRad), _ypos+dist*Math.sin(headingRad));
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for CLEAN
         */
        public final LogoObject pCLEAN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        if (_graphWind != null)
                        {
                                _graphWind.reset();
                        }
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for CLEARSCREEN
         */
        public final LogoObject pCLEARSCREEN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        if (_graphWind != null)
                        {
                                _graphWind.reset();
                        }
                        _xpos = 0;
                        _ypos = 0;
                        _heading = 0;
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for DISTANCETO
         */
        public final LogoObject pDISTANCETO(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoList))
                {
                        throw new LanguageException("Position list expected");
                }
                if (params[0].length() != 2)
                {
                        throw new LanguageException("Position list expected");
                }
                double xval = ((LogoList)(params[0])).pickInPlace(0).toNumber();
                double yval = ((LogoList)(params[0])).pickInPlace(1).toNumber();
                double dist;
                synchronized (this)
                {
                        dist = Math.sqrt((xval-_xpos)*(xval-_xpos)+(yval-_ypos)*(yval-_ypos));
                }
                return new LogoWord(dist);
        }


        /**
         * Primitive implementation for DISTANCETOXY
         */
        public final LogoObject pDISTANCETOXY(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                double xval = params[0].toNumber();
                double yval = params[1].toNumber();
                double dist;
                synchronized (this)
                {
                        dist = Math.sqrt((xval-_xpos)*(xval-_xpos)+(yval-_ypos)*(yval-_ypos));
                }
                return new LogoWord(dist);
        }


        /**
         * Primitive implementation for DRAW
         */
        public final LogoObject pDRAW(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                int xs, ys;
                if (params.length == 2)
                {
                        xs = params[0].toInteger();
                        ys = params[1].toInteger();
                }
                else if (params.length == 0)
                {
                        xs = DEFAULT_WINDOW_WIDTH/2;
                        ys = DEFAULT_WINDOW_HEIGHT/2;
                }
                else
                {
                        throw new LanguageException("Wrong number of arguments");
                }
                synchronized (this)
                {
                        _xhalfsize = xs;
                        _yhalfsize = ys;
                        if (_graphWind != null)
                        {
                                _graphWind.reset();
                        }
                        else
                        {
                                try
                                {
                                        _graphWind = new TurtleWind();
                                        _thread = new RedisplayThread();
                                        _thread.start();
                                }
                                catch (InternalError e) {}
                        }
                        if (_graphWind == null)
                        {
                                throw new LanguageException("Unable to open graphics window");
                        }
                        _backColor = Color.black;
                        _penColor = Color.white;
                        _penState = PS_DOWN;
                        _xpos = 0;
                        _ypos = 0;
                        _heading = 0;
                        _turtleVisible = true;
                        _wrapState = WS_WRAP;
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for FENCE
         */
        public final LogoObject pFENCE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        if (_xpos < -_xhalfsize || _xpos > _xhalfsize-1 ||
                                _ypos < -_yhalfsize || _ypos > _yhalfsize-1)
                        {
                                throw new LanguageException("Turtle out of bounds in FENCE");
                        }
                        _wrapState = WS_FENCE;
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for FORWARD
         */
        public final LogoObject pFORWARD(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double dist = params[0].toNumber();
                synchronized (this)
                {
                        double headingRad = (90.0-_heading)/180.0*Math.PI;
                        moveTo(_xpos+dist*Math.cos(headingRad), _ypos+dist*Math.sin(headingRad));
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for GETBACKGROUND
         */
        public final LogoObject pGETBACKGROUND(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                LogoObject[] a = new LogoObject[3];
                a[0] = new LogoWord(_backColor.getRed());
                a[1] = new LogoWord(_backColor.getGreen());
                a[2] = new LogoWord(_backColor.getBlue());
                return new LogoList(a);
        }


        /**
         * Primitive implementation for GETPENCOLOR
         */
        public final LogoObject pGETPENCOLOR(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                LogoObject[] a = new LogoObject[3];
                a[0] = new LogoWord(_penColor.getRed());
                a[1] = new LogoWord(_penColor.getGreen());
                a[2] = new LogoWord(_penColor.getBlue());
                return new LogoList(a);
        }


        /**
         * Primitive implementation for HEADING
         */
        public final LogoObject pHEADING(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                double temp;
                synchronized (this)
                {
                        temp = _heading;
                }
                return new LogoWord(temp);
        }


        /**
         * Primitive implementation for HIDETURTLE
         */
        public final LogoObject pHIDETURTLE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        _turtleVisible = false;
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for HOME
         */
        public final LogoObject pHOME(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        drawLineTo(0, 0);
                        _xpos = 0;
                        _ypos = 0;
                        _heading = 0;
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for LABEL
         */
        public final LogoObject pLABEL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                String str = null;
                if (params[0] instanceof LogoList)
                {
                        str = ((LogoList)(params[0])).toStringOpen();
                }
                else
                {
                        str = params[0].toString();
                }
                synchronized (this)
                {
                        if (_graphWind != null && _penState != PS_UP)
                        {
                                if (_penState == PS_DOWN)
                                {
                                        _graphContext.setColor(_penColor);
                                }
                                else
                                {
                                        _graphContext.setColor(_backColor);
                                }
                                _graphContext.drawString(str, (int)(Math.round(_xhalfsize+_xpos)),
                                        (int)(Math.round(_yhalfsize-_ypos)));
                                _graphWind.redisplay();
                        }
                }
                return LogoVoid.obj;
        }



        /**
         * Primitive implementation for LEFT
         */
        public final LogoObject pLEFT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                synchronized (this)
                {
                        _heading = fixAngle(_heading-val);
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for NODRAW
         */
        public final LogoObject pNODRAW(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        if (_graphWind != null)
                        {
                                _graphWind.dispose();
                                _graphWind = null;
                                _graphContext = null;
                                _thread.kill();
                                _thread = null;
                                _redisplayInterval = 0;
                        }
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PALETTE
         */
        public final LogoObject pPALETTE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Color name expected");
                }
                Color color = (Color)(_colorNames.get(params[0].toCaselessString()));
                if (color == null)
                {
                        return new LogoList();
                }
                LogoObject[] a = new LogoObject[3];
                a[0] = new LogoWord(color.getRed());
                a[1] = new LogoWord(color.getGreen());
                a[2] = new LogoWord(color.getBlue());
                return new LogoList(a);
        }


        /**
         * Primitive implementation for PALETTEP
         */
        public final LogoObject pPALETTEP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Color name expected");
                }
                return new LogoWord(_colorNames.get(params[0].toCaselessString()) != null);
        }


        /**
         * Primitive implementation for PENDOWN
         */
        public final LogoObject pPENDOWN(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        _penState = PS_DOWN;
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PENERASE
         */
        public final LogoObject pPENERASE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        _penState = PS_ERASE;
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for PENUP
         */
        public final LogoObject pPENUP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        _penState = PS_UP;
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for POS
         */
        public final LogoObject pPOS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                double val1;
                double val2;
                synchronized (this)
                {
                        val1 = _xpos;
                        val2 = _ypos;
                }
                LogoObject[] a = new LogoObject[2];
                a[0] = new LogoWord(val1);
                a[1] = new LogoWord(val2);
                return new LogoList(a);
        }


        /**
         * Primitive implementation for REFRESH
         */
        public final LogoObject pREFRESH(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized(this)
                {
                        if (_graphWind != null)
                        {
                                _graphWind.update();
                        }
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for REFRESHINTERVAL
         */
        public final LogoObject pREFRESHINTERVAL(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                synchronized(this)
                {
                        if (_graphWind != null)
                        {
                                _redisplayInterval = params[0].toInteger();
                                _thread.setInterval(_redisplayInterval);
                        }
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for RESETPALETTE
         */
        public final LogoObject pRESETPALETTE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                resetPalette();
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for RIGHT
         */
        public final LogoObject pRIGHT(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                synchronized (this)
                {
                        _heading = fixAngle(_heading+val);
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SETBACKGROUND
         */
        public final LogoObject pSETBACKGROUND(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                Color temp = findColor(params[0]);
                synchronized (this)
                {
                        _backColor = temp;
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SETHEADING
         */
        public final LogoObject pSETHEADING(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                synchronized (this)
                {
                        _heading = fixAngle(val);
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SETPALETTE
         */
        public final LogoObject pSETPALETTE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Color name expected");
                }
                _colorNames.put(params[0].toCaselessString(), findColor(params[1]));
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SETPENCOLOR
         */
        public final LogoObject pSETPENCOLOR(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                Color temp = findColor(params[0]);
                synchronized (this)
                {
                        _penColor = temp;
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SETPOS
         */
        public final LogoObject pSETPOS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoList))
                {
                        throw new LanguageException("Position list expected");
                }
                if (params[0].length() != 2)
                {
                        throw new LanguageException("Position list expected");
                }
                double xval = ((LogoList)(params[0])).pickInPlace(0).toNumber();
                double yval = ((LogoList)(params[0])).pickInPlace(1).toNumber();
                synchronized (this)
                {
                        moveTo(xval, yval);
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SETX
         */
        public final LogoObject pSETX(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                synchronized (this)
                {
                        if (_wrapState == WS_FENCE && (val < -_xhalfsize || val > _xhalfsize-1))
                        {
                                throw new LanguageException("Turtle out of bounds");
                        }
                        if (_wrapState == WS_WRAP)
                        {
                                while (true)
                                {
                                        if (val < -_xhalfsize-0.5)
                                        {
                                                val = wrapXMinus(val, _ypos);
                                        }
                                        else if (val > _xhalfsize-0.5)
                                        {
                                                val = wrapXPlus(val, _ypos);
                                        }
                                        else
                                        {
                                                break;
                                        }
                                }
                        }
                        drawLineTo(val, _ypos);
                        _xpos = val;
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SETXY
         */
        public final LogoObject pSETXY(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                double xval = params[0].toNumber();
                double yval = params[1].toNumber();
                synchronized (this)
                {
                        moveTo(xval, yval);
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SETY
         */
        public final LogoObject pSETY(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                double val = params[0].toNumber();
                synchronized (this)
                {
                        if (_wrapState == WS_FENCE && (val < -_yhalfsize || val > _yhalfsize-1))
                        {
                                throw new LanguageException("Turtle out of bounds");
                        }
                        if (_wrapState == WS_WRAP)
                        {
                                while (true)
                                {
                                        if (val < -_yhalfsize-0.5)
                                        {
                                                val = wrapYMinus(_xpos, val);
                                        }
                                        else if (val > _yhalfsize-0.5)
                                        {
                                                val = wrapYPlus(_xpos, val);
                                        }
                                        else
                                        {
                                                break;
                                        }
                                }
                        }
                        drawLineTo(_xpos, val);
                        _ypos = val;
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for SHOWTURTLE
         */
        public final LogoObject pSHOWTURTLE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        _turtleVisible = true;
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for TOWARDS
         */
        public final LogoObject pTOWARDS(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoList))
                {
                        throw new LanguageException("Position list expected");
                }
                if (params[0].length() != 2)
                {
                        throw new LanguageException("Position list expected");
                }
                double xval = ((LogoList)(params[0])).pickInPlace(0).toNumber();
                double yval = ((LogoList)(params[0])).pickInPlace(1).toNumber();
                double ang;
                synchronized (this)
                {
                        ang = fixAngle(90.0 - Math.atan2(yval-_ypos, xval-_xpos)/Math.PI*180);
                }
                return new LogoWord(ang);
        }


        /**
         * Primitive implementation for TOWARDSXY
         */
        public final LogoObject pTOWARDSXY(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 2);
                double xval = params[0].toNumber();
                double yval = params[1].toNumber();
                double ang;
                synchronized (this)
                {
                        ang = fixAngle(90.0 - Math.atan2(yval-_ypos, xval-_xpos)/Math.PI*180);
                }
                return new LogoWord(ang);
        }


        /**
         * Primitive implementation for UNSETPALETTE
         */
        public final LogoObject pUNSETPALETTE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 1);
                if (!(params[0] instanceof LogoWord))
                {
                        throw new LanguageException("Color name expected");
                }
                _colorNames.remove(params[0].toCaselessString());
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for WINDOW
         */
        public final LogoObject pWINDOW(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        _wrapState = WS_WINDOW;
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for WRAP
         */
        public final LogoObject pWRAP(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                synchronized (this)
                {
                        _xpos = wrapCoordinate(_xpos, _xhalfsize);
                        _ypos = wrapCoordinate(_ypos, _yhalfsize);
                        _wrapState = WS_WRAP;
                        updateTurtle();
                }
                return LogoVoid.obj;
        }


        /**
         * Primitive implementation for XCOR
         */
        public final LogoObject pXCOR(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                double val;
                synchronized (this)
                {
                        val = _xpos;
                }
                return new LogoWord(val);
        }


        /**
         * Primitive implementation for XSIZE
         */
        public final LogoObject pXSIZE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                double val;
                synchronized (this)
                {
                        val = _xhalfsize;
                }
                return new LogoWord(val);
        }


        /**
         * Primitive implementation for YCOR
         */
        public final LogoObject pYCOR(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                double val;
                synchronized (this)
                {
                        val = _ypos;
                }
                return new LogoWord(val);
        }


        /**
         * Primitive implementation for YSIZE
         */
        public final LogoObject pYSIZE(
                InterpEnviron interp,
                LogoObject[] params)
        throws
                LanguageException
        {
                testNumParams(params, 0);
                double val;
                synchronized (this)
                {
                        val = _yhalfsize;
                }
                return new LogoWord(val);
        }


        /**
         * Get color from a LogoObject
         *
         * @param param the object to convert
         *
         * @return the color
         *
         * @exception virtuoso.logo.LanguageException couldn't convert
         */
        private final Color findColor(
                LogoObject param)
        throws
                LanguageException
        {
                if (param instanceof LogoWord)
                {
                        Color colorVal = (Color)(_colorNames.get(param.toCaselessString()));
                        if (colorVal == null)
                        {
                                throw new LanguageException("Color not found");
                        }
                        return colorVal;
                }
                else
                {
                        if (param.length() != 3)
                        {
                                throw new LanguageException("Three elements expected in color list");
                        }
                        int r=0;
                        int g=0;
                        int b=0;
                        try
                        {
                                r = ((LogoList)param).pickInPlace(0).toInteger();
                        }
                        catch (LanguageException e)
                        {
                                throw new LanguageException(e.getMessage() + " in red value of color list");
                        }
                        try
                        {
                                g = ((LogoList)param).pickInPlace(1).toInteger();
                        }
                        catch (LanguageException e)
                        {
                                throw new LanguageException(e.getMessage() + " in green value of color list");
                        }
                        try
                        {
                                b = ((LogoList)param).pickInPlace(2).toInteger();
                        }
                        catch (LanguageException e)
                        {
                                throw new LanguageException(e.getMessage() + " in blue value of color list");
                        }
                        if (r < 0 || r > 255)
                        {
                                throw new LanguageException("Bad color (red value out of range)");
                        }
                        if (g < 0 || g > 255)
                        {
                                throw new LanguageException("Bad color (green value out of range)");
                        }
                        if (b < 0 || b > 255)
                        {
                                throw new LanguageException("Bad color (blue value out of range)");
                        }
                        return new Color(r, g, b);
                }
        }


        /**
         * Update graphics window's turtle info
         */
        private final void updateTurtle()
        {
                if (_graphWind != null)
                {
                        _graphWind.setTurtleState(_xpos, _ypos,
                                (90.0-_heading)/180.0*Math.PI,
                                _turtleVisible, _wrapState == WS_WRAP);
                }
        }


        /**
         * Draw a line in the graphics context
         */
        private final void drawLineTo(
                double x2,
                double y2)
        {
                if (_graphWind != null && _penState != PS_UP)
                {
                        if (_penState == PS_DOWN)
                        {
                                _graphContext.setColor(_penColor);
                        }
                        else
                        {
                                _graphContext.setColor(_backColor);
                        }
                        _graphContext.drawLine((int)(Math.round(_xhalfsize+_xpos)),
                                (int)(Math.round(_yhalfsize-_ypos)),
                                (int)(Math.round(_xhalfsize+x2)),
                                (int)(Math.round(_yhalfsize-y2)));
                }
        }


        /**
         * Wrap coordinate into window bounds
         *
         * @param val value
         * @param limit value limit
         *
         * @return wrapped value
         */
        private final double wrapCoordinate(
                double val,
                double limit)
        {
                double limit2 = limit*2.0;
                double ret;
                
                if (val < 0)
                {
                        ret = val + (limit2*Math.floor((-val)/limit2)) + limit2;
                }
                else
                {
                        ret = val - (limit2*Math.floor(val/limit2));
                }
                if (ret > limit-0.5)
                {
                        return ret-limit2;
                }
                else
                {
                        return ret;
                }
        }


        /**
         * Fix angle (make it between 0 and 360)
         *
         * @param ang angle
         *
         * @return fixed angle
         */
        private final double fixAngle(
                double ang)
        {
                if (ang < 0)
                {
                        return ang + (360.0*Math.floor((-ang)/360.0)) + 360.0;
                }
                else
                {
                        return ang - (360.0*Math.floor(ang/360.0));
                }
        }


        /**
         * Setxy wrap helper
         *
         * @param xval x coordinate
         * @param yval y coordinate
         *
         * @return new xval
         */
        private final double wrapXMinus(
                double xval,
                double yval)
        {
                double newy = _ypos+(-_xhalfsize-0.5-_xpos)/(xval-_xpos)*(yval-_ypos);
                drawLineTo(-_xhalfsize-0.5, newy);
                _xpos = _xhalfsize-0.5;
                _ypos = newy;
                updateTurtle();
                return xval + _xhalfsize*2.0;
        }


        /**
         * Setxy wrap helper
         *
         * @param xval x coordinate
         * @param yval y coordinate
         *
         * @return new yval
         */
        private final double wrapYMinus(
                double xval,
                double yval)
        {
                double newx = _xpos+(-_yhalfsize-0.5-_ypos)/(yval-_ypos)*(xval-_xpos);
                drawLineTo(newx, -_yhalfsize-0.5);
                _ypos = _yhalfsize-0.5;
                _xpos = newx;
                updateTurtle();
                return yval + _yhalfsize*2.0;
        }


        /**
         * Setxy wrap helper
         *
         * @param xval x coordinate
         * @param yval y coordinate
         *
         * @return new xval
         */
        private final double wrapXPlus(
                double xval,
                double yval)
        {
                double newy = _ypos+(_xhalfsize-0.5-_xpos)/(xval-_xpos)*(yval-_ypos);
                drawLineTo(_xhalfsize-0.5, newy);
                _xpos = -_xhalfsize-0.5;
                _ypos = newy;
                updateTurtle();
                return xval - _xhalfsize*2.0;
        }


        /**
         * Setxy wrap helper
         *
         * @param xval x coordinate
         * @param yval y coordinate
         *
         * @return new yval
         */
        private final double wrapYPlus(
                double xval,
                double yval)
        {
                double newx = _xpos+(_yhalfsize-0.5-_ypos)/(yval-_ypos)*(xval-_xpos);
                drawLineTo(newx, _yhalfsize-0.5);
                _ypos = -_yhalfsize-0.5;
                _xpos = newx;
                updateTurtle();
                return yval - _yhalfsize*2.0;
        }


        /**
         * Move to x and y coordinate
         *
         * @param xval x coordinate
         * @param yval y coordinate
         *
         * @exception virtuoso.logo.TurtleBoundsException turtle out of bounds
         */
        private final void moveTo(
                double xval,
                double yval)
        throws
                LanguageException
        {
                if (_wrapState == WS_FENCE &&
                        (xval < -_xhalfsize || xval > _xhalfsize-1 ||
                        yval < -_yhalfsize || yval > _yhalfsize-1))
                {
                        throw new LanguageException("Turtle out of bounds");
                }
                if (_wrapState == WS_WRAP)
                {
                        while (true)
                        {
                                if (xval < -_xhalfsize-0.5)
                                {
                                        if (yval < -_yhalfsize-0.5)
                                        {
                                                if ((-_xhalfsize-0.5-_xpos)*(yval-_ypos) > (-_yhalfsize-0.5-_ypos)*(xval-_xpos))
                                                {
                                                        yval = wrapYMinus(xval, yval);
                                                }
                                                else
                                                {
                                                        xval = wrapXMinus(xval, yval);
                                                }
                                        }
                                        else if (yval > _yhalfsize-0.5)
                                        {
                                                if ((-_xhalfsize-0.5-_xpos)*(yval-_ypos) < (_yhalfsize-0.5-_ypos)*(xval-_xpos))
                                                {
                                                        yval = wrapYPlus(xval, yval);
                                                }
                                                else
                                                {
                                                        xval = wrapXMinus(xval, yval);
                                                }
                                        }
                                        else
                                        {
                                                xval = wrapXMinus(xval, yval);
                                        }
                                }
                                else if (xval > _xhalfsize-0.5)
                                {
                                        if (yval < -_yhalfsize-0.5)
                                        {
                                                if ((_xhalfsize-0.5-_xpos)*(yval-_ypos) < (-_yhalfsize-0.5-_ypos)*(xval-_xpos))
                                                {
                                                        yval = wrapYMinus(xval, yval);
                                                }
                                                else
                                                {
                                                        xval = wrapXPlus(xval, yval);
                                                }
                                        }
                                        else if (yval > _yhalfsize-0.5)
                                        {
                                                if ((_xhalfsize-0.5-_xpos)*(yval-_ypos) > (_yhalfsize-0.5-_ypos)*(xval-_xpos))
                                                {
                                                        yval = wrapYPlus(xval, yval);
                                                }
                                                else
                                                {
                                                        xval = wrapXPlus(xval, yval);
                                                }
                                        }
                                        else
                                        {
                                                xval = wrapXPlus(xval, yval);
                                        }
                                }
                                else
                                {
                                        if (yval < -_yhalfsize-0.5)
                                        {
                                                yval = wrapYMinus(xval, yval);
                                        }
                                        else if (yval > _yhalfsize-0.5)
                                        {
                                                yval = wrapYPlus(xval, yval);
                                        }
                                        else
                                        {
                                                break;
                                        }
                                }
                        }
                }
                drawLineTo(xval, yval);
                _xpos = xval;
                _ypos = yval;
                updateTurtle();
        }


        /**
         * Reset color palette
         */
        private final void resetPalette()
        {
                _colorNames = new Hashtable();
                
                _colorNames.put(new CaselessString("0"), Color.black);
                _colorNames.put(new CaselessString("1"), Color.blue);
                _colorNames.put(new CaselessString("2"), Color.green);
                _colorNames.put(new CaselessString("3"), Color.cyan);
                _colorNames.put(new CaselessString("4"), Color.red);
                _colorNames.put(new CaselessString("5"), Color.magenta);
                _colorNames.put(new CaselessString("6"), Color.yellow);
                _colorNames.put(new CaselessString("7"), Color.white);
                
                _colorNames.put(new CaselessString("black"), Color.black);
                _colorNames.put(new CaselessString("blue"), Color.blue);
                _colorNames.put(new CaselessString("cyan"), Color.cyan);
                _colorNames.put(new CaselessString("darkgray"), Color.darkGray);
                _colorNames.put(new CaselessString("gray"), Color.gray);
                _colorNames.put(new CaselessString("green"), Color.green);
                _colorNames.put(new CaselessString("lightgray"), Color.lightGray);
                _colorNames.put(new CaselessString("magenta"), Color.magenta);
                _colorNames.put(new CaselessString("orange"), Color.orange);
                _colorNames.put(new CaselessString("pink"), Color.pink);
                _colorNames.put(new CaselessString("red"), Color.red);
                _colorNames.put(new CaselessString("white"), Color.white);
                _colorNames.put(new CaselessString("yellow"), Color.yellow);
        }
        
        
        /**
         * Update graph window
         */
        synchronized final void updateWind()
        {
                if (_graphWind != null)
                {
                        _graphWind.update();
                }
        }


        /**
         * Redisplay thread inner class
         */
        final class RedisplayThread
        extends Thread
        {

                private boolean _toggle;
                private boolean _alive;
                private int _interval;


                /**
                 * Constructor
                 */
                RedisplayThread()
                {
                        _toggle = false;
                        _alive = true;
                        _interval = 0;
                }


                /**
                 * Toggle the thread
                 */
                synchronized final void toggle()
                {
                        _toggle = true;
                }


                /**
                 * Kill the thread
                 */
                synchronized final void kill()
                {
                        _alive = false;
                        notifyAll();
                }


                /**
                 * Set the thread's tick interval
                 */
                synchronized final void setInterval(
                        int val)
                {
                        _interval = val;
                        notifyAll();
                }


                /**
                 * Run the thread
                 */
                public void run()
                {
                        boolean update = false;
                        
                        while (_alive)
                        {
                                synchronized(this)
                                {
                                        try
                                        {
                                                if (_interval > 0)
                                                {
                                                        wait(_interval);
                                                }
                                                else
                                                {
                                                        wait();
                                                }
                                        }
                                        catch (InterruptedException e) {}
                                        update = (_alive && _toggle);
                                        _toggle = false;
                                }
                                if (update)
                                {
                                        TurtlePrimitives.this.updateWind();
                                }
                        }
                }

        }


        /**
         * Graphics window inner class
         */
        final class TurtleWind
        extends Frame
        {
                /**
                 * Serialization version.
                 */
                final static long serialVersionUID = 1L;
                
                private Image _offImage;
                private int _xsize;
                private int _ysize;
                
                private double _turtleX;
                private double _turtleY;
                private double _turtleHead;
                private boolean _turtleVis;
                private boolean _turtleWrap;


                /**
                 * Create a TurtleWind
                 */
                TurtleWind()
                throws
                        LanguageException
                {
                        setLayout(new BorderLayout());
                        setResizable(false);
                        setTitle("Logo Graphics");
                        _xsize = _xhalfsize*2;
                        _ysize = _yhalfsize*2;
                        setSize(_xsize, _ysize);
                        setVisible(true);
                        _offImage = createImage(_xsize, _ysize);
                        if (_offImage == null)
                        {
                                throw new LanguageException("Unable to open graphics window");
                        }
                        _graphContext = _offImage.getGraphics();
                        _graphContext.setColor(Color.black);
                        _graphContext.fillRect(0, 0, _xsize, _ysize);
                        _graphContext.setFont(Font.decode("Monospaced"));
                }


                /**
                 * Reset a TurtleWind
                 */
                final void reset()
                throws
                        LanguageException
                {
                        if (_xsize != _xhalfsize*2 || _ysize != _yhalfsize*2)
                        {
                                _offImage = createImage(_xhalfsize*2, _yhalfsize*2);
                                if (_offImage == null)
                                {
                                        throw new LanguageException("Unable to resize graphics window");
                                }
                                _xsize = _xhalfsize*2;
                                _ysize = _yhalfsize*2;
                                setResizable(true);
                                setSize(_xsize, _ysize);
                                setResizable(false);
                                _graphContext = _offImage.getGraphics();
                        }
                        _graphContext.setColor(Color.black);
                        _graphContext.fillRect(0, 0, _xsize, _ysize);
                        update();
                }


                /**
                 * Redraw the window
                 */
                public void paint(
                        Graphics g)
                {
                        update(g);
                }


                /**
                 * Update the window
                 */
                public synchronized void update()
                {
                        update(getGraphics());
                }


                /**
                 * Update the window
                 */
                public synchronized void update(
                        Graphics g)
                {
                        g.drawImage(_offImage, 0, 0, this);
                        if (_turtleVis)
                        {
                                g.setColor(Color.white);
                                drawTurtle(g, _turtleX, _turtleY);
                                if (_turtleWrap)
                                {
                                        if (_turtleX > _xhalfsize-20)
                                        {
                                                drawTurtle(g, _turtleX-_xsize, _turtleY);
                                                if (_turtleY > _yhalfsize-20)
                                                {
                                                        drawTurtle(g, _turtleX, _turtleY-_ysize);
                                                        drawTurtle(g, _turtleX-_xsize, _turtleY-_ysize);
                                                }
                                                else if (_turtleY < -_yhalfsize+20)
                                                {
                                                        drawTurtle(g, _turtleX, _turtleY+_ysize);
                                                        drawTurtle(g, _turtleX-_xsize, _turtleY+_ysize);
                                                }
                                        }
                                        else if (_turtleX < -_xhalfsize+20)
                                        {
                                                drawTurtle(g, _turtleX+_xsize, _turtleY);
                                                if (_turtleY > _yhalfsize-20)
                                                {
                                                        drawTurtle(g, _turtleX, _turtleY-_ysize);
                                                        drawTurtle(g, _turtleX+_xsize, _turtleY-_ysize);
                                                }
                                                else if (_turtleY < -_yhalfsize+20)
                                                {
                                                        drawTurtle(g, _turtleX, _turtleY+_ysize);
                                                        drawTurtle(g, _turtleX+_xsize, _turtleY+_ysize);
                                                }
                                        }
                                        else
                                        {
                                                if (_turtleY > _yhalfsize-20)
                                                {
                                                        drawTurtle(g, _turtleX, _turtleY-_ysize);
                                                }
                                                else if (_turtleY < -_yhalfsize+20)
                                                {
                                                        drawTurtle(g, _turtleX, _turtleY+_ysize);
                                                }
                                        }
                                }
                        }
                }


                /**
                 * Draw the turtle
                 */
                final void drawTurtle(
                        Graphics g,
                        double x,
                        double y)
                {
                        int x1 = _xhalfsize+(int)(Math.round(x+TURTLE_HEIGHT*Math.cos(_turtleHead)));
                        int y1 = _yhalfsize-(int)(Math.round(y+TURTLE_HEIGHT*Math.sin(_turtleHead)));
                        int x2 = _xhalfsize+(int)(Math.round(x+TURTLE_HALF_WIDTH*Math.sin(_turtleHead)));
                        int y2 = _yhalfsize-(int)(Math.round(y-TURTLE_HALF_WIDTH*Math.cos(_turtleHead)));
                        int x3 = _xhalfsize+(int)(Math.round(x-TURTLE_HALF_WIDTH*Math.sin(_turtleHead)));
                        int y3 = _yhalfsize-(int)(Math.round(y+TURTLE_HALF_WIDTH*Math.cos(_turtleHead)));
                        g.drawLine(x1, y1, x2, y2);
                        g.drawLine(x2, y2, x3, y3);
                        g.drawLine(x3, y3, x1, y1);
                }


                /**
                 * Set turtle state
                 */
                final void setTurtleState(
                        double x, // screen coordinates
                        double y, // screen coordinates
                        double h, // standard polar coordinates
                        boolean v,
                        boolean w)
                {
                        _turtleX = x;
                        _turtleY = y;
                        _turtleHead = h;
                        _turtleVis = v;
                        _turtleWrap = w;
                        redisplay();
                }


                /**
                 * Redisplay screen
                 */
                final void redisplay()
                {
                        if (_redisplayInterval == 0)
                        {
                                update();
                        }
                        else if (_redisplayInterval > 0)
                        {
                                _thread.toggle();
                        }
                }

        }

}



