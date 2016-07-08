#geowe-core
GeoWE. GeoData Web Editing [core SIG Web]

**GeoWE** is a Geographic Information System (GIS) developed in Java, whose main aim is the editing of spatial data (geometric and alphanumeric).

The **geowe-core** is being developed using the following technologies:
- GWT (Google Web Toolkit)
- JBoss ERRAI Framework
- GXT
- OpenLayers

More information available on the [GeoWE Web site](http://geowe.org/).

##License

The **geowe-core** is licensed under the [GPLv3](https://www.gnu.org/licenses/gpl-3.0.html), meaning you can use it free of charge, according with license terms and conditions.

##Configuration
In order to use all **GeoWE** features, you must configure following basic params:

####Bing Maps config:
To use Bing maps, you need to specify your own Bing maps key. Please, [read this carefully] (https://www.microsoft.com/maps/create-a-bing-maps-key.aspx).

Put your key in BingConstants.properties file, located at: src\main\java\org\geowe\client\local\main\tool\map\catalog\model

	For example:
	bingKey = YOUR_BING_MAPS_KEY

####what3words config:
To use what3words feature, you need to specify your own what3words api key. You must contact [what3words](http://what3words.com) to obtain it.

Put your key in ErraiApp.properties file, located at: src\main\resources

	For example:
	w3w.key = YOUR_W3W_API_KEY
	
##Build the software
In order to compile and build **GeoWE**, the JDK 7 platform is necessary. The project uses maven for building and packaging.
	
	mvn clean package

##Deploy
Once you compiled the software, the geowe.war file can be deployed on any server/application container, like Apache Tomcat. Our [live demo](http://map.geowe.org) is running on OpenShift, Try it!

##Javadoc
You can see the complete javadoc [here](http://www.geowe.org/source/apidocs).

##Dependecies report
You can see the complete source dependencies [here](http://www.geowe.org/source/dependencies).
