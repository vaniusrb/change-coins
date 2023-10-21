package com.vanius.change.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.vanius.change.exception.ChangeNotEnoughException;
import com.vanius.change.services.change.ChangeService;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * The type Change controller.
 */
@RestController
@RequestMapping("/api/v1")
public class ChangeController {

    @Autowired
    ChangeService changeService;

    @ResponseBody
    @GetMapping("/change/{value}")
    public Object change(@PathVariable(value = "value") BigDecimal value)
            throws ChangeNotEnoughException {
        return changeService.change(value, Optional.empty());
    }

}
