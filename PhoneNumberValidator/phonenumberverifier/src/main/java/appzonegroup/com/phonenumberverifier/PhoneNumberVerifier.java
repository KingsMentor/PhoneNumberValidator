package appzonegroup.com.phonenumberverifier;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CreatedbyCrowdStaron8/1/2015.
 */
public class PhoneNumberVerifier {

    public enum Countries {


        Benin("Benin Republic", 229, 8, 8, "0", 616),
        BurkinaFaso("Burkina Faso", 226, 8, 8, "0", 613),
        CapeVerde("Cape Verce", 238, 7, 7, "0", 614),
        Cameroon("Cameroon", 237, 8, 8, "0", 302, 307),
        Canada("Canada,", 1, 10, 10, "0,1", 460),
        China("China", 86, 11, 11, "0", 612),
        CoteDIvoire("Cote d'Ivoire", 225, 8, 8, "", 602),
        Egypt("Egpyt", 20, 9, 10, "0", 625),
        Finland("Finland", 358, 6, 11, "0", 224),
        France("France", 33, 9, 9, "0", 208),
        Gambia("Gambia", 220, 7, 7, "0", 607),
        Germany("Germany", 49, 7, 12, "0", 262),
        Ghana("Ghana", 233, 9, 9, "0", 620),
        Greece("Greece", 30, 10, 10, "0", 202),
        GuineaBissau("Guinea Bissau", 245, 7, 7, "0", 632),
        Guinea("Guinea", 224, 8, 9, "0", 611),
        India("India", 91, 10, 10, "0", 404, 405),
        Italy("Italy", 39, 9, 10, "0", 222),
        Japan("Japan", 81, 10, 10, "0", 440, 441),
        Kenya("Kenya", 254, 9, 9, "0", 639),
        Liberia("Liberia", 231, 7, 9, "0", 618),
        Libya("Libya", 218, 9, 9, "0", 606),
        Malawi("Malawi", 265, 9, 9, "0", 650),
        Malaysia("Malaysia", 60, 9, 10, "0", 502),
        Mali("Mali", 223, 8, 8, "0", 610),
        Mauritania("Mauritania", 222, 8, 8, "0", 609),
        Morocco("Morocco", 212, 9, 9, "0", 604),
        Niger("Niger", 227, 8, 8, "0", 614),
        Nigeria("Nigeria", 234, 10, 10, "0", 621),
        NorthKorea("North Korea", 850, 10, 10, "0", 467),
        Russia("Russia", 7, 10, 10, "8", 250),
        SaudiArabia("Saudi Arabia", 966, 9, 9, "0", 420),
        Senegal("Senegal", 221, 9, 9, "0", 608),
        SierraLeone("Sierra Leone", 232, 8, 8, "0", 619),
        SouthAfrica("South Africa", 27, 9, 9, "0", 655),
        SouthKorea("South Korea", 82, 9, 10, "0", 450),
        Spain("Spain", 34, 9, 9, "0", 214),
        Sweden("Sweden", 46, 9, 9, "0", 240),
        Switzerland("Switzerland", 41, 9, 9, "0", 228),
        Togo("Togo", 228, 8, 8, "0", 615),
        Ukraine("Ukraine", 380, 9, 9, "0", 225),
        UnitedArabEmirate("United Arab Emirate", 971, 10, 10, "0", 424, 430, 431),
        UnitedKingdom("United Kingdom", 44, 10, 10, "0,1", 234, 235),
        UnitedStates("United States", 1, 10, 10, "0,1", 301);

        private final int countryCode;
        private final String countryName;
        private final int allowableFromLength;
        private final int allowableToLength;
        private final String startDigitToIgnore;
        private final List<Integer> mcc;
        private String preceeding = "";

        public String getCountryName() {
            return this.countryName;
        }

        public int getCountryCode() {
            return countryCode;
        }

        public int getAllowableFromLength() {
            return allowableFromLength;
        }

        public int getAllowableToLength() {
            return allowableToLength;
        }

        public String getStartDigitToIgnore() {
            return startDigitToIgnore;
        }

        public List<Integer> getMcc() {
            return mcc;
        }

        private boolean isDigit(String phoneNumber) {
            return phoneNumber.matches("[0-9]+");
        }

        private boolean startWithPlus(String phoneNumber) {
            return phoneNumber.matches("[+][0-9]+");
        }

        private boolean startWithCountryCode(String countryCode, String phoneNumber) {
            String buildreg = "";
            for (char a : countryCode.toCharArray()) {
                buildreg += "[" + a + "]";
            }
            return phoneNumber.matches(buildreg + "[0-9]+");
        }

        private boolean startWithPlusAndCountryCode(String countryCode, String phoneNumber) {
            String buildreg = "";
            for (char a : countryCode.toCharArray()) {
                buildreg += "[" + a + "]";
            }
            return phoneNumber.matches("[+]" + buildreg + "[0-9]+");
        }

        private boolean meetCountryPhoneNumberRequirement(Countries countries, String phoneNumber) {
            return ((phoneNumber.length() >= countries.getAllowableFromLength()) && (phoneNumber.length() <= countries.getAllowableToLength()));
        }

        public PhoneModel isNumberValid(Countries countries, String phoneNumber) throws PhoneFormatException {
            PhoneModel model = new PhoneModel();
            phoneNumber = phoneNumber.trim();
            if (startWithPlusAndCountryCode(String.valueOf(countries.getCountryCode()), phoneNumber)) {
                String cc = String.valueOf(countries.getCountryCode());
                int length = cc.length();
                if (startWithPlus(phoneNumber)) {
                    length += 1;
                }
                phoneNumber = formatNumber(countries, phoneNumber.substring(length));
                if (meetCountryPhoneNumberRequirement(countries, phoneNumber)) {
                    phoneNumber = "+" + cc + phoneNumber;
                    model.setIsValidPhoneNumber(true);
                }
            } else {
                if (isDigit(phoneNumber)) {
                    if (startWithCountryCode(String.valueOf(countries.getCountryCode()), phoneNumber)) {
                        String cc = String.valueOf(countries.getCountryCode());
                        phoneNumber = formatNumber(countries, phoneNumber.substring(cc.length()));
                        if (meetCountryPhoneNumberRequirement(countries, phoneNumber)) {
                            phoneNumber = cc + phoneNumber;
                            model.setIsValidPhoneNumber(true);
                        }
                    } else {
                        preceeding = "";
                        phoneNumber = formatNumber(countries, phoneNumber);
                        if (meetCountryPhoneNumberRequirement(countries, phoneNumber)) {
                            phoneNumber = preceeding + phoneNumber;
                            model.setIsValidPhoneNumber(true);
                        }
                    }
                } else {
                    System.out.println(phoneNumber);
                    if (startWithPlus(phoneNumber)) {
                        throw new PhoneFormatException("does not match " + countries + " countrycode");
                    } else {
                        throw new PhoneFormatException("Contains non digit characters");
                    }
                }
            }

            if (model.isValidPhoneNumber()) {
                model.setPhoneNumber(phoneNumber);
            }
            return model;
        }

        public String ToCountryCode(Countries countries, String phoneNumber) throws PhoneFormatException {
            PhoneModel model = null;
            phoneNumber = phoneNumber.trim();
            phoneNumber = phoneNumber.trim();
            if (startWithPlus(phoneNumber)) {
                if (startWithPlusAndCountryCode(String.valueOf(countries.getCountryCode()), phoneNumber)) {
                    model = isNumberValid(countries, phoneNumber);
                }
            } else if (startWithCountryCode(String.valueOf(countries.getCountryCode()), phoneNumber)) {
                model = isNumberValid(countries, phoneNumber);
            } else {
                phoneNumber = "+" + String.valueOf(countries.getCountryCode()) + phoneNumber;
                model = isNumberValid(countries, phoneNumber);

            }
            if (model != null) {
                if (model.isValidPhoneNumber()) {
                    return model.getPhoneNumber();
                }
            }
            return "";
        }

        public String ToPlainNumber(Countries countries, String phoneNumber) throws PhoneFormatException {
            PhoneModel model = null;
            phoneNumber = phoneNumber.trim();
            if (startWithPlusAndCountryCode(String.valueOf(countries.getCountryCode()), phoneNumber)) {
                String cc = String.valueOf(countries.getCountryCode());
                phoneNumber = phoneNumber.substring(cc.length() + 1);
                model = isNumberValid(countries, phoneNumber);
            } else if (startWithCountryCode(String.valueOf(countries.getCountryCode()), phoneNumber)) {
                String cc = String.valueOf(countries.getCountryCode());
                phoneNumber = phoneNumber.substring(cc.length());
                model = isNumberValid(countries, phoneNumber);
            } else {
                phoneNumber = formatNumber(countries, phoneNumber);
                phoneNumber = preceeding + phoneNumber;
                model = isNumberValid(countries, phoneNumber);
            }
            if (model != null) {
                if (model.isValidPhoneNumber()) {
                    return model.getPhoneNumber();
                }
            }
            return "";
        }

        private String formatNumber(Countries countries, String phoneNumber) {
            ArrayList<String> preceedingDigit = new ArrayList<>(Arrays.asList(countries.getStartDigitToIgnore().split(",")));
            while (!phoneNumber.trim().isEmpty()) {
                if (preceedingDigit.contains(phoneNumber.substring(0, 1))) {
                    preceeding = phoneNumber.substring(0, 1);
                    phoneNumber = phoneNumber.substring(1);
                } else {
                    return phoneNumber;
                }
            }
            return phoneNumber;
        }

        private Countries(String countryName, int countryCode, int allowableFromLength, int allowableToLength, String startDigitToIgnore, Integer... mcc) {
            this.countryCode = countryCode;
            this.countryName = countryName;
            this.allowableFromLength = allowableFromLength;
            this.allowableToLength = allowableToLength;
            this.startDigitToIgnore = startDigitToIgnore;
            this.mcc = new ArrayList<>();
            this.mcc.addAll(Arrays.asList(mcc));
        }

    }

    ;

    private ArrayList<Countries> countriesList;

    /**
     * Get country by name of country
     */
    public Countries getCountryByName(String countries) {
        for (Countries c : Countries.values()) {
            if (String.valueOf(c).equalsIgnoreCase(countries)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Get country by valid phone number of the country
     */
    public Countries getCountryByPhoneNumber(Countries defaultCountry, String phoneNumber) throws PhoneFormatException {
        for (Countries c : Countries.values()) {
            if (c.startWithCountryCode(String.valueOf(c.getCountryCode()), phoneNumber)) {
                PhoneModel model = c.isNumberValid(c, phoneNumber);
                if (model.isValidPhoneNumber()) {
                    return c;
                }

            } else if (c.startWithPlusAndCountryCode(String.valueOf(c.getCountryCode()), phoneNumber)) {
                PhoneModel model = c.isNumberValid(c, phoneNumber);
                if (model.isValidPhoneNumber()) {
                    return c;
                }
            }
        }
        return defaultCountry;
    }

    /**
     * Get country by Mobile country codes
     */
    public Countries getCountryByMcc(int mcc) {
        for (Countries countries : Countries.values()) {
            if (countries.mcc.contains(mcc)) {
                return countries;
            }
        }
        return null;
    }

    /**
     * get the list of countries with countryCode
     * This returns the list of countries because countries like Canada and US
     * has same country code
     */
    public ArrayList<Countries> getCountriesByCountryCode(int countryCode) {
        countriesList = new ArrayList<>();
        for (Countries countries : Countries.values()) {
            if (countries.countryCode == countryCode) {
                countriesList.add(countries);
            }
        }
        return countriesList;
    }

    /**
     * Get country of the user.
     */
    public Countries getUserCountry(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOperator = telephonyManager.getSimOperator();
        if (!simOperator.isEmpty()) {
            int mcc = Integer.parseInt(String.valueOf(context.getResources().getConfiguration().mcc));
            return getCountryByMcc(mcc);
        }
        return null;
    }

}
