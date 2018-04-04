/**
 * 
 */
package io.kube.spring.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.kube.spring.data.Message;
import io.kube.spring.data.Response;
import io.kube.spring.repo.MessageRepository;

/**
 * <p>
 * <b>Overview:</b>
 * <p>
 * 
 * 
 * <pre>
 * &#64;projectName kube-server
 * Creation date: Mar 30, 2018
 * &#64;author Amit Kshirsagar
 * &#64;version 1.0
 * &#64;since
 * 
 * <p><b>Modification History:</b><p>
 * 
 * 
 * </pre>
 */
@Service
public class MessageService {

	@Autowired
	private MessageRepository repository;

	@HystrixCommand(fallbackMethod = "alternateControllerMethod", commandKey = "list", groupKey = "MessageService")
	public Response findAll() throws Exception {
		List<Message> messageList = null;
		if (getRandomNumberInRange(10, 20) > 15) {
			throw new Exception("Artificial Test Exception");
		} else {
			messageList = repository.findAll();
		}
		return new Response().setBaseResponse(messageList);
	}

	@HystrixCommand(fallbackMethod = "alternateControllerMethod", commandKey = "save", groupKey = "MessageService")
	public Response save(Message message) throws Exception {
		if (!message.getName().contains("server")) {
			message = repository.save(message);
		} else {
			throw new Exception("Artificial Test Exception");
		}
		return new Response().setBaseResponse(message);
	}

	private static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	public Response<List<Message>> findDummy() {
		List<Message> messageList = new ArrayList<>();
		return new Response().setBaseResponse(messageList);
	}

	public Response alternateControllerMethod() {
		return new Response().addError("Mongodb not available...Failing over....alternateListControllerMethod")
				.setStatus("failure");
	}

	public Response alternateControllerMethod(Message message, Throwable e) {
		return new Response()
				.addError("Mongodb not available...Failing over....alternateControllerMethod:" + e.getMessage())
				.setStatus("failure");
	}

}
