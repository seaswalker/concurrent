package chapter10;

import java.util.Set;

/**
 * P195 11-6 应该进行锁分解的一个典型情况
 * @author skywalker
 *
 */
public class ServerStatus {

	private final Set<String> users;
	private final Set<String> queries;
	
	public ServerStatus(Set<String> users, Set<String> queries) {
		this.users = users;
		this.queries = queries;
	}
	
	public synchronized void addUser(String user) {
		users.add(user);
	}
	
	public synchronized void removeUser(String user) {
		users.remove(user);
	}
	
	public synchronized void addQuery(String query) {
		queries.add(query);
	}
	
	public synchronized void removeQuery(String query) {
		queries.remove(query);
	}
	
}
