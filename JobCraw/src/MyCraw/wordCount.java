package MyCraw;

import java.util.*;

import jeasy.analysis.MMAnalyzer;

public class wordCount {
	private static Set<String> allNoWords = new HashSet<String>();
	static {
		allNoWords.add("实习生");
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String text = "";
		MMAnalyzer mm = new MMAnalyzer();
		try {
			/*File file = new File("job_name.txt");
			InputStreamReader read = new InputStreamReader(new FileInputStream(file),"GBK");//考虑到编码格式
            BufferedReader br = new BufferedReader(read);
           if(file.exists()){
        	   System.out.println("WordsCount.main()");
           }
           String s;
           while((s=br.readLine())!=null){
	            text = text + s;
	            System.out.println(text);
           }
		     
		    read.close();
		    br.close();*/
		    
			String[] allWords = mm.segment(text, "|").split("\\|");
			Map<String, Integer>WordsCount = new HashMap<String, Integer>();
			for (String wordStr : allWords) {
				if (!(allNoWords.contains(wordStr) || wordStr.length() == 1)) {
					if(WordsCount.containsKey(wordStr)){
						int num = WordsCount.get(wordStr);
						num++;
						WordsCount.put(wordStr, num);
					}else {
						WordsCount.put(wordStr, 1);
					}			
				}
			}
			
			
			
			
			
			Iterator<Map.Entry<String,Integer>> iterator = WordsCount.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<String,Integer> entry = iterator.next();
				if(entry.getValue()!=1){
				System.out.print("{name:\""+entry.getKey()+"\",value:"+entry.getValue()+"},");
				}
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		

		

	}

}
