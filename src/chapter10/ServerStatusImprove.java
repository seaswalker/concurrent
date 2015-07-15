package chapter10;

import java.util.Set;

/**
 * P195 11-7 对代码11-6进行锁分解
 * @author skywalker
 *
 */
public class ServerStatusImprove {

	private final Set<String> users;
	private final Set<String> queries;
	
	public ServerStatusImprove(Set<String> users, Set<String> queries) {
		this.users = users;
		this.queries = queries;
	}
	
	public void addUser(String user) {
		synchronized (users) {
			users.add(user);
		}
	}
	
	public void removeUser(String user) {
		synchronized (users) {
			users.remove(user);
		}
	}
	
	public void addQuery(String query) {
		synchronized (queries) {
			queries.add(query);
		}
	}
	
	public void removeQuery(String query) {
		synchronized (queries) {
			queries.remove(query);
		}
	}
	
}
