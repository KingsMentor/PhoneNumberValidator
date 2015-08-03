# PhoneNumberVerifier
To help implement phone number verification before performing any further operation.
Convert phone number to phone number with country code and also remove country code 
from a phone number and automatically get the country of a user. 
This library helps make sure the number is valid for the selected country.

# Adding to your project.
Download the project and add the phonenumberverifier as a module on android studio as a dependency

#Samples
See sample application in project for a brief walk through on how to use the library.

#Snippet.
```PhoneNumberVerifier numberVerifier = new PhoneNumberVerifier();```

**Countries can be gotten by:**

* automatically getting the user country

```
numberVerifier.getUserCountry()
```

this returns the users country or null if nothing is found

* get user country by mc (mobile country code).

This is unique for all country

```
numberVerifier.getCountryByMnc(621)
```

to get mcc in android:

```
TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
String simOperator = telephonyManager.getSimOperator();
if (!simOperator.isEmpty()) {
int mcc = Integer.parseInt(String.valueOf(context.getResources().getConfiguration().mcc));
```

* phone number

get the country by passing a phone number to the method. This returns a country that allows the phone number format.
The phone number should have a country code else, this method would return the default country passed to it.
```
numberVerifier.getCountryByPhoneNumber(Countries.CANADA, "2348123456789")
```

* by name

returns country by name. Pass a string name of the coutry to get the country
```
numberVerifier.getCountryByName("Nigeria")
```

* by country code

this returns a list of country as some country shares same country code.
```
numberVerifier.getCountriesByCountryCode(234)
```

* direct initialisation

calling the country by its value type;

```Countries country = PhoneNumberVerifier.Countries.Nigeria;```


#Using the PhoneModel Class
To check if a number is valid for a country, use the phoneModel class.

```
PhoneModel model = country.isNumberValid(country, "08039164129");
```

```model.isNumberValid()``` tells if the number is valid or not. If it is 
valid, you can basically a better formatted version of the number from 
```model.getPhoneNumber().``` This is empty when model.isNumberValid() is false;


#Converting Numbers
Numbers can be converted to plain number from countrycode number and converted to plain number from counrtycode number
(i.e) from 080231**** to +23481231**** or from +23481231**** to 080231****

use ```country.ToPlainNumber(country,phonenumber)``` and ```country.ToCountryCodeNumber(country,phonenumber)``` to achieve this

#Handling Exception
Watch out for the PhoneFormatException. It is thrown when phone contains invalid characters mainly.



