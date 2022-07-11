package inflearn.springcorebasic.singleton;

public class SingletonService {

	private final static SingletonService instance = new SingletonService();

	private SingletonService() {
	}

	public static SingletonService getInstance() {
		return instance;
	}
}
