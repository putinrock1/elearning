package thecerealkillers.elearning.service;


import thecerealkillers.elearning.exceptions.ServiceException;
import thecerealkillers.elearning.model.Module;

import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by cuvidk on 12/22/2015.
 * .
 */
@Service
public interface ModuleService {
    /**
     * Stores the module @module in DB.
     *
     * @throws ServiceException if @module already exists in DB /
     *                          if the course associated to @module doesn't exist in DB /
     *                          if DAOException encountered.
     */
    void storeModule(Module module) throws ServiceException;

    /**
     * Deletes the module @module from DB.
     *
     * @throws ServiceException if DAOException encountered /
     *                          if the module is innexistent in the DB.
     */
    void deleteModule(Module module) throws ServiceException;

    /**
     * Returns a list with all the stored modules where course = @course.
     *
     * @return a list with all the modules.
     * @throws ServiceException if DAOException encountered.
     */
    List<Module> getAll(String cours) throws ServiceException;

    /**
     * Returns the module with the corresponding
     * title and course.
     * @param title
     * @param course
     * @return the corresponding module.
     * @throws ServiceException if the module is innexistent in DB /
     *                          if DAOException encountered.
     */
    Module get(String title, String course) throws ServiceException;

    /**
     * Renames the module @module to @newTitle
     * @param module
     * @param newTitle
     * @throws ServiceException if module is innexistent in DB /
     *                          if DAOException encountered.
     */
    void update(Module module, String newTitle) throws ServiceException;
}