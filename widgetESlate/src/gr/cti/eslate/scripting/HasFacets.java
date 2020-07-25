//Name: HasFacets
//Version: 19991129
//Author: George Birbilis (birbilis@cti.gr)
//Description: aggregation interface

package gr.cti.eslate.scripting;

public interface HasFacets {

 /** Return a specific facet
   * the "facetType" param is an interface implemented by the object implementing the HasFacets
   * or by some subobject or friend-object of it (you can ask for a "class" as well
   * instead of an interface
   * @return facet instance if this facetType is supported and null if it isn't
   */
 @SuppressWarnings("unchecked")
public Object getFacet(Class facetType);


 /** Return all facet types
   * Notice that we merely return an array of class objects, not instances of the facets
   * this way we avoid creating a facet until it is really asked for by a client
   * The client will usually either call getFacet(xxx) for some facet type he wants (and check for null result if that facet is not supported by the "server" object)
   * or call getFacetTypes and prompt the user or choose one someway and then ask for that FacetType's implementation
   * the server can defer the real Facet creation to the "getFacet" call in some scenarios when it doesn't want to have precreated facets
   * (in such a case it just has a constant array of facetTypes and returns that one, then creates the appropriate facet for that type
   * only when it is asked for by the client with the getFacet call)
   */
 @SuppressWarnings("unchecked")
public Class[] getFacetTypes();

}

