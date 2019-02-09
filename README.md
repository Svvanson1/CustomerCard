# CustomerCard
---------------------------------------------------------------------
Simple class I wrote to check validity &amp; encrypt card information
going to be used in a bigger JavaFX application for a university class
---------------------------------------------------------------------

public String getIssuer(String cardNumber):
Returns card issuer given card number

public static boolean validate(String cardNumber):
Returns true if passed in cardNumber is valid
Validates using Luhn Algorithm.
https://en.wikipedia.org/wiki/Luhn_algorithm

public String encryptCard(String secretKey):
Returns encrypted customer card.

public static String maskify(String str):
Returns a hidden version of the inputted string
every character besides last 4 hidden by '#'.

public static boolean validDate(String input):
Returns true if the inputted date is not expired.

public static String encrypt(String strToEncrypt, String secret):
Encrypts strings using AES

public static String decrypt(String strToDecrypt, String secret):
Decrypts AES encrypted string
