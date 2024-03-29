/*
 * Copyright (c) 2016 Vivid Solutions.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v20.html
 * and the Eclipse Distribution License is available at
 *
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */
package org.sede.core.geo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

/**
 * Reads a GeoJSON Geometry from a JSON fragment into a {@link Geometry}.
 * <p>
 * The current GeoJSON specification is <a href=
 * 'https://tools.ietf.org/html/rfc7946'>https://tools.ietf.org/html/rfc7946</a>.
 * An older specification is on the GeoJSON web site: <a href=
 * 'http://geojson.org/geojson-spec.html'>http://geojson.org/geojson-spec.html</a>.
 * <p>
 * The reader does not require a particular orientation for polygon rings.
 * <p>
 * The reader reads empty or null coordinate arrays as empty geometries.
 * <p>
 * It is the caller's responsibility to ensure that the supplied
 * {@link PrecisionModel} matches the precision of the incoming data. If a lower
 * precision for the data is required, a subsequent process must be run on the
 * data to reduce its precision.
 * 
 * @author Martin Davis
 * @author Paul Howells, Vivid Solutions.
 * 
 */
public class GeoJsonReader {

	public static final String NAME_GEOMETRIES = "geometries";
	public static final String NAME_CRS = "crs";
	public static final String NAME_PROPERTIES = "properties";
	public static final String NAME_NAME = "name";
	public static final String NAME_TYPE = "type";
	public static final String NAME_POINT = "Point";
	public static final String NAME_LINESTRING = "LineString";
	public static final String NAME_POLYGON = "Polygon";
	public static final String NAME_COORDINATES = "coordinates";
	public static final String NAME_GEOMETRYCOLLECTION = "GeometryCollection";
	public static final String NAME_MULTIPOLYGON = "MultiPolygon";
	public static final String NAME_MULTILINESTRING = "MultiLineString";
	public static final String NAME_MULTIPOINT = "MultiPoint";

	private GeometryFactory gf;

	/**
	 * The default constructor uses the SRID from the Geojson CRS and the default
	 * <code>PrecisionModel</code> to create a <code>GeometryFactory</code>. If
	 * there is no CRS specified then the default CRS is a geographic coordinate
	 * reference system, using the WGS84 datum, and with longitude and latitude
	 * units of decimal degrees (SRID = 4326)
	 */
	public GeoJsonReader() {
		// do nothing
	}

	/**
	 * This constructor accepts a <code>GeometryFactory</code> that is used to
	 * create the output geometries and to override the GeoJson CRS.
	 * 
	 * @param geometryFactory
	 *            a GeometryFactory
	 */
	public GeoJsonReader(GeometryFactory geometryFactory) {
		this.gf = geometryFactory;
	}

	/**
	 * Reads a GeoJson Geometry from a <tt>String</tt> into a single
	 * {@link Geometry}.
	 * 
	 * 
	 * @param json
	 *            The GeoJson String to parse
	 * @return the resulting JTS Geometry
	 * @throws Exception 
	 * 
	 * @throws ParseException
	 *             throws a ParseException if the JSON string cannot be parsed
	 */
	public Geometry read(String json) throws Exception {

		Map<String, Object> geometryMap = null;
		ObjectMapper parser = new ObjectMapper();
		geometryMap = (Map<String, Object>) parser.readValue(json, Map.class);
		GeometryFactory geometryFactory = null;
		if (this.gf == null) {
			geometryFactory = this.getGeometryFactory(geometryMap);
		} else {
			geometryFactory = this.gf;
		}

		Geometry result = create(geometryMap, geometryFactory);

		return result;
	}

	private Geometry create(Map<String, Object> geometryMap, GeometryFactory geometryFactory) throws Exception {

		Geometry result = null;

		String type = (String) geometryMap.get(NAME_TYPE);

		if (type == null) {
			throw new Exception("Could not parse Geometry from Json string.  No 'type' property found.");
		} else {

			if (NAME_POINT.equals(type)) {
				result = createPoint(geometryMap, geometryFactory);

			} else if (NAME_LINESTRING.equals(type)) {
				result = createLineString(geometryMap, geometryFactory);

			} else if (NAME_POLYGON.equals(type)) {
				result = createPolygon(geometryMap, geometryFactory);

			} else if (NAME_MULTIPOINT.equals(type)) {
				result = createMultiPoint(geometryMap, geometryFactory);

			} else if (NAME_MULTILINESTRING.equals(type)) {
				result = createMultiLineString(geometryMap, geometryFactory);

			} else if (NAME_MULTIPOLYGON.equals(type)) {
				result = createMultiPolygon(geometryMap, geometryFactory);

			} else if (NAME_GEOMETRYCOLLECTION.equals(type)) {
				result = createGeometryCollection(geometryMap, geometryFactory);

			}
		}

		return result;
	}

	private Geometry createGeometryCollection(Map<String, Object> geometryMap, GeometryFactory geometryFactory)
			throws Exception {

		Geometry result = null;

		try {

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> geometriesList = (List<Map<String, Object>>) geometryMap.get(NAME_GEOMETRIES);

			Geometry[] geometries = new Geometry[geometriesList.size()];

			int i = 0;
			for (Map<String, Object> map : geometriesList) {

				geometries[i] = this.create(map, geometryFactory);

				++i;
			}

			result = geometryFactory.createGeometryCollection(geometries);

		} catch (RuntimeException e) {
			throw new Exception("Could not parse GeometryCollection from GeoJson string.", e);
		}

		return result;
	}

	private Geometry createMultiPolygon(Map<String, Object> geometryMap, GeometryFactory geometryFactory) throws Exception {
		Geometry result = null;

		try {

			@SuppressWarnings("unchecked")
			List<List<List<List<Number>>>> polygonsList = (List<List<List<List<Number>>>>) geometryMap
					.get(NAME_COORDINATES);

			Polygon[] polygons = new Polygon[polygonsList.size()];

			int p = 0;
			for (List<List<List<Number>>> ringsList : polygonsList) {

				List<CoordinateSequence> rings = new ArrayList<CoordinateSequence>();

				for (List<List<Number>> coordinates : ringsList) {

					rings.add(createCoordinateSequence(coordinates));
				}

				if (rings.isEmpty()) {
					continue;
				}

				LinearRing outer = geometryFactory.createLinearRing(rings.get(0));
				LinearRing[] inner = null;
				if (rings.size() > 1) {
					inner = new LinearRing[rings.size() - 1];
					for (int i = 1; i < rings.size(); i++) {
						inner[i - 1] = geometryFactory.createLinearRing(rings.get(i));
					}
				}

				polygons[p] = geometryFactory.createPolygon(outer, inner);

				++p;
			}

			result = geometryFactory.createMultiPolygon(polygons);

		} catch (RuntimeException e) {
			throw new Exception("Could not parse MultiPolygon from GeoJson string.", e);
		}

		return result;
	}

	private Geometry createMultiLineString(Map<String, Object> geometryMap, GeometryFactory geometryFactory) throws Exception {

		Geometry result = null;

		try {

			@SuppressWarnings("unchecked")
			List<List<List<Number>>> linesList = (List<List<List<Number>>>) geometryMap.get(NAME_COORDINATES);

			LineString[] lineStrings = new LineString[linesList.size()];

			int i = 0;
			for (List<List<Number>> coordinates : linesList) {

				lineStrings[i] = geometryFactory.createLineString(createCoordinateSequence(coordinates));

				++i;
			}

			result = geometryFactory.createMultiLineString(lineStrings);

		} catch (RuntimeException e) {
			throw new Exception("Could not parse MultiLineString from GeoJson string.", e);
		}

		return result;
	}

	private Geometry createMultiPoint(Map<String, Object> geometryMap, GeometryFactory geometryFactory)
			throws Exception {

		Geometry result = null;

		try {

			@SuppressWarnings("unchecked")
			List<List<Number>> coordinatesList = (List<List<Number>>) geometryMap.get(NAME_COORDINATES);

			CoordinateSequence coordinates = this.createCoordinateSequence(coordinatesList);

			result = geometryFactory.createMultiPoint(coordinates);

		} catch (RuntimeException e) {
			throw new Exception("Could not parse MultiPoint from GeoJson string.", e);
		}

		return result;
	}

	private Geometry createPolygon(Map<String, Object> geometryMap, GeometryFactory geometryFactory) throws Exception {

		Geometry result = null;

		try {

			@SuppressWarnings("unchecked")
			List<List<List<Number>>> ringsList = (List<List<List<Number>>>) geometryMap.get(NAME_COORDINATES);

			if (ringsList == null || ringsList.isEmpty()) {
				return geometryFactory.createPolygon(null, null);
			}

			List<CoordinateSequence> rings = new ArrayList<CoordinateSequence>();

			for (List<List<Number>> coordinates : ringsList) {

				rings.add(createCoordinateSequence(coordinates));
			}

			LinearRing outer = geometryFactory.createLinearRing(rings.get(0));
			LinearRing[] inner = null;
			if (rings.size() > 1) {
				inner = new LinearRing[rings.size() - 1];
				for (int i = 1; i < rings.size(); i++) {
					inner[i - 1] = geometryFactory.createLinearRing(rings.get(i));
				}
			}

			result = geometryFactory.createPolygon(outer, inner);

		} catch (RuntimeException e) {
			throw new Exception("Could not parse Polygon from GeoJson string.", e);
		}

		return result;
	}

	private Geometry createLineString(Map<String, Object> geometryMap, GeometryFactory geometryFactory)
			throws Exception {

		Geometry result = null;

		try {

			@SuppressWarnings("unchecked")
			List<List<Number>> coordinatesList = (List<List<Number>>) geometryMap.get(NAME_COORDINATES);

			CoordinateSequence coordinates = createCoordinateSequence(coordinatesList);

			result = geometryFactory.createLineString(coordinates);

		} catch (RuntimeException e) {
			throw new Exception("Could not parse LineString from GeoJson string.", e);
		}

		return result;
	}

	private Geometry createPoint(Map<String, Object> geometryMap, GeometryFactory geometryFactory) throws Exception {

		Geometry result = null;

		try {

			@SuppressWarnings("unchecked")
			List<Number> coordinateList = (List<Number>) geometryMap.get(NAME_COORDINATES);

			CoordinateSequence coordinate = this.createCoordinate(coordinateList);

			result = geometryFactory.createPoint(coordinate);

		} catch (RuntimeException e) {
			throw new Exception("Could not parse Point from GeoJson string.", e);
		}

		return result;
	}

	private GeometryFactory getGeometryFactory(Map<String, Object> geometryMap) throws Exception {

		GeometryFactory result = null;
		@SuppressWarnings("unchecked")
		Map<String, Object> crsMap = (Map<String, Object>) geometryMap.get(NAME_CRS);
		Integer srid = null;

		if (crsMap != null) {

			try {

				@SuppressWarnings("unchecked")
				Map<String, Object> propertiesMap = (Map<String, Object>) crsMap.get(NAME_PROPERTIES);
				String name = (String) propertiesMap.get(NAME_NAME);
				String[] split = name.split(":");
				String epsg = split[1];
				srid = Integer.valueOf(epsg);
			} catch (RuntimeException e) {
				throw new Exception("Could not parse SRID from Geojson 'crs' object.", e);
			}
		}

		if (srid == null) {
			// The default CRS is a geographic coordinate reference
			// system, using the WGS84 datum, and with longitude and
			// latitude units of decimal degrees. SRID 4326
			srid = Integer.valueOf(4326);
		}

		result = new GeometryFactory(new PrecisionModel(), srid.intValue());
		return result;
	}

	private CoordinateSequence createCoordinateSequence(List<List<Number>> coordinates) {
		CoordinateSequence result = null;
		if (coordinates == null) {
			coordinates = Collections.EMPTY_LIST;
		}

		result = new CoordinateArraySequence(coordinates.size());

		for (int i = 0; i < coordinates.size(); ++i) {
			List<Number> ordinates = coordinates.get(i);

			if (ordinates.size() > 0) {
				result.setOrdinate(i, 0, ordinates.get(0).doubleValue());
			}
			if (ordinates.size() > 1) {
				result.setOrdinate(i, 1, ordinates.get(1).doubleValue());
			}
			if (ordinates.size() > 2) {
				result.setOrdinate(i, 2, ordinates.get(2).doubleValue());
			}

		}

		return result;
	}

	private CoordinateSequence createCoordinate(List<Number> ordinates) {
		if (ordinates == null || ordinates.size() == 0) {
			return new CoordinateArraySequence(0);
		}

		CoordinateSequence result = new CoordinateArraySequence(1);

		if (ordinates.size() > 0) {
			result.setOrdinate(0, 0, ordinates.get(0).doubleValue());
		}
		if (ordinates.size() > 1) {
			result.setOrdinate(0, 1, ordinates.get(1).doubleValue());
		}
		if (ordinates.size() > 2) {
			result.setOrdinate(0, 2, ordinates.get(2).doubleValue());
		}

		return result;
	}
}