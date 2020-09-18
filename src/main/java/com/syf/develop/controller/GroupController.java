package com.syf.develop.controller;

import com.syf.develop.entity.Group;
import com.syf.develop.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
class GroupController {

    private final Logger log = LoggerFactory.getLogger(GroupController.class);
    private GroupRepository groupRepository;

    public GroupController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @CrossOrigin
    @GetMapping("/groups")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    Collection<Group> groups() {
        return groupRepository.findAll();
    }

    @CrossOrigin
    @GetMapping("/group/{id}")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    ResponseEntity<?> getGroup(@PathVariable Long id) {
        Optional<Group> group = groupRepository.findById(id);
        return group.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @CrossOrigin
    @PostMapping("/group")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    ResponseEntity<Group> createGroup(@Valid @RequestBody Group group) throws URISyntaxException {
        log.info("Request to create group: {}", group);
        Group result = groupRepository.save(group);
        return ResponseEntity.created(new URI("/api/group/" + result.getId()))
                .body(result);
    }

    @CrossOrigin
    @PutMapping("/group/{id}")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    ResponseEntity<Group> updateGroup(@Valid @RequestBody Group group) {
        log.info("Request to update group: {}", group);
        Group result = groupRepository.save(group);
        return ResponseEntity.ok().body(result);
    }

    @CrossOrigin
    @DeleteMapping("/group/{id}")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        log.info("Request to delete group: {}", id);
        groupRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
