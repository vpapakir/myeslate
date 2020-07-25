//This тranslatiоn was adapted from a technical report about
//the translation of the terappin Logo

//5-2-1999: moved many primitives to Turtle Component's primitives' localization bundle
//20Sep1999: moved TELL/ASK/EACH to ComponentPrimitives localization bundle
//28Jun2000: added again "TO" and "END" and also added "TOMACRO" 

package gr.cti.eslate.scripting.logo;

import java.util.ListResourceBundle;

public class PrimitivesBundle_el_GR extends ListResourceBundle {
    public Object [][] getContents() {
        return contents;
    }

    static final Object[][] contents={
     
{"ABS","ап"},
{"AGET","ппаяе"},
{"ARRAY","пимайас"},
{"ARRAY?","пимайас?"},
{"ARRAYDIMS","пдиастасеис"},
{"ASET","пхе"},
{"BITXOR","апг"},

{"BL","ет"},
{"BUTFIRST","ейтоспяытоу"},
{"BUTLAST","ейтостекеутаиоу"},
{"BUTTON?","йоулпи?"},

{"CHAR","ваяайтгяас"},

{"FILLARRAY","пцелисе"},
{"LISTARRAY","пимайасекиста"},
{"AND","йаи"},
{"ARCTAN","тонеж"},

{"BEFORE?","пяим?"},
{"BF","еп"},
{"BITAND","ьйаи"},
{"BITOR","ьг"},
{"CLEARKEYS","сбгсепкгйтяа"},
{"CLEARTEXT","сбгсейеилемо"},
{"CONTINUE","сумевисе"},
{"COUNT","летягсе"},
{"COPYDEF","ояисесам"},
{"COS","сумгл"},
{"DATE","глеяа"},
{"DEBUG","диояхысг"},
{"DEFINE","ояисе"},

{"DIVIDE","диа"},
{"DOT?","текеиа?"},
//11-9-1998:wrote my own "цяаье" primitive implementation// {"EDIT","цяаье"},
{"EDALL","цяока"},
{"EDNAME","цяаьето"},
{"EDPROPS","цяаьеидиот"},

{"EJECT","енацыцг"},
{"EMPTY?","адеио"},

{"END","текос"}, //28Jun2000: removed from hard-coded

{"EQUAL?","исо?"},
{"ERALL","сбгсетапамта"},
{"ERASE","сбгсе"},
{"ER","сб"},
{"ERASEFILE","сбгсеаявеио"},
{"ERNAME","сбгсеомола"},

{"ERP","сбд"}, //28-8-1998
{"ERASEPROCEDURE","сбгседиадийасиа"}, //28-8-1998

{"ERPROPS","сбгсеидиот"},
{"ERROR","кахос"},
{"EXP","ейх"},

{"FIRST","пяыто"},
{"FORWARD","лпяоста"},
{"FPRINT","аятупысе"},
{"FPUT","бакепяыто"},
{"GO","пгцаиме"},
{"GOODBYE","текосеяцасиас"},
{"GPROP","паяеид"},

{"INRECT?","сепаяак?"},
{"IF","ам"},
{"INTEGER","айеяаиос"},
{"ITEM","амтийеилемо"},
{"KEYSDOWN","пкгйтяа"},
{"LAST","текеутаио"},

{"LIST","киста"},
{"LIST?","киста?"},
{"LOWERCASE","пефа"},
{"LOAD","жоятысе"},

{"LOADPICT","жоятысееийома"}, //Could be implemented by the Canvas or TV component
{"LOADTEXT","жоятысейеилемо"}, //Could be implemented by the LOGO component

{"LOCAL","топийа"},
{"LPUT","бакетекеутаио"},
{"MAKE", "жтиане"}, //31May2000: new greek name so that it doesn't conflict with SET or DO primitives' greek names
{"MEMBER?","амгйеи?"},
{"MINUS","леиом"},
{"MOUSE","помтийи"},
{"NAMES","омолата"},
{"ONLINE","сумдисйои"},
{"NODEBUG","овидиояхысг"},
{"NOT","ови"},
{"NUMBER?","аяихлос?"},
{"OP","ен"},
{"OR","г"},
{"OUTPUT","енодос"},
{"PARSE","летацкытисе"},
{"PAUSE","паусг"}, //Could be implemented by the LOGO component

{"PI","пи"},
{"PLIST","идиотгтес"},
{"PLUS","сум"},

{"POALL","тупысетапамта"},
{"PONAME","тупысето"},
{"POPROPS","тупысеидиот"},
{"PPPATTеRN","сведиостуко"},
{"POWER","думалг"},
{"PR","туп"},
{"PREFIX","пяохела"},
{"PRINTOUT","ейтупысе"},
{"PROCEDURE?","диадийасиа?"},
{"PROCEDURES","диадийасиес?"},
{"PRINT","тупысе"},
{"PUTPROP","бакеидиот"},

{"PX","са"},
{"QUOTIENT","пгкийо"},
{"RANDOM","туваио"},
{"RC?","упаявеиваяайтгяас?"},
{"RC","двая"},
{"READCHARACTER","диабасеваяайтгяа"},
{"READLIST","диабасекиста"},
{"RL","дк"},
{"READSTRING","диабсулбокосеияа"},
{"REMAINDER","упокоипо"},
{"REMPROP","сбидиот"},
{"RENAMEFILE","летомоласеаявеио"},
{"REPEAT","епамакабе"},
{"ROUND","стяоццукопоигсг"},

{"RUN","тяене"}, //Could be implemented by the LOGO component
{"SAVE","апохгйеусг"},
{"SAVEPICT","апохгйеусгеийомас"},
{"SAVETEXT","апохгйеусгйеилемоу"},

{"SE","пя"},
{"SENTENCE","пяотасг"},

{"SETPREFIX","хесепяохела"},
{"SETRANDOM","хесетуваио"},

{"SETSHAPE","хесесвгла"},
{"SETTFONT","хесецяаллатосеияа"},
{"SETTSTYLE","хесестук"},
{"SETTWINDOW","хесепаяахуяойеилемоу"},

{"SHAPE","свгла"},

{"SIN","гл"},
{"SQRT","яифа"},

{"STAMP","сжяажида"}, //should move to Canvas component's primitive bundle

{"STRING","сулбокосеияа"},
{"STRING?","сулбокосеияа?"},

{"STOP","сталатгсе"}, //Could be implemented by the LOGO component

{"TAN","еж"},

{"TEXT","йеилемо"},

{"THING","омтотгта"},
{"THING?","омтотгта?"},
{"TIMES","епи"},
{"TIME","ыяа"},

{"TO","циа"}, //28Jun2000: removed from hard-coded
{"TOMACRO","циалайяо"}, //28Jun2000: removed from hard-coded

{"TSTYLE","стукйеилемоу"},
{"TFONT","цяаллатйеилемоу"},

{"TWINDOW","паяахйеилемоу"},
{"UPPERCASE","йежакаиа"},
{"WAIT","пеяилеме"},
{"WHO","поиес"},
{"WRAP","амадипкысг"},
{"WORD","кенг"},
{"WORD?","кенг?"},

{"REPCOUNT","епамакгьеис"}, //Birb

{"PRINTOUTPROCEDURE","тупыседиадийасиа"}, //28-8-1998
{"POP","туд"} //28-8-1998

   };

}
