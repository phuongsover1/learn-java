package com.semanticsquare.concurrency.indexer;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * For N web links, this approach creates 2 * N threads.
 *
 * Note: htmlPage is declared volatile in Weblink
 *
 * Limitation: CPU cycles are wasted in Indexer as it is waiting for page to be
 * download
 *
 * @author Dheeru Mundluru
 */

class NaiveIndexer {
	private Deque<Weblink> queue = new ArrayDeque<>();

	private static class Weblink {
		private long id;
		private String title;
		private String url;
		private String host;

		private volatile String htmlpage;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getHtmlpage() {
			return htmlpage;
		}

		public void setHtmlpage(String htmlpage) {
			this.htmlpage = htmlpage;
		}

	}

	private static class Downloader implements Runnable {
		private Weblink weblink;

		public Downloader(Weblink weblink) {
			this.weblink = weblink;
		}

		@Override
		public void run() {
			try {
				String htmlPage = HttpConnect.download(weblink.getUrl());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static class Indexer implements Runnable {
		private Weblink weblink;

		private Indexer(Weblink weblink) {
			this.weblink = weblink;
		}

		@Override
		public void run() {
			while (true) {
				String htmlPage = weblink.getHtmlpage();
				if (htmlPage != null) {
					index(htmlPage);
					break;
				} else {
					System.out.println(weblink.getId() + " not yet downloaded!");
				}
			}

		}

		private void index(String htmlPage) {
			if (htmlPage != null) {
				System.out.println("\nIndexed: " + weblink.getId() + "\n");

			}
		}
	}

	public void go() {

		while (queue.size() > 0) {
			Weblink weblink = queue.remove();
			Thread downloaderThread = new Thread(new Downloader(weblink));
			Thread indexerThread = new Thread(new Indexer(weblink));

			downloaderThread.start();
			indexerThread.start();
		}
	}

	public void add(Weblink link) {
		queue.add(link);
	}

	public Weblink createWeblink(long id, String title, String url, String host) {
		Weblink weblink = new Weblink();
		weblink.setId(id);
		weblink.setTitle(title);
		weblink.setUrl(url);
		weblink.setHost(host);
		return weblink;
	}

	public static void main(String[] args) {
		NaiveIndexer naiveIndexer = new NaiveIndexer();
		naiveIndexer.add(naiveIndexer.createWeblink(2000, "Taming Tiger, Part 2",
				"http://www.javaworld.com/article/2072759/core-java/taming-tiger--part-2.html",
				"http://www.javaworld.com"));
		naiveIndexer.add(naiveIndexer.createWeblink(2001,
				"How do I import a pre-existing Java project into Eclipse and get up and running?",
				"http://stackoverflow.com/questions/142863/how-do-i-import-a-pre-existing-java-project-into-eclipse-and-get-up-and-running",
				"http://www.stackoverflow.com"));
		naiveIndexer.add(naiveIndexer.createWeblink(2002, "Interface vs Abstract Class",
				"http://mindprod.com/jgloss/interfacevsabstract.html", "http://mindprod.com"));
		naiveIndexer.add(naiveIndexer.createWeblink(2003, "NIO tutorial by Greg Travis",
				"http://cs.brown.edu/courses/cs161/papers/j-nio-ltr.pdf", "http://cs.brown.edu"));
		naiveIndexer.add(naiveIndexer.createWeblink(2004, "Virtual Hosting and Tomcat",
				"http://tomcat.apache.org/tomcat-6.0-doc/virtual-hosting-howto.html", "http://tomcat.apache.org"));
		naiveIndexer.go();
	}
}
