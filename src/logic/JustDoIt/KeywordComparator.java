package logic.JustDoIt;

import java.lang.Math;


/**
 This class is the KeywordComparator.
 * The Comparator class compare "how similar" a string is to the other string, or, to the other string array
 * It return a double between 0(exact match or one is contained in the other) to Pi/2(totally different)
 * @author Cui Wei A0091621
 */
public class KeywordComparator {
	
	
	/**
	 * This function compares the similarity of a string and a string array
	 * @param keyword
	 * 				is the string
	 * @param info
	 * 				is the string array
	 * the idea is:
	 * compare the keyword string with each sub-string in the string array using
	 * compareString(String first, String second) function, and keep the smallest value.
	 */
	
	private double compareStringArray(String keyword, String[] info){
		
		assert(keyword != null);
    	assert(info != null);
		
		double temp = Double.MAX_VALUE;
		double current = 0.0;
		
		for(int i = 0; i < info.length; i++){
			current = compareString(keyword,info[i]);
			if(temp > current){
				temp = current;
			}
		}
		
		return temp;
	}
	
	/**
	 * This function compares the similarity of two strings
	 * @param first
	 * 				is the first string
	 * @param second
	 * 				is the second string
	 * the idea is as follows:
	 * it creates a integer array of length 128, one position for each ASCII character, and the number at
	 * each position represents the frequency of appearance of the character in the the particular string.
	 * Them we can compare the two vector(represented by the two integer array). To tell the difference, we 
	 * calculate the angle between these two vectors with law of cosine.
	 * 0 represents a exact match or one is contained in the other(special cases to be dealt with), and Pi/2 
	 * represents these two strings are totally different. Any doubles between 0~Pi/2 means partial match. the
	 * smaller the value, the more similar these two strings are.
	 */
	
	private double compareString(String first, String second){
		
		if(first.contains(second) || second.contains(first)){
			return 0.0;
		}
		
		first = first.toLowerCase();
		second = second.toLowerCase();
		
		int[] firstStringArray = new int[128];
		int[] secondStringArray = new int[128];
		
		for(int i = 0; i < first.length(); i++){
			firstStringArray[first.charAt(i)]++;
		}
		
		for(int i = 0; i < second.length(); i++){
			secondStringArray[second.charAt(i)]++;
		}
		
		return calculateAngle(firstStringArray, secondStringArray);
		
		
	}
	
	/**
	 * This function calculates the angle between two high-dimensional vectors
	 * @param vectorOne
	 * 				is a integer array representing one vector
	 * @param vectorTwo
	 * 				is a integer array representing another vector
	 */
	private double calculateAngle(int[] vectorOne, int[] vectorTwo){
		
		double normOne = calculateNorm(vectorOne);
		double normTwo = calculateNorm(vectorTwo);
		double multiply = vectorMultiplication(vectorOne, vectorTwo);
		
		if(multiply / (normOne * normTwo) >= 1){
			return 0.0;
		}
		
		return Math.acos(multiply / (normOne * normTwo));
		
	}
	
	/**
	 * This function calculates the length of a given vector
	 * @param vector
	 * 				is a integer array representing this vector
	 */
	private double calculateNorm(int[] vector){
		
		assert(vector != null);
		
		double sum = 0.0;
		
		for(int i = 0; i < vector.length; i++){
			sum = sum + Math.pow(vector[i], 2.0);
		}
		
		return Math.sqrt(sum);
		
	}

	/**
	 * This function calculates the dot product between of high-dimensional vectors
	 * @param vectorOne
	 * 				is a integer array representing one vector
	 * @param vectorTwo
	 * 				is a integer array representing another vector
	 */
     private double vectorMultiplication(int[] vectorOne, int[] vectorTwo){
		
		double result = 0.0;
		
		assert(vectorOne.length == vectorTwo.length);
		
		for(int i = 0; i < vectorOne.length; i++){
			result = result + vectorOne[i] * vectorTwo[i];
		}
		
		return result;
		
	}
     
     public boolean similar(String first, String second){
    	 
    	 assert(first != null);
    	 assert(second != null);
    	 
  
         double similarity = compareString(first, second);
    	 
    	 if(similarity < 0.6){
    		 return true;
    	 } else{
    		 return false;
    	 }
     }
     
    public boolean similar(String first, String[] info){
    	 
    	assert(first != null);
    	assert(info != null);
    	
    	double similarity = compareStringArray(first, info);
    	 
    	 if(similarity < 0.6){
    		 return true;
    	 } else{
    		 return false;
    	 }
     }
     
}
