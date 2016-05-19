package math;


public class Ray
{
    private Point start, end;

    public Ray(Point start, Point end)
    {
        this.start = start;
        this.end = end;
    }

    public void setStart(Point start)
    {
        this.start = start;
    }

    public void setEnd(Point end)
    {
        this.end = end;
    }

    public Point getStart()
    {
        return start;
    }

    public Point getEnd()
    {
        return end;
    }

    public Point getIntersection(Ray other)
    {
        double k1 = getK();
        double k2 = other.getK();
        double m1 = getM();
        double m2 = other.getM();

        if (k2 - k1 == 0) return null;

        double x = (m1 - m2) / (k2 - k1);
        return new Point((int) x, (int) getY(x));
    }

    public double getK()
    {
        if (getEnd().getX() - getStart().getX() == 0) return 0;
        return ((double) (getEnd().getY() - getStart().getY())) / ((double) (getEnd().getX() - getStart().getX()));
    }

    public double getM()
    {
        return getStart().getY() - getK() * getStart().getX();
    }

    public double getY(double x)
    {
        return getK() * x + getM();
    }
}