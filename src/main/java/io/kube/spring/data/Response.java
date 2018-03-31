/**
 * 
 */
package io.kube.spring.data;

/**
 * <p>
 * <b>Overview:</b>
 * <p>
 * 
 * 
 * <pre>
 * &#64;projectName kube-server
 * Creation date: Mar 31, 2018
 * &#64;author Amit Kshirsagar
 * &#64;version 1.0
 * &#64;since
 * 
 * <p><b>Modification History:</b><p>
 * 
 * 
 * </pre>
 */
public class Response<T> {

	private long responseTime;
	private String server;
	private String port;
	private T baseResponse;

	/**
	 * @return the responseTime
	 */
	public long getResponseTime() {
		return responseTime;
	}

	/**
	 * @param responseTime
	 *            the responseTime to set
	 */
	public Response setResponseTime(long responseTime) {
		this.responseTime = responseTime;
		return this;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server
	 *            the server to set
	 */
	public Response setServer(String server) {
		this.server = server;
		return this;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public Response setPort(String port) {
		this.port = port;
		return this;
	}

	/**
	 * @return the baseResponse
	 */
	public T getBaseResponse() {
		return baseResponse;
	}

	/**
	 * @param baseResponse
	 *            the baseResponse to set
	 */
	public Response setBaseResponse(T baseResponse) {
		this.baseResponse = baseResponse;
		return this;
	}

}
