package utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MixedUtils {
	/**
	 * Returns a {@code File} that points to the application data with the 
	 * specified path. The directory returned will be a subdirectory with
	 * the application's name, in the platform-specific location for storing
	 * application data. If the subdirectory does not already exist it is
	 * created.
	 * @param appName The name of the application.
	 * @param path Relative path to the requested file.
	 * @return A {@code File} object that points to the requested file.
	 * @throws IllegalArgumentException if the path is empty or absolute.
	 */
	public static File getApplicationData(String appName, String path) {

	    if (appName.length() == 0) {
	        throw new IllegalArgumentException("Invalid application name");
	    }
	        
	    if ((path.length() == 0) || path.startsWith("/")) {
	        throw new IllegalArgumentException("Invalid path: " + path);
	    }

	    File appdir = null;
	        
	    if (System.getProperty("os.name").startsWith("Windows")) { 
	        appdir = new File(System.getenv("APPDATA"));
	    } else if (System.getProperty("os.name").startsWith("Mac OS X")) {
	        // This will also work for non-English versions of Mac OS X
	        appdir = new File(System.getenv("HOME") + "/Library/Application Support"); 
	    }
	    
	    // If auto-detection failed, or if not known, fall back to user home
	    if ((appdir == null) || !appdir.exists() || !appdir.isDirectory()) {
	        appdir = new File(System.getProperty("user.home"));
	        appName = "." + appName;
	    }
	        
	    File dir = new File(appdir, appName);
	    if (!dir.exists()) {
	        boolean success = dir.mkdir();
	        if (!success) {
	            throw new IllegalStateException("Cannot create directory: " + dir.getAbsolutePath());
	        }
	    }
	    File sub = new File(dir, path);
	    if (!sub.exists()) {
	        boolean success = sub.mkdir();
	        if (!success) {
	            throw new IllegalStateException("Cannot create directory: " + sub.getAbsolutePath());
	        }
	    }
	        
	    return new File(dir.getAbsolutePath(), path);
	}
	public static float getRotatedX(float currentX, float currentY, float pivotX, float pivotY, float angle) {
		float x = currentX-pivotX;
		float y = currentY-pivotY;
		float a = (float) Math.toRadians(angle);
		float xr = (float) ((x * Math.cos(a)) - (y * Math.sin(a)));
		return xr+pivotX;
	}
	
	public static float getRotatedY(float currentX, float currentY, float pivotX, float pivotY, float angle) {
		float x = currentX-pivotX;
		float y = currentY-pivotY;
		float a = (float) Math.toRadians(angle);
		float yr = (float) ((x * Math.sin(a)) + (y * Math.cos(a)));
		return yr+pivotY;
	}
	
	public static boolean shouldRotateCounterClockwise(float angleFrom, float angleTo) {
		float diff = (angleFrom - angleTo) % 360;
		return diff > 0 ? diff < 180 : diff < -180;
	}
	
	public static Point lineIntersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		if (denom == 0.0) { // Lines are parallel.
			return null;
		}
		double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))/denom;
		double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))/denom;
        if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
            // Get the intersection point.
            return new Point((int) (x1 + ua*(x2 - x1)), (int) (y1 + ua*(y2 - y1)));
        }

		return null;
	}
	
	public static float euclideanDistance(float x1, float y1, float x2, float y2) {
		float a = x1-x2;
		float b = y1-y2;
		return (float) Math.sqrt(a*a + b*b);
	}
	
	public static String getStackTrace(){
		StringWriter sw = new StringWriter();
		new Throwable().printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public static void ultraHandySysoutJumpWithOneClickToTheCodeInEclipse(Object o){
		System.out.println(""+o+"\n--------------\n"+getStackTrace());
	}
	
	public static float getXAtEndOfRotatedLineByOrigin(float x, float lineLength, float angle) {
		return x + (float) Math.cos(Math.toRadians(angle)) * lineLength;
	}

	public static float getYAtEndOfRotatedLineByOrigin(float y, float lineLength, float angle) {
		return y + (float) Math.sin(Math.toRadians(angle)) * lineLength;
	}
	
    /**
	 * The angle of the vector (x, y). The angle is between 0 and 2*PI.
	 * @param x The x value of the vector.
	 * @param y The y value of the vector.
	 * @return The angle of the vector (x, y).
	 */
	public static double findAngle(double x, double y){
		double theta = 0;
		if(x == 0){
			if(y>0){
				theta = Math.PI/2;
			}else if(y < 0){
				theta = Math.PI*3/2;
			}
		}
		if(x > 0){
			theta = Math.atan(y/Math.abs(x));
		}
		if(x < 0){
			theta = Math.PI - Math.atan(y/Math.abs(x));
		}

		if(theta < 0){
			theta += Math.PI*2;
		}
		return theta;
	}
	
	/**
	 * Tests to see if two lines intersect
	 * @param v1 A {@code Point} representing the start of line one
	 * @param v2 A {@code Point} representing the end of line one
	 * @param v3 A {@code Point} representing the start of line two
	 * @param v4 A {@code Point} representing the end of line two
	 * @return {@code true} if the lines intersect, {@code false} if they don't
	 */
	public boolean LineIntersectLine(Point v1, Point v2, Point v3, Point v4 )
	{
	    float denom = ((v4.y - v3.y) * (v2.x - v1.x)) - ((v4.x - v3.x) * (v2.y - v1.y));
	    float numerator = ((v4.x - v3.x) * (v1.y - v3.y)) - ((v4.y - v3.y) * (v1.x - v3.x));

	    float numerator2 = ((v2.x - v1.x) * (v1.y - v3.y)) - ((v2.y - v1.y) * (v1.x - v3.x));

	    if ( denom == 0.0f )
	    {
	        if ( numerator == 0.0f && numerator2 == 0.0f )
	        {
	            return false;//COINCIDENT;
	        }
	        return false;// PARALLEL;
	    }
	    float ua = numerator / denom;
	    float ub = numerator2/ denom;

	    return (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f);
	}


	/**
	 * Tests to see if two lines intersect
	 * @param v1 A {@code Point} representing the start of the line
	 * @param v2 A {@code Point} representing the end of the line
	 * @param r A {@code Rectangle} representing the start of rectangle
	 * @return {@code true} if they intersect, {@code false} if they don't
	 */
	public int LineIntersectsRectangle(Point v1, Point v2, Rectangle r)
	{
	        Point lowerLeft = new Point( r.x, r.y+r.height );
	        Point upperRight = new Point( r.x+r.width, r.y );
	        Point upperLeft = new Point( r.x, r.y );
	        Point lowerRight = new Point( r.x+r.width, r.y+r.height);
//	        // check if it is inside
//	        if (v1.x > lowerLeft.x && v1.x < upperRight.x && v1.y < lowerLeft.y && v1.y > upperRight.y &&
//	            v2.x > lowerLeft.x && v2.x < upperRight.x && v2.y < lowerLeft.y && v2.y > upperRight.y )
//	        {   
//	            return true;
//	        }
	        // check each line for intersection
	        if (LineIntersectLine(v1,v2, upperLeft, lowerLeft )) return 1;
	        if (LineIntersectLine(v1,v2, lowerLeft, lowerRight)) return 4;
	        if (LineIntersectLine(v1,v2, upperLeft, upperRight)) return 3;
	        if (LineIntersectLine(v1,v2, upperRight, lowerRight)) return 2;
	        return 0;
	}

}
