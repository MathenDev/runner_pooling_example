package runner_pooling;

public interface Task {

	Object perform() throws Exception;

	void onDone(Object t);

	void onException(Exception e);

}
