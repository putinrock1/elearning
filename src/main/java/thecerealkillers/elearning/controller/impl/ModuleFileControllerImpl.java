package thecerealkillers.elearning.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import thecerealkillers.elearning.controller.ModuleFileController;
import thecerealkillers.elearning.exceptions.ServiceException;
import thecerealkillers.elearning.model.AuditItem;
import thecerealkillers.elearning.model.ModuleFile;
import thecerealkillers.elearning.service.AuditService;
import thecerealkillers.elearning.service.ModuleFileService;
import thecerealkillers.elearning.service.PermissionService;
import thecerealkillers.elearning.service.SessionService;
import thecerealkillers.elearning.utilities.Constants;

import java.io.IOException;

/**
 * Created by cuvidk on 12/29/2015.
 */
@RestController
public class ModuleFileControllerImpl implements ModuleFileController {
    @Autowired
    private ModuleFileService moduleFileService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AuditService auditService;

    @Override
    @RequestMapping(value = "/courses/{courseTitle}/modules/{moduleTitle}/files", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@RequestHeader("token") String token, @PathVariable("courseTitle") String courseTitle,
                                        @PathVariable("moduleTitle") String moduleTitle,
                                        @RequestParam("file") MultipartFile file) {
        String actionName = "ModuleFileControllerImpl.uploadFile";

        try {
            if (!sessionService.isSessionActive(token)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            String userRoleForToken = sessionService.getUserRoleByToken(token);
            String usernameForToken = sessionService.getUsernameByToken(token);

            try {
                if (permissionService.isOperationAvailable(actionName, userRoleForToken)) {
                    if (!file.isEmpty()) {
                        ModuleFile moduleFile = new ModuleFile();

                        try {
                            moduleFile.setContent(file.getBytes());
                        } catch (IOException e) {
                            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
                        }
                        moduleFile.setName(file.getOriginalFilename());
                        moduleFile.setType(file.getContentType());
                        moduleFile.setSize(file.getSize());
                        moduleFile.setAssociatedCourse(courseTitle);
                        moduleFile.setAssociatedModule(moduleTitle);

                        moduleFileService.storeFile(moduleFile);

                        auditService.addEvent(new AuditItem(usernameForToken, actionName, file.toString(), Constants.MODULE_FILE_CREATED, true));
                        return new ResponseEntity<>(HttpStatus.CREATED);
                    } else {
                        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                } else {
                    auditService.addEvent(new AuditItem(usernameForToken, actionName, file.toString(), Constants.NO_PERMISSION, false));
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            } catch (ServiceException serviceException) {
                auditService.addEvent(new AuditItem(usernameForToken, actionName, file.toString(), serviceException.getMessage(), false));
                return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (ServiceException serviceException) {
            return new ResponseEntity<>(serviceException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
