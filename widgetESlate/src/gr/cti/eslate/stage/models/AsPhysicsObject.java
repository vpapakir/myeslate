//Title:        Scene
//Version:      3Apr2001
//Copyright:    Copyright (c) 1998-2006
//Author:       George Birbilis
//Company:      CTI
//Description:  Scene

package gr.cti.eslate.stage.models;

public interface AsPhysicsObject
 extends //29Oct1999: moved all base models that make a PhysicsObject into one interface that extends them all
  HasLocation2D,     //must have location: this exists in AsSceneObject, but AsPhysicsObject isn't descending from AsSceneObject (it shouldn't anyway)
  HasMass,           //m
  HasVelocity2D,     //u
  HasAcceleration2D, //a
  HasKineticEnergy, //calculatable: (1/2)*m*(u^2)
  //HasPotentialEnergy, //non-calculatable: m*g*h (missing the g one which is a property of the context - PhysicsScene) ... so we need a HasSceneContext i/f!
  HasAppliedForce, //calculatable: m*a
  HasAltitude //calculatable: l.y
{
 //nothing more//
}

