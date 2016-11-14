/**
 * Class: OpenLayers.Format.TopoJSON
 * A parser to read/write TopoJSON safely.  Create a new instance with the
 *     <OpenLayers.Format.TopoJSON> constructor.
 *
 * Inherits from:
 *  - <OpenLayers.Format.JSON>
 */
OpenLayers.Format.TopoJSON = OpenLayers.Class(OpenLayers.Format.JSON, {
    

    /**
     * Constructor: OpenLayers.Format.TopoJSON
     * Create a new parser for TopoJSON.
     *
     * Parameters:
     * options - {Object} An optional object whose properties will be set on
     *     this instance.
     */

    /**
     * APIMethod: read
     * Deserialize a TopoJSON string.
     *
     * Parameters:
     * json - {String} A JSON string
     * filter - {Function} A function which will be called for every key and
     *     value at every level of the final result. Each value will be
     *     replaced by the result of the filter function. This can be used to
     *     reform generic objects into instances of classes, or to transform
     *     date strings into Date objects.
     *     
     * Returns:
     * {Object} An object, array, string, or number .
     */
    read: function(json, filter, collections) {
      var coords, fc_name, feature, o, obj, point, points, poly, results, ring, topo, topo_geom, x, y, _i, _j, _len, _len1, _ref;
      if (typeof json === "string") {
        obj = OpenLayers.Format.JSON.prototype.read.apply(this, [json, filter]);
      } else {
        obj = json;
      }
      if (!obj) {
        OpenLayers.Console.error("Bad TopoJSON: " + json);
      } else if (typeof obj.type !== "string") {
        OpenLayers.Console.error("Bad TopoJSON - no type: " + json);
      } else {
        results = [];
        if ((collections != null) && !OpenLayers.Util.isArray(collections)) {
          collections = [collections];
        }
        for (fc_name in obj.objects) {
          if (!(collections != null) || __indexOf.call(collections, fc_name) >= 0) {
            topo = this.topojson.object(obj, obj.objects[fc_name]);
            if(typeof topo.geometries != 'undefined'){
              topo_geom = topo.geometries;
            }
            else if(typeof topo.coordinates != 'undefined'){
              topo_geom = topo.coordinates;
            }
            for (_i = 0, _len = topo_geom.length; _i < _len; _i++) {
              o = topo_geom[_i];
  		  if(typeof o != 'undefined'){
                points = [];
                if(o.type == 'Polygon'){
                  _ref = o.coordinates[0];
                  for (_j = 0, _len1 = _ref.length; _j < _len1; _j++) {
                    coords = _ref[_j];
                    x = coords[0];
                    y = coords[1];
                    point = new OpenLayers.Geometry.Point(x, y);
                    points.push(point);
                  }
                  console.log(points);
                }
                if(o.type == 'LineString'){
                  _ref = o.coordinates;
                 for (_j = 0, _len1 = _ref.length; _j < _len1; _j++) {
                    coords = _ref[_j];
                    x = coords[0];
                    y = coords[1];
                    point = new OpenLayers.Geometry.Point(x, y);
                    points.push(point);
                  }
                 
                }
				
		if(o.type == 'Point'){
                  _ref = o.coordinates;
                  point = new OpenLayers.Geometry.Point(_ref[0], _ref[1]);
                  points.push(point);
				}
				
                //TODO: make sure actually is a Polygon.
                // TopoJSON can specify other types as well
                if(o.type == 'Polygon'){
                  ring = new OpenLayers.Geometry.LinearRing(points);
                  poly = new OpenLayers.Geometry.Polygon(ring);
                  feature = new OpenLayers.Feature.Vector(poly);
                  if (o.properties != null) {
                    feature.attributes = o.properties;
                    feature.data= o.properties;
                  }
                  results.push(feature);
				}
                if(o.type == 'LineString'){
                  line = new OpenLayers.Geometry.LineString(points);
                  feature = new OpenLayers.Feature.Vector(line);
                  if (o.properties != null) {
                    feature.attributes = o.properties;
                    feature.data= o.properties;
                  }
				  results.push(feature);
                }
				
		if(o.type == 'Point'){
                  feature = new OpenLayers.Feature.Vector(point);
                if (o.properties != null) {
					feature.attributes = o.properties;
                    feature.data= o.properties;
                  }
				  results.push(feature);
				}
				                
              }
            }
          }
        }
      }
      return results;
    },

    /* This topojson portion below - 
    Copyright (c) 2012, Michael Bostock
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this
      list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

    * The name Michael Bostock may not be used to endorse or promote products
      derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED. IN NO EVENT SHALL MICHAEL BOSTOCK BE LIABLE FOR ANY DIRECT,
    INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
    BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
    OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
    EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
    */

    topojson: (function() {

      function merge(topology, arcs) {
        var arcsByEnd = {},
            fragmentByStart = {},
            fragmentByEnd = {};

        arcs.forEach(function(i) {
          var e = ends(i);
          (arcsByEnd[e[0]] || (arcsByEnd[e[0]] = [])).push(i);
          (arcsByEnd[e[1]] || (arcsByEnd[e[1]] = [])).push(~i);
        });

        arcs.forEach(function(i) {
          var e = ends(i),
              start = e[0],
              end = e[1],
              f, g;

          if (f = fragmentByEnd[start]) {
            delete fragmentByEnd[f.end];
            f.push(i);
            f.end = end;
            if (g = fragmentByStart[end]) {
              delete fragmentByStart[g.start];
              var fg = g === f ? f : f.concat(g);
              fragmentByStart[fg.start = f.start] = fragmentByEnd[fg.end = g.end] = fg;
            } else if (g = fragmentByEnd[end]) {
              delete fragmentByStart[g.start];
              delete fragmentByEnd[g.end];
              var fg = f.concat(g.map(function(i) { return ~i; }).reverse());
              fragmentByStart[fg.start = f.start] = fragmentByEnd[fg.end = g.start] = fg;
            } else {
              fragmentByStart[f.start] = fragmentByEnd[f.end] = f;
            }
          } else if (f = fragmentByStart[end]) {
            delete fragmentByStart[f.start];
            f.unshift(i);
            f.start = start;
            if (g = fragmentByEnd[start]) {
              delete fragmentByEnd[g.end];
              var gf = g === f ? f : g.concat(f);
              fragmentByStart[gf.start = g.start] = fragmentByEnd[gf.end = f.end] = gf;
            } else if (g = fragmentByStart[start]) {
              delete fragmentByStart[g.start];
              delete fragmentByEnd[g.end];
              var gf = g.map(function(i) { return ~i; }).reverse().concat(f);
              fragmentByStart[gf.start = g.end] = fragmentByEnd[gf.end = f.end] = gf;
            } else {
              fragmentByStart[f.start] = fragmentByEnd[f.end] = f;
            }
          } else if (f = fragmentByStart[start]) {
            delete fragmentByStart[f.start];
            f.unshift(~i);
            f.start = end;
            if (g = fragmentByEnd[end]) {
              delete fragmentByEnd[g.end];
              var gf = g === f ? f : g.concat(f);
              fragmentByStart[gf.start = g.start] = fragmentByEnd[gf.end = f.end] = gf;
            } else if (g = fragmentByStart[end]) {
              delete fragmentByStart[g.start];
              delete fragmentByEnd[g.end];
              var gf = g.map(function(i) { return ~i; }).reverse().concat(f);
              fragmentByStart[gf.start = g.end] = fragmentByEnd[gf.end = f.end] = gf;
            } else {
              fragmentByStart[f.start] = fragmentByEnd[f.end] = f;
            }
          } else if (f = fragmentByEnd[end]) {
            delete fragmentByEnd[f.end];
            f.push(~i);
            f.end = start;
            if (g = fragmentByEnd[start]) {
              delete fragmentByStart[g.start];
              var fg = g === f ? f : f.concat(g);
              fragmentByStart[fg.start = f.start] = fragmentByEnd[fg.end = g.end] = fg;
            } else if (g = fragmentByStart[start]) {
              delete fragmentByStart[g.start];
              delete fragmentByEnd[g.end];
              var fg = f.concat(g.map(function(i) { return ~i; }).reverse());
              fragmentByStart[fg.start = f.start] = fragmentByEnd[fg.end = g.start] = fg;
            } else {
              fragmentByStart[f.start] = fragmentByEnd[f.end] = f;
            }
          } else {
            f = [i];
            fragmentByStart[f.start = start] = fragmentByEnd[f.end = end] = f;
          }
        });

        function ends(i) {
          var arc = topology.arcs[i], p0 = arc[0], p1 = [0, 0];
          arc.forEach(function(dp) { p1[0] += dp[0], p1[1] += dp[1]; });
          return [p0, p1];
        }

        var fragments = [];
        for (var k in fragmentByEnd) fragments.push(fragmentByEnd[k]);
        return fragments;
      }

      function mesh(topology, o, filter) {
        var arcs = [];

        if (arguments.length > 1) {
          var geomsByArc = [],
              geom;

          function arc(i) {
            if (i < 0) i = ~i;
            (geomsByArc[i] || (geomsByArc[i] = [])).push(geom);
          }

          function line(arcs) {
            arcs.forEach(arc);
          }

          function polygon(arcs) {
            arcs.forEach(line);
          }

          function geometry(o) {
            geom = o;
            geometryType[o.type](o.arcs);
          }

          var geometryType = {
            LineString: line,
            MultiLineString: polygon,
            Polygon: polygon,
            MultiPolygon: function(arcs) { arcs.forEach(polygon); }
          };

          o.type === "GeometryCollection"
              ? o.geometries.forEach(geometry)
              : geometry(o);

          geomsByArc.forEach(arguments.length < 3
              ? function(geoms, i) { arcs.push([i]); }
              : function(geoms, i) { if (filter(geoms[0], geoms[geoms.length - 1])) arcs.push([i]); });
        } else {
          for (var i = 0, n = topology.arcs.length; i < n; ++i) arcs.push([i]);
        }

        return object(topology, {type: "MultiLineString", arcs: merge(topology, arcs)});
      }

      function object(topology, o) {
        var tf = topology.transform,
            kx = tf.scale[0],
            ky = tf.scale[1],
            dx = tf.translate[0],
            dy = tf.translate[1],
            arcs = topology.arcs;

        function arc(i, points) {
          if (points.length) points.pop();
          for (var a = arcs[i < 0 ? ~i : i], k = 0, n = a.length, x = 0, y = 0, p; k < n; ++k) points.push([
            (x += (p = a[k])[0]) * kx + dx,
            (y += p[1]) * ky + dy
          ]);
          if (i < 0) reverse(points, n);
        }

        function point(coordinates) {
          return [coordinates[0] * kx + dx, coordinates[1] * ky + dy];
        }

        function line(arcs) {
          var points = [];
          for (var i = 0, n = arcs.length; i < n; ++i) arc(arcs[i], points);
          return points;
        }

        function polygon(arcs) {
          return arcs.map(line);
        }

        function geometry(o) {
          o = Object.create(o);
          if(typeof o.type != 'undefined'){
            
            o.coordinates = geometryType[o.type](o);
            return o;
          }
        }

        var geometryType = {
          Point: function(o) { return point(o.coordinates); },
          MultiPoint: function(o) { return o.coordinates.map(point); },
          LineString: function(o) { return line(o.arcs); },
          MultiLineString: function(o) { return polygon(o.arcs); },
          Polygon: function(o) { return polygon(o.arcs); },
          MultiPolygon: function(o) { return o.arcs.map(polygon); }
        };

        return o.type === "GeometryCollection"
            ? (o = Object.create(o), o.geometries = o.geometries.map(geometry), o)
            : geometry(o);
      }

      function reverse(array, n) {
        var t, j = array.length, i = j - n; while (i < --j) t = array[i], array[i++] = array[j], array[j] = t;
      }

      function bisect(a, x) {
        var lo = 0, hi = a.length;
        while (lo < hi) {
          var mid = lo + hi >>> 1;
          if (a[mid] < x) lo = mid + 1;
          else hi = mid;
        }
        return lo;
      }

      function neighbors(topology, objects) {
        var objectsByArc = [],
            neighbors = objects.map(function() { return []; });

        function line(arcs, i) {
          arcs.forEach(function(a) {
            if (a < 0) a = ~a;
            var o = objectsByArc[a] || (objectsByArc[a] = []);
            if (!o[i]) o.forEach(function(j) {
              var n, k;
              k = bisect(n = neighbors[i], j); if (n[k] !== j) n.splice(k, 0, j);
              k = bisect(n = neighbors[j], i); if (n[k] !== i) n.splice(k, 0, i);
            }), o[i] = i;
          });
        }

        function polygon(arcs, i) {
          arcs.forEach(function(arc) { line(arc, i); });
        }

        function geometry(o, i) {
          geometryType[o.type](o.arcs, i);
        }

        var geometryType = {
          LineString: line,
          MultiLineString: polygon,
          Polygon: polygon,
          MultiPolygon: function(arcs, i) { arcs.forEach(function(arc) { polygon(arc, i); }); }
        };

        objects.forEach(geometry);
        return neighbors;
      }

      return {
        version: "0.0.10",
        mesh: mesh,
        object: object,
        neighbors: neighbors
      };
    })(),
        
    CLASS_NAME: "OpenLayers.Format.TopoJSON" 

});     