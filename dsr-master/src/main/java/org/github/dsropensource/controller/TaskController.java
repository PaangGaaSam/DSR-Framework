package org.github.dsropensource.controller;

import org.github.dsropensource.core.quartz.job.ExampleJob;
import org.github.dsropensource.service.QuartzService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private QuartzService quartzService;

    @GetMapping("/addExample")
    public JSONObject addExample() throws JSONException {
        quartzService.addJob("exampleJob",ExampleJob.class,"* * * * * ?",null);
        return new JSONObject().put("status",true);
    }

    @GetMapping("/removeExample")
    public JSONObject removeExample() throws JSONException {
        quartzService.removeJob("exampleJob");
        return new JSONObject().put("status",true);
    }
}
