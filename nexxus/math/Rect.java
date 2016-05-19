package math;


public class Rect
{
    private Point start, end;

    public Rect(Point start, Point end)
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
        return this.start;
    }

    public Point getEnd()
    {
        return this.end;
    }

    public boolean contains(Point p)
    {
        return ((p.getX() > Math.min(getStart().getX(), getEnd().getX()))
             && (p.getY() > Math.min(getStart().getY(), getEnd().getY()))
             && (p.getX() < Math.max(getStart().getX(), getEnd().getX()))
             && (p.getY() < Math.max(getStart().getY(), getEnd().getY())));
    }
}