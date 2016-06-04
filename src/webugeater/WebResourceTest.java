package webugeater;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class WebResourceTest
{
	private Element referer, element;
	private String explanation;
	
	public WebResourceTest(Element referer, Element element, String explanation)
	{
		this.referer = referer; this.element = element; this.explanation = explanation;
	}
	
	public String to_string()
	{
		return this.explanation;
	}
}
