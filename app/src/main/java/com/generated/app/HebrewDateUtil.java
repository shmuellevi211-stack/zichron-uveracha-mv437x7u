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

    // Hebrew days representations 1 through 30
    public static final String[] HEBREW_DAYS = {
            "א'", "ב'", "ג'", "ד'", "ה'", "ו'", "ז'", "ח'", "ט'", "י'",
            "י\"א", "י\"ב", "י\"ג", "י\"ד", "ט\"ו", "ט\"ז", "י\"ז", "י\"ח", "י\"ט", "כ'",
            "כ\"א", "כ\"ב", "כ\"ג", "כ\"ד", "כ\"ה", "כ\"ו", "כ\"ז", "כ\"ח", "כ\"ט", "ל'"
    };

    public static String toHebrewNumber(int n) {
        if (n >= 1 && n <= 30) {
            return HEBREW_DAYS[n - 1];
        }
        return String.valueOf(n);
    }

    public static class HebrewDate {
        public int day;
        public int monthIndex; // 0: Tishrei, 1: Cheshvan, ...
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
        int mod = year % 1000;
        if (mod == 780) return "תש\"פ";
        if (mod == 781) return "תשפ\"א";
        if (mod == 782) return "תשפ\"ב";
        if (mod == 783) return "תשפ\"ג";
        if (mod == 784) return "תשפ\"ד";
        if (mod == 785) return "תשפ\"ה";
        if (mod == 786) return "תשפ\"ו";
        if (mod == 787) return "תשפ\"ז";
        if (mod == 788) return "תשפ\"ח";
        return "תשפ\"ה";
    }

    private static long gregorianToJdn(int year, int month, int day) {
        int a = (14 - month) / 12;
        int y = year + 4800 - a;
        int m = month + 12 * a - 3;
        return day + (153 * m + 2) / 5 + 365L * y + y / 4 - y / 100 + y / 400 - 32045;
    }

    private static boolean isHebrewLeapYear(int hYear) {
        return ((7 * hYear + 1) % 19) < 7;
    }

    private static long roshHashanaJdn(int hYear) {
        long months = (235L * hYear - 234) / 19;
        long parts = 12084 + 13753 * months;
        long day = 29 * months + parts / 25920;
        parts %= 25920;

        long dayOfWeek = (day + 1) % 7;
        if (parts >= 19440 ||
                (dayOfWeek == 2 && parts >= 9924 && !isHebrewLeapYear(hYear)) ||
                (dayOfWeek == 1 && parts >= 16789 && isHebrewLeapYear(hYear - 1))) {
            day++;
            dayOfWeek = (dayOfWeek + 1) % 7;
        }

        if (dayOfWeek == 0 || dayOfWeek == 3 || dayOfWeek == 5) {
            day++;
        }
        return day + 347997;
    }

    public static HebrewDate getHebrewDate(int gYear, int gMonth, int gDay) {
        long jdn = gregorianToJdn(gYear, gMonth, gDay);

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
                    30,
                    cheshvanLong ? 30 : 29,
                    kislevShort ? 29 : 30,
                    29,
                    30,
                    29,
                    30,
                    29,
                    30,
                    29,
                    30,
                    29
            };
        } else {
            monthDays = new int[]{
                    30,
                    cheshvanLong ? 30 : 29,
                    kislevShort ? 29 : 30,
                    29,
                    30,
                    30,
                    29,
                    30,
                    29,
                    30,
                    29,
                    30,
                    29
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
        int nameIdx = mIdx;
        if (!leap && mIdx >= 5) {
            if (mIdx == 5) nameIdx = 5;
            else nameIdx = mIdx + 2;
        } else if (leap && mIdx >= 5) {
            if (mIdx == 5) nameIdx = 6;
            else if (mIdx == 6) nameIdx = 7;
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
            if (d == 22) return "שמיני עצרת ושמחת תורה";
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
