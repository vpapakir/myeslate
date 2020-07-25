//should remove all DoublePoint2D stuff after Sun makes Point2D serializable (this means we won't run in Java1.2 but only in Java1.3 and above - Sun still hasn't done this!!!): remove all Java2D dependencies by creating my own Vector class to abstract 2D points and 2D velocity, acceleration etc.

//PRESSING SHIFT and dragging a Control Point should result in dragging all cp's at this place [from top to bottom) and not only the top one (needed some times)

//..add Bezier splines (see implementation using parametric curves at that QD3D article at home)

//...add a button for two way bind... also the split routine should disconnect veto listeners

//commandSO.setCommand(s,topLevelClock);

/*
 1.0 - 23Mar1999 - first creation
 1.1 - 24Mar1999 - adding objects manipulatable through Control Points
 1.2 - 25Mar1999 - added Zoom In/Out (ZoomOut has some bug)
                 - now we also have Spring objects and Slope objects
 1.3 - 26Mar1999 - now the viewToWorld transformation for mouse events works OK
                 - when moving over a control point, the cursor changes to a cross-hair
                 - now unZoom works OK
 1.4 -  2Apr1999 - objects now have color and location manipulated via scripting
                 - "File/New" now clears the view (removes all objects)
 1.5 -  8Apr1999 - "File/New" also revokes all objects from Scripting name service
                 - more scripting support in all physics objects
                 - the Physics Scene (PhysicsPanel) is now scriptable
                 - fixed Slope object to use degrees instead of radians for its angle
       15Apr1999 - now has TimeSO input pin (connects to MasterClock)
                 - demo: passes MasterClock's tick events to Logo by calling proc: TICK <time>
                 - now can save/load to/from an .slt using MasterControl
                 - now can drag arround objects (if we drag them from some place other than their visible control points)
                 - using a HAND_CURSOR when over a visible control point else a MOVE_CURSOR when over an object else a CROSSHAIR_CURSOR
                 - holding down CTRL while dragging results in panning the scene, even if we're over a visible control point or an object
                 - holding down SHIFT (when no CTRL is pressed) while dragging results in moving an object if we pressed the mouse button on own, even if we pressed it on a visible control point (if both CTRL and SHIFT are pressed, CTRL [view panning] takes precedence) (if SHIFT is pressed while dragging a control point and under the point we first clicked there isn't some object part, then the view is panned)
       16Apr1999 - fixed-bug: CTRL isn't needed to drag the scene (can be used to make sure that the scene is dragged even if we pressed the mouse button on a visible control point or object)
 1.6 -  1Jun1999 - allowing programmatic batch refresh control (both from Java and from Logo)
                 - caching calculated shapes and not recalculating an Object's shape (geometry) if it hasn't changed [that is its update() method han't been called]
                 - if some property of a physic's object is set to its current value, its ignored, instead of recalculating it and updating the view
     -  2Jun1999 - added LENGTH/SETLENGTH to SquareBox and Slope objects
 2.0 - 21Jun1999 - porting to new E-Slate
       29Jun1999 - ControlPoints are now ESlate objects
                 - ControlPoints are now LogoScriptable
                 - Added "Object" or "Control Point" selection support (CTRL+click)
                 - Now can "Cut" to remove any selected objects
                 - Control Points can be bound to one another in order to synchornize
       30Jun1999 - Added Quadratic and Cubic curves and moved more of Physic's objects' tasks to base PhysicsObject
        1Jul1999 - Moved binding tasks from ControlPoints to Constraint objects (now can bind into shapes, at min/max dist from points, do n-tier bindings etc.)
                 - Added point to shape binding button
 2.1 - 22Jul1999 - Adding constraints with more than 2 members
                 - now ALT is used to change the default drag action, cause CTRL was used to select objects & control points
                 - moved getAppletInfo() method to PhysicsApplet class
                 - fixed-bug: now can use SHIFT again to always drag the object we clicked onto, even if the place we clicked on it was a control point's area
                 - now dragging an object that belongs to the current "object selection" translates all of the selected ones, instead of just the one the mouse was pressed upon
                 - fixed-bug: can instantiate Physics' Scene objects via scripting again (Logo's "Physics.makeObject")
                 - fixed-bug: when the physics' scene, its background image (if any) does get removed
                 - PhysicsObjects are now ESlate objects
                 - PhysicsObject's now contain atomic "update" routines, where updating is done after all of their inner objects (the control points) have been changed (for example, the "translate" routine was updating() as many times as the object' control points while these ones were getting translated)
     - 26Jul1999 - fixed-bug: when changed the constraints to support more than 2 members, made a typo at AbstractMasterSlavesConstraint and was setting by mistake as master the 1st of the slave members of the constraint
                 - can right-click on objects to show popup menu (only for objects for now: contains the "bring-to-front" and "send-to-back" actions)
     - 29Jul1999 - modified the constraints architecture to use two static method to create or to create&enforce constraints
                 - now all the are kept at the PhysicsPanel and are not held by their members
 2.2 -  3Aug1999 - moved to package gr.cti.eslate
        8Aug1999 - fixed distance constraints: there was a case where trying to enforce them would throw a "division by zero" error
                 - readded: now after setting a point-point constraint, we clear the control points selection
                 - added point-point offset contraints
                 - now selectAll works OK
 2.3 - 10Aug1999 - fixed-bug: was showing a greek word at the menus when in English locale
     - 11Aug1999 - added "Permanent Point Distance From Point" constraint
                 - added "Points Median Point" constraint
 2.4 - 20Aug1999 - removed debug messages from AbstractConstraint class
                 - added "accuracy" instead of "absolute" distance and offset comparisons at the respective constraints
 2.5 - 27Aug1999 - now Control Points and PhysicsObject use NameSupport objects, so that they can easily switch between being E-Slate objects or just Scriptable objects (for now set to the last one, so that Physics can be placed in the container which doesn't support nested E-Slate object hierarchies)
     - 28Aug1999 - now "clearScene" method implementation of PhysicsPanel (and thus the File/New menu option) removes all GUI components that have been added to the panel (for example text areas)
                 - added console error messages if a shape can't be created for some reason
     - 29Aug1999 - when closing Physics, objects are removed from scripting's name service
     - 30Aug1999 - bug-fix: Spring object was forgetting to ask for LengthPrimitives
                 - bug-fix: now when clearing the scene, the location label doesn't get removed
 2.6 - 16Sep1999 - temp: constructor failures are assumed to be due to non-Java2 compatibility of the running JVM and exception with appropriate message is thrown (in the future check for JVM version number >=2 or something like that)
     - 24Sep1999 - added a "setDoubleBuffered(true)" call in PhysicsPanel's constructor
     - 30Sep1999 - paintComponent now checks if view<->world transforms have been calculated and doesn't throw exceptions
     -  6Oct1999 - mouse event handlers now check if view<->world transforms have been calculated and doesn't throw exceptions
 2.7 - 12Oct1999 - PhysicsObject's Velocity&Acceleration properties are now initialized to "new DoublePoint(0,0)"
                 - Spring object now has serialVersionUID, is Externalizable and stores/retrieves its springConstant and physicalLength properties
 2.8 - 13Oct1999 - SETACCELERATION Logo primitive now works OK (was calling HasVelocity.setVelocity instead of HasAcceleration.setAcceleration)
                 - Scene.DISABLEREFRESH is now correctly mapped to its Greek name (had a [duplicate] entry that was matching Scene.ENABLEREFRESH with its correct Greek name and with DISABLEREFRESH's Greek one too)
                 - Scene.SETCOLOR is now correctly mapped to its Greek name (had a [duplicate] entry that was matching SETVELOCITY with SETCOLOR's Greek name)
 2.9 - 15Oct1999 - ACCELERATION/SETACCELERATION Logo primitives' implementations were searching the TELL vector for a HasVelocity object instead of a HasAcceleration object
                 - PhysicsPanel (Scene) is now saving/loading it's color
                 - PhysicsPanel now has storable boolean property "CoordinatesLabelVisible"
                 - Physics now has "CoordinatesLabelVisible" property (exposing the PhysicsPanel object's property to access from E-Slate's property editor) [this property is stored by the PhysicsPanel itself, not by Physics]
                 - Physics now has storable "MenuBarVisible" and "ToolBarVisible" properties
 3.0 - 16Oct1999 - Added SerialVersionUID field to PhysicsPanel and to Physics classes (so that new versions load OK with new data and reverse)
                 - now all of [Physics, PhysicsPanel, ControlPoint, PhysicsObject, Spring] which are Externalizable have serialVersionUID and try/catch arround their writeExternal/readExternal implementations
 3.1 - 17Oct1999 - trying to store/load worldToViewTransform
                 - renamed Spring's "physicalLength" property to "naturalLength" (and corresponding Logo primitives)
     - 18Oct1999 - now resize doesn't change the WorldToViewTransform if one exists
 3.2 - 19Oct1999 - added "serialVersionUID" to Ball, CubicCurve, QuadraticCurve, PolyLine and Rope objects (shouldn't need it cause their ancestor [PhysicsObject] does the save/load, but Java seems to need it at all descendents if we change the ancestor's code)
 3.3 - 20Oct1999 - made constraints serializable
                 - PhysicsPanel.setWorldToViewTransform doesn't print the AffineTransform to the Java console anymore
 3.4 - 20Oct1999 - CoordinatesLabel now is by default visible (so that new physics' instances show it when started)
 3.5 - 22Oct1999 - PhysicsPanel now has properties "ObjectMovementEnabled" and "ControlPointMovementEnabled" (storable)
                 - Physics now has "ObjectMovementEnabled" and "ControlPointMovementEnabled" properties (exposing the respective PhysicsPanel object's property to access from E-Slate's property editor) [this properties are stored by the PhysicsPanel itself, not by Physics]
 3.6 - 29Oct1999 - now all "physical models" that PhysicsObject class was implementing are grouped in the AsPhysicsObject model (and PhysicsObject class implements this one instead)
                 - now both AsSpring and AsSlope models extend AsPhysicsObject
     - 30Oct1999 - added "getScene" method to Physics class, so that having a Physics instance, some Java code can get the scene object, that is an AsPhysicsScene interface implementor (the PhysicsPanel)
     - 31Oct1999 - now Ball object implements the AsBall interface (also found out that it wasn't declaring that it implemented HasWidth/HasHeight interfaces: now AsBall extends these)
                 - now boxes implement the AsBox interface (it extends just the HasWidth & HasHeight for now)
                 - removed the "implements IControlPointContainer" declaration from Spring, Slope & Box classes (they don't need to say they implement IControlPointContainer, since their ancestor - PhysicsObject - does that already [and also loads/saves the states of a shape's control points])
                 - added HasAppliedForce model (interface) and had PhysicsObject implement it
                 - added APPLIEDFORCE/SETAPPLIEDFORCE primitives (and their Greek localization)
                 - now AsSpring model extends the HasAngle model too
                 - now Spring object asks for AnglePrimitives Logo PrimitiveGroup to be loaded
                 - fixed Offset constraints to work with the renamed method "getOffsetedCopy" of Point2DUtilities.class (from gr.cti.shapes package / shapes.jar)
     -  2Nov1999 - added "translate" method to PhysicsPanel to pan/tilt the view
                 - added Physics.TranslateView Logo primitive
                 - named the "Displacement" property of AsSpring model (Spring object implements it) and defined DISPLACEMENT/SETDISPLACEMENT Logo primitives (and their Greek translations)
                 - added HasAltitude model, extended by AsPhysicsObject model
                 - ControlPoint class implements HasAltitude model and asks for AltitudePrimitives to be loaded
                 - added ALTITUDE/SETALTITUDE Logo primitives (AltitudePrimitives Logo primitive group) [and their Greek translations]
                 - now PhysicsObject's LogoScriptable interface implementation asks for KineticEnergyPrimitives, AppliedForcePrimitives and AltitudePrimitives to be loaded
 3.7 -  2Nov1999 - fixed Spring object implementation to ask for SpringPrimitives primitive group too to be loaded (had replaced it [at 31Oct1999] with AnglePrimitives by accident, instead of adding AnglePrimitives aswell)
 3.8 -  3Nov1999 - now PhysicsPanel's "paintControlPoint" method implementation reuses the same Circle2D instance instead of creating one for each control point, just to paint it (to minimize object allocation)
     -  8Nov1999 - now Ball object's "calculateShape" method implementation reuses the same (per ball instance) Circle2D instance instead of creating a new one every time (to minimize object allocation)
 3.9 - 14Nov1999 - now PhysicsPanel's "paintComponent reuses a transform object to keep the old transform (to restore it after it does its drawing)
     - 15Nov1999 - added marksOverShapes property, initially set to "false" (now grid&axis by default show below the control points & the shapes)
                 - PhysicsPanel's properties now throw property change events
                 - now Box object's "calculateShape" method implementation reuses the same (per box instance) Rectangle2D instance instead of creating a new one every time (to minimize object allocation)
                 - now RoundBox object's "calculateShape" method implementation reuses the same (per roundbox instance) RoundRectangle2D instance instead of creating a new one every time (to minimize object allocation)
                 - now RoundBox shows indeed with rounded corners! (using arcWidth=10, arcHeight=10 for corner trimming)
 4.0 - 15Nov1999 - now Rope object's "calculateShape" method implementation reuses the same (per rope instance) Line2D.Double instance instead of creating a new one every time (to minimize object allocation)
                 - now ControlPoint.getLocation2D doesn't return a copy of the internal location keeper, but a reference to it.
                   ControlPoint.setLocation2D method always makes a new point cause the "old location" point is needed together with the "new locaiton" point at the property change/vetoablechange event passed to the "constraints" subsystem.
                   Clients are sure the ControlPoint's setLocation2D won't touch the data in some Point2D return by a previous getLocation2D.
                   However clients should never change the data in the Point2D returned by ControlPoint.getLocation2D (and by PhysicsObject's HasLocation2D.getLocation2D() implementation) cause other clients that had called getLocation2D while the object was still, will see their point data change (unless they had copied/replicated its data after getting the Point2D object)
 5.0 - 25Nov1999 - using new mechanism for object proeprties storage (with HashTables)
     - 26Nov1999 - renamed from Physics to Scene and moved everything to package gr.cti.eslate.stage
                 - restructured gr.cti.eslate.stage.constraints package, adding "base" and "exception" subpackages
                 - commented out the plugs' creation code (to remove the LogoCommandSO and TickSO plugs)
     - 27Nov1999 - View/Grid menu item now allows toggling its state and is synchronized with the ShowGrid button on the toolbar
     - 28Nov1999 - fixed Box's setObjectWidth method to work OK when passing it a non-zero width when current width is 0 (it failed to change a width=0 setting)
 5.1 - 29Nov1999 - renamed the "New>" submenu to "New object>" and moved to the Edit menu
 6.0 - 29Nov1999 - renamed the main component to Stage and the document to Scene
                 - using aggregation for scripting the Stage component by talking to it as if you were talking to the Scene object
                 - using new mechanism for Stage's proeprties storage (with HashTables)
     - 30Nov1999 - now loading ScenePrimitives OK (was searching for old class name: PhysicsScenePrimitives)
                 - using new mechanism for Scene's proeprties storage (with HashTables)
                 - scene object now registers with localized name to scripting service
                 - control points now register with localized names to scripting service
                 - Box, RoundBox, SquareBox, Ball, Spring, Slope and Rope objects now register with localized names to scripting service
 6.1 -  1Dec1999 - changed menus layout
                 - localized popup menu of objects
 6.2 -  2Dec1999 - localized constraint names and messages
                 - now constraint buttons start disabled and are enabled when an action has been done at their popup.
                   If they get pressed from then on, they invoke the last action from their popup
 6.3 -  8Dec1999 - added preliminary support for customizers
     - 14Dec1999 - now when calling SceneObject.setLocation using a location same as the object's current location, nothing is done
                 - more customizer support added (SceneObjectCustomizer and PhysicsObjectCustomizer)
                 - now setAppliedForce calls setAcceleration(new DoublePoint2D(0,0)) if mass==0 instead of throwing a division-by-zero exception (maybe should set to [INFINITE,INFINITE])
 6.4 - 16Dec1999 - now PhysicsObject.getKineticEnergy returns a correct result
 6.5 - 21Dec1999 - SceneObjectCustomizer now lays out OK with SmartLayout
                 - PhysicsObjectCustomizer now uses SmartLayout
                 - DoublePoint2DWidget now uses SmartLayout
                 - localized the customizer dialogs
 6.6 - 22Dec1999 - now Scene also tries to locate an object from any control point of it we might have pressed a mouse button on
                 - fixed Spring, so that when doing setAngle(...) movingEdge rotates and not just moves up-down
                 - fixed bug: when CTRL+CLICKING on an object or a control point, the customizer dialog doesn't show up anymore (it shows only when clicking the object or control point with no CTRL pressed)
                 - now allowing SHIFT to force object selection when CTRL+SHIFT+CLICKING on a control point (instead of selecting the control point)
                 - added SpringCustomizer
 6.7 - 12Jan2000 - added capability to SceneObjectCustomizer to use IconEditorDialog to edit an object's image/texture
 6.8 - 19Jan2000 - now added the "HasLocation2D" interface to the list of interfaces that the "AsPhysicsObject" interface is extending
 7.0 - 12Feb2000 - now ControlPoint&SceneObject both descend from BaseObject and not from JComponent
                 - now the constraints subsystem works with Object[] instead of JComponent[] for constraint members (Masters usually implement the HasBoundProperties i/f and Slaves the HasVetoableProperties one)
 7.1 - 23Feb2000 - now Scene's mousePressed/mouseMoved/mouseDragged event handlers don't create any new Point2D objects, but reuse the same ones
                 - moved all GUI menu/toolbar Actions code relating to scene manipulation to SceneActions
                 - renamed "coordinatesLabelVisible" property to "coordinatesVisible" (older saved state will now load with the default value for this property)
                 - added "Coordinates" toggling option at the "View" menu
 7.2 - 24Feb2000 - now when adding a new object, it won't do a repaint (and thus show up) if refresh is disabled
                 - not registering the Scene object with the scripting nameService anymore, can talk to it indirectly through the Stage object that hosts it (else talking without using ask, resulted in talking to both the Stage->Scene and the Scene directly, thus doing all actions twice!)
                 - added "cloneNamedObject" method to Scene class and "SCENE.CLONEOBJECT" primitive (and corresponding Greek localized primitive)
                 - the "Scene.clearScene" Logo primitive was renamed to "Scene.clear"
                 - the Greek localized primitive for "Scene.clear" now should work OK and has been renamed to a shorter name
                 - BaseObject and its descendents (SceneObject,PhysicsObject,Ball etc.) now support cloning via the "clone()" method
                 - the Stage class is now implementing the LogoScriptable interface, returning the LogoPrimitiveGroups' classnames that the Scene class (its document class) supports
                 - Customizers were enabled again since SmartLayout 1.1.1 was released!
                 - added "marksOverShapes" and "gridType" properties to Stage class that delegate their get/set calls to the respective Scene properties
                 - allowing transparent colors to be set to objects through scripting!
 7.3 - 25Feb2000 - BaseObject, SceneObject and PhysicsObject classes are now correctly cloneable doing a deep copy of their data (the clone is registered with a unique name based on its generator/source object's name)
                 - using new storage mechanism for the String object, old Spring data won't load anymore
                 - Spring is now cloneable doing a deep copy of its data
                 - Scene's "gridVisible", "gridType" and "image" properties made storable
                 - added "gridVisible" and "image" properties to Stage class that delegate their get/set calls to the respective Scene properties
                 - bug-fix: now having a null value for the image property of Scene or SceneObject won't throw errors while saving state
                 - "removeSceneObject(String name)" method of Scene class is now implemented, so SCENE.REMOVEOBJECT Logo primitive should now work
 7.4 - 26Feb2000 - started adding events to Scene (for ControlPoints' or SceneObjects' manipulation via the mouse)
 7.5 - 31Mar2000 - now an object generated by "Scene.cloneObject" is added just below its "genetor" object and not on top of all scene objects
     -  1Apr2000 - added "SceneObject cloneSceneObject(SceneObject)" method to Scene class
                 - added "bringToFront(String objName)" and "sendToBack(String objName)" methods
                 - now if Scene's "bringToFront(Object)" and "sendToBack(Object)" methods are passed in a null param they do nothing
                 - added "SCENE.BRINGTOFRONT" and "SCENE.SENDTOBACK" Logo primitives for Scene (and their Greek localized versions)
                 - fixed-bug in "SCENE.MAKEOBJECT" and "SCENE.CLONEOBJECT" Logo primitives' implementations: was talking to all Scenes, instead of the first one found (these are functions, can't return many results, just one!)
                 - fixed-bug (introduced in 31Mar2000) in Scene's cloneSceneObject method: now correctly returning the clone and not the genetor object
 7.6 -  3Apr2000 - now names' comparison at "Scene.findObjectByName" method is case insensitive (for Greek too) and "Greek punctuation insensitive"
 7.7 - 12Apr2000 - now supporting Cut/Copy/Paste
                 - Cut/Copy/Paste can be done throughout Scenes (only in the same JVM for now)
 7.8 - 13Apr2000 - added the missing Copy & Paste buttons to the toolbar
 7.9 - 13Apr2000 - method "clearScene" of Scene class now doen't do a repaint if refresh is disabled
                 - added popup when clicking to an empty area of the scene, containing common actions like Cut/Copy/Paste/Clear/Clone, Print etc.
                 - renamed method "clear" of Scene class to "clearSelection" (though clearSelection refers to "clear" action of Scene that doesn't clear the whole scene but only the current selection [to be consistent with WordPad])
                 - removed the icon for the "clear" edit action (maybe should have an icon that shows a selection and a red "X", but since selections aren't necessary rectangular, can't represent them with an icon easily)
                 - on mouse exit from scene, the coordinates label text is now emptied (in case the coordinates label is visible, we wouldn't want it to keep showing the last coordinate it had when we moved the mouse to some other component)
                 - added "(De)select" action to objects' popup menu
                 - added cut/copy/clear & clone actions to objects' popup menu - these actions work on the current selection if the object is a member of it, else current selection isn't used (selection remains untoutched, except in the cut one object case, where it is removed from the current selection if its a member of it)
                 - added Clone item to the "Edit" menu (to clone a selection)
                 - fixed-bug in Scene: now instantiating the clipboard at startup (would cause an Exception to be thrown when just doing Cut/Copy of one object or when doing Paste without having first done a Copy)
                 - fixed-bug in Scene: Cut was disposing objects before placing them in the clipboard
 8.0 - 14Apr2000 - now using Java2 printing API instead of the [broken?] Java1.1 printing API, still doesn't work though!
                 - coordinates label now shows x,y coordinates rounded to a maximum of 3 decimal digits
 8.1 - 14Apr2000 - some changes in Print action implementation, still doesn't work though
                 - right-aligned all labels in SceneObjectCustomizer, PhysicsObjectCustomizer and SpringCustomizer
                 - changed PhysicsObjectCustomizer's sizing so that its text labels aren't cut off
                 - added SlopeCustomizer
                 - fixed-potential-bug in CustomizerDialog which wouldn't allow more than 1 non-default customizers to be setup
                 - readded DoublePoint2DWidget source file to the Stage's JBuilder project (had been removed somehow)
     - 15Apr2000 - now when control points aren't visible and the cursor moves over one of them, it doesn't change to a hand cursor
                 - now customizers' text fields show allow +/- to be entered (fixed utilsBirb.jar's JNumberField class [need 20000415 version of it])
                 - all numberic fields in customizers now have a maximum of 3 decimal digits
 8.2 - 19Apr2000 - the print icon's background is now transparent and the icon's filesize is much smaller (reduced its color depth)
                 - fixed-bug: had wrong resource key at Constraints Greek localization bundle for the title of PointsMedianConstraint (was displaying the english message)
                 - now constraint setting error dialog doesn't show "null" on the dialog when the exception object returns a null error description message
                 - now printing a stack trace whenever a user constraint can't be set (sometimes it's OK to refuse the setting of a constraint, however in some cases it might be a bug and the stack trace would be useful for the bug report)
                 - AbstractMasterPointSlavePointContraint's "storeMembers" method now doesn't throw "ArrayOutOfBoundsException" when the number of members passed to it is not 2, but instead throws IllegalMembersException with a localized explanation method
                 - BaseObject class is now Externalizable and works as the base object implementing the ObjectHash persistence mechanism
                 - SceneObject class now allows its ancestor (BaseObject) to read/write properties to the ObjectHash during persistence/restoration of state
                 - ControlPoint class is now using the ObjectHash persistence mechanism
                 - fixed-bug: now constraint setting menus still work after Stage has been restored from saved state
     - 20Apr2000 - reorganized package structure (moved some classes arround and created package "gr.cti.eslate.stage.customizers.models")
                 - added "axisVisible" and "controlPointsVisible" properties to Stage class that delegate their get/set calls to the respective Scene properties
                 - now Scene class is storing/loading its AxisVisible property when writing/reading its persistent state
                 - now Stage has beanInfo with localized property names
 8.3 - 20Apr2000 - now Stage specifies a 16x16 icon in its beanInfo
 8.4 - 21Apr2000 - added "Properties..." localized item at objects' popup menu to show an object's customizer
 8.5 - 23Apr2000 - implemented "Image" drawing (with rotation if they implement HasAngle) for SceneObjects' that have this property set (ignoring their Shape in that case [if they have one])
                 - Ball and Rope objects now implement "HasAngle" interface
 8.6 - 26Apr2000 - added Rectangular area selection tool (sets new selection containing all objects intersecting the selected area) [holding down CTRL appends those to the current selection]
 8.7 - 26Apr2000 - fixed-bug: removeObject now checks for null parameters and doesn't throw errors: this was affecting the LOGO primitive "Scene.removeObject" which was throwing NullPointerException when a non-existing object name was passed to it as a parameter
                 - removed View/Grid>Lines option from the menus: not impemented yet!
                 - removed all "gr.cti.shapes.DoublePoint2D" references from DoublePoint2DWidget's and now using plain "java.awt.Point2D" type
                 - fixed a layout bug in the name field of SceneObject's customizer
                 - now SceneObject's customizer shows the icon on the image button which has been made bigger. User can set a new image by editing the current (if any) or making a new image with IconEditor and then after previewing it on the image button, press Apply or OK at the customizer dialog
 8.8 -  8May2000 - when pressing OK or Cancel, was just setting a customizer dialog to invisible, now calling "dispose" on it to destroy the dialog's window peer and release its contained widgets etc.
                 - fixed a bug at SceneObjectCustomizer: wasn't checking for a null icon (when object has no image assigned to it)
                 - fixed a bug affecting PhysicsObjectCustomizer: DoublePoint2DWidget's getPoint() method now returns "gr.cti.eslate.DoublePoint2D" type again, since PhysicsObjects use that type for storing 2D properties like velocity, acceleration etc.
                 - CustomizerDialog's "OK" and "Apply" button handlers now catch exceptions and printing a stack trace (OK wasn't able to close the dialog if some exception was thrown from customizer page's "setupObjectFromWidgets" method)
                 - optimized the for-loop at SceneObject's translate method. This method is called from setLocation2D, so that one should now be a bit faster too
 9.0 -  9May2000 - SceneObject.setImage(String) now can accept an empty (or null) string as a filename to remove the current image [used in Logo with (SETIMAGE ") to remove the current image of a scene object]
                 - added preliminary support for saving image snapshots of the current Scene view
 9.1 - 10May2000 - now can save image snapshots of the current Scene view as GIF files, using the menus/toolbar or scripting
 9.2 - 11May2000 - synchronizing UI with Scene's axisVisible, GridVisible etc. properties at startup and after every state loading
                 - using a plain checkbox under View menu for Grid and not a checkable-submenu, since we only have one Grid type (dots) for now
                 - name property storage was moved to BaseObject from its SceneObject descendent, so that the ControlPoint descendent of BaseObject saves its name too (needed for Constraints' persistence).
                 - added serialVersionID field at BaseObject class so that it can load older saved data
     - 12May2000 - Customizer dialog now has localized title
                 - SpringCustomizer now shows its widzets' labels correctly
                 - now when ESC is pressed on the customizer dialog, it is cancelled/closed
                 - !!! temp: localizing ImageEditor dialog's title before showing it (ask N.Drossos to do that)
                 - !!! constraints are almost storable (not yet: can't be restored cause control points seem to be changing names or something) !!!
 9.3 - 12May2000 - menu elements that show dialogs now have a ... suffix at their caption
                 - the "controlPointsVisible" property has been added to the beanInfo of the Stage component
                 - the "image" property has been added to the beanInfo of the Stage component
                 - synchronizing UI with Scene's axisVisible, GridVisible etc. properties whenever they're changed via Stage's respective properties that delegate to the scene
                 - at Scene's popup (when right clicking at empty space inside the Scene), now showing Cut/Copy/Clone/Clear options only if there's a selection
                 - at Scene's popup (when right clicking at empty space inside the Scene), now showing Paste option only if there's content at the clipboard
                 - now Scene flushes its old background image (if any) before changing it at setImage() method
                 - now SceneObject flushes its old image (if any) before changing it at setImage() method
                 - now SceneObject removes its old image (if any) at dispose() method call
 9.4 - 15May2000 - changed the way "setControlPoints" worked at SceneObject class so that it doesn't just copy the locations of the ControlPoints passed to it, but keep those control point instances
                 - changed CustomizerDialog: now it doesn't have an apply button anymore: changes are immediately done on the object and can press OK to accept them or Cancel to discard them and return to the previous (stored temporarily in memory) state
                 - now changing the color at the SceneObjectCustomizer removes any image that the object may have (re-setting it to use its Shape instead of a custom Image)
     - 16May2000 - Stage now has Load/Save options under the File menu for loading/saving a scene "document"
                 - now BaseObject is LogoScriptable
                 - fixed-bug: SceneObject wasn't asking for AltitudePrimitives to be loaded (now its ancestor BaseObject does take care of that)
                 - when a customizer widget changes an object's property, it asks for all the customizer pages to be updated, so that side-effects are immediately shown and all pages are kept in sync
     - 17May2000 - fixed-bug: "PhysicsObject.setAltitude(double)" method was just moving the 1st (center) control point of an object instead of translating the whole object on the Y axis
                 - Customizers now are operable, customizer pages stay in-sync and side-effects of a property change on other properties are instantly shown!
                 - added BallCustomizer
                 - fixed-bug: ControlPoint.setLocation(int,int) wasn't setting the requested location unless the ControlPoint had listeners attatched to it!
                 - ControlPoint's storage now doesn't depend on DoublePoint2D (no dependence on Java2D's Point2D structure). Compatibility has been kept with older saved data that used DoublePoint2D
     - 18May2000 - now BaseObject.setName doesn't accept an empty name, so user can't set an empty name for an object from the SceneObjectCustomizer
                 - Added BaseObjectCustomizer and moved some widgets from SceneObjectCustomizer to that one
                 - now with a left click on a visible control point a customizer dialog is shown for the control point itself and not for its container object (right click menu still refers to the whole object, so selecting "Properties..." from there will show the container object's properties: useful for Spring etc. which has a too thin shape to click)
                 - fixed-bug: SceneObject now unregisters from the nameservice all its control
                   points that are created at the component's no-parameters constructor, at
                   readExternal, just before it calls its parent's readExternal and the hashtable
                   with its saved controlPoints is loaded (those control points got registered and
                   clashed with the names of the had-forgotten-to-remove control points which are
                   made by the object's empty-constructor which is called by Java before
                   internalization is done)
                 - Constraints are now saved OK!
                 - added LOAD and SAVE buttons to the toolbar and made the default size a bit wider for the toolbar to show all of its buttons
 9.5 - 18May2000 - made default size a bit wider and added a separator between the New/Load/Save and Photo/Print options of the File menu and the toolbar
                 - Split action of Scene now works and removes constraints
                 - PointIntoShape constraint now shows a localized message
                 - now after loading a Scene "document", Stage's UI gets synchronized with the new state of the Scene
 9.6 - 19May2000 - not localizing anymore Image Editor dialog's title any more: version 1.9.16 and upwards of ImageEditor show a localized title
                 - now showing customizer pages with an order that goes from special to general (the last one is the one of the BaseObject)
                 - now an object's popup shows different title (either select or deselect) for toggleSelected objectAction depending on whether the object is already selected or not (that action toggle its selection state)
                 - not showing redundant separator at the start of the scene popup (was shown when both Cut/Copy/Clear/Clone and Paste actions were not available and thus not shown [no objects selection and clipboard empty cases respectively])
                 - now the clipboard is an array of bytes containing a serialized Vector of objects and not containing live objects (which caused problems with naming cause they and their children control points were still registered to the nameservice)
                 - fixed-bug: now when removing an object it is removed from the current objects selection as well if it's in it
                 - fixed-bug: now when removing an object all of its control points are removed from the current control points selection (in case they're selected)
                 - fixed-bug: now when removing an object all the constraints it takes part in are removed
                 - fixed-bug: now when removing an object all the constraints its control points take part in are removed
 9.7 - 21May2000 - fixed-bug: we copy the current selection Vector contents before we enumerate on its elements and call removeObject on each of them, cause removeObject now (since ver 9.6) removes the elements from the objectsSelection vector
                 - added load, save and photo sceneActions to Scene popup menu
                 - now the scene popup shows the "Select All" option only if the scene is not empty
                 - now the scene object internally reuses a "SceneActions" object instead of making a new one each time the user right-clicks on the scene to make&show the scene's popup
                 - added keyboard actions at Scene: DEL=clear, Ctrl+X=cut, Ctrl+C=copy, Ctrl+V=paste, Ctrl+N=new, Ctrl+L=load, Ctrl+S=save, Ctrl+F=Photo, Ctrl+P=Print, Ctrl+A=select all
                 - commented-out debugging messages of BaseObject
                 - fixed SceneObject's clone() method to work OK again (had been broken at version 9.4 cause of a change in the behaviour of SceneObject.setControlPoints)
                 - printing now partially works
 9.8 - 22May2000 - SceneCustomizer's Image button is now tall again (had re-made it small by accident) and places its label under its icon when it has one
 9.9 -  1Jun2000 - now Stage asks its live Scene document for what Logo primitive groups it wants to have loaded
                 - if the user presses Cancel at a scene load/save dialog just return silently and don't print a Java stack trace
     -  7Jun2000 - the file dialogs' greek titles now say "σκηνικού" instead of "σκηνής"
10.0 -  8Jun2000 - now AbstractConstraint and its descendents use the ObjectHash mechanism for persistence instead of standard Java serialization (so that fields can be added in the future at any place in the hierarchy)
                 - added serialVersionUID to all constraints classes
10.1 -  9Jun2000 - fixed-bug: due to an exception thrown from the "getSupportedPrimitiveGroups" method (LogoScriptable implementation) of Stage, Stage wasn't being registered correctly with E-Slate: didn't get a localized name at startup and didn't get notified to dispose itself when needed

1.0.0 - ?Aug2000 - E-Slate team changed versioning scheme, version is 1.0.0 now!
1.0.1 -   ???    - N.Drossos: fixed bug: Problem storing naturallenght spring property
                 - Number storageVersion (FieldMap) for all storable classes
                 - BeanInfo deficiencies + Border
1.0.2 -   ???    - N.Drossos: >>  - Credits changed
                                  - Deficiencies in English bundle
1.0.3 -   ???    - N.Drossos: >> Substitution of IconEditorDialog with ImageEditorDialog
1.0.4 - 29Mar2001 - G.Birbilis: added loading-only compatibility with old ObjectHash persistence mechanism
      -  2Apr2001 - G.Birbilis: fixed-bug: changed <String>.equals("") checks to <String>.length()==0
                  - G.Birbilis: fixed-bug: changed bringToFront(Object) and sendToBack(Object) at AsScene interface to bringToFront(String) and sendToBack(String) respectively (cause Logo's SCENE.BRINGTOFRONT/SCENE.SENDTOBACK primitives weren't working OK)
      -  3Apr2001 - G.Birbilis: fixed-bug: Scene's setImage(String) property was using Class.getResource which was resulting in OutOfMemoryErrors, fixed using "new ImageIcon"
      -  4Apr2001 - G.Birbilis: fixed Scene.setImage(ImageIcon) to accept null
                  - G.Birbilis: fixed setImage(String) of Scene and SceneObject to use "new ImageIcon" instead of "Res.loadImageIcon" which was trying to load a file from the component's jar
                  - G.Birbilis: added setImage(URL) method to SceneObject
1.0.5 - 11Dec2001 - K.Kyrimis: Fixed error() method in Scene.java, which was
                    infinitely recursive.
1.0.6 - 17Dec2001 - K.Kyrimis: Changed Scene.java and objects/SceneObject.java
                    to use NewRestorableImageIcon instead of GIFImageIcon,
                    which cannot handle images with more than 8 bits of depth.
1.0.7 - 25Jan2002 - K.Kyrimis: Added perrfomance manager support.
1.0.8 - 17Apr2002 - K.Kyrimis: Added perrfomance manager 1.7.17 support.

DEPENDENCIES (JARs):
 - ImageEditor
 - UtilsBirb
 - BirbBase
 - Shapes

TO DO:
       - would expect the Swing ColorChooser to allow one to set transparencies: need a custom color chooser page that allows transparent colors! (reported this to Sun for 1.3RC3 and they entered it as a bug)
       - add cut/copy/paste as "text-flavor" to clipboard so that can exchange data with Delphi app or cross JVMs
       - need icon for "Clone" action (for objects' popup)
       - fix printing! (it's partially working)
       - popups should always show inside the screen area and not have parts outsize of it
       - add "frame of reference" object (singleton???)
       - check if AffineTransforms do atomic operations (else bug report to Sun or ask them to add ThreadSafeAffineTransform class)
       - why doesn't the image property of Stage show up in the object inspector of E-Slate?
       - fix the customizer's cancel which is not restoring control-point locations problem!
*/

package gr.cti.eslate.stage;

import gr.cti.eslate.base.*;
import gr.cti.eslate.base.container.*;
import gr.cti.eslate.base.container.event.*;
import gr.cti.eslate.base.sharedObject.*;
import gr.cti.eslate.utils.*;

import javax.swing.*;

import java.awt.*;
import java.beans.*;
import java.io.*;

import gr.cti.utils.*;

import gr.cti.eslate.stage.models.AsScene;

/**
 * Stage component.
 *
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 * @version     2.0.13, 23-Jan-2008
 */
import gr.cti.eslate.scripting.HasFacets; //29Nov1999

/**
 * @version     2.0.11, 26-Jun-2007
 * @author      George Birbilis
 * @author      Nikos Drossos
 * @author      Kriton Kyrimis
 */
public class Stage extends JPanel implements gr.cti.eslate.scripting.LogoScriptable,
                                        SharedObjectListener, Externalizable, HasFacets, //29Nov1999
                                        PropertyChangeListener, ESlatePart { //18May2000: listens for changes of Scene properties to update its UI accordingly

  public final static String VERSION="2.0.13";
  //public final static String STORAGE_VERSION="2"; //5/6/2002
  public final static int STORAGE_VERSION=2; //6/6/2002
  private ESlateHandle stageHandle;
  // PERSISTENCE
  public static final String SCENE_PROPERTY="scene";
  public static final String MENUBAR_VISIBLE_PROPERTY="menuBarVisibleProperty";
  public static final String TOOLBAR_VISIBLE_PROPERTY="toolBarVisibleProperty";

  //private JCheckMenu gridMenu; //27Nov1999
  //private ClickSelector gridMenuSelector; //27Nov1999
  private Component pointPointConstraints, pointShapeConstraints;

  //Private Fields
  private Scene scene;
  //contains the command sent to LOGO
  //private transient LogoCommandSO commandSO=new LogoCommandSO(getESlateHandle());
  private transient JMenuBar menubar;
  private transient JToolBar toolbar;


  /**
   * Timer which measures the time required for loading the state of the
   * component.
   */
  PerformanceTimer loadTimer;
  /**
   * Timer which measures the time required for saving the state of the
   * component.
   */
  PerformanceTimer saveTimer;
  /**
   * The listener that notifies about changes to the state of the
   * Performance Manager.
   */
  PerformanceListener perfListener = null;

  //23Feb2000: must call setScene(Scene) on this object at setScene: reuse this object and
  //don't use the "actions" field of Scene (else would have to recreate the menus each time
  //another scene object is set)
  private SceneActions actions=new SceneActions();

  //16Oct1999: serial-version, so that new vers load OK
  static final long serialVersionUID = 16101999L;

  public Stage(){
    //System.out.println("Stage contructor Begin");
    //createPlugs();
    //GUI//
    //Container c=getContent();
    //24-2-1999: removed GridBagLayout (caused menu bar to disappear when resizing to a small height)
    setLayout(new BorderLayout());
    //make sure a scene object is created and assigned (should have been created already as a
    //side-effect of a call "LogoScriptable.getSupportedPrimitiveGroups" done by the ESlateHandle:
    //in case we're running outside of the E-Slate container, just make sure we have a scene at
    //startup)
    getScene();
    add(createMenus(),BorderLayout.NORTH);
    //c.add(makeStatusBar(),BorderLayout.SOUTH);
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.constructionEnded(this);
    //setUniqueComponentName(Res.localize("Stage"));
    //System.out.println("Stage contructor End");
  }

  /**
   * Returns Copyright information.
   * @return    The Copyright information.
   */
  public ESlateInfo getInfo()
  {
      return new ESlateInfo(Res.localize("title")+VERSION,Res.localizeArray("info"));
  }

/// Scene Object accessor /////////
  public Scene getScene(){ //30Oct1999
    //9Jun2000: need to do this so that we'll have a scene object available when
    //LogoScriptable.getSupportedPrimitiveGroups is called at startup
    if(scene==null)
      setScene(new Scene());
    return scene;
  }

  //don't pass null to this: if want to clear scene contents, do scene.dispose() and don't remove
  //the scene document
  public void setScene(Scene newScene){
    //11May2000: temp: just-for-safety: if null is passed in, clear the current scene-object
    //but don't remove it
    if(newScene==null){
      scene.dispose();
      return; //don't remove Stage from Scene's property change listeners
    }
    //remove the current document
    if(scene!=null){
      remove(scene);
      scene.removePropertyChangeListener(this); //18May2000
      scene.dispose(); //11May2000: dispose old scene and unregister its objects!
      //add(newScene,BorderLayout.CENTER);
    }
    //set as new document
    add(newScene,BorderLayout.CENTER);
    scene=newScene;
    actions.setScene(scene); //23Feb2000
    //19Apr2000: must do this else constraints won't work after the stage's restoration
    //from persisted state toolbar will be null the first time this is called, menus haven't
    //been yet created (it is handled OK by the implementation of that method, returns without
    //doing something, will do it later at toolbar creation)
    setConstraintsMenuDropDowns(toolbar,scene);
    updateToggleActionsFromSceneState(); //18May2000
    scene.addPropertyChangeListener(this);
  }

 //LogoScriptable//
  public String[] getSupportedPrimitiveGroups(){
    //Trace.printStackTrace();
    return Scene._getSupportedPrimitiveGroups(); //9Jun2000: use this one!!!
    //1Jun2000: asking the live hosted object for its primitives, not calling a static method
    //on the Scene class
    //return getScene().getSupportedPrimitiveGroups();
  }
  //9Jun2000: use "getScene()" and not just "scene": must create a scene object if needed:
  //else a null-pointer-exception is thrown

  //HasFacets//    //29Nov1999
  @SuppressWarnings("unchecked")
  public Object getFacet(Class facet){
    AsScene document=getScene();
    //this allows Stage to return as a face "gr.cti.eslate.stage.Scene" facet (Scene has no
    //common hierarchy with Stage) or "gr.cti.eslate.models.AsScene" or whatever other
    //interfaces the Scene "document" instance implements (if Scene implemented HasFacets we
    //could just delegate to its "getFacet" method)
    if(facet.isInstance(document))
      return document;
    return null;
  }

  @SuppressWarnings("unchecked")
  public Class[] getFacetTypes(){
    //1Jun2000: this could also return "scene.getClass().getInterfaces()"+"scene.getClass()" or
    //if Scene was implementing itself HasFacets delegate to its "getFacetTypes"
    return scene.getClass().getInterfaces();
  }

  //PROPERTIES

  //MenuBarVisible property//
  public void setMenuBarVisible(boolean visible){ //15Oct1999
    if(menubar!=null)
      menubar.setVisible(visible);
  }

  public boolean isMenuBarVisible(){ //15Oct1999
    return (menubar!=null)?menubar.isVisible():false;
  }

 //ToolBarVisible property//
  public void setToolBarVisible(boolean visible){ //15Oct1999
    if(toolbar!=null)
      toolbar.setVisible(visible);
  }

  public boolean isToolBarVisible(){ //15Oct1999
    return (toolbar!=null)?toolbar.isVisible():false;
  }

  //Scene's ObjectMovementEnabled property delegate//
  public boolean isObjectMovementEnabled(){
    return scene.isObjectMovementEnabled();
  }

  public void setObjectMovementEnabled(boolean flag){
    scene.setObjectMovementEnabled(flag);
  }

  //Scene's ViewMovementEnabled property delegate//
  public boolean isViewMovementEnabled()
  {
    return scene.isViewMovementEnabled();
  }

  public void setViewMovementEnabled(boolean flag)
  {
    scene.setViewMovementEnabled(flag);
  }

  //Scene's ObjectsAdjustable property delegate//
  public boolean isObjectsAdjustable()
  {
    return scene.isObjectsAdjustable();
  }

  public void setObjectsAdjustable(boolean flag)
  {
    scene.setObjectsAdjustable(flag);
  }

 //Scene's ControlPointMovementEnabled property delegate//
  public boolean isControlPointMovementEnabled(){
    return scene.isControlPointMovementEnabled();
  }

  public void setControlPointMovementEnabled(boolean flag){
    scene.setControlPointMovementEnabled(flag);
  }

 //Scene's MarksOverShapes property delegate//
  public boolean isMarksOverShapes(){ //24Feb2000
    return scene.isMarksOverShapes();
  }

  public void setMarksOverShapes(boolean flag){ //24Feb2000
    scene.setMarksOverShapes(flag);
  }

 //Scene's CoordinatesVisible property delegate//
  public void setCoordinatesVisible(boolean visible){ //15Oct1999: don't store this property, delegates to stage which does the saving
    scene.setCoordinatesVisible(visible);
  }

  public boolean isCoordinatesVisible(){ //15Oct1999: don't store this property, delegates to stage which does the saving
    return scene.isCoordinatesVisible();
  }

  //Scene's GridVisible property delegate//
  public boolean isGridVisible(){ //25Feb2000
    return scene.isGridVisible();
  }

  public void setGridVisible(boolean flag){ //25Feb2000
    scene.setGridVisible(flag);
  }

  //Scene's GridSize property delegate//
  public double getGridSize()
  {
    return scene.getGridSize();
  }

  public void setGridSize(double size)
  {
    scene.setGridSize(size);
  }

  //Scene's AxisVisible property delegate//
  public boolean isAxisVisible(){ //20Apr2000
    return scene.isAxisVisible();
  }

  public void setAxisVisible(boolean flag){ //20Apr2000
    scene.setAxisVisible(flag);
  }

  //Scene's ControlPointsVisible property delegate//
  public boolean isControlPointsVisible(){ //20Apr2000
    return scene.isControlPointsVisible();
  }

  public void setControlPointsVisible(boolean flag){ //20Apr2000
    scene.setControlPointsVisible(flag);
  }

 //Scene's GridType property delegate//
  public int getGridType(){ //24Feb2000
    return scene.getGridType();
  }

  public void setGridType(int value){ //24Feb2000
    scene.setGridType(value);
  }

 //Scene's Image property delegate//
  public Image getImage(){ //25Feb2000
    return scene.getImage();
  }

  public void setImage(Image flag){ //25Feb2000
    scene.setImage(flag);
  }

/*
  private void createPlugs(){ //27Nov1999
    //LOGOCommandSO output pin// (to LOGO)
    try{
      Pin pin=new SingleOutputPin(getESlateHandle(),Res.localize("logopin"),new Color(160,80,170),Class.forName("gr.cti.eslate.sharedObject.LogoCommandSO"),commandSO);
      addPin(pin);
    }catch(Exception ex){System.out.println(ex+"\nError creating LogoCommandSO ouput pin");} //in case the class is missing

    //TickSO input pin// (from MasterClock)
    try{
      Pin pin=new SingleInputPin(getESlateHandle(),Res.localize("timepin"),Color.yellow,Class.forName("gr.cti.eslate.sharedObject.Tick"),this);
      addPin(pin);
    }catch(Exception ex){System.out.println(ex+"\nError creating TickSO input pin");} //in case the class is missing
  }
*/



  // DESTRUCTOR //
  public void destroy(){
    if (scene!=null) {
      scene.dispose(); //call to unregister from scripting name service
      scene=null;
    }
  }

  /**
    * Handle an event from some SO
    * @param    soe     The event
    */
  //need new ESlate
  public synchronized void handleSharedObjectEvent(SharedObjectEvent soe){
    /*
      SharedObject so=soe.getSharedObject();
      if (so instanceof Tick){ //getting dt and passing it to Logo, which should calculate the current TIME by accumulating those "dt" values we pass it
        Tick tick=(Tick)so;
        int dt=tick.getTick();
        //System.out.println(dt);
        //...
        commandSO.setCommand("TICK "+Integer.toString(dt),0); //pass the dt to Logo's "TICK routine (should have an edit box for the designer to choose TICK proc's name
      }
    */
  }

  public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
    // Performance Manager
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(loadTimer);

    Object o=in.readObject();

    if(o instanceof StorageStructure){
      StorageStructure properties = (StorageStructure)o;
      if(properties == null)
        return;
      // Common properties
      setMenuBarVisible(properties.get("MENUBAR_VISIBLE_PROPERTY",true));
      setToolBarVisible(properties.get("TOOLBAR_VISIBLE_PROPERTY",true));
      BorderDescriptor bd = (BorderDescriptor)properties.get("BorderDescriptor");
      if (bd != null){
        try{
          setBorder(bd.getBorder());
        }catch(Throwable thr){}
      }

      // Properties before changing internal objects to handles
      if (Integer.valueOf(properties.getDataVersion()).intValue() < 2){
        //clear current scene (note: at the 11May2000 implementation, the setScene(null)
        setScene(null);
        try{
          //setScene((Scene)properties.get("SCENE_PROPERTY"));
          Scene tempScene = (Scene)properties.get("SCENE_PROPERTY");
          stageHandle.remove(scene.getESlateHandle());
          remove(scene);
          scene.removePropertyChangeListener(this); //18May2000
          scene.dispose(); //11May2000: dispose old scene and unregister its objects!
          stageHandle.add(tempScene.getESlateHandle());
          setScene(tempScene);
        }catch(Exception e){setScene(new Scene());} //this will also update Stage's UI
      }
      // Properties after changing internal objects to handles. This means that we have to remove
      // the handle of the Scene that had been added in the constructor, as well
      // as the scene itself, and then retrieve the handle using the restoreChildObjects
      // method. The scene object is respectively retieved from its handles and finally added to
      // the stage component.
      else{
          if (scene != null){
            stageHandle.remove(scene.getESlateHandle());
            remove(scene);
            scene.removePropertyChangeListener(this); //18May2000
            scene.dispose(); //11May2000: dispose old scene and unregister its objects!
          }
          Object[] objects = getESlateHandle().restoreChildObjects((ESlateFieldMap2)o, "SceneChild");
          ESlateHandle sceneHandle = (ESlateHandle)objects[0];
          setScene((Scene)sceneHandle.getComponent());
      }
    }
    //29Mar2001: compatibility with old microworlds, that are possibly stored without using
    // ESlateFieldMap
    else{
      ObjectHash properties = (ObjectHash)o;
      if(properties==null)
        return;
      try{
        setMenuBarVisible(properties.getBoolean(MENUBAR_VISIBLE_PROPERTY));
      }catch(Exception e){setMenuBarVisible(true);}
      try{
        setToolBarVisible(properties.getBoolean(TOOLBAR_VISIBLE_PROPERTY));
      }catch(Exception e){setToolBarVisible(true);}
      //this will also update Stage's UI
      try{
        setScene((Scene)properties.get(SCENE_PROPERTY));
      }catch(Exception e){setScene(new Scene());}
    }

    //before reading the saved state must have first removed all inner objects of scene that
    //are registered to the scripting namespace so that they don't do conflicts with the loaded
    //objects' names, that's why we clear the scene!
    pm.stop(loadTimer);
    pm.displayElapsedTime(loadTimer, getESlateHandle(), "", "ms");
    //System.out.println("readExternal in Stage End");
  }

  public void writeExternal(ObjectOutput out) throws IOException{
    //descendents who want to keep compatibility with their old data, should override this to do nothing!
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    pm.init(saveTimer);

    ESlateFieldMap2 properties = new ESlateFieldMap2(STORAGE_VERSION);
    properties.put("MENUBAR_VISIBLE_PROPERTY",isMenuBarVisible());
    properties.put("TOOLBAR_VISIBLE_PROPERTY",isToolBarVisible());
    if (getBorder() != null) {
      try{
        BorderDescriptor bd = ESlateUtils.getBorderDescriptor(getBorder(), this);
        properties.put("BorderDescriptor", bd);
      }catch(Throwable thr){
        thr.printStackTrace();
      }
    }

    /* Check if there exists childer of type ControlPoint. If it exists then save it using
      * saveChildren().
    */
    ESlateHandle[] handles = getESlateHandle().toArray();
    ESlateHandle sceneHandle = null;
    for (int i=0; i < handles.length; i++) {
      if (Scene.class.isAssignableFrom(handles[i].getComponent().getClass())) {
        sceneHandle = handles[i];
        break;
      }
    }
    if (sceneHandle != null){
        getESlateHandle().saveChildren(properties, "SceneChild", new ESlateHandle[]{sceneHandle});
    }
    out.writeObject(properties);
    out.flush();
    pm.stop(saveTimer);
    pm.displayElapsedTime(saveTimer, getESlateHandle(), "", "ms");
  }

  public void updateToggleActionsFromSceneState(){ //11May2000 //Stage could also be a propertyChange listener of Scene and update its UI upon changes to the scene's state (should do that in the future and remove all calls to this method [and remove this method too])
    //update UI from scene's state
    actions.toggleAction_viewAxis.setSelected(scene.isAxisVisible());
    actions.toggleAction_viewControlPoints.setSelected(scene.isControlPointsVisible());
    actions.toggleAction_viewGrid.setSelected(scene.isGridVisible());
    actions.toggleAction_viewCoordinates.setSelected(scene.isCoordinatesVisible()); //this action could be available as a button on the toolbar and an item on the view menu
  }

  public void propertyChange(PropertyChangeEvent evt) { //18May2000: update Stage's UI when certain properties of Scene change
    String property=evt.getPropertyName();
    if(property.equals(Scene.AXIS_VISIBLE_PROPERTY))
      actions.toggleAction_viewAxis.setSelected(scene.isAxisVisible());
    else if(property.equals(Scene.CONTROL_POINTS_VISIBLE_PROPERTY))
      actions.toggleAction_viewControlPoints.setSelected(scene.isControlPointsVisible());
    else if(property.equals(Scene.GRID_VISIBLE_PROPERTY))
      actions.toggleAction_viewGrid.setSelected(scene.isGridVisible());
    else if(property.equals(Scene.COORDINATES_VISIBLE_PROPERTY))
      actions.toggleAction_viewCoordinates.setSelected(scene.isCoordinatesVisible()); //this action could be available as a button on the toolbar and an item on the view menu
  }

// MENUS+TOOLBAR //////////////////////////
  public JPanel createMenus(){ //16Oct1999: renamed to "createMenus" from "makeMenus"
    JPanel p=new JPanel();
    p.setLayout(new BorderLayout());
    p.add(menubar=makeMenuBar(),BorderLayout.NORTH); //15Oct1999: keeping menubar reference, to use with setMenuBarVisible()/isMenuBarVisible()
    p.add(toolbar=makeToolBar(),BorderLayout.SOUTH); //15Oct1999: keeping toolbar reference, to use with setToolBarVisible()/isToolBarVisible()
    return p;
  }

  public JMenuBar makeMenuBar(){
    JMenuBar mb=new JMenuBar();
    JMenu m;
    m=mb.add(new JMenu(Res.localize("File")));
    m.add(actions.action_New);
    m.add(actions.action_Load); //16May2000
    m.add(actions.action_Save); //16May2000
    m.addSeparator();
    m.add(actions.action_Photo); //9May2000
    m.add(actions.action_Print);
    m=mb.add(new JMenu(Res.localize("Edit")));

    //m.add(actions.action_Undo);
    //m.add(actions.action_Redo);
    //m.addSeparator();
    m.add(actions.action_Cut);
    m.add(actions.action_Copy);  //12Apr2000: now supporting "Copy" action
    m.add(actions.action_Paste); //12Apr2000: now supporting "Paste" action
    m.add(actions.action_Clear);
    m.add(actions.action_Clone); //13Apr2000: now supporting "Clone" action for the current selection
    m.addSeparator(); //29Nov1999
    m.add(new ToggleActionCheckMenuItem(actions.toggleAction_selectSelectionTool));
    m.add(actions.action_SelectAll);
    //...merge here all the popup items from the currently selected object(s) [only for 1 for the moment: easier to implement]... or

    m=mb.add(new JCheckMenu(Res.localize("Insert"))); //10Aug1999: fixed-bug: was showing a greek word at the menus when in English locale //29Nov1999: renamed the "New>" submenu to "New object>" and moved to the Edit menu //1Dec1999: made top-level Insert menu
    for(int i=0;i<actions.newShapeActions.length;i++)
      m.add(actions.newShapeActions[i]); //30Jun1999
    // m.add(actions.action_newJTextField);
    m=mb.add(new JMenu(Res.localize("View")));
    m.add(new ToggleActionCheckMenuItem(actions.toggleAction_viewCoordinates));
    m.add(new ToggleActionCheckMenuItem(actions.toggleAction_viewControlPoints));
    m.add(new ToggleActionCheckMenuItem(actions.toggleAction_viewAxis));
    m.add(new ToggleActionCheckMenuItem(actions.toggleAction_viewGrid)); //11May2000: since we only have one grid type, use this for now instead of a checkable-submenu
    /*
    gridMenu=new JCheckMenu(Res.localize("Grid")); //27Nov1999: keep in "gridMenu" field to use in this menu's toggle_actions
    gridMenu.add(actions.toggleAction_selectDotGrid);
    //26Apr2000: not-implemented-yet// gridMenu.add(actions.toggleAction_selectLineGrid);
    m.add(gridMenu);

    gridMenuSelector=new ClickSelector(gridMenu,Color.yellow); //27Nov1999
    gridMenuSelector.addChangeListener(new ChangeListener(){
    public void stateChanged(ChangeEvent e){
     actions.toggleAction_viewGrid.setSelected(gridMenuSelector.isSelected());
    }
    });
    gridMenu.setSelectionRequired(true); //27Nov1999: don't allow empty selection (do this after adding the mouseListener, so that the same gridType is set on the menus and at the "stage" object)
    */
    m.addSeparator(); //29Nov1999
    m.add(actions.action_zoomIn);
    m.add(actions.action_zoomOut);

    /* //bug: JRadioMenu&JCheckMenu seems to need a JToggleToolBar too in order for events to be dispatched ok
    m=mb.add(new JCheckMenu(Res.localize("Tools")));
    m.add(actions.toggleAction_useSelectionTool);
    m.add(actions.toggleAction_useZoomTool);
    */

    return mb;
  }



  private Component setConstraintsMenuDropDown(JToolBar toolbar,Scene scene,Component current,String title,JCheckedActionPopup popup){
    Component c=MenuDropButton.makePair(Res.setupAction(new DummyAction(),title),popup);
    if(current!=null){
      int index=toolbar.getComponentIndex(current);
      toolbar.remove(index);
      toolbar.add(c,index);
    }else
      toolbar.add(c);
    return c;
  }

  private void setConstraintsMenuDropDowns(JToolBar toolbar,Scene scene){
    if(toolbar==null)
      return;
    pointPointConstraints=setConstraintsMenuDropDown(toolbar,scene,pointPointConstraints,"BindPointPoint",scene.getPointPointConstraintsPopup()); //19Apr2000: keep this instance to change the popup when we change Scene object (e.g. when a new one is loaded from saved state)
    pointShapeConstraints=setConstraintsMenuDropDown(toolbar,scene,pointShapeConstraints,"BindPointShape",scene.getPointShapeConstraintsPopup()); //19Apr2000: keep this instance to change the popup when we change Scene object (e.g. when a new one is loaded from saved state)
  }

  public JToolBar makeToolBar(){ //add tools in their separate toggle toolbar, for now
    MyToolBar m=new MyToolBar(); //WARNING: this local variable must be of type "MyToolBar" and not "JToolBar", else the toggle buttons won't work!!!
    m.add(actions.action_New);
    m.add(actions.action_Load); //18May2000
    m.add(actions.action_Save); //18May2000
    m.addSeparator();
    m.add(actions.action_Photo); //10May2000
    m.add(actions.action_Print);
    m.addSeparator(); //don't use "add(new JSeparator())" cause stretches the separators horizontally

    m.add(actions.action_Cut);
    m.add(actions.action_Copy); //13Apr2000: added Copy button
    m.add(actions.action_Paste); //13Apr2000: added Paste button
    m.add(actions.toggleAction_selectSelectionTool);
    m.addSeparator(); //don't use "add(new JSeparator())" cause stretches the separators horizontally

    m.add(actions.toggleAction_viewAxis);
    m.add(actions.toggleAction_viewGrid);
    m.add(actions.toggleAction_viewControlPoints);
    m.addSeparator();

    setConstraintsMenuDropDowns(m,scene);
    m.add(actions.action_Split);

    m.add(javax.swing.Box.createHorizontalGlue());
    return m;
  }


// When running as an application... ////////////////////////////////////////////////////////////
  public static void main(String[] args) {
    //ForcesSimulator forcesSimulator = new ForcesSimulator();
    JComponentFrame.startJComponent("gr.cti.eslate.stage.Stage",Res.localize("title")+VERSION,new String[]{});

    try{ //28Aug1999
      JComponentFrame.startJComponents(args,args); //27-3-1999: start any additional components requested by the user
    }catch(Exception e){}

    /*
      try{ //28Aug1999: should have a utility that will spawn either Components or Applets in a frame...
        JAppletFrame.startApplets(args,args); //27-3-1999: start any additional applets requested by the user
      }catch(Exception e){}
    */
  }

  public Dimension getPreferredSize(){
    return new Dimension(430,400);
  }

  /**
   * This method creates and adds a PerformanceListener to the E-Slate's
   * Performance Manager. The PerformanceListener attaches the component's
   * timers when the Performance Manager becomes enabled.
   */
  private void createPerformanceManagerListener(PerformanceManager pm){
    if (perfListener == null) {
      perfListener = new PerformanceAdapter() {
        public void performanceManagerStateChanged(PropertyChangeEvent e){
          boolean enabled = ((Boolean) e.getNewValue()).booleanValue();
          // When the Performance Manager is enabled, try to attach the
          // timers.
          if (enabled) {
            attachTimers();
          }
        }
      };
      pm.addPerformanceListener(perfListener);
    }
  }

  /**
   * This method creates and attaches the component's timers. The timers are
   * created only once and are assigned to global variables. If the timers
   * have been already created, they are not re-created. If the timers have
   * been already attached, they are not attached again.
   * This method does not create any timers while the PerformanceManager is
   * disabled.
   */
  protected void attachTimers(){
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    boolean pmEnabled = pm.isEnabled();

    // If the performance manager is disabled, install a listener which will
    // re-invoke this method when the performance manager is enabled.
    if (!pmEnabled && (perfListener == null)){
      createPerformanceManagerListener(pm);
    }

    // Do nothing if the PerformanceManager is disabled.
    if (!pmEnabled){
      return;
    }

    boolean timersCreated = (loadTimer != null);
    // If the timers have already been constructed and attached, there is
    // nothing to do.
    if (!timersCreated){
      // If the component's timers have not been constructed yet, then
      // construct them. During constuction, the timers are also attached.
      if (!timersCreated) {
        PerformanceTimer loadTimer = (PerformanceTimer)pm.createGlobalPerformanceTimerGroup(
                                      Res.localize("LoadTimer"), true);
        PerformanceTimer saveTimer = (PerformanceTimer)pm.createGlobalPerformanceTimerGroup(
                                      Res.localize("SaveTimer"), true);
        ESlateHandle h = getESlateHandle();
        pm.registerPerformanceTimerGroup(PerformanceManager.LOAD_STATE, loadTimer, h);
        pm.registerPerformanceTimerGroup(PerformanceManager.SAVE_STATE, saveTimer, h);
      }
    }
  }

  /**
   * Returns the component's E-Slate handle.
   * @return    The component's E-Slate handle.
   */
  public ESlateHandle getESlateHandle(){
    //System.out.println("getESlateHandle in Stage Begin");
    if (stageHandle == null) {
      stageHandle = ESlate.registerPart(this);
      stageHandle.setInfo(getInfo());
      try{
        stageHandle.setUniqueComponentName(Res.localize("Stage"));
      }catch(RenamingForbiddenException e){}

      PerformanceManager pm = PerformanceManager.getPerformanceManager();
      pm.eSlateAspectInitStarted(this);
      stageHandle.addESlateListener(new ESlateAdapter(){
        public void handleDisposed(HandleDisposalEvent e){
          pmCleanup();
        }
      });
      pm.eSlateAspectInitEnded(this);

      if (scene != null)
        stageHandle.add(scene.getESlateHandle());
    }
    //System.out.println("getESlateHandle in Stage End");
    return stageHandle;
  }

  /**
   * Performance manager-related cleanup.
   */
  private void pmCleanup(){
    PerformanceManager pm = PerformanceManager.getPerformanceManager();
    /*
    ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.LOAD_STATE);
    pm.removePerformanceTimerGroup(ptg, loadTimer);
    ptg = pm.getPerformanceTimerGroupByID(PerformanceManager.SAVE_STATE);
    pm.removePerformanceTimerGroup(ptg, saveTimer);
    */
    pm.removePerformanceListener(perfListener);
    perfListener = null;
  }
}
