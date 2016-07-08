#geowe-core
GeoData Web Editing [core SIG Web]

**GeoWE** is a Geographic Information System (GIS) developed in Java, whose main aim is the edition of spatial data (geometric and alphanumeric)

The **geowe-core** is being developed using the following technologies:
- GWT (Google Web Toolkit)
- JBoss ERRAI Framework
- GXT
- OpenLayers

More information available on [GeoWE Web site](http://geowe.org/)

##License

The **geowe-core** is licensed under the [GPLv3](https://www.gnu.org/licenses/gpl-3.0.html), meaning you can use it free of charge, according with license terms and conditions.

##Configuration
In order to use geowe full featured you must configure the basic params:

####Bing Maps config:
In order to use Bing maps, you need specify your own bing maps key [read carefully] (https://www.microsoft.com/maps/create-a-bing-maps-key.aspx).

Put your key in BingConstants.properties file, located at: src\main\java\org\geowe\client\local\main\tool\map\catalog\model

	For example:
	bingKey = YOUR_BING_MAPS_KEY

####what3words config:
In order to use what3words feature, you need specify your own what3words api key. You must contact with [what3words](http://what3words.com) to obtain it.

Put your key in ErraiApp.properties file, located at: src\main\resources

	For example:
	w3w.key = YOUR_W3W_API_KEY
	
##Build the software
In order to compile and build geowe the Java 7 platform is necessary. The project uses maven to build and package.
	
	mvn clean package

##Deploy
Once you compiled the software, the geowe.war file can be deployed on any server/application container, like tomcat. Our [live demo](http://map.geowe.org) is running on OpenShift, Try it!
