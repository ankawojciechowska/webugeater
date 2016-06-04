package webugeater;

import java.util.logging.Logger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

// Represents Website assesment, a website is a set of webpages in tree structure.
// Tryes to assess simulating user experience while navigating over pages.
public class WebsiteAssesment extends Construct
{
	private Integer max_deepnes = null, current_deep = 0;
	private String url_string;
	private WebResource web_resource;
	private Integer fail_number = 0, tests_number = 0;
	private Boolean PASS = true, FAIL = false;

	private ArrayList<Element> passed_tests = new ArrayList<Element>();
	private ArrayList<WebResourceTest> failed_tests = new ArrayList<WebResourceTest>();

	//	deepness - check: 0 = only current URL, 1 = with subpages, 2 ... 
	public WebsiteAssesment(Integer param_max_deepnes, String param_url_string) throws MalformedURLException
	{
		url_string = param_url_string;
		max_deepnes = param_max_deepnes;
	}

	public Double calculate()
	{
		Element referer = WebResource.create_element(url_string, "Referer");
		Element element = WebResource.create_element(url_string, "Anhor");
		web_resource = new WebResource(referer, element); web_resource.do_test();
		logger.info(web_resource.to_string());

		test_web_resource(referer, element);
		logger.info(result_string());
		return percent_fail();
	}

	public String result_string()
	{
		String failed_tests_string = "";

		for (WebResourceTest current_test : this.failed_tests)
		{
			failed_tests_string += "\n" + current_test.to_string();
		}

		return mark_string() + ": " + this.url_string + failed_tests_string;
	}

	private void test_web_resource(Element referer, Element element)
	{
		WebResource web_resource = new WebResource(referer, element);
		if (web_resource.is_supported())
		{
			if (! requires_test(element)) return;
			web_resource.do_test();
			if (web_resource.is_valid())
			{
				record_url_test(referer, element, PASS, web_resource.to_string());
				if (web_resource.has_sub_resources())
				{
					for (Element sub_resource_url : web_resource.sub_resources_urls())
					{
						this.current_deep++;
						test_web_resource(element, sub_resource_url);
						this.current_deep--;
					}
				}
			} else { record_url_test(referer, element, FAIL, web_resource.to_string()); }
		} else { this.logger.info(web_resource.to_string()); }
	}

	private void record_url_test(Element referer, Element element, Boolean test_passed, String explanation)
	{
		this.tests_number++;

		if (test_passed) this.passed_tests.add(element);
		else
		{
			this.fail_number++;
			this.failed_tests.add(new WebResourceTest(referer, element, explanation));
		}
		this.logger.info(test_passed.toString() + ": " + 
				explanation + ", " + this.current_deep + ", " + this.mark_string());
	}

	private Boolean requires_test(Element element)
	{
		if ( is_to_deep() ) return false;

		if (this.web_resource.url_is_part_of(element)) ; else return false;

		for (Element current_element : this.passed_tests)
		{
			if (WebResource.non_fragment_url(current_element.attr("abs:href")).
					equals(WebResource.non_fragment_url(element.attr("abs:href")))) return false;
		}
		return true;
	}



	private Boolean is_to_deep() { return max_deepnes != null && this.current_deep > this.max_deepnes; }

	private Double percent_fail() { return (100.0 * fail_number.doubleValue() / tests_number.doubleValue()); }

	private String mark_string()
	{
		return String.format("%d/%d, %1.1f%%", this.fail_number, this.tests_number, this.percent_fail());
	}
}
