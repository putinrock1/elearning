package thecerealkillers.elearning.controller.impl;


import thecerealkillers.elearning.controller.ForumThreadController;
import thecerealkillers.elearning.exceptions.NotFoundException;
import thecerealkillers.elearning.exceptions.ServiceException;
import thecerealkillers.elearning.model.ForumThreadIdentifier;
import thecerealkillers.elearning.service.ForumThreadService;
import thecerealkillers.elearning.service.PermissionService;
import thecerealkillers.elearning.service.SessionService;
import thecerealkillers.elearning.service.AuditService;
import thecerealkillers.elearning.utilities.Constants;
import thecerealkillers.elearning.model.ForumThread;
import thecerealkillers.elearning.model.AuditItem;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * Created by Dani
 */
@RestController
@CrossOrigin
public class ForumThreadControllerImpl implements ForumThreadController {

    @Autowired
    private ForumThreadService forumThreadService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private AuditService auditService;


    @Override
    @RequestMapping(value = "/threads", method = RequestMethod.POST)
    public ResponseEntity createThread(@RequestBody ForumThread newThread,
                                       @RequestHeader(value = "token") String token) {
        String actionName = "ForumThreadControllerImpl.createThread";

        try {
            if (!sessionService.isSessionActive(token)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String userRoleForToken = sessionService.getUserRoleByToken(token);
            String usernameForToken = sessionService.getUsernameByToken(token);

            try {
                if (permissionService.isOperationAvailable(actionName, userRoleForToken)) {
                    forumThreadService.add(newThread);

                    auditService.addEvent(new AuditItem(usernameForToken, actionName, newThread.toString(), Constants.THREAD_CREATE_THREAD, true));
                    return new ResponseEntity(HttpStatus.CREATED);
                } else {
                    auditService.addEvent(new AuditItem(usernameForToken, actionName, newThread.toString(), Constants.NO_PERMISSION, false));
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } catch (ServiceException serviceException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, newThread.toString(), serviceException.getMessage(), false));
                return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

            } catch (NotFoundException notFoundException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, newThread.toString(), notFoundException.getMessage(), false));
                return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException serviceException) {
            return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    @RequestMapping(value = "/threads/{topicTitle}{threadTitle}", method = RequestMethod.POST)
    public ResponseEntity<?> getThread(@RequestParam(value = "topicTitle") String topicTitle,
                                       @RequestParam(value = "threadTitle") String threadTitle,
                                       @RequestHeader(value = "token") String token) {
        String actionName = "ForumThreadControllerImpl.getThread";

        try {
            if (!sessionService.isSessionActive(token)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String userRoleForToken = sessionService.getUserRoleByToken(token);
            String usernameForToken = sessionService.getUsernameByToken(token);

            try {
                if (permissionService.isOperationAvailable(actionName, userRoleForToken)) {
                    ForumThread thread = forumThreadService.getThread(threadTitle, topicTitle);

                    auditService.addEvent(new AuditItem(usernameForToken, actionName,
                            "Topic: " + topicTitle + " Thread: " + threadTitle, Constants.THREAD_GET, true));
                    return new ResponseEntity<>(thread, HttpStatus.OK);
                } else {
                    auditService.addEvent(new AuditItem(usernameForToken, actionName,
                            "Topic: " + topicTitle + " Thread: " + threadTitle, Constants.NO_PERMISSION, false));
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } catch (ServiceException serviceException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName,
                        "Topic: " + topicTitle + " Thread: " + threadTitle, serviceException.getMessage(), false));
                return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

            } catch (NotFoundException notFoundException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName,
                        "Topic: " + topicTitle + " Thread: " + threadTitle, notFoundException.getMessage(), false));
                return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException serviceException) {
            return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    @RequestMapping(value = "/threads/topic/{topicTitle}", method = RequestMethod.GET)
    public ResponseEntity<?> getThreadsInTopic(@PathVariable("topicTitle") String topicTitle,
                                               @RequestHeader(value = "token") String token) {
        String actionName = "ForumThreadControllerImpl.getThreadsInTopic";

        try {
            if (!sessionService.isSessionActive(token)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String userRoleForToken = sessionService.getUserRoleByToken(token);
            String usernameForToken = sessionService.getUsernameByToken(token);

            try {
                if (permissionService.isOperationAvailable(actionName, userRoleForToken)) {
                    List<ForumThread> threadList = forumThreadService.getThreadsInTopic(topicTitle);

                    auditService.addEvent(new AuditItem(usernameForToken, actionName, topicTitle, Constants.THREAD_GET_IN_TOPIC, true));
                    return new ResponseEntity<>(threadList, HttpStatus.OK);
                } else {
                    auditService.addEvent(new AuditItem(usernameForToken, actionName, topicTitle, Constants.NO_PERMISSION, false));
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } catch (ServiceException serviceException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, topicTitle, serviceException.getMessage(), false));
                return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

            } catch (NotFoundException notFoundException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, topicTitle, notFoundException.getMessage(), false));
                return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException serviceException) {
            return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    @RequestMapping(value = "/threads/owner/{threadOwner}", method = RequestMethod.GET)
    public ResponseEntity<?> getThreadsOwnedByUser(@PathVariable("threadOwner") String userName,
                                                   @RequestHeader(value = "token") String token) {
        String actionName = "ForumThreadControllerImpl.getThreadsOwnedByUser";

        try {
            if (!sessionService.isSessionActive(token)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String userRoleForToken = sessionService.getUserRoleByToken(token);
            String usernameForToken = sessionService.getUsernameByToken(token);

            try {
                if (permissionService.isOperationAvailable(actionName, userRoleForToken)) {
                    List<ForumThread> threadList = forumThreadService.getThreadsOwnedByUser(userName);

                    auditService.addEvent(new AuditItem(usernameForToken, actionName, userName, Constants.THREAD_GET_BY_USER, true));
                    return new ResponseEntity<>(threadList, HttpStatus.OK);
                } else {
                    auditService.addEvent(new AuditItem(usernameForToken, actionName, userName, Constants.NO_PERMISSION, false));
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } catch (ServiceException serviceException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, userName, serviceException.getMessage(), false));
                return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

            } catch (NotFoundException notFoundException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, userName, notFoundException.getMessage(), false));
                return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException serviceException) {
            return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    @RequestMapping(value = "/threads", method = RequestMethod.GET)
    public ResponseEntity<?> getAll(@RequestHeader(value = "token") String token) {
        String actionName = "ForumThreadControllerImpl.getAll";

        try {
            if (!sessionService.isSessionActive(token)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String userRoleForToken = sessionService.getUserRoleByToken(token);
            String usernameForToken = sessionService.getUsernameByToken(token);

            try {
                if (permissionService.isOperationAvailable(actionName, userRoleForToken)) {
                    List<ForumThread> threadList = forumThreadService.getAll();

                    auditService.addEvent(new AuditItem(usernameForToken, actionName, "", Constants.THREAD_GET_ALL, true));
                    return new ResponseEntity<>(threadList, HttpStatus.OK);
                } else {
                    auditService.addEvent(new AuditItem(usernameForToken, actionName, "", Constants.NO_PERMISSION, false));
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } catch (ServiceException serviceException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, "", serviceException.getMessage(), false));
                return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

            } catch (NotFoundException notFoundException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, "", notFoundException.getMessage(), false));
                return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException serviceException) {
            return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    @RequestMapping(value = "/threads/update/{newTitle}", method = RequestMethod.POST)
    public ResponseEntity updateThread(@PathVariable("newTitle") String newTitle,
                                       @RequestBody ForumThread thread,
                                       @RequestHeader(value = "token") String token) {
        String actionName = "ForumThreadControllerImpl.updateThread";

        try {
            if (!sessionService.isSessionActive(token)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String userRoleForToken = sessionService.getUserRoleByToken(token);
            String usernameForToken = sessionService.getUsernameByToken(token);

            try {
                if (permissionService.isOperationAvailable(actionName, userRoleForToken)) {
                    forumThreadService.updateThread(newTitle, thread);

                    auditService.addEvent(new AuditItem(usernameForToken, actionName, "newTitle = " + newTitle + " | Old thread = " +
                            thread.toString(), Constants.THREAD_UPDATE, true));
                    return new ResponseEntity(HttpStatus.OK);
                } else {
                    auditService.addEvent(new AuditItem(usernameForToken, actionName, "newTitle = " + newTitle + " | Old thread = " +
                            thread.toString(), Constants.NO_PERMISSION, false));
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } catch (ServiceException serviceException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, "newTitle = " + newTitle + " | Old thread = " +
                        thread.toString(), serviceException.getMessage(), false));
                return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

            } catch (NotFoundException notFoundException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, "newTitle = " + newTitle + " | Old thread = " +
                        thread.toString(), notFoundException.getMessage(), false));
                return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException serviceException) {
            return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    @RequestMapping(value = "/threads", method = RequestMethod.DELETE)
    public ResponseEntity deleteThread(@RequestBody ForumThreadIdentifier threadToDelete,
                                       @RequestHeader(value = "token") String token) {
        String actionName = "ForumThreadControllerImpl.deleteThread";

        try {
            if (!sessionService.isSessionActive(token)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String userRoleForToken = sessionService.getUserRoleByToken(token);
            String usernameForToken = sessionService.getUsernameByToken(token);

            try {
                if (permissionService.isOperationAvailable(actionName, userRoleForToken)) {
                    forumThreadService.deleteThread(threadToDelete);

                    auditService.addEvent(new AuditItem(usernameForToken, actionName, threadToDelete.toString(), Constants.THREAD_DELETE, true));
                    return new ResponseEntity(HttpStatus.OK);
                } else {
                    auditService.addEvent(new AuditItem(usernameForToken, actionName, threadToDelete.toString(), Constants.NO_PERMISSION, false));
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } catch (ServiceException serviceException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, threadToDelete.toString(), serviceException.getMessage(), false));
                return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

            } catch (NotFoundException notFoundException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, threadToDelete.toString(), notFoundException.getMessage(), false));
                return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException serviceException) {
            return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
