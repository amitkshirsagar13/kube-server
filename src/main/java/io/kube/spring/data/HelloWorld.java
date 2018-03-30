package io.kube.spring.data;

import org.springframework.data.annotation.Id;

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

public class HelloWorld {
	@Id
	private String id;
	private String name;
	private String message;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public HelloWorld setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return new StringBuilder().append("Hello ").append(name).append("!!!").toString();
	}
}
