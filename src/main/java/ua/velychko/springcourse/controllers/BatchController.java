package ua.velychko.springcourse.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.velychko.springcourse.dao.PersonDAO;

@Controller
@RequestMapping("/test-batch-update")
public class BatchController {

    private final PersonDAO personDAO;

    private static final Logger logger = LoggerFactory.getLogger(BatchController.class);

    @Autowired
    public BatchController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String index() {
        return "batch/index";
    }

    @GetMapping("/without")
    public String withoutBatch() {
        long before = System.currentTimeMillis();
        personDAO.testMultipleUpdate();
        long after = System.currentTimeMillis();
        logger.info("Time (without batch): {} ms", (after - before));
        return "redirect:/people";
    }
    @GetMapping("/with")
    public String withBatch() {
        long before = System.currentTimeMillis();
        personDAO.testBatchUpdate();
        long after = System.currentTimeMillis();
        logger.info("Batch update time: {} ms", (after - before));
        return "redirect:/people";
    }
}
