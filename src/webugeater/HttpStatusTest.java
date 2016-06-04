package webugeater;

import java.io.IOException;
import java.net.HttpURLConnection;

import webugeater.WebResource;

public class HttpStatusTest extends Test implements WebResource.Test
{
	private HttpURLConnection url_connection;
	
	public HttpStatusTest configure(HttpURLConnection url_connection)
	{
		this.url_connection = url_connection;
		return this;
	}

	public void perform() throws IOException { }
	
	public Boolean failed() throws IOException
	{
		if (this.url_connection.getResponseCode() == 404) { return true; }
		if (this.url_connection.getResponseCode() == 500) { return true; }
		return false;
	}
	
	public String info() throws IOException
	{
		return this.getClass().getName() + " result " + this.url_connection.getResponseCode() + ".";
	}
}
