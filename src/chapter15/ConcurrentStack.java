package chapter15;

import java.util.concurrent.atomic.AtomicReference;

/**
 * P271 15-6 使用Treiber算法构造非阻塞栈
 * @author skywalker
 *
 */
public class ConcurrentStack<E> {
	
	//栈顶指针
	private final AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();
	
	public void push(E data) {
		Node<E> node = new Node<E>(data);
		Node<E> oldTop;
		while (true) {
			oldTop = top.get();
			node.next = oldTop;
			if (top.compareAndSet(oldTop, node)) {
				break;
			}
		}
	}
	
	public E pop() {
		Node<E> head;
		while (true) {
			head = top.get();
			if (head == null) {
				return null;
			}
			if (top.compareAndSet(head, head.next)) {
				return head.data;
			}
		}
	}
	
	private static class Node<E> {
		private Node<E> next;
		private E data;
		
		public Node(E data) {
			this.data = data;
		}
	}

}
