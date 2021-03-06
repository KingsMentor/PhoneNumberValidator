package xyz.belvi.validator;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CreatedbyCrowdStaron8/1/2015.
 */
public class PhoneNumberValidator {

    public enum Country {


        Benin("Benin Republic", "BJ", 229, 8, 8, "0", 616),
        BurkinaFaso("Burkina Faso", "BF", 226, 8, 8, "0", 613),
        CapeVerde("Cape Verde", "CV", 238, 7, 7, "0", 614),
        Cameroon("Cameroon", "CM", 237, 8, 8, "0", 302, 307),
        Canada("Canada", "CA", 1, 10, 10, "0,1", 460),
        China("China", "CN", 86, 11, 11, "0", 612),
        CoteDIvoire("Cote d'Ivoire", "CI", 225, 8, 8, "", 602),
        Egypt("Egpyt", "EG", 20, 9, 10, "0", 625),
        Finland("Finland", "FI", 358, 6, 11, "0", 224),
        France("France", "FR", 33, 9, 9, "0", 208),
        Gambia("Gambia", "GM", 220, 7, 7, "0", 607),
        Germany("Germany", "DE", 49, 7, 12, "0", 262),
        Ghana("Ghana", "GH", 233, 9, 9, "0", 620),
        Greece("Greece", "GR", 30, 10, 10, "0", 202),
        GuineaBissau("Guinea Bissau", "GW", 245, 7, 7, "0", 632),
        Guinea("Guinea", "GN", 224, 8, 9, "0", 611),
        India("India", "IN", 91, 10, 10, "0", 404, 405),
        Italy("Italy", "IT", 39, 9, 10, "0", 222),
        Japan("Japan", "JP", 81, 10, 10, "0", 440, 441),
        Kenya("Kenya", "KE", 254, 9, 9, "0", 639),
        Liberia("Liberia", "LR", 231, 7, 9, "0", 618),
        Libya("Libya", "LY", 218, 9, 9, "0", 606),
        Malawi("Malawi", "MW", 265, 9, 9, "0", 650),
        Malaysia("Malaysia", "MY", 60, 9, 10, "0", 502),
        Mali("Mali", "ML", 223, 8, 8, "0", 610),
        Mauritania("Mauritania", "MR", 222, 8, 8, "0", 609),
        Morocco("Morocco", "MA", 212, 9, 9, "0", 604),
        Niger("Niger", "NE", 227, 8, 8, "0", 614),
        Nigeria("Nigeria", "NG", 234, 10, 10, "0", 621),
        NorthKorea("North Korea", "KP", 850, 10, 10, "0", 467),
        Russia("Russia", "RU", 7, 10, 10, "8", 250),
        SaudiArabia("Saudi Arabia", "SA", 966, 9, 9, "0", 420),
        Senegal("Senegal", "SN", 221, 9, 9, "0", 608),
        SierraLeone("Sierra Leone", "SL", 232, 8, 8, "0", 619),
        SouthAfrica("South Africa", "ZA", 27, 9, 9, "0", 655),
        SouthKorea("South Korea", "KR", 82, 9, 10, "0", 450),
        Spain("Spain", "ES", 34, 9, 9, "0", 214),
        Sweden("Sweden", "SE", 46, 9, 9, "0", 240),
        Switzerland("Switzerland", "CH", 41, 9, 9, "0", 228),
        Togo("Togo", "TG", 228, 8, 8, "0", 615),
        Ukraine("Ukraine", "UA", 380, 9, 9, "0", 225),
        UnitedArabEmirate("United Arab Emirate", "AE", 971, 10, 10, "0", 424, 430, 431),
        UnitedKingdom("United Kingdom", "GB", 44, 10, 10, "0,1", 234, 235),
        UnitedStates("United States", "US", 1, 10, 10, "0,1", 301);

        private final int countryCode;
        private final String countryName;
        private final String iso;
        private final int allowableFromLength;
        private final int allowableToLength;
        private final String startDigitToIgnore;
        private final List<Integer> mcc;
        private String preceeding = "";

        public String getIso() {
            return iso;
        }

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

        private boolean meetCountryPhoneNumberRequirement(Country countries, String phoneNumber) {
            return ((phoneNumber.length() >= countries.getAllowableFromLength()) && (phoneNumber.length() <= countries.getAllowableToLength()));
        }


        private PhoneModel isNumberValid(Country countries, String phoneNumber) throws PhoneFormatException {
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

        public PhoneModel isNumberValid(String phoneNumber) throws PhoneFormatException {
            return isNumberValid(this, phoneNumber);
        }

        public String toCountryCode(String phoneNumber) throws PhoneFormatException {
            PhoneModel model = null;
            phoneNumber = phoneNumber.trim();
            phoneNumber = phoneNumber.trim();
            if (startWithPlus(phoneNumber)) {
                if (startWithPlusAndCountryCode(String.valueOf(getCountryCode()), phoneNumber)) {
                    model = isNumberValid(this, phoneNumber);
                }
            } else if (startWithCountryCode(String.valueOf(getCountryCode()), phoneNumber)) {
                model = isNumberValid(this, phoneNumber);
            } else {
                phoneNumber = "+" + String.valueOf(getCountryCode()) + phoneNumber;
                model = isNumberValid(this, phoneNumber);

            }
            if (model != null) {
                if (model.isValidPhoneNumber()) {
                    return model.getPhoneNumber();
                }
            }
            return "";
        }

        public String toPlainNumber(String phoneNumber) throws PhoneFormatException {
            PhoneModel model = null;
            phoneNumber = phoneNumber.trim();
            if (startWithPlusAndCountryCode(String.valueOf(getCountryCode()), phoneNumber)) {
                String cc = String.valueOf(getCountryCode());
                phoneNumber = phoneNumber.substring(cc.length() + 1);
                model = isNumberValid(this, phoneNumber);
            } else if (startWithCountryCode(String.valueOf(getCountryCode()), phoneNumber)) {
                String cc = String.valueOf(getCountryCode());
                phoneNumber = phoneNumber.substring(cc.length());
                model = isNumberValid(this, phoneNumber);
            } else {
                phoneNumber = formatNumber(this, phoneNumber);
                phoneNumber = preceeding + phoneNumber;
                model = isNumberValid(this, phoneNumber);
            }
            if (model != null) {
                if (model.isValidPhoneNumber()) {
                    return model.getPhoneNumber();
                }
            }
            return "";
        }

        private String formatNumber(Country countries, String phoneNumber) {
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

        private Country(String countryName, String iso, int countryCode, int allowableFromLength, int allowableToLength, String startDigitToIgnore, Integer... mcc) {
            this.countryCode = countryCode;
            this.iso = iso;
            this.countryName = countryName;
            this.allowableFromLength = allowableFromLength;
            this.allowableToLength = allowableToLength;
            this.startDigitToIgnore = startDigitToIgnore;
            this.mcc = new ArrayList<>();
            this.mcc.addAll(Arrays.asList(mcc));
        }

    }


    private ArrayList<Country> countriesList;

    /**
     * Get country by name of country
     */
    public Country getCountryByName(String countries) {
        for (Country c : Country.values()) {
            if (String.valueOf(c).equalsIgnoreCase(countries)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Get country by valid phone number of the country
     */
    public Country getCountryByPhoneNumber(Country defaultCountry, String phoneNumber) throws PhoneFormatException {
        for (Country c : Country.values()) {
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
    public Country getCountryByMcc(int mcc) {
        for (Country countries : Country.values()) {
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
    public ArrayList<Country> getCountriesByCountryCode(int countryCode) {
        countriesList = new ArrayList<>();
        for (Country countries : Country.values()) {
            if (countries.countryCode == countryCode) {
                countriesList.add(countries);
            }
        }
        return countriesList;
    }

    /**
     * Get country of the user.
     */
    public Country getUserCountry(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOperator = telephonyManager.getSimOperator();
        if (!simOperator.isEmpty()) {
            int mcc = Integer.parseInt(String.valueOf(context.getResources().getConfiguration().mcc));
            return getCountryByMcc(mcc);
        }
        return null;
    }

}
