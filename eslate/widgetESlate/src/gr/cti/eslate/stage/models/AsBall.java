//31Oct1999: using AsBall to group all interfaces implemented by a Ball object (maybe this one should extend an AsRoundObject i/f when more stuff, Ball-specific but not roundness-specific are added to the ball model)
//23Apr2000: now extending AsRope too

package gr.cti.eslate.stage.models;

public interface AsBall
 extends AsPhysicsObject,
         HasRadius,
         HasWidth,
         HasHeight,
         HasAngle //23Apr2000
         /*,HasLength*/
{

 //a Ball must have a radius (and can set this via width/height properties too, which correspond to radius*2)

}




