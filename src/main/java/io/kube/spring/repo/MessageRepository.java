package io.kube.spring.repo;

import java.util.List;

import org.springframework.data.repository.Repository;

import io.kube.spring.data.HelloWorld;

/**
 * 
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
public interface MessageRepository extends Repository<HelloWorld, String> {

	List<HelloWorld> findAll();

	HelloWorld save(HelloWorld saved);

}
