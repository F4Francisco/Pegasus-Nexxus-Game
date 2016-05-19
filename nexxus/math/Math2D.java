package math;

import java.awt.Polygon;


public abstract class Math2D
{
    /**
     * Calculates and returns the distance between the two given positions.
     * @param a The first position.
     * @param b The second position.
     * @return The distance between them.
     */
    public static double distance(Point a, Point b)
    {
        return Math.sqrt(
                    Math.pow(a.getX() - b.getX(), 2) +
                    Math.pow(a.getY() - b.getY(), 2));
    }

    /**
     * Calculates and returns the direction from the first given position to the
     * second one. If two two positions are located at the exact same spot, the
     * direction will be 0.
     * @param a The first position.
     * @param b The second position.
     * @return The direction from the first to the second.
     */
    public static double direction(Point a, Point b)
    {
        if (a.getX() < b.getX())
            return Math.atan(
                    (a.getY() - b.getY()) /
                    (a.getX() - b.getX()));
        else if (a.getX() > b.getX())
            return Math.PI + Math.atan(
                    (a.getY() - b.getY()) /
                    (a.getX() - b.getX()));
        else
            if (a.getY() < b.getY())
                return Math.PI * 0.5d;
            else if (a.getY() > b.getY())
                return Math.PI * 1.5d;
            else return 0;
    }

    /**
     * Returns a point representing the distance along the x- and y-axis from
     * a position to another located the specified amount of steps away in the
     * specified direction from the first one.
     *
     * As an example you could assume that you want to calculate where a body
     * will be located after 1 second, assuming the body is moving with a speed
     * of 2 and in the direction of 3 radians. The next location of the body
     * will be lengthDir(2, 3) relative to the bodys original position. If the
     * original position is stored in a Position2D called pos, then the movement
     * can be calculated as follows:
     *      pos.appendPosition(Math2D.lengthDir(speed, direction));
     *
     * @param length The length.
     * @param dir The direction.
     * @return The distance along the x- and y-axis.
     */
    public static Point lengthDir(double length, double dir)
    {
        return new Point(
                (int) (Math.cos(dir) * length),
                (int) (Math.sin(dir) * length));
    }

    /**
     * Returns the position where the first collision occurs on a line drawn
     * from the first given point to the second one.
     * @param pol The polygon to check collision towards.
     * @param start The first point.
     * @param end The end point.
     * @return The position of the collision or null if none occured.
     */
    public static Point getCollisionPoint(Polygon pol, Point start, Point end)
    {
        if (pol == null) throw new Error("Polygon is null.");
        if (start == null) throw new Error("Start collision-point is null.");
        if (end == null) throw new Error("End collision-point is null.");

        if (pol.npoints < 2) return null;

        Point col = null;

        for (int i = 1; i < pol.npoints; i++)
        {
            Point p1 = new Point(pol.xpoints[i - 1], pol.ypoints[i - 1]);
            Point p2 = new Point(pol.xpoints[i], pol.ypoints[i]);

            if (p1 == null) throw new Error("P1 collision-point is null.");
            if (p2 == null) throw new Error("P2 collision-point is null.");

            Ray r1 = new Ray(start, end);
            Ray r2 = new Ray(p1, p2);
            
            Point cross = r1.getIntersection(r2);
            if (cross == null) continue;
            if (new Rect(start, end).contains(cross)
            &&  new Rect(p1, p2).contains(cross))
                if (col == null || distance(start, cross) < distance(start, col))
                    col = cross;
            else continue;
        }
        return col;
    }
}