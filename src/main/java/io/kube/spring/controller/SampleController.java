package io.kube.spring.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.kube.spring.data.HelloWorld;
import io.kube.spring.service.MessageService;

/**
 * <p>
 * <b>Overview:</b>
 * <p>
 * 
 * 
 * <pre>
 * &#64;projectName rds
 * Creation date: Mar 13, 2018
 * &#64;author Amit Kshirsagar
 * &#64;version 1.0
 * &#64;since
 * 
 * <p><b>Modification History:</b><p>
 * 
 * 
 * </pre>
 */
@RestController
@RequestMapping(value = "/rest")
public class SampleController {

	/**
	 * log4j object for debugging.
	 */
	private static Logger log4j = Logger.getLogger(SampleController.class);

	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "/api/hello", method = RequestMethod.GET, produces = "application/json")
	public HelloWorld list(@RequestParam(name = "name") String name) throws Exception {
		return messageService.save(new HelloWorld().setName(name));
	}

}
