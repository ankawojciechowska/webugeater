package webugeater;
//package com.rel_qual_rank.server;
//
//import java.io.*;
//import java.net.*;
//
//import org.jsoup.Jsoup;
//import org.jsoup.helper.Validate;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import com.rel_qual_rank.server.WebResource;
//
//
//
//public class WebPage1
//{
//	private String string_content;
//
//	public WebPage(String web_page_url_string)
//	{
//		String result;
//		result = "";
//
//		try
//		{
//			URL url = new URL(url_string);
//
//			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
//			url.
//
//			String line;
//
//			while ((line = reader.readLine()) != null)
//			{
//				result += line;
//			}
//
//			reader.close();
//		}
//		catch (MalformedURLException e)
//		{
//			e.printStackTrace();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//
//		string_content = result;
//
//	}
//	
//	public void test_url_resource(String url_string)
//	{
//		String content = new String("");
//		try
//		{
//			URL url = new URL(url_string);
//			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
//				String content_line;
//				while ( (content_line = reader.readLine()) != null)
//				{
//					content += content_line;
//				}				
//				
//			if (content.length() == 0 )
//			{
//				throw new Exception("Empty url resource content");
//			}
//		}
//		catch (MalformedURLException e)
//		{
//			e.printStackTrace();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//	public String as_string()
//	{
//		return string_content;
//	}
//
//	public void validate_dom() throws Exception
//	{
////		DocumentBuilderFactory builderFactory =
////			DocumentBuilderFactory.newInstance();
////		DocumentBuilder builder = null;
////		
////		builder = builderFactory.newDocumentBuilder();
////
////		builder.parse(string_content);
//	}
//	
//	public String[] (String url) throws IOException
//	{
//	        print("Fetching %s...", url);
//
//	        Document doc = Jsoup.connect(url).get();
//	        Elements links = doc.select("a[href]");
//	        Elements media = doc.select("[src]");
//	        Elements imports = doc.select("link[href]");
//
//	        print("\nMedia: (%d)", media.size());
//	        for (Element src : media) {
//	            if (src.tagName().equals("img")) {
//	                print(" * %s: <%s> %sx%s (%s)",
//	                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
//	                        trim(src.attr("alt"), 20));
//	             }
//	            else
//	                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
//	            //this.test_url_resource(src.attr("abs:src"));
//
//	        }
//
//	        print("\nImports: (%d)", imports.size());
//	        for (Element link : imports) {
//	            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
//	            //this.test_url_resource(link.attr("abs:href"));
//	        }
//
//	        print("\nLinks: (%d)", links.size());
//	        for (Element link : links) {
//	            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
//	            this.test_url_resource(link.attr("abs:href"));
//	            if ((new URL(link.attr("abs:href"))).getHost().equals((new URL(url)).getHost()))
//	            {	
//	            	print("in Nested");
//	            	this.print_urls(link.attr("abs:href"));
//	            	print("out Nested");
//	            }
//	        }
//	 
//
//	}
//	    private static void print(String msg, Object... args) {
//	        System.out.println(String.format(msg, args));
//	    }
//
//	    private static String trim(String s, int width) {
//	        if (s.length() > width)
//	            return s.substring(0, width-1) + ".";
//	        else
//	            return s;
//	    }
//
//
//}


//Listing 1. Validating an Extensible Hypertext Markup Language (XHTML) document
//
//import java.io.*;
//import javax.xml.transform.Source;
//import javax.xml.transform.stream.StreamSource;
//import javax.xml.validation.*;
//import org.xml.sax.SAXException;
//
//public class DocbookXSDCheck {
//
//    public static void main(String[] args) throws SAXException, IOException {
//
//        // 1. Lookup a factory for the W3C XML Schema language
//        SchemaFactory factory = 
//            SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
//        
//        // 2. Compile the schema. 
//        // Here the schema is loaded from a java.io.File, but you could use 
//        // a java.net.URL or a javax.xml.transform.Source instead.
//        File schemaLocation = new File("/opt/xml/docbook/xsd/docbook.xsd");
//        Schema schema = factory.newSchema(schemaLocation);
//    
//        // 3. Get a validator from the schema.
//        Validator validator = schema.newValidator();
//        
//        // 4. Parse the document you want to check.
//        Source source = new StreamSource(args[0]);
//        
//        // 5. Check the document
//        try {
//            validator.validate(source);
//            System.out.println(args[0] + " is valid.");
//        }
//        catch (SAXException ex) {
//            System.out.println(args[0] + " is not valid because ");
//            System.out.println(ex.getMessage());
//        }  
//        
//    }
