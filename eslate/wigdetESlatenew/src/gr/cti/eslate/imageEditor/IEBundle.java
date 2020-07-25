package gr.cti.eslate.imageEditor;

import java.util.ListResourceBundle;

public class IEBundle extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents= { {"Error","Error"}, {"Cancel","Cancel "}, {"okDialog","OK "}, {"IconEditorMsg1","Save icon "}, {"IconEditorMsg2","Image "}, {"IconEditorMsg3","Open icon "}, {"IconEditorMsg4","Image too large (w: "}, {"IconEditorMsg5"," h: "}, {"IconEditorMsg6",") to fit in the icon editor "}, {"IconEditorMsg7","Clear "}, {"IconEditorMsg8","Icon editor "}, {"IconEditorMsg9","Pen color "}, {"IconEditorMsg10","Color Chooser "}, {"IconEditorMsg11","Background color "},
			{"IconEditorMsg12","Select "}, {"IconEditorMsg13","Draw "}, {"IconEditorMsg14","Clear selection "}, {"IconEditorMsg15","Fill selection "}, {"IconEditorMsg16","Invert selection "}, {"IconEditorMsg17","Load icon "}, {"IconEditorMsg18","Save icon "}, {"IconEditorMsg19","Unable to create Image. File \""}, {"IconEditorMsg20","\" does not exist "}, {"IconEditorMsg21","Color picker "}, {"IconEditorMsg22","Unable to open the icon in the IconEditor. The icon is greater than 32x32 "},
			{"IconEditorMsg23","Grid On "}, {"IconEditorMsg24","Grid Off "}, {"IconEditorMsg25","Cell size "}, {"IconEditorMsg26","Rubber "}, {"IconEditorMsg27","Pen "}, {"IconEditorMsg28","Pick Foreground Color "}, {"IconEditorMsg29","Pick Background Color "}, {"IconEditorMsg30","Pan "}, {"IconEditorMsg31","Icon Dimension "}, {"IconEditorMsg32","zoom "}, {"IconEditorMsg33","unzoom "}, {"IconEditorMsg34","Magic Wand "}, {"IconEditorMsg35","New Image "}, {"pin","Icon"},
			{"componame","Image Editor"}, {"IconEditorMsg36","Send Image "}, {"big","Very large image "}, {"alert","Alert !! "}, {"alertPalette","Alert !! Icon Palette modified..."}, {"alertColorPalette","Alert !! Color Palette modified..."},

			{"saving","Do you want to save this image ?"}, {"clickto","Click to"}, {"preview","Preview"}, {"imgprev","Image Preview"}, {"loading","Loading Image..."}, {"IconChooser","Icon Chooser"}, {"openPalette","Open Palette"}, {"savePalette","Save Palette"}, {"wannaSavePalette","Do you want to save the icon palette into file?"}, {"wannaSaveColorPalette","Do you want to save the color palette into file?"},

			{"newPalette","New Palette"}, {"palette","Palette"}, {"saveThisPalette?","   Do you want to save this palette? "}, {"yes","Yes"}, {"no","No"}, {"addtopalette","Add to palette"}, {"cPalette","Color Palette"}, {"iPalette","Icon Palette"}, {"colorPalette","Colors"}, {"iconPalette","Icons"}, {"noimage","No such file "}, {"cannotLoad1","Image "}, {"cannotLoad2"," can not open!! "}, {"line","Line "}, {"rectangle","Rectangle "}, {"ellipse","Ellipse "}, {"poly","PolyLine "},
			{"file","File "}, {"new","New... "}, {"openM","Open... "}, {"saveM","Save "}, {"printPr","Print Preview... "}, {"print","Print... "}, {"exit","Exit "}, {"image","Image "}, {"imsize","Image Size... "}, {"clearim","Clear Image... "}, {"grid","Grid... "}, {"zoom","Zoom... "}, {"initiallevel","Initial Level "}, {"tools","Tools "}, {"newIconPalette","New Icon Palette... "}, {"openIconPalette","Open Icon Palette... "}, {"saveIconPalette","Save Icon Palette... "},
			{"newColorPalette","New Color Palette... "}, {"openColorPalette","Open Color Palette... "}, {"saveColorPalette","Save Color Palette... "}, {"cropText","Crop "}, {"undoText","Undo "}, {"copyText","Copy "}, {"cutText","Cut "}, {"pasteText","Paste "}, {"jpggif","Save as .gif(OK) or .jpg(NO)?"}, {"ChooseType","Choose save type"}, {"ok","OK"}, {"cancel","CANCEL"}, {"select","Choose the Image Dimensions"}, {"xdim","Width: "}, {"ydim","Height: "}, {"fFill","Flat fill "},
			{"totalColors"," Number of total colors "}, {"customColors"," Number of custom colors "}, {"totalIcons"," Number of icons "}, {"edit","Edit "}, {"pencilMode","Pencil Mode "}, {"lineMode","Line Mode "}, {"rectMode","Rectangle Mode "}, {"ellMode","Ellipse Mode "}, {"polyMode","Polyline Mode "}, {"rubberMode","Rubber Mode "}, {"wandMode","Magic Wand Mode "}, {"panMode","Pan Mode "}, {"pickMode","Pick Mode "}, {"flatMode","Flat fill Mode "}, {"selectMode","Select Mode "},
			{"remSelMode","Remove From Selection Mode "}, {"addSelMode","Add To Selection Mode "}, {"imSize","Image Size:  "}, {"cell","Point:  "}, {"pleasewait","Please Wait ...  "}, {"noflatMode","No flat fill allowed here! "}, {"SaveIm","Do you want to save the image?"}, {"warnClose","Warning! Closing Image Editor component..."},

	};
}