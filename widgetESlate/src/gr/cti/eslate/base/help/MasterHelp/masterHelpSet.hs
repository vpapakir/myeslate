<?xml version='1.0' encoding='Windows-1253' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">

<helpset version="1.0">

  <!-- title -->
  <title>Master HelpSet</title>

  <!-- maps -->
  <maps>
     <homeID>master</homeID>
     <mapref location="masterMap.jhm"/>
  </maps>

  <!-- views -->
  <view>
    <name>TOC</name>
    <label>Contents</label>
    <type>javax.help.TOCView</type>
    <data>masterTOC.xml</data>
  </view>

  <view>
    <name>Index</name>
    <label>Index</label>
    <type>javax.help.IndexView</type>
    <data>masterIndex.xml</data>
  </view> 

 <view>
    <name>Search</name>
    <label>Search</label>
    <type>javax.help.SearchView</type>
    <data engine="com.sun.java.help.search.DefaultSearchEngine">
    	JavaHelpSearch
    </data>
  </view>
</helpset>
