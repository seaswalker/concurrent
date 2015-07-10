package chapter4;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.concurrent.CopyOnWriteArrayList;

import annotations.ThreadSafe;

/**
 * 可视化组件
 * 把线程安全性委托给多个线程安全的变量，只要
 * 这些变量是彼此独立的，即组合而成的类并不会在其中包含的多个状态变量上增加任何不变性条件
 * @author skywalker
 *
 */
@ThreadSafe
public class VisualComponent {

	//鼠标监听器
	private final CopyOnWriteArrayList<MouseListener> mouseListeners = new CopyOnWriteArrayList<MouseListener>();
	//键盘监视器
	private final CopyOnWriteArrayList<KeyListener> keyListeners = new CopyOnWriteArrayList<KeyListener>();
	
	public void addMouseListener(MouseListener mouseListener) {
		mouseListeners.add(mouseListener);
	}
	
	public void removeMouseListener(MouseListener mouseListener) {
		mouseListeners.remove(mouseListener);
	}
	
	public void addKeyListener(KeyListener keyListener) {
		keyListeners.add(keyListener);
	}
	
	public void removeKeyListener(KeyListener keyListener) {
		keyListeners.remove(keyListener);
	}
	
}
