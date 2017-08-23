package util;

import java.util.UUID;

import junit.framework.TestCase;

 
public class UUIDTest extends TestCase  {    
	
  public void testRandom (  )   {  
    UUID a = UUID.randomUUID (  ) ; 
    System.out.println(a);
    UUID b = UUID.randomUUID (  ) ; 
    System.out.println(b);
    assertFalse ( a.equals ( b )  ) ;    
   }      
   
  public void testVariant (  )   {  
    UUID a = UUID.randomUUID (  ) ; 
    System.out.println(a);
    assertEquals ( a.variant (  ) , 2 ) ;    
   }      
   
  public void testVersion (  )   {  
    UUID a1 = UUID.randomUUID (  ) ;  
    assertEquals ( a1.version (  ) , 4 ) ;   
    // here is a version 1 UUID plucked from my own HKLMSoftwareClasses 
    // for history of UUIDs ending in 444553540000      
    // http://blogs.msdn.com/oldnewthing/archive/2004/02/11/71307.aspx#71356 
    UUID a2 = UUID.fromString ( "d27cdb6e-ae6d-11cf-96b8-444553540000" ) ; 
    assertEquals ( a2.variant (  ) , 2 ) ; 
    assertEquals ( a2.version (  ) , 1 ) ; 
   }  
   
  public void testFromString (  )   {  
    String s = "d27cdb6e-ae6d-11cf-96b8-444553540000"; 
    UUID a = UUID.fromString ( s ) ; 
    assertEquals ( a.toString (  ) , s ) ;    
   }  
   
  public void testVersionFromCommonsTestCase (  )   {  
    // these UUIDs are from commons-id      
    UUID v1 = UUID.fromString ( "3051a8d7-aea7-1801-e0bf-bc539dd60cf3" ) ; 
    UUID v2 = UUID.fromString ( "3051a8d7-aea7-2801-e0bf-bc539dd60cf3" ) ; 
    UUID v3 = UUID.fromString ( "3051a8d7-aea7-3801-e0bf-bc539dd60cf3" ) ; 
    UUID v4 = UUID.fromString ( "3051a8d7-aea7-4801-e0bf-bc539dd60cf3" ) ; 
    //UUID v5 = UUID.fromString ( "3051a8d7-aea7-3801-e0bf-bc539dd60cf3" ) ; 
    assertEquals ( 1, v1.version (  )  ) ;      
    assertEquals ( 2, v2.version (  )  ) ;      
    assertEquals ( 3, v3.version (  )  ) ;      
    assertEquals ( 4, v4.version (  )  ) ;       
    // java.util.UUID doesn't support version 5 UUIDs while commons-id does      
    //assertEquals ( 5, v5.version (  )  ) ;    
   }    
 }  
