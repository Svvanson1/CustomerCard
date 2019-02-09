import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//Benjamin Swanson 02/19
//UMO Store Project

public class CustomerCard {
	
  	private static SecretKeySpec secretKey;
  	private static byte[] key;
	private long customerID;
	private String customerName;
	private String cardNumber;
	private String expirationDate;
	
	//Default
	public CustomerCard() {
		this.customerID = 0;
		this.customerName = "John Doe";
		this.cardNumber = "12345";
		this.expirationDate = "12/99";
	}
	
	//Parameterized
	public CustomerCard(long cID, String cName, String cNumber, String expDate) {
		this.customerID = cID;
		this.customerName = cName;
		this.cardNumber = cNumber;
		this.expirationDate = expDate;
	}
	
	/*
	 * ----------MUTATORS----------
	 */
	
	 public long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public static byte[] getKey() {
		return key;
	}

	public static void setKey(byte[] key) {
		CustomerCard.key = key;
	}
	
       public static void setKey(String myKey) {
	        MessageDigest sha = null;
	        try {
	            key = myKey.getBytes("UTF-8");
	            sha = MessageDigest.getInstance("SHA-1");
	            key = sha.digest(key);
	            key = Arrays.copyOf(key, 16);
	            secretKey = new SecretKeySpec(key, "AES");
	        }
	        catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	    }
	
	/*
	 * 
	 */
	
         /*
	  *----------Card Validation Stuff----------
	  */

	public String getIssuer(String cardNumber) {
	//Returns card issuer given card number
		
	    //Regex matching length and leading digit
	    String visa = "4+(\\d{15}|\\d{12})";
	    String amex = "(34|37)+\\d{13}";
	    String discover = "6011+\\d{12}";
	    String mastercard = "(51|52|53|54|55)+\\d{14}";
	    
	    //If any of those match return them, else return unknown
	    if (cardNumber.matches(visa)) return "VISA";
	    if (cardNumber.matches(amex)) return "AMEX";
	    if (cardNumber.matches(discover)) return "Discover";
	    if (cardNumber.matches(mastercard)) return "Mastercard";
	    else return "Unknown";
		  }
	  
	  public static boolean validate(String cardNumber){
      	  //Returns true if passed in cardNumber is valid
	  //Validates using Luhn Algorithm
	  //https://en.wikipedia.org/wiki/Luhn_algorithm
		  
		  //Parse input into a long
		  long number = Long.parseLong(cardNumber);
		  
		  //Create and array to store values
		  long[] numAr = new long[cardNumber.length()];
		  
		  //Assign values to array, breaks string by digit
		  int count = cardNumber.length() - 1;
		  while(number > 0) {
			  numAr[count] = number % 10;
			  number = number / 10;
			  count--;
		  }
		  
		  //Creates a variable to store results from algorithm
		  long result;
		  
		  //if card number is even
		  if(cardNumber.length() % 2 == 0) {
			  for(int i = 0; i < numAr.length; i+=2) {
				  //doubles every other digit starting with first
				  result = numAr[i] * 2;
				  
				  //If result is greater than nine, subtract nine from result
				  if(result > 9) {
					  result = result - 9;
				  }
				  
				  //store result in array
				  numAr[i] = result;
			  }
		  }
		  //if card number is odd
		  else {
			  for(int i = 1; i < numAr.length; i+=2) {
				  //doubles every other digit starting with second
				  result = numAr[i] * 2;
				  
				  //If result is greater than nine, subtract nine from result
				  if(result > 9) {
					  result = result - 9;
				  }
				  
				  //store result in array
				  numAr[i] = result;
			  }
		  }
		  
		  //Variable which will store sum of everything in array
		  long sum = 0;
		  
		  //Sums all the digits
		  for(int i = 0; i < numAr.length; i++) {
			  sum += numAr[i];
		  }
		  
		  //If divisible by 10, is valid
		  if(sum % 10 == 0) {
			  return true;
		  }
		  else {
			  return false;
		  }
		  
		  }
	  
	  	public String encryptCard(String secretKey) {
	  	//Returns encrypted customer card
	  		
	  		//Creates a string based on card information
	  		String cardInfo = this.customerID + " " + this.cardNumber + " " + this.expirationDate + " " + this.customerName;
	  		
	  		//encrypts and returns string with secret key
	  		return CustomerCard.encrypt(cardInfo, secretKey);
	  		
	  	}
	  
	    public static String maskify(String str) {
	    //Returns a hidden version of the inputted string
	    //every character besides last 4 hidden by '#'
	    	
	    	//If length is less than four return as is
	        if(str.length() <= 4) {
	        	return str;
	        }
	        
	        //Gets last four chars
	        String lastFour = str.substring(str.length() - 4);
	        //Gets everything else
	        String firstSection = str.substring(0, str.length() - 4);
	        //replaces everything besides last 4 chars with #
	        firstSection = firstSection.replaceAll(".", "#");
	        
	        return firstSection + lastFour;
	    }
	    
	    public static boolean validDate(String input) {
	    //Returns true if the inputted date is not expired
	    	
	    	//Stores current year and month
	    	String currentYear = new SimpleDateFormat("YY").format(Calendar.getInstance().getTime());
	    	String currentMonth = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
	    	
	    	//Split the input into 
	    	String[] inputDate = input.split("[-/]");
	    	String inputMonth = inputDate[0];
	    	String inputYear = inputDate[1];
	    	
	    	//Replace remaining whitespace with nothing
	    	inputMonth.replaceAll("\\s+","");
	    	inputYear.replaceAll("\\s+","");
	    	
	    	//Input month and year as integer
	    	int iMonth = Integer.parseInt(inputMonth);
	    	int iYear = Integer.parseInt(inputYear);
	    	
	    	//Current month and year as integer
	    	int cMonth = Integer.parseInt(currentMonth);
	    	int cYear = Integer.parseInt(currentYear);
	    	
	    	//if inputted year is before current year, return false
	    	if(iYear < cYear) {
	    		return false;
	    	}
	    	//if years are equal check months
	    	else if(iYear == cYear) {
	    		//if the current month is before inputted month, return true 
	    		if(cMonth <= iMonth) {
	    			return true;
	    		}
	    		//else return false
	    		else {
	    			return false;
	    		}
	    	}
	    	//else return true
	    	else {
	    		return true;
	    	}
	    }
	
	    /*
	    *
	    */
	
	    /*
	    *----------AES Stuff----------
	    */
	 
	    public static String encrypt(String strToEncrypt, String secret) {
	        try {
		    //Set key to input
	            setKey(secret);
		    //Set cipher to AES
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		    //Set cipher to encrypt mode
	            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		    //run through cipher, return
	            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	        }
	        catch (Exception e) {
	            System.out.println("Error while encrypting: " + e.toString());
	        }
	        return null;
	    }
	 
	    public static String decrypt(String strToDecrypt, String secret) {
	        try {
		    //Set key to input
	            setKey(secret);
		    //Set cipher to AES
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		    //Set cipher to decrypt mode
	            cipher.init(Cipher.DECRYPT_MODE, secretKey);
		    //run through cipher, return
	            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	        }
	        catch (Exception e) {
	            System.out.println("Error while decrypting: " + e.toString());
	        }
	        return null;
	    }
	    
	    /*
	    *
	    */
	
}
