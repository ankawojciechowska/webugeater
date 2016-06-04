package webugeater;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// Represents elementary one web page localized by url
// Class validates webpage defined by correct url string.
public class WebPage
{
	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private Document content;
	private Boolean is_valid = true;
	private ArrayList<String> sub_pages_urls = new ArrayList<String>();
	private ArrayList<String> non_page_urls = new ArrayList<String>();	

	// Assumes url is correct.
	public WebPage(String referer, String url_string)
	{ 
		try
		{
			Connection connection = Jsoup.connect(url_string);
			connection.followRedirects(true);
			if (referer != null) connection.referrer(referer);
			connection.userAgent("Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0");
			content = connection.get();
		}
		catch (IOException e)
		{
			is_valid = false;
			logger.warning(e.toString());
		}
	}
	
	public Boolean is_valid() {	return is_valid; }

	// Return URL validated urls

	
	// Return URL validated urls
	public ArrayList<String> non_page_urls()
	{
		if ( ! this.is_valid) return (non_page_urls);
		
        Elements src_param_elements = content.select("[src]");
        Elements link_href_elements = content.select("link[href]");

        
        for (Element current : src_param_elements)
        {
        	URL temp_url;
        	try
        	{
        		temp_url = new URL(current.attr("abs:src"));
        	} 
        	catch (MalformedURLException e) { logger.warning(e.toString()); continue; }
			if ( ! temp_url.getProtocol().equalsIgnoreCase("http") && ! temp_url.getProtocol().equalsIgnoreCase("https"))
				continue;
        	non_page_urls.add(current.attr("abs:src"));
        }
        
        for (Element current : link_href_elements)
        {
        	try
        	{
        		new URL(current.attr("abs:href"));
        	} 
        	catch (MalformedURLException e) { logger.warning(e.toString()); continue; }
        	
        	non_page_urls.add(current.attr("abs:href"));
        }       
        
        return non_page_urls;
	}
	
	
}

