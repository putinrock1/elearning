package thecerealkillers.elearning.dao;


import thecerealkillers.elearning.exceptions.DAOException;
import thecerealkillers.elearning.model.SessionDM;

import org.springframework.stereotype.Repository;


/**
 * Created by cuvidk on 11/20/2015.
 * Modified by Dani.
 */
@Repository
public interface SessionDAO {
    /**
     * Adds @session in the database.
     *
     * @throws DAOException if DB problems / other weird problem
     */
    void addSession(SessionDM session) throws DAOException;

    /**
     * Retrieves the Session assigned to user, if it exists one.
     *
     * @return - The session of the user if exists.
     * @throws DAOException if username is inexistent in the DB / DB problems/
     *                      other weird problem.
     */
    SessionDM getSessionByUser(String username) throws DAOException;

    /**
     * Checks if @token is an valid token in Session table in DB.
     *
     * @return - the associated session to token
     * @throws DAOException if token is inexistent / DB problems /
     *                      other weird problem.
     */
    SessionDM getSessionByToken(String token) throws DAOException;

    /**
     * Checks if there exists an active session in the DB for user @username
     *
     * @return true if there already is an active session, false else
     * @throws DAOException if DB problems / etc.
     */
    boolean isSessionAvailable(String username) throws DAOException;

    /**
     * Deletes the session for @user.
     *
     * @throws DAOException
     */
    void deleteSession(String username) throws DAOException;
}
