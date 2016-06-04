package webugeater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webugeater.HttpStatusTest;

public class WebResource extends Construct
{
	public interface Test
	{
		public Test configure(HttpURLConnection connection);
		public void perform() throws Exception;
		public Boolean failed() throws Exception;
		public String info() throws Exception;
	}

	public interface Connection
	{

	}

	private Element resource_tag, referer_tag;
	private String url_string;	
	private String referer;
	private URL url_URL;
	private ConnectionResult connection_result;
	private URL content_url;
	private Boolean is_supported = true;
	private Boolean is_valid = true;
	private String raw_content;
	private Document content_doc;
	private ArrayList<Element> sub_resources_urls = new ArrayList<Element>();
	private String info = "";


	public WebResource(Element referer_tag, Element resource_tag)
	{
		this.resource_tag = resource_tag; this.referer_tag = referer_tag;

		this.url_string = this.resource_tag.attr("abs:href");
		this.referer = this.referer_tag.attr("abs:href");
	}

	private Boolean is_direct_http_link(Element resource_tag)
	{
		if (this.has_empty_body(resource_tag)) return false;
		if (this.is_not_http(resource_tag)) return false;
		if (this.has_click_handler(resource_tag)) return false;
		return true;
	}

	public Boolean has_empty_body(Element resource_tag)
	{
		if (resource_tag.html().isEmpty()) { return true; } else return false;
	}
	
	public Boolean is_not_http(Element resource_tag)
	{
		try
		{
			if ((new URL(this.url_string)).getProtocol().equals("http")) return false;
		} catch (Exception e) { return true; }
		return false;
	}
	
	public Boolean has_click_handler(Element resource_tag)
	{
		if (resource_tag.attr("onclick").isEmpty()) return false;
		return true;
	}
	

	public static Element create_element(String url, String html)
	{
		return Jsoup.parseBodyFragment("<a href=\"" + url + "\">" + html + "</a>").select("a[href]").first();
	}

	public Boolean url_is_part_of(Element element)
	{
		try
		{
			String content_url_cmp;
			if (this.connection_result.content_url().getPath().endsWith("/"))
				content_url_cmp = this.connection_result.content_url().getPath().substring(0, this.connection_result.content_url().getPath().length()-1);
			else content_url_cmp = this.connection_result.content_url().getPath();
			URL element_url = new URL(element.attr("abs:href"));
			if ( element_url.getHost().equals(this.connection_result.content_url().getHost()) &&
					element_url.getPath().startsWith(content_url_cmp)) return true; else return false;
		} 
		catch (MalformedURLException e) { this.logger.warning(e.toString()); return false; }
	}

	public void do_test()
	{
		this.connection_result = new ConnectionResult().configure(this.url_string, this.referer);
		this.connection_result.connect();

		if (this.connection_result.has_http_error_status())
		{
			this.is_valid = false;
			return;
		}
	}


	public  Boolean has_sub_resources()
	{
		if (this.connection_result.is_webpage())
		{
//			try
	//		{
				this.content_doc = 
					Jsoup.parse(this.connection_result.raw_content(), non_fragment_url(this.connection_result.content_url().toString()));
		//	}
		//	catch (Exception e) { return false; }

			Elements a_href_elements = this.content_doc.select("a[href]");

			for (Element current_a_href : a_href_elements)
			{
				this.sub_resources_urls.add(current_a_href);
			}

			return ! this.sub_resources_urls.isEmpty();
		}
		return false;

	}

	public ArrayList<Element> sub_resources_urls()
	{

		return this.sub_resources_urls;
	}	

	public Boolean is_supported() { return this.is_direct_http_link(this.resource_tag); }

	public Boolean is_valid() { return this.is_valid; }

	public static String non_fragment_url(String url) { return url.split("#")[0]; }

	public String non_fragment_url() { return non_fragment_url(this.url_string); }	

	private void add_info(String info)
	{
		this.info += "* " + info + ". ";
	}

	public String to_string() { return this.info + this.resource_tag.outerHtml() + " <- " + this.referer; }
}