//Copyright:    Copyright (c) 1999
//Company:      Computer Technology Institute

package gr.cti.utils;

/**
 * A case-insensitive INameService implementation (implemented as a NameService descendent)
 *
 * @version 2.0.0, 19-May-2006
 * @author George Birbilis [birbilis@cti.gr]
 */
public class CaseInsensitiveNameService extends NameService {

 /**
  * Overriden "areEqual" method to consider two names the same if their capitalized versions are the same (for Greek, punctuation marks are ignored in comparison)
  * @param name1 the first name
  * @param name2 the second name
  * @return whether the two names are considered as equal
  */
 protected boolean areEqual(String name1,String name2){
  return CharacterCase.areEqualIgnoreCase(name1,name2);
 }

}
