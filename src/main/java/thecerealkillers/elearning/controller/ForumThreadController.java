package thecerealkillers.elearning.controller;


import thecerealkillers.elearning.model.ForumThread;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


/**
 * Created by Dani
 */
public interface ForumThreadController {

    @RequestMapping(value = "/threads/topic/{topic}", method = RequestMethod.POST)
    ResponseEntity createThread(
            @RequestBody ForumThread newThread,
            @PathVariable("topic") String topic,
            @RequestHeader(value = "token") String token);


    @RequestMapping(value = "/threads", method = RequestMethod.GET)
    ResponseEntity<?> getAll(
            @RequestHeader(value = "token") String token);


    @RequestMapping(value = "/threads/owner/{threadOwner}", method = RequestMethod.GET)
    ResponseEntity<?> getThreadsOwnedByUser(
            @PathVariable("threadOwner") String userName,
            @RequestHeader(value = "token") String token);


    @RequestMapping(value = "/threads/title/{threadTitle}", method = RequestMethod.GET)
    ResponseEntity<?> getThreadByTitle(
            @PathVariable("threadTitle") String threadTitle,
            @RequestHeader(value = "token") String token);


    @RequestMapping(value = "/threads/topic/{threadTopic}", method = RequestMethod.GET)
    ResponseEntity<?> getThreadsForTopic(
            @PathVariable("threadTopic") String threadTopic,
            @RequestHeader(value = "token") String token);


    @RequestMapping(value = "/threads/update/{oldTitle}", method = RequestMethod.POST)
    ResponseEntity updateThread(
            @PathVariable("oldTitle") String oldTitle,
            @RequestBody ForumThread newThread,
            @RequestHeader(value = "token") String token);


    @RequestMapping(value = "/threads", method = RequestMethod.DELETE)
    ResponseEntity deleteThreadByTitle(
            @RequestParam(value = "threadTitle", required = true) String threadTitle,
            @RequestHeader(value = "token") String token);
}