package org.aptech.metube.videoservice.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
    private static Logger logger = LogManager.getLogger(BaseController.class);

    private int level_1 = 5000;
    private int level_2 = 10000;
    protected void logKpi(long startTime, String method) {
        Long processTime = System.currentTimeMillis() - startTime;
        if (processTime < level_1) {
            logger.info("Process time method {} = {}", method, processTime );
        }

        if (processTime >= level_1 && processTime < level_2) {
            logger.warn("Process time method {} = {}", method, processTime );
        }

        if (processTime > level_2) {
            logger.warn("Process time method {} = {}", method, processTime );
            // todo: send mail --> operator system
        }
    }
}
