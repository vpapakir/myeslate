package gr.cti.eslate.scripting.logo;

import gr.cti.eslate.imageEditor.ImageEditor;

import java.awt.Color;
import java.util.Vector;

import virtuoso.logo.Console;
import virtuoso.logo.InterpEnviron;
import virtuoso.logo.LanguageException;
import virtuoso.logo.LogoList;
import virtuoso.logo.LogoObject;
import virtuoso.logo.LogoVoid;
import virtuoso.logo.Machine;
import virtuoso.logo.MyMachine;
import virtuoso.logo.PrimitiveGroup;
import virtuoso.logo.SetupException;

public class ImageEditorPrimitives extends PrimitiveGroup {
	MyMachine myMachine;

	protected void setup(Machine machine,Console console) throws SetupException {
		registerPrimitive("IMSETICON","pSETICON",1);
		registerPrimitive("IMCLEAR","pCLEAR",0);
		registerPrimitive("IMSETGRID","pSETGRID",1);
		registerPrimitive("IMSETFOREGROUNDCOLOR","pSETFOREGROUNDCOLOR",1);
		registerPrimitive("IMSETBACKGROUNDCOLOR","pSETBACKGROUNDCOLOR",1);
		registerPrimitive("IMNEWICON","pNEWICON",0);
		registerPrimitive("IMSAVEICON","pSAVEICON",0);
		registerPrimitive("IMDRAW","pDRAW",1);
		registerPrimitive("IMERASE","pERASE",1);
		registerPrimitive("IMFILL","pFILL",4);
		registerPrimitive("IMPICKFOREGROUNDCOLOR","pPICKFOREGROUNDCOLOR",2);
		registerPrimitive("IMPICKBACKGROUNDCOLOR","pPICKBACKGROUNDCOLOR",2);
		registerPrimitive("IMSELECT","pSELECT",1);
		registerPrimitive("IMADDTOSELECTION","pADDTOSELECTION",1);
		registerPrimitive("IMREMOVEFROMSELECTION","pREMOVEFROMSELECTION",1);
		registerPrimitive("IMFILLSELECTION","pFILLSELECTION",0);
		registerPrimitive("IMINVERTSELECTION","pINVERTSELECTION",0);
		registerPrimitive("IMCLEARSELECTION","pCLEARSELECTION",0);
		registerPrimitive("IMSELECTCELLS","pSELECTCELLS",1);
		registerPrimitive("IMSETCELLSIZE","pSETCELLSIZE",1);
		registerPrimitive("IMZOOM","pZOOM",0);
		registerPrimitive("IMUNZOOM","pUNZOOM",0);
		registerPrimitive("IMSHOWCELL","pSHOWCELL",2);
		registerPrimitive("IMSETIMAGESIZE","pSETIMAGESIZE",2);
		registerPrimitive("IMDRAWELLIPSE","pDRAWELLIPSE",1);
		registerPrimitive("IMFILLELLIPSE","pFILLELLIPSE",1);
		registerPrimitive("IMDRAWCIRCLE","pDRAWCIRCLE",3);
		registerPrimitive("IMFILLCIRCLE","pFILLCIRCLE",3);
		registerPrimitive("IMDRAWLINE","pDRAWLINE",4);
		registerPrimitive("IMDRAWRECTANGLE","pDRAWRECTANGLE",4);
		registerPrimitive("IMFILLRECTANGLE","pFILLRECTANGLE",4);
		myMachine=(MyMachine) machine;
		if (console != null)
			console.putSetupMessage("Loaded Image Editor primitives");
	}

	public final LogoObject pSETICON(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		String iconPath=aLogoObject[0].toString();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			try {
				iEditor.setIcon(iconPath);
			} catch (Exception e) {
				throw new LanguageException("No such image");
			}
		}
		return LogoVoid.obj;
	}

	public final LogoObject pCLEAR(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,0);
		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);

		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.clearImage();
		}
		return LogoVoid.obj;
	}

	public final LogoObject pSETGRID(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		boolean gridChoice=aLogoObject[0].toBoolean();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.setGrid(gridChoice);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pSETFOREGROUNDCOLOR(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		if (aLogoObject[0] instanceof LogoList) {
			LogoList list=(LogoList) aLogoObject[0];
			int listSize=list.length();
			int r=list.pick(1).toInteger();
			int g=list.pick(2).toInteger();
			int b=list.pick(3).toInteger();

			Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
			for (int i=0;i < v.size();i++) {
				ImageEditor iEditor=(ImageEditor) v.elementAt(i);
				if (listSize == 4) {
					int a=list.pick(4).toInteger();
					iEditor.setForegroundColor(new Color(r,g,b,a));
				}
				if (listSize == 3)
					iEditor.setForegroundColor(new Color(r,g,b));
			}
		}

		return LogoVoid.obj;
	}

	public final LogoObject pSETBACKGROUNDCOLOR(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		if (aLogoObject[0] instanceof LogoList) {
			LogoList list=(LogoList) aLogoObject[0];
			int listSize=list.length();
			int r=list.pick(1).toInteger();
			int g=list.pick(2).toInteger();
			int b=list.pick(3).toInteger();

			Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
			System.out.println("size: " + v.size());
			for (int i=0;i < v.size();i++) {
				ImageEditor iEditor=(ImageEditor) v.elementAt(i);
				if (listSize == 4) {
					int a=list.pick(4).toInteger();
					iEditor.setBackgroundColor(new Color(r,g,b,a));
				}
				if (listSize == 3)
					iEditor.setBackgroundColor(new Color(r,g,b));
			}
		}

		return LogoVoid.obj;
	}

	public final LogoObject pNEWICON(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,0);
		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);

		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.createNewImage();
		}
		return LogoVoid.obj;
	}

	public final LogoObject pSAVEICON(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,0);
		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);

		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.saveSelected();
		}
		return LogoVoid.obj;
	}

	public final LogoObject pDRAW(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		if (aLogoObject[0] instanceof LogoList) {
			LogoList list=(LogoList) aLogoObject[0];
			int listSize=list.length();

			int x=list.pick(1).toInteger();
			int y=list.pick(2).toInteger();

			Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
			for (int i=0;i < v.size();i++) {
				ImageEditor iEditor=(ImageEditor) v.elementAt(i);
				if (listSize == 2)
					iEditor.drawIconPoint(x,y);
			}
		}
		return LogoVoid.obj;
	}

	public final LogoObject pDRAWELLIPSE(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		if (aLogoObject[0] instanceof LogoList) {
			LogoList list=(LogoList) aLogoObject[0];

			int startX=list.pick(1).toInteger();
			int startY=list.pick(2).toInteger();
			int endX=list.pick(3).toInteger();
			int endY=list.pick(4).toInteger();

			Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
			for (int i=0;i < v.size();i++) {
				ImageEditor iEditor=(ImageEditor) v.elementAt(i);
				iEditor.drawLogoEllipse(startX,startY,endX,endY);
			}
		}
		return LogoVoid.obj;
	}

	public final LogoObject pFILLELLIPSE(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		if (aLogoObject[0] instanceof LogoList) {
			LogoList list=(LogoList) aLogoObject[0];

			int startX=list.pick(1).toInteger();
			int startY=list.pick(2).toInteger();
			int endX=list.pick(3).toInteger();
			int endY=list.pick(4).toInteger();

			Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
			for (int i=0;i < v.size();i++) {
				ImageEditor iEditor=(ImageEditor) v.elementAt(i);
				iEditor.fillLogoEllipse(startX,startY,endX,endY);
			}
		}
		return LogoVoid.obj;
	}

	public final LogoObject pDRAWCIRCLE(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,3);
		int centerX=aLogoObject[0].toInteger();
		int centerY=aLogoObject[1].toInteger();
		int rad=aLogoObject[2].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.drawLogoCircle(centerX,centerY,rad);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pFILLCIRCLE(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,3);
		int centerX=aLogoObject[0].toInteger();
		int centerY=aLogoObject[1].toInteger();
		int rad=aLogoObject[2].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.fillLogoCircle(centerX,centerY,rad);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pDRAWLINE(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,4);
		int startX=aLogoObject[0].toInteger();
		int startY=aLogoObject[1].toInteger();
		int endX=aLogoObject[2].toInteger();
		int endY=aLogoObject[3].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.drawLogoLine(startX,startY,endX,endY);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pDRAWRECTANGLE(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,4);
		int startX=aLogoObject[0].toInteger();
		int startY=aLogoObject[1].toInteger();
		int endX=aLogoObject[2].toInteger();
		int endY=aLogoObject[3].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.drawLogoRectangle(startX,startY,endX,endY);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pFILLRECTANGLE(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,4);
		int startX=aLogoObject[0].toInteger();
		int startY=aLogoObject[1].toInteger();
		int endX=aLogoObject[2].toInteger();
		int endY=aLogoObject[3].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.fillLogoRectangle(startX,startY,endX,endY);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pERASE(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		if (aLogoObject[0] instanceof LogoList) {
			LogoList list=(LogoList) aLogoObject[0];

			int listSize=list.length();
			int x=list.pick(1).toInteger();
			int y=list.pick(2).toInteger();

			Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
			for (int i=0;i < v.size();i++) {
				ImageEditor iEditor=(ImageEditor) v.elementAt(i);
				if (listSize == 2) {
					Color previousColor=iEditor.getForegroundColor();
					iEditor.setForegroundColor(Color.white);
					iEditor.drawIconPoint(x,y);
					iEditor.setForegroundColor(previousColor);
				}
			}
		}
		return LogoVoid.obj;
	}

	public final LogoObject pFILL(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,4);
		int x=aLogoObject[0].toInteger();
		int y=aLogoObject[1].toInteger();
		int width=aLogoObject[2].toInteger();
		int height=aLogoObject[3].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.fillLogoArea(x,y,width,height);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pPICKFOREGROUNDCOLOR(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,2);
		int x=aLogoObject[0].toInteger();
		int y=aLogoObject[1].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.pickLogoForeground(x,y);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pPICKBACKGROUNDCOLOR(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,2);
		int x=aLogoObject[0].toInteger();
		int y=aLogoObject[1].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.pickLogoBackground(x,y);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pSELECT(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		if (aLogoObject[0] instanceof LogoList) {
			LogoList list=(LogoList) aLogoObject[0];
			int listSize=list.length();

			int x=list.pick(1).toInteger();
			int y=list.pick(2).toInteger();

			Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
			for (int i=0;i < v.size();i++) {
				ImageEditor iEditor=(ImageEditor) v.elementAt(i);
				if (listSize == 4) {
					int width=list.pick(3).toInteger();
					int height=list.pick(4).toInteger();
					iEditor.selectLogoArea(x,y,width,height);
				}
			}
		}
		return LogoVoid.obj;
	}

	public final LogoObject pADDTOSELECTION(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		if (aLogoObject[0] instanceof LogoList) {
			LogoList list=(LogoList) aLogoObject[0];
			int listSize=list.length();

			int x=list.pick(1).toInteger();
			int y=list.pick(2).toInteger();

			Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
			for (int i=0;i < v.size();i++) {
				ImageEditor iEditor=(ImageEditor) v.elementAt(i);
				if (listSize == 4) {
					int width=list.pick(3).toInteger();
					int height=list.pick(4).toInteger();
					iEditor.addToLogoSelectionArea(x,y,width,height);
				}
			}
		}
		return LogoVoid.obj;
	}

	public final LogoObject pREMOVEFROMSELECTION(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		if (aLogoObject[0] instanceof LogoList) {
			LogoList list=(LogoList) aLogoObject[0];
			int listSize=list.length();

			int x=list.pick(1).toInteger();
			int y=list.pick(2).toInteger();

			Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
			for (int i=0;i < v.size();i++) {
				ImageEditor iEditor=(ImageEditor) v.elementAt(i);
				if (listSize == 4) {
					int width=list.pick(3).toInteger();
					int height=list.pick(4).toInteger();
					iEditor.removeFromLogoSelectionArea(x,y,width,height);
				}
			}
		}
		return LogoVoid.obj;
	}

	public final LogoObject pFILLSELECTION(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,0);
		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);

		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.fillSelectedArea();
		}
		return LogoVoid.obj;
	}

	public final LogoObject pINVERTSELECTION(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,0);
		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);

		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.invertSelectedArea();
		}
		return LogoVoid.obj;
	}

	public final LogoObject pCLEARSELECTION(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,0);
		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);

		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.clearSelectedArea();
		}
		return LogoVoid.obj;
	}

	public final LogoObject pSELECTCELLS(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		if (aLogoObject[0] instanceof LogoList) {
			LogoList list=(LogoList) aLogoObject[0];
			int listSize=list.length();
			int r=list.pick(1).toInteger();
			int g=list.pick(2).toInteger();
			int b=list.pick(3).toInteger();

			Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
			for (int i=0;i < v.size();i++) {
				ImageEditor iEditor=(ImageEditor) v.elementAt(i);
				if (listSize == 4) {
					int a=list.pick(4).toInteger();
					iEditor.selectCells(new Color(r,g,b,a));
				}
				if (listSize == 3)
					iEditor.selectCells(new Color(r,g,b));
			}
		}

		return LogoVoid.obj;
	}

	public final LogoObject pSETCELLSIZE(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,1);
		int cellSize=aLogoObject[0].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.setLogoCellSize(cellSize);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pZOOM(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,0);

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.zoomOperation("increase",1);
		}
		return LogoVoid.obj;
	}

	public final LogoObject pUNZOOM(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,0);

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.unzoomOperation();
		}
		return LogoVoid.obj;
	}

	public final LogoObject pSHOWCELL(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,2);
		int x=aLogoObject[0].toInteger();
		int y=aLogoObject[1].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			boolean invalid=iEditor.showCell(x,y);
			if (invalid)
				throw new LanguageException("Out of range");
		}
		return LogoVoid.obj;
	}

	public final LogoObject pSETIMAGESIZE(InterpEnviron interpenviron,LogoObject[] aLogoObject) throws LanguageException {
		testNumParams(aLogoObject,2);
		int xsize=aLogoObject[0].toInteger();
		int ysize=aLogoObject[1].toInteger();

		Vector v=myMachine.componentPrimitives.getComponentsToTell(gr.cti.eslate.imageEditor.ImageEditor.class);
		for (int i=0;i < v.size();i++) {
			ImageEditor iEditor=(ImageEditor) v.elementAt(i);
			iEditor.setLogoImageSize(xsize,ysize);
		}
		return LogoVoid.obj;
	}
}
