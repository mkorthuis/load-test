package com.candescent.selenium;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/*
 * This will track and manage what study to load for given threads.
 */
public class Study {

	public List<Integer> studyList = new ArrayList<Integer>();
	public Integer studyPointer = 0;
	
	private static Study studyImpl = new Study();
	
	private Study() {
		try (Stream<String> stream = Files.lines(Paths.get(Constants.STUDIES_LOCATION))) {
			stream.forEach(item ->{
				studyList.add(Integer.parseInt(item));
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized Integer getStudyId() {
		if(studyList.size() <= (studyPointer + 1)) {
			studyPointer=0;
		} else {
			studyPointer++;
		}
		return studyList.get(studyPointer);
	}
	
	public static Study getInstance() {
		return studyImpl;
	}
}
