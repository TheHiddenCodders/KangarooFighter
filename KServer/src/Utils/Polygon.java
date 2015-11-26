package Utils;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


/** Encapsulates a 2D polygon defined by it's vertices relative to an origin point (default of 0, 0). */
public class Polygon {
	private float[] localVertices;
	private float[] worldVertices;
	private float x, y;
	private float originX, originY;
	private float rotation;
	private float scaleX = 1, scaleY = 1;
	private boolean dirty = true;
	private Rectangle bounds;

	/** Constructs a new polygon with no vertices. */
	public Polygon () {
		this.localVertices = new float[0];
	}

	/** Constructs a new polygon from a float array of parts of vertex points.
	 * 
	 * @param vertices an array where every even element represents the horizontal part of a point, and the following element
	 *           representing the vertical part
	 * 
	 * @throws IllegalArgumentException if less than 6 elements, representing 3 points, are provided */
	public Polygon (float[] vertices) {
		if (vertices.length < 6) throw new IllegalArgumentException("polygons must contain at least 3 points.");
		this.localVertices = vertices;
	}

	/** Returns the polygon's local vertices without scaling or rotation and without being offset by the polygon position. */
	public float[] getVertices () {
		return localVertices;
	}

	/** Calculates and returns the vertices of the polygon after scaling, rotation, and positional translations have been applied,
	 * as they are position within the world.
	 * 
	 * @return vertices scaled, rotated, and offset by the polygon position. */
	public float[] getTransformedVertices () {
		if (!dirty) return worldVertices;
		dirty = false;

		final float[] localVertices = this.localVertices;
		if (worldVertices == null || worldVertices.length != localVertices.length) worldVertices = new float[localVertices.length];

		final float[] worldVertices = this.worldVertices;
		final float positionX = x;
		final float positionY = y;
		final float originX = this.originX;
		final float originY = this.originY;
		final float scaleX = this.scaleX;
		final float scaleY = this.scaleY;
		final boolean scale = scaleX != 1 || scaleY != 1;
		final float rotation = this.rotation;
		final float cos = (float) (Math.cos(rotation) * 180 / Math.PI);
		final float sin = (float) (Math.sin(rotation) * 180 / Math.PI);

		for (int i = 0, n = localVertices.length; i < n; i += 2) {
			float x = localVertices[i] - originX;
			float y = localVertices[i + 1] - originY;

			// scale if needed
			if (scale) {
				x *= scaleX;
				y *= scaleY;
			}

			// rotate if needed
			if (rotation != 0) {
				float oldX = x;
				x = cos * x - sin * y;
				y = sin * oldX + cos * y;
			}

			worldVertices[i] = positionX + x + originX;
			worldVertices[i + 1] = positionY + y + originY;
		}
		return worldVertices;
	}

	/** Sets the origin point to which all of the polygon's local vertices are relative to. */
	public void setOrigin (float originX, float originY) {
		this.originX = originX;
		this.originY = originY;
		dirty = true;
	}

	/** Sets the polygon's position within the world. */
	public void setPosition (float x, float y) {
		this.x = x;
		this.y = y;
		dirty = true;
	}

	/** Sets the polygon's local vertices relative to the origin point, without any scaling, rotating or translations being applied.
	 * 
	 * @param vertices float array where every even element represents the x-coordinate of a vertex, and the proceeding element
	 *           representing the y-coordinate.
	 * @throws IllegalArgumentException if less than 6 elements, representing 3 points, are provided */
	public void setVertices (float[] vertices) {
		if (vertices.length < 6) throw new IllegalArgumentException("polygons must contain at least 3 points.");
		localVertices = vertices;
		dirty = true;
	}

	/** Translates the polygon's position by the specified horizontal and vertical amounts. */
	public void translate (float x, float y) {
		this.x += x;
		this.y += y;
		dirty = true;
	}

	/** Sets the polygon to be rotated by the supplied degrees. */
	public void setRotation (float degrees) {
		this.rotation = degrees;
		dirty = true;
	}

	/** Applies additional rotation to the polygon by the supplied degrees. */
	public void rotate (float degrees) {
		rotation += degrees;
		dirty = true;
	}

	/** Sets the amount of scaling to be applied to the polygon. */
	public void setScale (float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		dirty = true;
	}

	/** Applies additional scaling to the polygon by the supplied amount. */
	public void scale (float amount) {
		this.scaleX += amount;
		this.scaleY += amount;
		dirty = true;
	}

	/** Sets the polygon's world vertices to be recalculated when calling {@link #getTransformedVertices() getTransformedVertices}. */
	public void dirty () {
		dirty = true;
	}
	
	public boolean overlap(Polygon other)
	{
		float overlap = Float.MAX_VALUE;
		//float smallestAxisX = 0;
		//float smallestAxisY = 0;
		//int numInNormalDir;
		
		int offset1 = 0, offset2 = 0;
		int count1 = this.getVertices().length, count2 = other.getVertices().length;
		float[] verts1 = this.getTransformedVertices(), verts2 = getTransformedVertices();

		int end1 = offset1 + count1;
		int end2 = offset2 + count2;

		// Get polygon1 axes
		for (int i = offset1; i < end1; i += 2) {
			float x1 = verts1[i];
			float y1 = verts1[i + 1];
			float x2 = verts1[(i + 2) % count1];
			float y2 = verts1[(i + 3) % count1];

			float axisX = y1 - y2;
			float axisY = -(x1 - x2);

			final float length = (float)Math.sqrt(axisX * axisX + axisY * axisY);
			axisX /= length;
			axisY /= length;

			// -- Begin check for separation on this axis --//

			// Project polygon1 onto this axis
			float min1 = axisX * verts1[0] + axisY * verts1[1];
			float max1 = min1;
			for (int j = offset1; j < end1; j += 2) {
				float p = axisX * verts1[j] + axisY * verts1[j + 1];
				if (p < min1) {
					min1 = p;
				} else if (p > max1) {
					max1 = p;
				}
			}

			// Project polygon2 onto this axis
			float min2 = axisX * verts2[0] + axisY * verts2[1];
			float max2 = min2;
			for (int j = offset2; j < end2; j += 2) {
				// Counts the number of points that are within the projected area.
				float p = axisX * verts2[j] + axisY * verts2[j + 1];
				if (p < min2) {
					min2 = p;
				} else if (p > max2) {
					max2 = p;
				}
			}

			if (!(min1 <= min2 && max1 >= min2 || min2 <= min1 && max2 >= min1)) {
				return false;
			} else {
				float o = Math.min(max1, max2) - Math.max(min1, min2);
				if (min1 < min2 && max1 > max2 || min2 < min1 && max2 > max1) {
					float mins = Math.abs(min1 - min2);
					float maxs = Math.abs(max1 - max2);
					if (mins < maxs) {
						o += mins;
					} else {
						o += maxs;
					}
				}
				if (o < overlap) {
					overlap = o;
				}
			}
			// -- End check for separation on this axis --//
		}

		// Get polygon2 axes
		for (int i = offset2; i < end2; i += 2) {
			float x1 = verts2[i];
			float y1 = verts2[i + 1];
			float x2 = verts2[(i + 2) % count2];
			float y2 = verts2[(i + 3) % count2];

			float axisX = y1 - y2;
			float axisY = -(x1 - x2);

			final float length = (float)Math.sqrt(axisX * axisX + axisY * axisY);
			axisX /= length;
			axisY /= length;

			// -- Begin check for separation on this axis --//
			// Project polygon1 onto this axis
			float min1 = axisX * verts1[0] + axisY * verts1[1];
			float max1 = min1;
			for (int j = offset1; j < end1; j += 2) {
				float p = axisX * verts1[j] + axisY * verts1[j + 1];
				// Counts the number of points that are within the projected area.
				if (p < min1) {
					min1 = p;
				} else if (p > max1) {
					max1 = p;
				}
			}

			// Project polygon2 onto this axis
			float min2 = axisX * verts2[0] + axisY * verts2[1];
			float max2 = min2;
			for (int j = offset2; j < end2; j += 2) {
				float p = axisX * verts2[j] + axisY * verts2[j + 1];
				if (p < min2) {
					min2 = p;
				} else if (p > max2) {
					max2 = p;
				}
			}

			if (!(min1 <= min2 && max1 >= min2 || min2 <= min1 && max2 >= min1)) {
				return false;
			} else {
				float o = Math.min(max1, max2) - Math.max(min1, min2);

				if (min1 < min2 && max1 > max2 || min2 < min1 && max2 > max1) {
					float mins = Math.abs(min1 - min2);
					float maxs = Math.abs(max1 - max2);
					if (mins < maxs) {
						o += mins;
					} else {
						o += maxs;
					}
				}

				if (o < overlap) {
					overlap = o;
				}
			}
			// -- End check for separation on this axis --//
		}

		return true;
	}

	/** Returns an axis-aligned bounding box of this polygon.
	 * 
	 * Note the returned Rectangle is cached in this polygon, and will be reused if this Polygon is changed.
	 * 
	 * @return this polygon's bounding box {@link Rectangle} */
	public Rectangle getBoundingRectangle () {
		float[] vertices = getTransformedVertices();

		float minX = vertices[0];
		float minY = vertices[1];
		float maxX = vertices[0];
		float maxY = vertices[1];

		final int numFloats = vertices.length;
		for (int i = 2; i < numFloats; i += 2) {
			minX = minX > vertices[i] ? vertices[i] : minX;
			minY = minY > vertices[i + 1] ? vertices[i + 1] : minY;
			maxX = maxX < vertices[i] ? vertices[i] : maxX;
			maxY = maxY < vertices[i + 1] ? vertices[i + 1] : maxY;
		}

		if (bounds == null) bounds = new Rectangle();
		bounds.x = minX;
		bounds.y = minY;
		bounds.width = maxX - minX;
		bounds.height = maxY - minY;

		return bounds;
	}

	/** Returns whether an x, y pair is contained within the polygon. */
	public boolean contains (float x, float y) {
		final float[] vertices = getTransformedVertices();
		final int numFloats = vertices.length;
		int intersects = 0;

		for (int i = 0; i < numFloats; i += 2) {
			float x1 = vertices[i];
			float y1 = vertices[i + 1];
			float x2 = vertices[(i + 2) % numFloats];
			float y2 = vertices[(i + 3) % numFloats];
			if (((y1 <= y && y < y2) || (y2 <= y && y < y1)) && x < ((x2 - x1) / (y2 - y1) * (y - y1) + x1)) intersects++;
		}
		return (intersects & 1) == 1;
	}

	public boolean contains (Vector2 point) {
		return contains(point.x, point.y);
	}

	/** Returns the x-coordinate of the polygon's position within the world. */
	public float getX () {
		return x;
	}

	/** Returns the y-coordinate of the polygon's position within the world. */
	public float getY () {
		return y;
	}

	/** Returns the x-coordinate of the polygon's origin point. */
	public float getOriginX () {
		return originX;
	}

	/** Returns the y-coordinate of the polygon's origin point. */
	public float getOriginY () {
		return originY;
	}

	/** Returns the total rotation applied to the polygon. */
	public float getRotation () {
		return rotation;
	}

	/** Returns the total horizontal scaling applied to the polygon. */
	public float getScaleX () {
		return scaleX;
	}

	/** Returns the total vertical scaling applied to the polygon. */
	public float getScaleY () {
		return scaleY;
	}
}