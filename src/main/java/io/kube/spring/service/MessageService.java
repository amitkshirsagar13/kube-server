/**
 * 
 */
package io.kube.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public Response<List<Message>> findAll() {
		List<Message> messageList = repository.findAll();
		return new Response().setBaseResponse(messageList);
	}

	public Response<List<Message>> findDummy() {
		List<Message> messageList = new ArrayList<>();
		return new Response().setBaseResponse(messageList);
	}

	public Response save(Message message) {
		message = repository.save(message);
		return new Response().setBaseResponse(message);
	}

}
