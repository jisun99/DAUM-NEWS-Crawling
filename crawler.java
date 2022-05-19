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

// 구동하기 전에 Jsoup를 다운받아서 설치해야됨! 해당 자바프로젝트파일에 설치 필요.

// 첫번째 스레드
class FirstThread extends Thread {
	public void run() {
		String URL = "https://media.daum.net/ranking/popular/";	// 크롤링 대상의 URL, 여기서는 다음 뉴스
		Document doc = null;	// 모든 페이지의 소스를 저장
		
		try {
			doc = Jsoup.connect(URL).get();
		} catch (IOException e) {
			e.printStackTrace();
		}	// 예외처리를 위한 try catch문
		
		
		// 여기서부터는 해당 페이지 들어가서 F12누르고 확인 필요! (크롬에서 작동)
		
		Elements element = doc.select("div.cont_thumb");	
		// title 분류
		Iterator<Element> dist = element.select("strong.tit_thumb").iterator();	// 뽑아낼것은 기사 타이틀만!		
		
		
			for(int i=1; i<41; i++) {	// 1위~40위까지의 랭킹뉴스의 제목만 크롤링
				System.out.print("["+i+"]  ");
				System.out.println(dist.next().text()+"\n");
		}
		
	}
}

// 두번째 스레드
class SecondThread extends Thread {
	public void run() {
		String URL = "https://media.daum.net/ranking/popular/";	
		Document doc = null;
		
		try {
			doc = Jsoup.connect(URL).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 첫번째 스레드인 FirstThread와 동일
		
		
		// 키워드를 입력받고, 해당 키워드가 있는 뉴스페이지를 다시 크롤링
		String keyword;
		Scanner sc = new Scanner(System.in);
		System.out.println("찾고싶은 키워드를 입력하세요 : "); // 찾고자하는 키워드 입력받기
		keyword = sc.nextLine();
		
		System.out.println("입력한 키워드 '" + keyword + "'에 대해 수집된 페이지에서 검색을 수행합니다.\n");
		
		
		Elements element2 = doc.select("div.cont_thumb");
		Iterator<Element> dist2 = element2.select("div.cont_thumb").iterator();	// 뉴스기사의 제목+내용을 뽑아오기 위함.
		ArrayList<String> list = new ArrayList<String>();	// 위의 뉴스기사의 제목+내용을 저장해줄 list
		
		Elements element3 = doc.select("a.link_thumb");
		Iterator<Element> dist3 = element3.select("a.link_thumb").iterator();	// 뉴스기사의 URL을 뽑아오기 위함.
		ArrayList<String> list2 = new ArrayList<String>();	// 위의 뉴스기사의 URL을 저장해줄 list2
		
		
		try {	// try-catch문을 이용해 예외처리
		for(int i=0; i<40; i++) {	// 1~40개의 뉴스페이지를 다시 크롤링
			list.add(dist2.next().text());	
			// list에 코드의 71번째줄의 뉴스기사의 제목+내용을 text형태로 저장해줄 것
			list2.add(dist3.next().attr("abs:href"));
			// list2에 코드의 75번째줄의 뉴스기사의 URL을 저장해줄 것
			
			if(list.get(i).contains(keyword)) {	
				// 만약, list에 넣어둥 기사의 제목과 내용에 키워드와 일치하는 단어가 있다면, 다음을 수행
				
				String articleURL = list2.get(i);
				// list2에 저장한 뉴스기사의 URL을 String articleURL로..
				
				
				Document articledoc = Jsoup.connect(articleURL).get();
				Elements articleele = articledoc.select("div#mArticle");
				// 해당 URL로 가서 기사의 내용을 확인
				
				String str = articleele.text();
				// 기사의 내용을 text형태로 바꾸어 String str로...

			    Pattern pattern = Pattern.compile(keyword);
			    Matcher matcher = pattern.matcher(str);

			    int count = 0;
			    while (matcher.find())
			           count++;
			    // 해당 기사에서 keyword와 같은 단어가 있으면 count++

				System.out.print("["+(i+1)+"]  ");
				// 크롤링한 40개의 뉴스페이지 중에서 몇번째 기사였는지 출력
				System.out.println(list.get(i));
				// 뉴스기사의 제목과 내용을 출력
				System.out.println(list2.get(i));
				// 뉴스기사의 URL 출력
				System.out.println("해당 기사 속에는 키워드가 " + count +"번 카운트 되었습니다."+"\n");
				// 기사의 내용 안에서 키워드가 몇번 카운트 되었는지 출력
			}
			else {
				continue;
			}	// keyword가 포함되어있지 않은 경우에는 건너뛰기
			
		}			
	
		} catch(Exception e1) {
			System.out.println("\n");
		}
	}
}


public class crawler {
	public static void main(String args[]) {
		
		//40개의 뉴스페이지 크롤링
		
		Thread t1 = new FirstThread();
		t1.start();	// 첫번째 스레드 실행
		try {
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	// 예외처리
			
		System.out.println("======================================================\n");
		System.out.println("40개의 페이지 크롤링이 끝났습니다.");
		
		
		// 여기서부터는 키워드가 포함된 것 찾기
		
		Thread t2 = new SecondThread();
		t2.start();
		try {
			t2.join();	// 두번째 스레드 실행 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	// 예외처리
		
	}
	
}

		
