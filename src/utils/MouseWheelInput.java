package utils;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseWheelInput implements MouseWheelListener {
	public int change=0;
	
	public void reset() {
		change=0;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		int notches = e.getWheelRotation();
		if (notches < 0) {
			change = 1;
		} else {
			change = -1;
		}
	}

}
