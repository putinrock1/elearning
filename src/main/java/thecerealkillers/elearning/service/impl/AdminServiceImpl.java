package thecerealkillers.elearning.service.impl;


import thecerealkillers.elearning.exceptions.EmailException;
import thecerealkillers.elearning.exceptions.NotFoundException;
import thecerealkillers.elearning.exceptions.ServiceException;
import thecerealkillers.elearning.model.AccountSuspensionInfo;
import thecerealkillers.elearning.model.ChangeAccountTypeInfo;
import thecerealkillers.elearning.exceptions.DAOException;
import thecerealkillers.elearning.model.AdminSignUpInfo;
import thecerealkillers.elearning.model.UserSignUpInfo;
import thecerealkillers.elearning.service.AdminService;
import thecerealkillers.elearning.service.UserService;
import thecerealkillers.elearning.dao.UserStatusDAO;
import thecerealkillers.elearning.dao.UserRoleDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by Dani.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleDAO userRoleDAO;

    @Autowired
    private UserStatusDAO userStatusDAO;


    @Override
    public void createAccount(AdminSignUpInfo newUser) throws ServiceException, NotFoundException {
        try {
            userService.addAccount(new UserSignUpInfo(newUser.getUsername(), newUser.getFirstName(), newUser.getLastName(),
                    "", newUser.getEmail()), newUser.getUserRole());

            userStatusDAO.activateAccount(newUser.getUsername());
            userService.setPassword(newUser.getUsername());

        } catch (DAOException daoException) {
            throw new ServiceException(ServiceException.FAILED_DAO_ROLE_CHG);

        } catch (ServiceException serviceException) {
            throw new ServiceException(serviceException.getMessage());

        } catch (EmailException emailException) {
            try {
                userService.deleteUserAccount(newUser.getUsername());
            } catch (DAOException dao_ex) {
                throw new ServiceException(ServiceException.FAILED_DAO_DELETE_ACCOUNT);
            }

            throw new ServiceException(emailException.getMessage());
        }
    }

    @Override
    public void suspendAccount(AccountSuspensionInfo suspendInfo) throws ServiceException, NotFoundException {
        try {
            if (userService.exists(suspendInfo.getAccountUsername())) {
                userStatusDAO.suspendAccount(suspendInfo.getAccountUsername());
            } else {
                throw new NotFoundException(NotFoundException.NO_USER);
            }
        } catch (DAOException daoException) {
            throw new ServiceException(ServiceException.FAILED_SUSPEND_ACCOUNT);
        }
    }

    @Override
    public void reactivateAccount(AccountSuspensionInfo reactivateInfo) throws ServiceException, NotFoundException {
        try {
            if (userService.exists(reactivateInfo.getAccountUsername())) {
                userStatusDAO.activateAccount(reactivateInfo.getAccountUsername());
            } else {
                throw new NotFoundException(NotFoundException.NO_USER);
            }
        } catch (DAOException daoException) {
            throw new ServiceException(ServiceException.FAILED_ACTIVATE_ACCOUNT);
        }
    }

    @Override
    public void changeAccountType(ChangeAccountTypeInfo accountTypeInfo) throws ServiceException, NotFoundException {
        try {
            if (userService.exists(accountTypeInfo.getAccountUsername())) {
                userRoleDAO.changeRole(accountTypeInfo.getAccountUsername(), accountTypeInfo.getNewAccountType());
            } else {
                throw new NotFoundException(NotFoundException.NO_USER);
            }
        } catch (DAOException daoException) {
            throw new ServiceException(ServiceException.FAILED_CHANGE_ACCOUNT_TYPE);
        }
    }
}
