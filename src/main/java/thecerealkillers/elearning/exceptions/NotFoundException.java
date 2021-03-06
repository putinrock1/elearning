package thecerealkillers.elearning.exceptions;

/**
 * Created by Dani.
 */
public class NotFoundException extends Exception  {


    //----------------------------Topic------------------------------------------------------------------------

    public static final String NO_TOPIC_WITH_TITLE = "No topic in the database with this name.\n";
    public static final String NO_TOPIC = "No topics in the database with this title.\n";
    public static final String NO_TOPICS = "No topics in the database.\n";


    //----------------------------Thread-----------------------------------------------------------------------

    public static final String NO_THREADS_BY_OWNER = "No threads owned by this user in the database.\n";
    public static final String NO_THREAD = "The specified topic doesn't contain this thread.\n";
    public static final String NO_THREADS = "No threads in the database.\n";


    //----------------------------Comment----------------------------------------------------------------------

    public static final String NO_COMMENT = "No comment found with this ID.\n";


    //----------------------------User-------------------------------------------------------------------------

    public static final String NO_USER = "No user with this username.\n";


    public NotFoundException(String message) {
        super(message);
    }
}
