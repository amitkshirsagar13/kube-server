package io.kube.spring.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.kube.spring.data.Message;
import io.kube.spring.data.Response;
import io.kube.spring.service.MessageService;

/**
 * <p>
 * <b>Overview:</b>
 * <p>
 * 
 * 
 * <pre>
 * &#64;projectName kube-server
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
public class MessageController {

	/**
	 * log4j object for debugging.
	 */
	private static Logger log4j = Logger.getLogger(MessageController.class);

	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "/api/message", method = RequestMethod.GET, produces = "application/json")
	public Response<Message> list(@RequestParam(name = "name") String name) throws Exception {
		log4j.info("Called Kube Server controller");
		return messageService.save(new Message().setName(name));
	}

	@RequestMapping(value = "/api/listAll", method = RequestMethod.GET, produces = "application/json")
	public Response<List<Message>> list() throws Exception {
		return messageService.findAll();
	}

	@RequestMapping(value = "/api/listDummy", method = RequestMethod.GET, produces = "application/json")
	public Response<List<Message>> listDummy() throws Exception {
		return messageService.findDummy();
	}

}
