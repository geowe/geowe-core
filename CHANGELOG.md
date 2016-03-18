# Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).
You can see the previous changelog [here] (http://www.geowe.org/index.php?id=changelog)

## [Unreleased]
### Added
- Support for GeoJson-css

## [0.1.73] - 2016-03-18
version deployed for testing [map.geowe.org](http://map.geowe.org)
##Fixed
- improve performance.
- several issues.

## [0.1.70] - 2016-03-15
### Added
- Delete multiple elements from DeteteTool
- Added Create layer Tool in main panel

##Fixed
- load layer from url (pending test).

## [0.1.66] - 2016-03-11
version deployed for testing [map.geowe.org](http://map.geowe.org) no longer available.
### Added
- Calculte selected elements envelope
- Export selected elements to: KML, WKT, GML, GeoJson, CSV

### Fixed
- Improve performance.

## [0.1.63] - 2016-03-09
### Added

### Fixed
- issue 12 (Delete multiple vector).


## [0.1.62] - 2016-03-08
### Added
- Redesign Basic Tool Bar
- Redesign what3words info
- show/hide basic tool bar Tool.
- Active/deactive what3words click on map

### Fixed
- StatusBar view on IE and Chrome

## [0.1.60] - 2016-03-04
version deployed for testing [map.geowe.org](http://map.geowe.org) no longer available.
### Added
- Layer Spatial operations: intersect, union, buffer, difference, intersection
- Copy elements between layers
- What3words integration
- Load vector layer from file limited to 15MB

## [0.1.40] - 2016-02-09
### Added
- Layer summary in html (Layer info Dialog)

### Fixed
- issue 27 (zoom to imported layer from url or file).
- issue 33 (links in attribute).

## [0.1.37] - 2016-02-05
version deployed for testing [map.geowe.org](http://map.geowe.org) no longer available.
### Added
- Export layer's elements to csv (without Geometry).
- Export csv from search dialog
- Redesign of the status bar
- The toolbar is positioned bottom
- Default Projection EPSG 3857
- You can use images (miniature) as an attribute value in an element of the vector layer
- links can be used in the attributes of a layer.

### Fixed
- Tool division is improved when there is a large number of elements.
- Correct reprojection for GML formats.

## [0.1.36] - 2016-02-04
### Added
- Tooltip maps show images and links.
- Export layer's elements to csv (without Geometry).

## [0.1.35] - 2016-02-03
### Added
- You can use images (miniature) as an attribute value in an element of the vector layer
- Export csv from search dialog

### Fixed
- issues 28 and 18

## [0.1.34] - 2016-02-02
### Added
- You can use links as an attribute value in an element of the vector layer
- Redesign of the status bar
- The toolbar is positioned bottom

## [0.1.33] - 2016-02-02
### Added
- Default Projection EPSG 3857

### Fixed
- issues 6, 22, 24, 25 and 26.


## [0.1.32-Alpha] - 2016-01-29
version deployed for testing [map.geowe.org](http://map.geowe.org) no longer available.
### Added
- Search elements by a filter (set of attibutes).
- Upload files to import layer (KML, GML, GeoJson, WKT).
- Load layer from URL (KML).
- ToolTip in maps.

### Removed
- Print tool no longer available.

## [0.1.25] - 2016-01-25
### Added
- Se añade la funcionalidad de tooltips en el mapa. Ahora cuando se desplaza el puntero de ratón sobre una feature concreta de una capa, al resaltarse aparece un pop-up con los valores de los atributos alfanumericos de la feature y su ID.

## [0.1.22] - 2016-01-21
### Added
- Se habilita el proxy para la carga de datos desde URL externa.
- Se añaden pestañas a la ventana de importación, permitiendo carga de datos por URL y por texto. En esta versión, la carga desde URL solo admite ficheros con formatos KML.
- Se elimina la ventana con botones para importación de datos.

### Fixed
- Se soluciona el problema de los valores de los atributos en las cargas de datos desde formato KML. Ya permite la visualización en texto en lugar de Object.


## [0.1.18-Alpha] - 2016-01-15
version deployed for testing [map.geowe.org](http://map.geowe.org) no longer available.
###Added
- IDE Andalucía ortofoto 2010 layer available in catalog.
- Spanish and english language.

### Fixed
- several issues.

## [0.1.15] - 2016-01-09

### Fixed
- issue #16. Element Measure.

## [0.1.14] - 2016-01-05
### Added
- i18n. Spanish translation

### Fixed
- Las capas de Recintos y Frutales SICPAC no aparecían en el catálogo.
 
## [0.1.13] - 2016-01-04
### Added
- i18n. Partial Spanish translation

### Fixed
- aplicar estilos a una nueva capa creada al duplicar capa. bug #14
 

## [0.1.12] - 2015-12-22
### Added
- Incluidas en el catalogo las capas de recintos y arboles SIGPAC 2015.

### Fixed
- aplicar estilos a una nueva capa creada a través de una consulta. bug #11

## [0.1.10] - 2015-12-11
### Added
- i18n. Partial Spanish translation


## [0.1.9-Alpha] - 2015-12-10
version deployed for testing [map.geowe.org](http://map.geowe.org) no longer available.
### Fixed
- Download files to export layer (KML, GML, GeoJson, WKT). bug #10

## [0.1.7-Alpha2] - 2015-12-07
version deployed for testing [map.geowe.org](http://map.geowe.org) no longer available.
### Added
- Tools for filtering elements: zoom, select, info, create layer from elements.


## [0.1.6] - 2015-12-03
### Added
- Download files to export layer (KML, GML, GeoJson, WKT).

### Fixed
- bug union #7
- bug geolocate #9

## [0.1.4] - 2015-12-01
### Changed
- Download GML File

## [0.1.3] - 2015-11-29
### Added
- search elements by an attribute
- ESRI raster layer added to the catalog.

### Fixed
- Several bugs
 
## [0.0.96-Alpha] - 2015-11-23
version deployed for testing [map.geowe.org](http://map.geowe.org) no longer available

### Added
- Geocoding
 

### Fixed
- Several bugs
