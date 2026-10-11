package com.generated.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {

    private static final int TAB_HOME = 0;
    private static final int TAB_CALENDAR = 1;
    private static final int TAB_YAHRZEITS = 2;
    private static final int TAB_FAMILY = 3;
    private static final int TAB_TEHILLIM = 4;
    private static final int TAB_SETTINGS = 5;

    private int currentTab = TAB_HOME;

    private DatabaseHelper dbHelper;
    private LinearLayout mainContentContainer;
    private LinearLayout tabStripLayout;
    private Button[] tabButtons;

    // Calendar state
    private int calYear;
    private int calMonth; // 0-11

    private static final String[] TEHILLIM_TITLES = {
            "פרק כ\"ג - ה' רֹעִי לֹא אֶחְסָר",
            "פרק קכ\"א - שִׁיר לַמַּעֲלוֹת אֶשָּׂא עֵינַי",
            "פרק צ\"א - יֹשֵׁב בְּסֵתֶר עֶלְיוֹן",
            "פרק ק\"ל - מִמַּעֲמַקִּים קְרָאתִיךָ ה'",
            "פרק קמ\"ב - מַשְׂכִּיל לְדָוִד בִּהְיוֹתוֹ בַמְּעָרָה",
            "פרק כ' - יַעַנְךָ ה' בְּיוֹם צָרָה"
    };

    private static final String[] TEHILLIM_TEXTS = {
            "מִזְמוֹר לְדָוִד: ה' רֹעִי לֹא אֶחְסָר. בִּנְאוֹת דֶּשֶׁא יַרְבִּיצֵנִי עַל מֵי מְנֻחוֹת יְנַהֲלֵנִי. נַפְשִׁי יְשׁוֹבֵב יַנְחֵנִי בְמַעְגְּלֵי צֶדֶק לְמַעַן שְׁמוֹ. גַּם כִּי אֵלֵךְ בְּגֵיא צַלְמָוֶת לֹא אִירָא רָע כִּי אַתָּה עִמָּדִי, שִׁבְטְךָ וּמִשְׁעַנְתֶּךָ הֵמָּה יְנַחֲמֻנִי. תַּעֲרֹךְ לְפָנַי שֻׁלְחָן נֶגֶד צֹרְרָי, דִּשַּׁנְתָּ בַשֶּׁמֶן רֹאשִׁי כּוֹסִי רְוָיָה. אַךְ טוֹב וָחֶסֶד יִרְדְּפוּנִי כָּל יְמֵי חַיָּי וְשַׁבְתִּי בְּבֵית ה' לְאֹרֶךְ יָמִים.",
            "שִׁיר לַמַּעֲלוֹת: אֶשָּׂא עֵינַי אֶל הֶהָרִים מֵאַיִן יָבֹא עֶזְרִי. עֶזְרִי מֵעִם ה' עֹשֵׂה שָׁמַיִם וָאָרֶץ. אַל יִתֵּן לַמּוֹט רַגְלֶךָ אַל יָנוּם שֹׁמְרֶךָ. הִנֵּה לֹא יָנוּם וְלֹא יִישָׁן שׁוֹמֵר יִשְׂרָאֵל. ה' שֹׁמְרֶךָ ה' צִלְּךָ עַל יַד יְמִינֶךָ. יוֹמָם הַשֶּׁמֶשׁ לֹא יַכֶּכָּה וְיָרֵחַ בַּלָּיְלָה. ה' יִשְׁמָרְךָ מִכָּל רָע יִשְׁמֹר אֶת נַפְשֶׁךָ. ה' יִשְׁמָר צֵאתְךָ וּבוֹאֲךָ מֵעַתָּה וְעַד עוֹלָם.",
            "יֹשֵׁב בְּסֵתֶר עֶלְיוֹן בְּצֵל שַׁדַּי יִתְלוֹנָן. אֹמַר לַה' מַחְסִי וּמְצוּדָתִי אֱלֹהַי אֶבְטַח בּוֹ. כִּי הוּא יַצִּילְךָ מִפַּח יָקוּשׁ מִדֶּבֶר הַוּוֹת. בְּאֶבְרָתוֹ יָסֶךְ לָךְ וְתַחַת כְּנָפָיו תֶּחְסֶה צִנָּה וְסֹחֵרָה אֲמִתּוֹ. לֹא תִירָא מִפַּחַד לָיְלָה מֵחֵץ יָעוּף יוֹמָם. מִדֶּבֶר בָּאֹפֶל יַהֲלֹךְ מִקֶּטֶב יָשׁוּד צָהֳרָיִם. יִפֹּל מִצִּדְּךָ אֶלֶף וּרְבָבָה מִימִינֶךָ אֵלֶיךָ לֹא יִגָּשׁ. רַק בְּעֵינֶיךָ תַבִּיט וְשִׁלֻּמַת רְשָׁעִים תִּרְאֶה.",
            "שִׁיר הַמַּעֲלוֹת: מִמַּעֲמַקִּים קְרָאתִיךָ ה'. אֲדֹנָי שִׁמְעָה בְקוֹלִי תִּהְיֶינָה אָזְנֶיךָ קַשֻּׁבוֹת לְקוֹל תַּחֲנוּנָי. אִם עֲוֹנוֹת תִּשְׁמָר יָהּ אֲדֹנָי מִי יַעֲמֹד. כִּי עִמְּךָ הַסְּלִיחָה לְמַעַן תִּוָּרֵא. קִוִּיתִי ה' קִוְּתָה נַפְשִׁי וְלִדְבָרוֹ הוֹחָלְתִּי. נַפְשִׁי לַאדֹנָי מִשֹּׁמְרִים לַבֹּקֶר שֹׁמְרִים לַבֹּקֶר. יַחֵל יִשְׂרָאֵל אֶל ה' כִּי עִם ה' הַחֶסֶד וְהַרְבֵּה עִמּוֹ פְדוּת. וְהוּא יִפְדֶּה אֶת יִשְׂרָאֵל מִכֹּל עֲוֹנֹתָיו.",
            "מַשְׂכִּיל לְדָוִד בִּהְיוֹתוֹ בַמְּעָרָה תְפִלָּה: קוֹלִי אֶל ה' אֶזְעָק קוֹלִי אֶל ה' אֶתְחַנָּן. אֶשְׁפֹּךְ לְפָנָיו שִׂיחִי צָרָתִי לְפָנָיו אַגִּיד. בְּהִתְעַטֵּף עָלַי רוּחִי וְאַתָּה יָדַעְתָּ נְתִיבָתִי בְּאֹרַח זוּ אֲהַלֵּךְ טָמְנוּ פַח לִי. הַבֵּיט יָמִין וּרְאֵה וְאֵין לִי מַכִּיר אָבַד מָנוֹס מִמֶּנִּי אֵין דּוֹרֵשׁ לְנַפְשִׁי. זָעַקְתִּי אֵלֶיךָ ה' אָמַרְתִּי אַתָּה מַחְסִי חֶלְקִי בְּאֶרֶץ הַחַיִּים.",
            "לַמְנַצֵּחַ מִזְמוֹר לְדָוִד: יַעַנְךָ ה' בְּיוֹם צָרָה יְשַׂגֶּבְךָ שֵׁם אֱלֹהֵי יַעֲקֹב. יִשְׁלַח עֶזְרְךָ מִקֹּדֶשׁ וּמִצִּיּוֹן יִסְעָדֶךָּ. יִזְכֹּר כָּל מִנְחֹתֶךָ וְעוֹלָתְךָ יְדַשְּׁנֶה סֶלָה. יִתֶּן לְךָ כִלְבָבֶךָ וְכָל עֲצָתְךָ יְמַלֵּא. נְרַנְּנָה בִּישׁוּעָתֶךָ וּבְשֵׁם אֱלֹהֵינוּ נִדְגֹּל יְמַלֵּא ה' כָּל מִשְׁאֲלוֹתֶיךָ."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);

        Calendar cal = Calendar.getInstance();
        calYear = cal.get(Calendar.YEAR);
        calMonth = cal.get(Calendar.MONTH);

        buildMainLayout();
        switchTab(TAB_HOME);
    }

    private void buildMainLayout() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.parseColor("#F7F6F2"));
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Header
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setBackgroundColor(Color.parseColor("#101D33"));
        header.setPadding(dp(18), dp(16), dp(18), dp(16));
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView titleView = new TextView(this);
        titleView.setText("זיכרון וברכה");
        titleView.setTextColor(Color.parseColor("#D4AF37"));
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        titleView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        header.addView(titleView);

        TextView subTitle = new TextView(this);
        subTitle.setText("לוח חיים יהודי");
        subTitle.setTextColor(Color.parseColor("#E0E6ED"));
        subTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        header.addView(subTitle);

        root.addView(header);

        // Responsive touch & keypad Tab Bar
        HorizontalScrollView tabScroll = new HorizontalScrollView(this);
        tabScroll.setHorizontalScrollBarEnabled(false);
        tabScroll.setBackgroundColor(Color.parseColor("#152238"));

        tabStripLayout = new LinearLayout(this);
        tabStripLayout.setOrientation(LinearLayout.HORIZONTAL);
        tabStripLayout.setPadding(dp(8), dp(8), dp(8), dp(8));

        String[] tabNames = {
                "בית",
                "לוח שנה",
                "יארצייטים",
                "ספר משפחה",
                "תהילים ותפילה",
                "הגדרות"
        };

        tabButtons = new Button[tabNames.length];
        for (int i = 0; i < tabNames.length; i++) {
            final int index = i;
            Button b = new Button(this);
            b.setText(tabNames[i]);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            b.setTypeface(Typeface.DEFAULT_BOLD);
            b.setTextColor(Color.WHITE);
            b.setBackgroundResource(R.drawable.tab_item_selector);
            // Touch & Keypad optimized: focusable for D-pad, NOT focusableInTouchMode so 1-touch responds immediately!
            b.setFocusable(true);
            b.setFocusableInTouchMode(false);
            b.setMinHeight(dp(48));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(dp(4), dp(2), dp(4), dp(2));
            b.setLayoutParams(lp);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchTab(index);
                }
            });
            tabButtons[i] = b;
            tabStripLayout.addView(b);
        }
        tabScroll.addView(tabStripLayout);
        root.addView(tabScroll);

        // Content Area
        ScrollView scroller = new ScrollView(this);
        scroller.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
        scroller.setFillViewport(true);

        mainContentContainer = new LinearLayout(this);
        mainContentContainer.setOrientation(LinearLayout.VERTICAL);
        mainContentContainer.setPadding(dp(16), dp(16), dp(16), dp(28));
        scroller.addView(mainContentContainer);

        root.addView(scroller);
        setContentView(root);
    }

    private void switchTab(int tabIndex) {
        currentTab = tabIndex;
        for (int i = 0; i < tabButtons.length; i++) {
            tabButtons[i].setSelected(i == tabIndex);
        }
        mainContentContainer.removeAllViews();

        switch (tabIndex) {
            case TAB_HOME:
                renderHomeScreen();
                break;
            case TAB_CALENDAR:
                renderCalendarScreen();
                break;
            case TAB_YAHRZEITS:
                renderYahrzeitsScreen();
                break;
            case TAB_FAMILY:
                renderFamilyScreen();
                break;
            case TAB_TEHILLIM:
                renderTehillimScreen();
                break;
            case TAB_SETTINGS:
                renderSettingsScreen();
                break;
        }
    }

    // ========================================================
    // TAB 1: בית (Home)
    // ========================================================
    private void renderHomeScreen() {
        HebrewDateUtil.HebrewDate hebToday = HebrewDateUtil.getTodayHebrew();
        Calendar cal = Calendar.getInstance();

        // Hebrew Date Card
        View dateCard = createCardView();
        LinearLayout dateInner = (LinearLayout) dateCard;

        TextView todayLabel = new TextView(this);
        todayLabel.setText("היום יום " + getHebrewDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)));
        todayLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        todayLabel.setTextColor(Color.parseColor("#6A7885"));
        dateInner.addView(todayLabel);

        TextView hebDateText = new TextView(this);
        hebDateText.setText(hebToday.formatHebrewDate());
        hebDateText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        hebDateText.setTypeface(Typeface.DEFAULT_BOLD);
        hebDateText.setTextColor(Color.parseColor("#101D33"));
        hebDateText.setPadding(0, dp(4), 0, dp(4));
        dateInner.addView(hebDateText);

        String gregDateStr = cal.get(Calendar.DAY_OF_MONTH) + " ב" +
                HebrewDateUtil.GREGORIAN_MONTHS_HEB[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR);
        TextView gregDateText = new TextView(this);
        gregDateText.setText("תאריך לועזי: " + gregDateStr);
        gregDateText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        gregDateText.setTextColor(Color.parseColor("#445261"));
        dateInner.addView(gregDateText);

        String holiday = HebrewDateUtil.getHolidayOrSpecialDay(hebToday);
        if (holiday.length() > 0) {
            TextView holidayText = new TextView(this);
            holidayText.setText("★ " + holiday + " ★");
            holidayText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            holidayText.setTypeface(Typeface.DEFAULT_BOLD);
            holidayText.setTextColor(Color.parseColor("#B8860B"));
            holidayText.setPadding(0, dp(6), 0, 0);
            dateInner.addView(holidayText);
        }
        mainContentContainer.addView(dateCard);

        // Quick Action Shortcuts for Touch & Keypad
        LinearLayout actionRow = new LinearLayout(this);
        actionRow.setOrientation(LinearLayout.HORIZONTAL);
        actionRow.setPadding(0, 0, 0, dp(14));

        Button addYahHome = createFocusableButton("+ יארצייט עברי", true);
        addYahHome.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        addYahHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddYahrzeitDialog(null);
            }
        });
        actionRow.addView(addYahHome);

        View gap = new View(this);
        gap.setLayoutParams(new LinearLayout.LayoutParams(dp(10), ViewGroup.LayoutParams.MATCH_PARENT));
        actionRow.addView(gap);

        Button addFamHome = createFocusableButton("+ בן משפחה", false);
        addFamHome.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        addFamHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFamilyDialog(null);
            }
        });
        actionRow.addView(addFamHome);

        mainContentContainer.addView(actionRow);

        // Personal Tehillim Chapter
        View tehillimCard = createCardView();
        LinearLayout tehillimInner = (LinearLayout) tehillimCard;

        tehillimInner.addView(createSectionHeader("הפרק האישי שלך לשנה זו"));

        TextView tehillimDesc = new TextView(this);
        tehillimDesc.setText("על פי מנהג ישראל, אומר אדם את מזמור התהילים המתאים לגילו + 1. הקלד את הגיל לקבלת המזמור:");
        tehillimDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tehillimDesc.setTextColor(Color.parseColor("#333333"));
        tehillimDesc.setPadding(0, 0, 0, dp(8));
        tehillimInner.addView(tehillimDesc);

        LinearLayout ageRow = new LinearLayout(this);
        ageRow.setOrientation(LinearLayout.HORIZONTAL);
        ageRow.setGravity(Gravity.CENTER_VERTICAL);

        final EditText ageInput = new EditText(this);
        ageInput.setHint("לדוגמה: 40");
        ageInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        ageInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        ageInput.setBackgroundResource(R.drawable.input_selector);
        ageInput.setMinHeight(dp(48));
        ageInput.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        ageRow.addView(ageInput);

        Button showChapterBtn = createFocusableButton("הצג מזמור", false);
        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnLp.setMargins(dp(8), 0, 0, 0);
        showChapterBtn.setLayoutParams(btnLp);
        ageRow.addView(showChapterBtn);

        tehillimInner.addView(ageRow);

        final TextView personalResult = new TextView(this);
        personalResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        personalResult.setTextColor(Color.parseColor("#101D33"));
        personalResult.setTypeface(Typeface.DEFAULT_BOLD);
        personalResult.setPadding(0, dp(8), 0, 0);
        tehillimInner.addView(personalResult);

        showChapterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = ageInput.getText().toString().trim();
                if (!val.isEmpty()) {
                    try {
                        int age = Integer.parseInt(val);
                        int chapter = age + 1;
                        personalResult.setText("הפרק האישי שלך: מזמור " + HebrewDateUtil.toHebrewNumber(chapter) + " (פרק " + chapter + ")");
                    } catch (Exception ex) {
                        personalResult.setText("נא להזין מספר תקין");
                    }
                }
            }
        });

        mainContentContainer.addView(tehillimCard);

        // Upcoming Yahrzeits
        View yahrzeitSection = createCardView();
        LinearLayout yahInner = (LinearLayout) yahrzeitSection;
        yahInner.addView(createSectionHeader("יארצייטים קרובים"));

        List<DatabaseHelper.Yahrzeit> yList = dbHelper.getAllYahrzeits();
        if (yList.isEmpty()) {
            TextView emptyYah = new TextView(this);
            emptyYah.setText("אין יארצייטים מוגדרים במערכת");
            emptyYah.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            emptyYah.setTextColor(Color.GRAY);
            yahInner.addView(emptyYah);
        } else {
            for (DatabaseHelper.Yahrzeit y : yList) {
                String monthName = (y.hMonth >= 0 && y.hMonth < HebrewDateUtil.HEBREW_MONTHS.length)
                        ? HebrewDateUtil.HEBREW_MONTHS[y.hMonth] : "";
                View item = createFocusableRow(
                        "🕯 " + y.getFullName() + " (" + y.getMemorialTitle() + ")",
                        "תאריך עברי: " + HebrewDateUtil.toHebrewNumber(y.hDay) + " ב" + monthName +
                                (y.notes != null && !y.notes.isEmpty() ? " | " + y.notes : "")
                );
                yahInner.addView(item);
            }
        }
        mainContentContainer.addView(yahrzeitSection);

        // Upcoming Family Birthdays
        View bdaySection = createCardView();
        LinearLayout bdayInner = (LinearLayout) bdaySection;
        bdayInner.addView(createSectionHeader("ימי הולדת קרובים"));

        List<DatabaseHelper.Member> mList = dbHelper.getAllMembers();
        if (mList.isEmpty()) {
            TextView emptyBday = new TextView(this);
            emptyBday.setText("אין בני משפחה רשומים כרגע");
            emptyBday.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            emptyBday.setTextColor(Color.GRAY);
            bdayInner.addView(emptyBday);
        } else {
            for (final DatabaseHelper.Member m : mList) {
                LinearLayout row = (LinearLayout) createFocusableRow(
                        "🎂 " + m.name + " (" + m.relation + ") - בן/בת " + m.getAge(),
                        "תאריך: " + m.day + "/" + m.month + "/" + m.year + " (" + m.hebrewDate + ") | פרק תהילים אישי: " + m.getPersonalTehillim()
                );
                Button shareBtn = createFocusableButton("ברכה מוכנה", true);
                shareBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                shareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBlessingDialog(m);
                    }
                });
                row.addView(shareBtn);
                bdayInner.addView(row);
            }
        }
        mainContentContainer.addView(bdaySection);
    }

    private String getHebrewDayOfWeek(int dow) {
        switch (dow) {
            case Calendar.SUNDAY: return "ראשון";
            case Calendar.MONDAY: return "שני";
            case Calendar.TUESDAY: return "שלישי";
            case Calendar.WEDNESDAY: return "רביעי";
            case Calendar.THURSDAY: return "חמישי";
            case Calendar.FRIDAY: return "שישי";
            case Calendar.SATURDAY: return "שבת קודש";
        }
        return "";
    }

    // ========================================================
    // TAB 2: לוח שנה (Calendar)
    // ========================================================
    private void renderCalendarScreen() {
        View calCard = createCardView();
        LinearLayout calInner = (LinearLayout) calCard;

        // Month Bar
        LinearLayout navBar = new LinearLayout(this);
        navBar.setOrientation(LinearLayout.HORIZONTAL);
        navBar.setGravity(Gravity.CENTER_VERTICAL);

        Button prevBtn = createFocusableButton("חודש קודם ►", false);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calMonth--;
                if (calMonth < 0) {
                    calMonth = 11;
                    calYear--;
                }
                switchTab(TAB_CALENDAR);
            }
        });
        navBar.addView(prevBtn);

        TextView monthTitle = new TextView(this);
        monthTitle.setText(HebrewDateUtil.GREGORIAN_MONTHS_HEB[calMonth] + " " + calYear);
        monthTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        monthTitle.setTypeface(Typeface.DEFAULT_BOLD);
        monthTitle.setTextColor(Color.parseColor("#101D33"));
        monthTitle.setGravity(Gravity.CENTER);
        monthTitle.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        navBar.addView(monthTitle);

        Button nextBtn = createFocusableButton("◄ חודש הבא", false);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calMonth++;
                if (calMonth > 11) {
                    calMonth = 0;
                    calYear++;
                }
                switchTab(TAB_CALENDAR);
            }
        });
        navBar.addView(nextBtn);

        calInner.addView(navBar);

        // Weekday Headers
        LinearLayout daysHeader = new LinearLayout(this);
        daysHeader.setOrientation(LinearLayout.HORIZONTAL);
        daysHeader.setPadding(0, dp(14), 0, dp(8));
        String[] daysOfWeek = {"א'", "ב'", "ג'", "ד'", "ה'", "ו'", "שבת"};
        for (String dow : daysOfWeek) {
            TextView tv = new TextView(this);
            tv.setText(dow);
            tv.setTextColor(Color.parseColor("#4B5563"));
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            daysHeader.addView(tv);
        }
        calInner.addView(daysHeader);

        // Calendar Grid
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, calYear);
        cal.set(Calendar.MONTH, calMonth);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        LinearLayout weekRow = new LinearLayout(this);
        weekRow.setOrientation(LinearLayout.HORIZONTAL);

        int currentCell = 1;
        for (int i = 1; i < firstDayOfWeek; i++) {
            View emptyCell = new View(this);
            emptyCell.setLayoutParams(new LinearLayout.LayoutParams(0, dp(64), 1f));
            weekRow.addView(emptyCell);
            currentCell++;
        }

        List<DatabaseHelper.Yahrzeit> yList = dbHelper.getAllYahrzeits();
        List<DatabaseHelper.Member> mList = dbHelper.getAllMembers();

        for (int day = 1; day <= maxDaysInMonth; day++) {
            HebrewDateUtil.HebrewDate hd = HebrewDateUtil.getHebrewDate(calYear, calMonth + 1, day);
            String holiday = HebrewDateUtil.getHolidayOrSpecialDay(hd);

            boolean hasYahrzeit = false;
            for (DatabaseHelper.Yahrzeit y : yList) {
                if (y.hDay == hd.day && y.hMonth == hd.monthIndex) {
                    hasYahrzeit = true;
                    break;
                }
            }

            boolean hasBirthday = false;
            for (DatabaseHelper.Member m : mList) {
                if (m.day == day && m.month == (calMonth + 1)) {
                    hasBirthday = true;
                    break;
                }
            }

            final int chosenDay = day;
            final HebrewDateUtil.HebrewDate currentHd = hd;
            final String currentHoliday = holiday;

            Button cell = new Button(this);
            cell.setFocusable(true);
            cell.setFocusableInTouchMode(false); // Touch reacts on 1st tap
            cell.setBackgroundResource(R.drawable.card_selector);
            cell.setPadding(dp(2), dp(2), dp(2), dp(2));

            String text = day + "\n" + HebrewDateUtil.toHebrewNumber(hd.day);
            if (hasYahrzeit) text += "\n🕯";
            else if (hasBirthday) text += "\n🎂";
            else if (!holiday.isEmpty()) text += "\n★";

            cell.setText(text);
            cell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            cell.setTextColor(Color.parseColor("#101D33"));
            if (currentCell % 7 == 0) {
                cell.setTextColor(Color.parseColor("#B8860B"));
            }

            cell.setLayoutParams(new LinearLayout.LayoutParams(0, dp(64), 1f));
            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDayDetails(chosenDay, calMonth + 1, calYear, currentHd, currentHoliday);
                }
            });

            weekRow.addView(cell);

            if (currentCell % 7 == 0) {
                calInner.addView(weekRow);
                weekRow = new LinearLayout(this);
                weekRow.setOrientation(LinearLayout.HORIZONTAL);
            }
            currentCell++;
        }

        while (currentCell % 7 != 1) {
            View emptyCell = new View(this);
            emptyCell.setLayoutParams(new LinearLayout.LayoutParams(0, dp(64), 1f));
            weekRow.addView(emptyCell);
            currentCell++;
        }
        calInner.addView(weekRow);

        mainContentContainer.addView(calCard);
    }

    private void showDayDetails(int gDay, int gMonth, int gYear, HebrewDateUtil.HebrewDate hd, String holiday) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(gDay + "/" + gMonth + "/" + gYear + " - " + hd.formatHebrewDate());

        StringBuilder sb = new StringBuilder();
        if (!holiday.isEmpty()) {
            sb.append("אירוע: ").append(holiday).append("\n\n");
        }

        List<DatabaseHelper.Yahrzeit> yList = dbHelper.getAllYahrzeits();
        for (DatabaseHelper.Yahrzeit y : yList) {
            if (y.hDay == hd.day && y.hMonth == hd.monthIndex) {
                sb.append("🕯 יארצייט: ").append(y.getFullName()).append(" (").append(y.getMemorialTitle()).append(")\n");
                if (y.notes != null && !y.notes.isEmpty()) sb.append("הערה: ").append(y.notes).append("\n");
            }
        }

        List<DatabaseHelper.Member> mList = dbHelper.getAllMembers();
        for (DatabaseHelper.Member m : mList) {
            if (m.day == gDay && m.month == gMonth) {
                sb.append("🎂 יום הולדת: ").append(m.name).append(" (").append(m.relation).append(")\n");
                sb.append("פרק אישי: ").append(m.getPersonalTehillim()).append("\n");
            }
        }

        if (sb.length() == 0) {
            sb.append("אין אירועים שמורים בתאריך זה.");
        }

        builder.setMessage(sb.toString().trim());
        builder.setPositiveButton("סגור", null);
        builder.show();
    }

    // ========================================================
    // TAB 3: יארצייטים (Yahrzeits) - Hebrew Date input & Default "אבא" / "אמא"
    // ========================================================
    private void renderYahrzeitsScreen() {
        // Quick Presets Row for "אבא" and "אמא"
        LinearLayout quickAddBar = new LinearLayout(this);
        quickAddBar.setOrientation(LinearLayout.HORIZONTAL);
        quickAddBar.setPadding(0, 0, 0, dp(12));

        Button addDadBtn = createFocusableButton("+ יארצייט עבור אבא", true);
        addDadBtn.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        addDadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddYahrzeitDialog("אבא");
            }
        });
        quickAddBar.addView(addDadBtn);

        View g1 = new View(this);
        g1.setLayoutParams(new LinearLayout.LayoutParams(dp(8), ViewGroup.LayoutParams.MATCH_PARENT));
        quickAddBar.addView(g1);

        Button addMomBtn = createFocusableButton("+ יארצייט עבור אמא", true);
        addMomBtn.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        addMomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddYahrzeitDialog("אמא");
            }
        });
        quickAddBar.addView(addMomBtn);

        mainContentContainer.addView(quickAddBar);

        Button addCustomBtn = createFocusableButton("+ הוסף יארצייט חדש", false);
        addCustomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddYahrzeitDialog(null);
            }
        });
        mainContentContainer.addView(addCustomBtn);

        // List
        View listCard = createCardView();
        LinearLayout listInner = (LinearLayout) listCard;
        listInner.addView(createSectionHeader("רשימת יארצייטים (לפי תאריך עברי)"));

        List<DatabaseHelper.Yahrzeit> list = dbHelper.getAllYahrzeits();
        if (list.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("טרם נוספו שמות לרשימת היארצייטים.");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setTextColor(Color.GRAY);
            listInner.addView(tv);
        } else {
            for (final DatabaseHelper.Yahrzeit y : list) {
                String monthName = (y.hMonth >= 0 && y.hMonth < HebrewDateUtil.HEBREW_MONTHS.length)
                        ? HebrewDateUtil.HEBREW_MONTHS[y.hMonth] : "";
                String hebDateFormatted = HebrewDateUtil.toHebrewNumber(y.hDay) + " ב" + monthName;
                if (y.hYear > 0) {
                    hebDateFormatted += " " + HebrewDateUtil.formatHebrewYear(y.hYear);
                }

                LinearLayout row = (LinearLayout) createFocusableRow(
                        "🕯 " + y.getFullName() + " (" + y.getMemorialTitle() + ")",
                        "תאריך עברי: " + hebDateFormatted +
                                (y.notes != null && !y.notes.isEmpty() ? "\nהערות: " + y.notes : "")
                );

                Button delBtn = createFocusableButton("מחק", false);
                delBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                delBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDeleteYahrzeit(y);
                    }
                });
                row.addView(delBtn);
                listInner.addView(row);
            }
        }

        mainContentContainer.addView(listCard);
    }

    // Hebrew date picker dialog with default "אבא" / "אמא"
    private void showAddYahrzeitDialog(String defaultRole) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("הוספת יארצייט - תאריך עברי");

        ScrollView sv = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(20), dp(10), dp(20), dp(10));

        // Quick Preset Selector Row
        TextView presetLabel = new TextView(this);
        presetLabel.setText("בחירה מהירה:");
        presetLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        presetLabel.setTextColor(Color.parseColor("#4B5563"));
        layout.addView(presetLabel);

        LinearLayout chipsRow = new LinearLayout(this);
        chipsRow.setOrientation(LinearLayout.HORIZONTAL);
        chipsRow.setPadding(0, dp(4), 0, dp(10));

        Button chipDad = createChipButton("אבא");
        Button chipMom = createChipButton("אמא");
        Button chipOther = createChipButton("קרוב אחר");
        chipsRow.addView(chipDad);

        View chipSpacer = new View(this);
        chipSpacer.setLayoutParams(new LinearLayout.LayoutParams(dp(8), ViewGroup.LayoutParams.MATCH_PARENT));
        chipsRow.addView(chipSpacer);
        chipsRow.addView(chipMom);

        View chipSpacer2 = new View(this);
        chipSpacer2.setLayoutParams(new LinearLayout.LayoutParams(dp(8), ViewGroup.LayoutParams.MATCH_PARENT));
        chipsRow.addView(chipSpacer2);
        chipsRow.addView(chipOther);

        layout.addView(chipsRow);

        final EditText etFirst = createLabeledInput(layout, "שם פרטי של הנפטר/ת:");
        final EditText etLast = createLabeledInput(layout, "שם משפחה:");
        final EditText etParent = createLabeledInput(layout, "שם ההורה (בן / בת):");

        // Apply defaults
        final boolean[] isFather = new boolean[]{true};
        if ("אבא".equals(defaultRole)) {
            etFirst.setText("אבא מורי");
            isFather[0] = true;
        } else if ("אמא".equals(defaultRole)) {
            etFirst.setText("אמי מורתי");
            isFather[0] = false;
        } else {
            // Default name prefilled to "אבא" as requested
            etFirst.setText("אבא");
            isFather[0] = true;
        }

        chipDad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirst.setText("אבא מורי");
                isFather[0] = true;
            }
        });
        chipMom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirst.setText("אמי מורתי");
                isFather[0] = false;
            }
        });
        chipOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirst.setText("");
            }
        });

        // HEBREW DATE SELECTION
        TextView hebDateHeader = new TextView(this);
        hebDateHeader.setText("תאריך עברי של היארצייט:");
        hebDateHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        hebDateHeader.setTypeface(Typeface.DEFAULT_BOLD);
        hebDateHeader.setTextColor(Color.parseColor("#101D33"));
        hebDateHeader.setPadding(0, dp(12), 0, dp(4));
        layout.addView(hebDateHeader);

        // Spinner for Hebrew Day (א' - ל')
        TextView dayLabel = new TextView(this);
        dayLabel.setText("יום בחודש (עברי):");
        dayLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        dayLabel.setTextColor(Color.parseColor("#333333"));
        layout.addView(dayLabel);

        final Spinner daySpinner = new Spinner(this);
        daySpinner.setBackgroundResource(R.drawable.spinner_background);
        daySpinner.setMinimumHeight(dp(48));
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, HebrewDateUtil.HEBREW_DAYS);
        daySpinner.setAdapter(dayAdapter);
        // Default to today's Hebrew day
        HebrewDateUtil.HebrewDate todayHeb = HebrewDateUtil.getTodayHebrew();
        if (todayHeb.day >= 1 && todayHeb.day <= 30) {
            daySpinner.setSelection(todayHeb.day - 1);
        }
        layout.addView(daySpinner);

        // Spinner for Hebrew Month (תשרי - אלול)
        TextView monthLabel = new TextView(this);
        monthLabel.setText("חודש עברי:");
        monthLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        monthLabel.setTextColor(Color.parseColor("#333333"));
        monthLabel.setPadding(0, dp(8), 0, dp(2));
        layout.addView(monthLabel);

        final Spinner monthSpinner = new Spinner(this);
        monthSpinner.setBackgroundResource(R.drawable.spinner_background);
        monthSpinner.setMinimumHeight(dp(48));
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, HebrewDateUtil.HEBREW_MONTHS);
        monthSpinner.setAdapter(monthAdapter);
        if (todayHeb.monthIndex >= 0 && todayHeb.monthIndex < HebrewDateUtil.HEBREW_MONTHS.length) {
            monthSpinner.setSelection(todayHeb.monthIndex);
        }
        layout.addView(monthSpinner);

        // Hebrew Year input
        final EditText etHYear = createLabeledInput(layout, "שנת פטירה עברית (למשל: תשפ\"ד או מספר 5784):");
        etHYear.setText(HebrewDateUtil.formatHebrewYear(todayHeb.year));

        final EditText etNotes = createLabeledInput(layout, "הערות / מנהגים (קדיש, הדלקת נר, לימוד משניות):");
        etNotes.setText("להדליק נר נשמה, לומר קדיש");

        sv.addView(layout);
        builder.setView(sv);

        builder.setPositiveButton("שמור יארצייט", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fn = etFirst.getText().toString().trim();
                String ln = etLast.getText().toString().trim();
                String pr = etParent.getText().toString().trim();
                if (fn.isEmpty()) {
                    fn = "אבא"; // Default fallback
                }

                int chosenHebDay = daySpinner.getSelectedItemPosition() + 1;
                int chosenHebMonth = monthSpinner.getSelectedItemPosition();

                String yrInput = etHYear.getText().toString().trim();
                int chosenHebYear = parseHebrewYearNumber(yrInput);

                dbHelper.insertYahrzeit(
                        fn,
                        ln,
                        pr.isEmpty() ? "פלוני" : pr,
                        isFather[0],
                        0, 0, 0, // Hebrew date is primary
                        chosenHebDay,
                        chosenHebMonth,
                        chosenHebYear,
                        etNotes.getText().toString().trim()
                );

                Toast.makeText(MainActivity.this, "היארצייט נשמר בהצלחה!", Toast.LENGTH_SHORT).show();
                switchTab(TAB_YAHRZEITS);
            }
        });
        builder.setNegativeButton("ביטול", null);
        builder.show();
    }

    private int parseHebrewYearNumber(String input) {
        if (input == null || input.isEmpty()) return 5785;
        try {
            return Integer.parseInt(input);
        } catch (Exception e) {
            if (input.contains("תשפ\"ה")) return 5785;
            if (input.contains("תשפ\"ד")) return 5784;
            if (input.contains("תשפ\"ו")) return 5786;
            if (input.contains("תשפ\"ג")) return 5783;
            if (input.contains("תשפ\"ב")) return 5782;
            if (input.contains("תשפ\"א")) return 5781;
            if (input.contains("תש\"פ")) return 5780;
            return 5785;
        }
    }

    private void confirmDeleteYahrzeit(final DatabaseHelper.Yahrzeit y) {
        new AlertDialog.Builder(this)
                .setTitle("מחיקת יארצייט")
                .setMessage("האם למחוק את " + y.getFullName() + "?")
                .setPositiveButton("כן, מחק", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteYahrzeit(y.id);
                        switchTab(TAB_YAHRZEITS);
                    }
                })
                .setNegativeButton("ביטול", null)
                .show();
    }

    // ========================================================
    // TAB 4: ספר משפחה (Family Book) - Default "אבא" ו"אמא"
    // ========================================================
    private void renderFamilyScreen() {
        // Quick Presets Row for "אבא" and "אמא"
        LinearLayout quickAddBar = new LinearLayout(this);
        quickAddBar.setOrientation(LinearLayout.HORIZONTAL);
        quickAddBar.setPadding(0, 0, 0, dp(12));

        Button addDadBtn = createFocusableButton("+ הוסף אבא", true);
        addDadBtn.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        addDadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFamilyDialog("אבא");
            }
        });
        quickAddBar.addView(addDadBtn);

        View g1 = new View(this);
        g1.setLayoutParams(new LinearLayout.LayoutParams(dp(8), ViewGroup.LayoutParams.MATCH_PARENT));
        quickAddBar.addView(g1);

        Button addMomBtn = createFocusableButton("+ הוסף אמא", true);
        addMomBtn.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        addMomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFamilyDialog("אמא");
            }
        });
        quickAddBar.addView(addMomBtn);

        mainContentContainer.addView(quickAddBar);

        Button addCustomMember = createFocusableButton("+ הוסף בן משפחה נוסף", false);
        addCustomMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFamilyDialog(null);
            }
        });
        mainContentContainer.addView(addCustomMember);

        View card = createCardView();
        LinearLayout cardInner = (LinearLayout) card;
        cardInner.addView(createSectionHeader("ספר המשפחה והקרובים"));

        List<DatabaseHelper.Member> list = dbHelper.getAllMembers();
        if (list.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("ספר המשפחה ריק כרגע.");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setTextColor(Color.GRAY);
            cardInner.addView(tv);
        } else {
            for (final DatabaseHelper.Member m : list) {
                LinearLayout row = (LinearLayout) createFocusableRow(
                        m.name + " (" + m.relation + ") - גיל " + m.getAge(),
                        "תאריך לידה: " + m.day + "/" + m.month + "/" + m.year +
                                (m.hebrewDate != null && !m.hebrewDate.isEmpty() ? " | עברי: " + m.hebrewDate : "") +
                                "\nהפרק האישי לשנה זו: מזמור " + HebrewDateUtil.toHebrewNumber(m.getPersonalTehillim()) +
                                " (פרק " + m.getPersonalTehillim() + ")" +
                                (m.notes != null && !m.notes.isEmpty() ? "\nהערות: " + m.notes : "")
                );

                LinearLayout btnLayout = new LinearLayout(this);
                btnLayout.setOrientation(LinearLayout.HORIZONTAL);

                Button blessBtn = createFocusableButton("ברכה מוכנה", true);
                blessBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                blessBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBlessingDialog(m);
                    }
                });
                btnLayout.addView(blessBtn);

                View sp = new View(this);
                sp.setLayoutParams(new LinearLayout.LayoutParams(dp(8), ViewGroup.LayoutParams.MATCH_PARENT));
                btnLayout.addView(sp);

                Button delBtn = createFocusableButton("מחק", false);
                delBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                delBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDeleteMember(m);
                    }
                });
                btnLayout.addView(delBtn);

                row.addView(btnLayout);
                cardInner.addView(row);
            }
        }
        mainContentContainer.addView(card);
    }

    private void showAddFamilyDialog(String defaultRelation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("הוספת בן/בת משפחה");

        ScrollView sv = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(20), dp(10), dp(20), dp(10));

        // Quick Preset Buttons
        TextView presetLabel = new TextView(this);
        presetLabel.setText("בחירה מהירה:");
        presetLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        presetLabel.setTextColor(Color.parseColor("#4B5563"));
        layout.addView(presetLabel);

        LinearLayout chipsRow = new LinearLayout(this);
        chipsRow.setOrientation(LinearLayout.HORIZONTAL);
        chipsRow.setPadding(0, dp(4), 0, dp(10));

        Button chipDad = createChipButton("אבא");
        Button chipMom = createChipButton("אמא");
        Button chipOther = createChipButton("אחר");
        chipsRow.addView(chipDad);

        View cSp1 = new View(this);
        cSp1.setLayoutParams(new LinearLayout.LayoutParams(dp(8), ViewGroup.LayoutParams.MATCH_PARENT));
        chipsRow.addView(cSp1);
        chipsRow.addView(chipMom);

        View cSp2 = new View(this);
        cSp2.setLayoutParams(new LinearLayout.LayoutParams(dp(8), ViewGroup.LayoutParams.MATCH_PARENT));
        chipsRow.addView(cSp2);
        chipsRow.addView(chipOther);

        layout.addView(chipsRow);

        final EditText etName = createLabeledInput(layout, "שם מלא:");
        final EditText etRel = createLabeledInput(layout, "קרבה (אבא / אמא / בן / בת / נכד...):");

        // Set default name and relation to אבא or אמא
        if ("אבא".equals(defaultRelation)) {
            etName.setText("אבא");
            etRel.setText("אבא");
        } else if ("אמא".equals(defaultRelation)) {
            etName.setText("אמא");
            etRel.setText("אמא");
        } else {
            // Default preset is "אבא"
            etName.setText("אבא");
            etRel.setText("אבא");
        }

        chipDad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("אבא");
                etRel.setText("אבא");
            }
        });
        chipMom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("אמא");
                etRel.setText("אמא");
            }
        });
        chipOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText("");
                etRel.setText("");
            }
        });

        final EditText etDay = createLabeledInput(layout, "יום לידה (1-31):");
        etDay.setInputType(InputType.TYPE_CLASS_NUMBER);
        etDay.setText("1");

        final EditText etMonth = createLabeledInput(layout, "חודש לידה (1-12):");
        etMonth.setInputType(InputType.TYPE_CLASS_NUMBER);
        etMonth.setText("1");

        final EditText etYear = createLabeledInput(layout, "שנת לידה לועזית (למשל: 1965):");
        etYear.setInputType(InputType.TYPE_CLASS_NUMBER);
        etYear.setText("1965");

        final EditText etNotes = createLabeledInput(layout, "הערות אישיות:");

        sv.addView(layout);
        builder.setView(sv);

        builder.setPositiveButton("שמור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString().trim();
                String rel = etRel.getText().toString().trim();
                if (name.isEmpty()) {
                    name = "אבא";
                }
                if (rel.isEmpty()) {
                    rel = "אבא";
                }

                int d = parseIntSafe(etDay.getText().toString(), 1);
                int m = parseIntSafe(etMonth.getText().toString(), 1);
                int y = parseIntSafe(etYear.getText().toString(), 1965);

                HebrewDateUtil.HebrewDate hd = HebrewDateUtil.getHebrewDate(y, m, d);
                String hebDateStr = hd.formatHebrewDate();

                dbHelper.insertMember(name, rel, d, m, y, hebDateStr, etNotes.getText().toString().trim());
                Toast.makeText(MainActivity.this, "נוסף בהצלחה!", Toast.LENGTH_SHORT).show();
                switchTab(TAB_FAMILY);
            }
        });
        builder.setNegativeButton("ביטול", null);
        builder.show();
    }

    private void confirmDeleteMember(final DatabaseHelper.Member m) {
        new AlertDialog.Builder(this)
                .setTitle("מחיקה מספר המשפחה")
                .setMessage("האם למחוק את " + m.name + "?")
                .setPositiveButton("כן, מחק", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteMember(m.id);
                        switchTab(TAB_FAMILY);
                    }
                })
                .setNegativeButton("ביטול", null)
                .show();
    }

    private void showBlessingDialog(DatabaseHelper.Member m) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("ברכה ליום הולדת - " + m.name);

        final String blessing = "לכבוד " + m.name + " היקר/ה,\n\n" +
                "מזל טוב לרגל יום הולדתך! שירבו שנותיך בבריאות איתנה, שמחה, נחת, ברכה והצלחה בכל מעשי ידיך.\n" +
                "שתזכה לשנה מבורכת, שנת עליה בתורה, מעשים טובים ואור גדול.\n" +
                "פרק התהילים המלווה אותך לשנה זו: מזמור " + HebrewDateUtil.toHebrewNumber(m.getPersonalTehillim()) + ".\n\n" +
                "באהבה ובהערכה רבה!";

        TextView tv = new TextView(this);
        tv.setText(blessing);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(Color.parseColor("#101D33"));
        tv.setPadding(dp(20), dp(16), dp(20), dp(12));
        b.setView(tv);

        b.setPositiveButton("העתק ברכה", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ברכה", blessing);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "הברכה הועתקה ללוח!", Toast.LENGTH_SHORT).show();
            }
        });

        b.setNeutralButton("שתף", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, blessing);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "שיתוף ברכה"));
            }
        });

        b.setNegativeButton("סגור", null);
        b.show();
    }

    // ========================================================
    // TAB 5: תהילים ותפילה (Tehillim & Prayers)
    // ========================================================
    private void renderTehillimScreen() {
        View prayerNamesCard = createCardView();
        LinearLayout pnInner = (LinearLayout) prayerNamesCard;

        LinearLayout pnHeader = new LinearLayout(this);
        pnHeader.setOrientation(LinearLayout.HORIZONTAL);
        pnHeader.setGravity(Gravity.CENTER_VERTICAL);

        TextView pTitle = createSectionHeader("רשימת שמות לתפילה");
        pTitle.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        pnHeader.addView(pTitle);

        Button addNameBtn = createFocusableButton("+ הוסף שם לתפילה", true);
        addNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPrayerDialog();
            }
        });
        pnHeader.addView(addNameBtn);
        pnInner.addView(pnHeader);

        List<DatabaseHelper.PrayerPerson> prayers = dbHelper.getAllPrayers();
        if (prayers.isEmpty()) {
            TextView emptyTv = new TextView(this);
            emptyTv.setText("אין שמות ברשימת התפילה כעת.");
            emptyTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            emptyTv.setTextColor(Color.GRAY);
            pnInner.addView(emptyTv);
        } else {
            for (final DatabaseHelper.PrayerPerson p : prayers) {
                LinearLayout row = (LinearLayout) createFocusableRow(
                        p.name + " בן/בת " + p.motherName,
                        "סוג תפילה: " + p.type
                );

                Button delBtn = createFocusableButton("הסר", false);
                delBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                delBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbHelper.deletePrayer(p.id);
                        switchTab(TAB_TEHILLIM);
                    }
                });
                row.addView(delBtn);
                pnInner.addView(row);
            }
        }
        mainContentContainer.addView(prayerNamesCard);

        View chaptersCard = createCardView();
        LinearLayout chInner = (LinearLayout) chaptersCard;
        chInner.addView(createSectionHeader("פרקי תהילים נבחרים"));

        for (int i = 0; i < TEHILLIM_TITLES.length; i++) {
            final int chapterIdx = i;
            Button chapterBtn = createFocusableButton(TEHILLIM_TITLES[i], false);
            chapterBtn.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            chapterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTehillimDialog(TEHILLIM_TITLES[chapterIdx], TEHILLIM_TEXTS[chapterIdx]);
                }
            });
            chInner.addView(chapterBtn);

            View spacer = new View(this);
            spacer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(6)));
            chInner.addView(spacer);
        }

        mainContentContainer.addView(chaptersCard);
    }

    private void showAddPrayerDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("הוספת שם לתפילה");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(20), dp(10), dp(20), dp(10));

        final EditText etName = createLabeledInput(layout, "שם האדם:");
        final EditText etMother = createLabeledInput(layout, "שם האם:");
        final EditText etType = createLabeledInput(layout, "מטרה (לרפואה שלמה / לעילוי נשמת):");
        etType.setText("לרפואה שלמה");

        b.setView(layout);
        b.setPositiveButton("שמור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString().trim();
                String mother = etMother.getText().toString().trim();
                String type = etType.getText().toString().trim();
                if (!name.isEmpty() && !mother.isEmpty()) {
                    dbHelper.insertPrayer(name, mother, type);
                    switchTab(TAB_TEHILLIM);
                }
            }
        });
        b.setNegativeButton("ביטול", null);
        b.show();
    }

    private void showTehillimDialog(String title, String text) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(title);

        ScrollView sv = new ScrollView(this);
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        tv.setLineSpacing(dp(6), 1.25f);
        tv.setTextColor(Color.parseColor("#101D33"));
        tv.setTypeface(Typeface.SERIF);
        tv.setPadding(dp(20), dp(16), dp(20), dp(20));
        sv.addView(tv);

        b.setView(sv);
        b.setPositiveButton("סגור", null);
        b.show();
    }

    // ========================================================
    // TAB 6: הגדרות (Settings)
    // ========================================================
    private void renderSettingsScreen() {
        View card = createCardView();
        LinearLayout inner = (LinearLayout) card;
        inner.addView(createSectionHeader("הגדרות תזכורות והתראות"));

        TextView desc = new TextView(this);
        desc.setText("בחר מתי לקבל תזכורת עבור יארצייטים וימי הולדת:");
        desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        desc.setTextColor(Color.parseColor("#333333"));
        desc.setPadding(0, 0, 0, dp(12));
        inner.addView(desc);

        String[] alertOptions = {
                "✓ תזכורת שבוע לפני התאריך העברי",
                "✓ תזכורת יום לפני המועד (בערב להדלקת נר)",
                "✓ תזכורת בבוקר יום היארצייט (לקדיש)",
                "✓ תזכורת שבועית לפרק תהילים אישי"
        };

        for (String opt : alertOptions) {
            Button toggleBtn = createFocusableButton(opt, false);
            toggleBtn.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            toggleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    if (b.getText().toString().startsWith("✓")) {
                        b.setText(b.getText().toString().replace("✓", "✗ (כבוי)"));
                        b.setTextColor(Color.parseColor("#CCCCCC"));
                    } else {
                        b.setText(b.getText().toString().replace("✗ (כבוי)", "✓"));
                        b.setTextColor(Color.WHITE);
                    }
                    Toast.makeText(MainActivity.this, "ההגדרה עודכנה", Toast.LENGTH_SHORT).show();
                }
            });
            inner.addView(toggleBtn);

            View sp = new View(this);
            sp.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dp(8)));
            inner.addView(sp);
        }

        mainContentContainer.addView(card);

        View aboutCard = createCardView();
        LinearLayout aboutInner = (LinearLayout) aboutCard;
        aboutInner.addView(createSectionHeader("אודות 'זיכרון וברכה'"));

        TextView aboutTv = new TextView(this);
        aboutTv.setText("אפליקציית 'זיכרון וברכה' מותאמת באופן מלא לכל סוגי המכשירים:\n\n• שליטה מלאה גם במסך מגע (טאץ') וגם במקשי ניווט (D-Pad).\n• הגדרת יארצייטים לפי לוח השנה העברי במדויק (יום וחודש עברי).\n• שמות וקשרים בברירת מחדל לאבא ואמא לחיסכון בזמן הקלדה.\n• חישוב פרק תהילים אישי לכל גיל ושיתוף ברכות מרגשות.");
        aboutTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        aboutTv.setTextColor(Color.parseColor("#4B5563"));
        aboutTv.setLineSpacing(dp(4), 1.25f);
        aboutInner.addView(aboutTv);

        mainContentContainer.addView(aboutCard);
    }

    // ========================================================
    // UI Helpers (Both Touch & Keypad optimized)
    // ========================================================
    private View createCardView() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.card_selector);
        card.setFocusable(true);
        card.setFocusableInTouchMode(false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(0, 0, 0, dp(14));
        card.setLayoutParams(lp);
        return card;
    }

    private TextView createSectionHeader(String title) {
        TextView tv = new TextView(this);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextColor(Color.parseColor("#101D33"));
        tv.setPadding(0, 0, 0, dp(8));
        return tv;
    }

    private Button createFocusableButton(String text, boolean isGold) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        b.setTypeface(Typeface.DEFAULT_BOLD);
        b.setTextColor(isGold ? Color.parseColor("#101D33") : Color.WHITE);
        b.setBackgroundResource(isGold ? R.drawable.btn_gold_selector : R.drawable.btn_selector);
        b.setMinHeight(dp(48)); // Standard touch target size
        b.setFocusable(true);
        b.setFocusableInTouchMode(false); // Responds on 1st touch
        return b;
    }

    private Button createChipButton(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        b.setTypeface(Typeface.DEFAULT_BOLD);
        b.setTextColor(Color.parseColor("#101D33"));
        b.setBackgroundResource(R.drawable.chip_selector);
        b.setMinHeight(dp(40));
        b.setFocusable(true);
        b.setFocusableInTouchMode(false);
        return b;
    }

    private View createFocusableRow(String mainText, String subText) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setBackgroundResource(R.drawable.card_selector);
        row.setFocusable(true);
        row.setFocusableInTouchMode(false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp(4), 0, dp(8));
        row.setLayoutParams(lp);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(mainText);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        tvTitle.setTextColor(Color.parseColor("#101D33"));
        row.addView(tvTitle);

        if (subText != null && !subText.isEmpty()) {
            TextView tvSub = new TextView(this);
            tvSub.setText(subText);
            tvSub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tvSub.setTextColor(Color.parseColor("#5A6772"));
            tvSub.setPadding(0, dp(3), 0, dp(4));
            row.addView(tvSub);
        }

        return row;
    }

    private EditText createLabeledInput(LinearLayout parent, String label) {
        TextView tv = new TextView(this);
        tv.setText(label);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tv.setTextColor(Color.parseColor("#101D33"));
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setPadding(0, dp(6), 0, dp(2));
        parent.addView(tv);

        EditText et = new EditText(this);
        et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        et.setBackgroundResource(R.drawable.input_selector);
        et.setMinHeight(dp(48));
        et.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parent.addView(et);
        return et;
    }

    private int parseIntSafe(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics()
        );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentTab != TAB_HOME) {
                switchTab(TAB_HOME);
                tabButtons[TAB_HOME].requestFocus();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
