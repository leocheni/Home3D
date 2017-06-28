package net.msdh.h3d;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: TkachenkoAA
 * Date: 22.06.17
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 */
public class SimpleShadow extends Shape3D {

      SimpleShadow(GeometryArray geom, Vector3f direction, Color3f col,
        float height) {

      int vCount = geom.getVertexCount();
      QuadArray poly = new QuadArray(vCount, GeometryArray.COORDINATES|GeometryArray.COLOR_3);

      int v;
      Point3f vertex = new Point3f();
      Point3f shadow = new Point3f();
      for (v = 0; v < vCount; v++) {
        geom.getCoordinate(v, vertex);
        shadow.set(vertex.x + (vertex.y - height) * direction.x,
            height + 0.0001f, vertex.z +
            (vertex.y - height) * direction.y);

        poly.setCoordinate(v, shadow);
        poly.setColor(v, col);
      }

      this.setGeometry(poly);
    }
}