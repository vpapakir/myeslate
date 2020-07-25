//31Oct1999: using AsBox to group all interfaces implemented by a Box objectall object (maybe this one should extend an AsRectangularObject i/f when more stuff, Box-specific but not rectangularness-specific are added to the box model)

package gr.cti.eslate.stage.models;

public interface AsBox
 extends AsPhysicsObject,
         HasWidth,
         HasHeight
         /*,HasLength*/
{

 //a Box must have width & height properties

}




