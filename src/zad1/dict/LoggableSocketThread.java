package zad1.dict;

public interface LoggableSocketThread {

    default String getThreadLabel() {
        return this.getClass().getSimpleName() + "@" + this.hashCode() + ":";
    }

    default String getConnectionLabel() {
        return "Connection";
    }

    default void logThreadException(Exception exception) {
        System.out.println(getThreadLabel() + " " + exception.getMessage());
        exception.printStackTrace();
    }

    default void logThreadCustomText(String customText) {
        System.out.println(getThreadLabel() + " " + customText);
    }

    default void logThreadStarted() {
        System.out.println(getThreadLabel() + " Started.");
    }

    default void logThreadCannotStart() {
        System.out.println(getThreadLabel() + " Thread cannot start.");
    }

    default void logThreadConnectionEstablished() {
        System.out.println(getThreadLabel() + " " + getConnectionLabel() + " established.");
    }

    default void logThreadConnectionClosed() {
        System.out.println(getThreadLabel() + " " + getConnectionLabel() + " closed.");
    }
}
