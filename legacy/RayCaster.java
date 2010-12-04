package utils;


import java.awt.Point;

import graphics.BlockWorld;

public class RayCaster {
	public static Point3D raytrace(int x0, int y0, Point p1, BlockWorld world) {
		return raytrace(x0,y0,p1.x*25,p1.y*25,world);
	}
	
	public static Point3D raytrace(int x0, int y0, int x1, int y1, BlockWorld world) {
		Point3D result = new Point3D(42,42,42);
		Point3D lastPoint = new Point3D(x0, y0,42);
	    int dx = Math.abs(x1 - x0);
	    int dy = Math.abs(y1 - y0);
	    int x = x0;
	    int y = y0;
	    int n = 1 + dx + dy;
	    int x_inc = (x1 > x0) ? 1 : -1;
	    int y_inc = (y1 > y0) ? 1 : -1;
	    int error = dx - dy;
	    dx *= 2;
	    dy *= 2;
	    
	    for (; n > 0; --n) {
	    	lastPoint = new Point3D(x,y,42);
	        if (error > 0) {
	            x += x_inc;
	            error -= dy;
	        } else {
	            y += y_inc;
	            error += dx;
	        }
	        
	    	if (!world.isBlockTraversable(p2b(x), p2b(y))) {
	    		result = new Point3D(p2b(x), p2b(y), 42);
	    		dx = p2b(lastPoint.x)-result.x; 
	    		dy = p2b(lastPoint.y)-result.y;
	    		result = new Point3D(result.x-p2b(x0), result.y-p2b(y0),42);
	    		if (dx<0 && dy==0) {
	    			result.z=1;
	    		} else if (dx==0 && dy<0) {
	    			result.z=2;
	    		} else if (dx>0 && dy==0) {
	    			result.z=3;
	    		} else if (dx==0 && dy>0) {
	    			result.z=4;
	    		}
	    		break;
	    	}
	    }
	    
	    return result;
	}
	
	public static int p2b(int p) {
		return (int) Math.floor(p/25);
	}
}
