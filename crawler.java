package webcrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// �����ϱ� ���� Jsoup�� �ٿ�޾Ƽ� ��ġ�ؾߵ�! �ش� �ڹ�������Ʈ���Ͽ� ��ġ �ʿ�.

// ù��° ������
class FirstThread extends Thread {
	public void run() {
		String URL = "https://media.daum.net/ranking/popular/";	// ũ�Ѹ� ����� URL, ���⼭�� ���� ����
		Document doc = null;	// ��� �������� �ҽ��� ����
		
		try {
			doc = Jsoup.connect(URL).get();
		} catch (IOException e) {
			e.printStackTrace();
		}	// ����ó���� ���� try catch��
		
		
		// ���⼭���ʹ� �ش� ������ ���� F12������ Ȯ�� �ʿ�! (ũ�ҿ��� �۵�)
		
		Elements element = doc.select("div.cont_thumb");	
		// title �з�
		Iterator<Element> dist = element.select("strong.tit_thumb").iterator();	// �̾Ƴ����� ��� Ÿ��Ʋ��!		
		
		
			for(int i=1; i<41; i++) {	// 1��~40�������� ��ŷ������ ���� ũ�Ѹ�
				System.out.print("["+i+"]  ");
				System.out.println(dist.next().text()+"\n");
		}
		
	}
}

// �ι�° ������
class SecondThread extends Thread {
	public void run() {
		String URL = "https://media.daum.net/ranking/popular/";	
		Document doc = null;
		
		try {
			doc = Jsoup.connect(URL).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// ù��° �������� FirstThread�� ����
		
		
		// Ű���带 �Է¹ް�, �ش� Ű���尡 �ִ� ������������ �ٽ� ũ�Ѹ�
		String keyword;
		Scanner sc = new Scanner(System.in);
		System.out.println("ã����� Ű���带 �Է��ϼ��� : "); // ã�����ϴ� Ű���� �Է¹ޱ�
		keyword = sc.nextLine();
		
		System.out.println("�Է��� Ű���� '" + keyword + "'�� ���� ������ ���������� �˻��� �����մϴ�.\n");
		
		
		Elements element2 = doc.select("div.cont_thumb");
		Iterator<Element> dist2 = element2.select("div.cont_thumb").iterator();	// ��������� ����+������ �̾ƿ��� ����.
		ArrayList<String> list = new ArrayList<String>();	// ���� ��������� ����+������ �������� list
		
		Elements element3 = doc.select("a.link_thumb");
		Iterator<Element> dist3 = element3.select("a.link_thumb").iterator();	// ��������� URL�� �̾ƿ��� ����.
		ArrayList<String> list2 = new ArrayList<String>();	// ���� ��������� URL�� �������� list2
		
		
		try {	// try-catch���� �̿��� ����ó��
		for(int i=0; i<40; i++) {	// 1~40���� ������������ �ٽ� ũ�Ѹ�
			list.add(dist2.next().text());	
			// list�� �ڵ��� 71��°���� ��������� ����+������ text���·� �������� ��
			list2.add(dist3.next().attr("abs:href"));
			// list2�� �ڵ��� 75��°���� ��������� URL�� �������� ��
			
			if(list.get(i).contains(keyword)) {	
				// ����, list�� �־�� ����� ����� ���뿡 Ű����� ��ġ�ϴ� �ܾ �ִٸ�, ������ ����
				
				String articleURL = list2.get(i);
				// list2�� ������ ��������� URL�� String articleURL��..
				
				
				Document articledoc = Jsoup.connect(articleURL).get();
				Elements articleele = articledoc.select("div#mArticle");
				// �ش� URL�� ���� ����� ������ Ȯ��
				
				String str = articleele.text();
				// ����� ������ text���·� �ٲپ� String str��...

			    Pattern pattern = Pattern.compile(keyword);
			    Matcher matcher = pattern.matcher(str);

			    int count = 0;
			    while (matcher.find())
			           count++;
			    // �ش� ��翡�� keyword�� ���� �ܾ ������ count++

				System.out.print("["+(i+1)+"]  ");
				// ũ�Ѹ��� 40���� ���������� �߿��� ���° ��翴���� ���
				System.out.println(list.get(i));
				// ��������� ����� ������ ���
				System.out.println(list2.get(i));
				// ��������� URL ���
				System.out.println("�ش� ��� �ӿ��� Ű���尡 " + count +"�� ī��Ʈ �Ǿ����ϴ�."+"\n");
				// ����� ���� �ȿ��� Ű���尡 ��� ī��Ʈ �Ǿ����� ���
			}
			else {
				continue;
			}	// keyword�� ���ԵǾ����� ���� ��쿡�� �ǳʶٱ�
			
		}			
	
		} catch(Exception e1) {
			System.out.println("\n");
		}
	}
}


public class crawler {
	public static void main(String args[]) {
		
		//40���� ���������� ũ�Ѹ�
		
		Thread t1 = new FirstThread();
		t1.start();	// ù��° ������ ����
		try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	// ����ó��
			
		System.out.println("======================================================\n");
		System.out.println("40���� ������ ũ�Ѹ��� �������ϴ�.");
		
		
		// ���⼭���ʹ� Ű���尡 ���Ե� �� ã��
		
		Thread t2 = new SecondThread();
		t2.start();
		try {
			t2.join();	// �ι�° ������ ���� 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	// ����ó��
		
	}
	
}

		
