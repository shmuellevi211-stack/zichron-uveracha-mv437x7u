package com.generated.app;

import java.util.Calendar;

public class HebrewDateUtil {

    public static final String[] HEBREW_MONTHS = {
            "תשרי", "מרחשוון", "כסלו", "טבת", "שבט", "אדר", "אדר א'", "אדר ב'",
            "ניסן", "אייר", "סיוון", "תמוז", "אב", "אלול"
    };

    public static final String[] GREGORIAN_MONTHS_HEB = {
            "ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני",
            "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"
    };

    // Hebrew representation of numbers 1-30
    public static String toHebrewNumber(int n) {
        if (n <= 0 || n > 30) return String.valueOf(n);
        String[] ones = {"", "א'", "ב'", "ג'", "ד'", "ה'", "ו'", "ז'", "ח'", "ט'"};
        if (n < 10) return ones[n];
        if (n == 10) return "י'";
        if (n == 11) return "י\"א";
        if (n == 12) return "י\"ב";
        if (n == 13) return "י\"ג";
        if (n == 14) return "י\"ד";
        if (n == 15) return "ט\"ו";
        if (n == 16) return "ט\"ז";
        if (n == 17) return "י\"ז";
        if (n == 18) return "י\"ח";
        if (n == 19) return "י\"ט";
        if (n == 20) return "כ'";
        if (n == 21) return "כ\"א";
        if (n == 22) return "כ\"ב";
        if (n == 23) return "כ\"ג";
        if (n == 24) return "כ\"ד";
        if (n == 25) return "כ\"ה";
        if (n == 26) return "כ\"ו";
        if (n == 27) return "כ\"ז";
        if (n == 28) return "כ\"ח";
        if (n == 29) return "כ\"ט";
        if (n == 30) return "ל'";
        return String.valueOf(n);
    }

    public static class HebrewDate {
        public int day;
        public int monthIndex; // 0: Tishrei, 1: Cheshvan, ... 8: Nisan, etc.
        public int year;

        public HebrewDate(int d, int m, int y) {
            this.day = d;
            this.monthIndex = m;
            this.year = y;
        }

        public String getMonthName() {
            if (monthIndex >= 0 && monthIndex < HEBREW_MONTHS.length) {
                return HEBREW_MONTHS[monthIndex];
            }
            return "";
        }

        public String formatHebrewDate() {
            return toHebrewNumber(day) + " ב" + getMonthName() + " " + formatHebrewYear(year);
        }
    }

    public static String formatHebrewYear(int year) {
        // e.g., 5785 -> תשפ"ה
        int mod = year % 1000;
        if (mod == 785) return "תשפ\"ה";
        if (mod == 784) return "תשפ\"ד";
        if (mod == 786) return "תשפ\"ו";
        if (mod == 787) return "תשפ\"ז";
        return "תשפ\"ה";
    }

    /**
     * Converts a Julian Day Number (JDN) to Gregorian Date
     */
    private static long gregorianToJdn(int year, int month, int day) {
        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;
        return day + (153 * m + 2) / 5 + 365L * y + y / 4 - y / 100 + y / 400 - 32045;
    }

    /**
     * Basic Jewish calendar conversion: calculates Molad of Tishrei for year
     */
    private static boolean isHebrewLeapYear(int hYear) {
        return ((7 * hYear + 1) % 19) < 7;
    }

    private static long roshHashanaJdn(int hYear) {
        // Molad calculation
        long months = (235L * hYear - 234) / 19;
        long parts = 12084 + 13753 * months;
        long day = 29 * months + parts / 25920;
        parts %= 25920;

        // Postponements (Dechiyot)
        long dayOfWeek = (day + 1) % 7; // 0 Sunday .. 6 Saturday
        if (parts >= 19440 ||
                (dayOfWeek == 2 && parts >= 9924 && !isHebrewLeapYear(hYear)) ||
                (dayOfWeek == 1 && parts >= 16789 && isHebrewLeapYear(hYear - 1))) {
            day++;
            dayOfWeek = (dayOfWeek + 1) % 7;
        }

        // Rosh Hashana cannot fall on Sun(0), Wed(3), Fri(5)
        if (dayOfWeek == 0 || dayOfWeek == 3 || dayOfWeek == 5) {
            day++;
        }
        return day + 347997; // base offset to match JDN
    }

    public static HebrewDate getHebrewDate(int gYear, int gMonth, int gDay) {
        long jdn = gregorianToJdn(gYear, gMonth, gDay);

        // Approximate Hebrew year
        int hYear = gYear + 3760;
        if (roshHashanaJdn(hYear + 1) <= jdn) {
            hYear++;
        } else if (roshHashanaJdn(hYear) > jdn) {
            hYear--;
        }

        long rhCurrent = roshHashanaJdn(hYear);
        int daysSinceRH = (int) (jdn - rhCurrent);

        boolean leap = isHebrewLeapYear(hYear);
        long rhNext = roshHashanaJdn(hYear + 1);
        int yearLen = (int) (rhNext - rhCurrent);

        boolean cheshvanLong = (yearLen == 355 || yearLen == 385);
        boolean kislevShort = (yearLen == 353 || yearLen == 383);

        int[] monthDays;
        if (!leap) {
            monthDays = new int[]{
                    30, // Tishrei
                    cheshvanLong ? 30 : 29, // Cheshvan
                    kislevShort ? 29 : 30, // Kislev
                    29, // Tevet
                    30, // Shevat
                    29, // Adar
                    30, // Nisan
                    29, // Iyyar
                    30, // Sivan
                    29, // Tammuz
                    30, // Av
                    29  // Elul
            };
        } else {
            monthDays = new int[]{
                    30, // Tishrei
                    cheshvanLong ? 30 : 29, // Cheshvan
                    kislevShort ? 29 : 30, // Kislev
                    29, // Tevet
                    30, // Shevat
                    30, // Adar I
                    29, // Adar II
                    30, // Nisan
                    29, // Iyyar
                    30, // Sivan
                    29, // Tammuz
                    30, // Av
                    29  // Elul
            };
        }

        int mIdx = 0;
        int d = daysSinceRH;
        for (int i = 0; i < monthDays.length; i++) {
            if (d < monthDays[i]) {
                mIdx = i;
                break;
            }
            d -= monthDays[i];
        }

        int hDay = d + 1;
        // Map to our HEBREW_MONTHS names
        int nameIdx = mIdx;
        if (!leap && mIdx >= 5) {
            // normal adar is at index 5 in our list
            if (mIdx == 5) nameIdx = 5;
            else nameIdx = mIdx + 2; // Nisan -> index 8, etc.
        } else if (leap && mIdx >= 5) {
            if (mIdx == 5) nameIdx = 6; // Adar A
            else if (mIdx == 6) nameIdx = 7; // Adar B
            else nameIdx = mIdx + 1;
        }

        if (nameIdx >= HEBREW_MONTHS.length) nameIdx = 0;
        return new HebrewDate(hDay, nameIdx, hYear);
    }

    public static HebrewDate getTodayHebrew() {
        Calendar cal = Calendar.getInstance();
        return getHebrewDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String getHolidayOrSpecialDay(HebrewDate hd) {
        String m = hd.getMonthName();
        int d = hd.day;

        if (m.equals("תשרי")) {
            if (d == 1 || d == 2) return "ראש השנה";
            if (d == 3) return "צום גדליה";
            if (d == 10) return "יום הכיפורים";
            if (d >= 15 && d <= 21) return "חג הסוכות";
            if (d == 22) return "שמיני עצרת וסוכות";
        } else if (m.equals("כסלו")) {
            if (d >= 25) return "חנוכה";
        } else if (m.equals("טבת")) {
            if (d <= 2) return "חנוכה";
            if (d == 10) return "עשרה בטבת";
        } else if (m.equals("שבט")) {
            if (d == 15) return "ט\"ו בשבט";
        } else if (m.contains("אדר")) {
            if (d == 13) return "תענית אסתר";
            if (d == 14) return "פורים";
            if (d == 15) return "שושן פורים";
        } else if (m.equals("ניסן")) {
            if (d >= 15 && d <= 22) return "חג הפסח";
        } else if (m.equals("אייר")) {
            if (d == 18) return "ל\"ג בעומר";
        } else if (m.equals("סיוון")) {
            if (d == 6) return "חג השבועות";
        } else if (m.equals("תמוז")) {
            if (d == 17) return "שבעה עשר בתמוז";
        } else if (m.equals("אב")) {
            if (d == 9) return "תשעה באב";
            if (d == 15) return "ט\"ו באב";
        } else if (m.equals("אלול")) {
            if (d == 29) return "ערב ראש השנה";
        }
        if (d == 1 || d == 30) return "ראש חודש";
        return "";
    }
}
