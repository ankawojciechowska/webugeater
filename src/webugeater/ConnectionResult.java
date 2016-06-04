package webugeater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;

public class ConnectionResult extends Construct
{
	private String referer;
	private String url;
	private HttpURLConnection url_connection;
	private URL url_URL, content_url;
	private String raw_content;
	private IOException encountered_exception;
	private int response_code;

	public ConnectionResult configure(String url, String referer)
	{
		this.url = url;
		this.url = referer;
		return this;
	}

	public Boolean has_http_error_status()
	{
		if (this.response_code == 404) { return true; }
		if (this.response_code == 500) { return true; }
		return false;
	}

	public void connect()
	{
		try
		{
			this.prepare();
			this.upload();

			this.content_url = this.url_connection.getURL(); // Get URL after redirections
			this.response_code = this.url_connection.getResponseCode();
		}
		catch (IOException e)
		{
			this.encountered_exception = e;
		}
	}

	private void prepare() throws IOException
	{
		this.url_URL = new URL(this.url);
		this.url_connection = ( HttpURLConnection ) this.url_URL.openConnection();
		this.url_connection.setInstanceFollowRedirects(true);
		this.url_connection.setConnectTimeout(10 * 1000);
		this.url_connection.setRequestMethod("GET");
		if (this.referer != null) this.url_connection.setRequestProperty("Referer", this.referer);
		this.url_connection.setRequestProperty("User-Agent",
		"Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0");
		this.url_connection.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
		this.url_connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	}

	private void upload() throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.url_connection.getInputStream()));

		String line;

		while ((line = reader.readLine()) != null)
		{
			this.raw_content += line;
			if (this.raw_content.length() > 1000)
			{ if (is_webpage()) continue; else break; }
		}

		reader.close();
	}

	public Boolean is_webpage()
	{ 
		if (this.url_connection.getContentType() == null) return false;
		if (this.url_connection.getContentType().indexOf("text/html") == -1) return false;
		return true;
	}

	public String raw_content() { return this.raw_content; }
	
	public URL content_url() { return this.content_url; }

}
