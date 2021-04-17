package zad1.dict;

public interface LoggableSocketThread {

    default String getThreadLabel() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + ":";
    }

    default String getDefaultConnectionLabel() {
        return "Default connection";
    }

    default void logThreadException(Exception exception) {
        System.out.println(getThreadLabel() + " " + exception.getMessage());
        exception.printStackTrace();
    }

    default void logThreadStarted() {
        System.out.println(getThreadLabel() + " Started.");
    }

    default void logThreadInitialized() {
        System.out.println(getThreadLabel() + " Initialized.");
    }

    default void logThreadCannotStart() {
        System.out.println(getThreadLabel() + " Thread cannot start.");
    }

    default void logThreadCannotInitialize() {
        System.out.println(getThreadLabel() + " Thread cannot initialize.");
    }

    default void logThreadConnectionEstablished() {
        System.out.println(getThreadLabel() + " " + getDefaultConnectionLabel() + " established.");
    }

    default void logThreadConnectionEstablished(String connectionName) {
        System.out.println(getThreadLabel() + " " + connectionName + " established.");
    }

    default void logThreadConnectionClosed() {
        System.out.println(getThreadLabel() + " " + getDefaultConnectionLabel() + " closed.");
    }

    default void logThreadConnectionClosed(String connectionName) {
        System.out.println(getThreadLabel() + " " + connectionName + " closed.");
    }

    default void logThreadConnectionResourcesOpened() {
        System.out.println(getThreadLabel() + " " + getDefaultConnectionLabel() + " resources opened.");
    }

    default void logThreadConnectionResourcesOpened(String connectionName) {
        System.out.println(getThreadLabel() + " " + connectionName + " resources opened.");
    }

    default void logThreadConnectionResourcesClosed() {
        System.out.println(getThreadLabel() + " " + getDefaultConnectionLabel() + " resources closed.");
    }

    default void logThreadConnectionResourcesClosed(String connectionName) {
        System.out.println(getThreadLabel() + " " + connectionName + " resources closed.");
    }

    default void logThreadSent(String line) {
        System.out.println(getThreadLabel() + " Sent " + line);
    }

    default void logThreadReceived(String line) {
        System.out.println(getThreadLabel() + " Received " + line);
    }
}
