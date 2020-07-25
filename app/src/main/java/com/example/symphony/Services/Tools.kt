package com.example.symphony.Services

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.*

object Tools {
    const val ENDPOINT = "https://firsttry-87e5f.firebaseio.com"
    fun encodeString(string: String): String {
        return string.replace(".", ",")
    }

    fun decodeString(string: String): String {
        return string.replace(",", ".")
    }


    fun toProperName(s: String): String {
        return if (s.length <= 1) s.toUpperCase() else s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase()
    }

    /*
    public static int createUniqueIdPerUser(String userEmail) {
        String email = userEmail.split("@")[0].toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
        final Map<Character, Integer> map;
        map = new HashMap<>();
        map.put('a', 1);
        map.put('b', 2);
        map.put('c', 3);
        map.put('d', 4);
        map.put('e', 5);
        map.put('f', 6);
        map.put('g', 7);
        map.put('h', 8);
        map.put('i', 9);
        map.put('j', 10);
        map.put('k', 11);
        map.put('l', 12);
        map.put('m', 13);
        map.put('n', 14);
        map.put('o', 15);
        map.put('p', 16);
        map.put('q', 17);
        map.put('r', 18);
        map.put('s', 19);
        map.put('t', 20);
        map.put('u', 21);
        map.put('v', 22);
        map.put('w', 23);
        map.put('x', 24);
        map.put('y', 25);
        map.put('z', 26);
        String intEmail = "";

        for (char c : email.toCharArray()) {
            int val = 0;
            try {
                val = map.get(c);
            } catch (Exception e) {

            }
            intEmail += val;
        }

        if (intEmail.length() > 9) {
            intEmail = intEmail.substring(0, 9);
        }

        return Integer.parseInt(intEmail);

    }

    public static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-zA-Z0-9._-]+";
        return email.matches(emailPattern);
    }

     */
    fun toCharacterMonth(month: Int): String {
        return if (month == 1) "Jan" else if (month == 2) "Feb" else if (month == 3) "Mar" else if (month == 4) "Apr" else if (month == 5) "May" else if (month == 6) "Jun" else if (month == 7) "Jul" else if (month == 8) "Aug" else if (month == 9) "Sep" else if (month == 10) "Oct" else if (month == 11) "Nov" else "Dec"
    }

    fun lastSeenProper(lastSeenDate: String): String {
        val dateFormat = SimpleDateFormat("dd MM yy hh:mm a")
        val currentDate = Date()
        val cuurentDateString = dateFormat.format(currentDate)
        var nw: Date? = null
        var seen: Date? = null
        return try {
            nw = dateFormat.parse(cuurentDateString)
            seen = dateFormat.parse(lastSeenDate)
            val diff = nw.time - seen.time
            val diffDays = diff / (24 * 60 * 60 * 1000)
            val diffHours = diff / (60 * 60 * 1000) % 24
            val diffMinutes = diff / (60 * 1000) % 60
            if (diffDays > 0) {
                val originalDate = lastSeenDate.split(" ").toTypedArray()
                "Last seen " + originalDate[0] + " " + toCharacterMonth(originalDate[1].toInt()) + " " + originalDate[2]
            } else if (diffHours > 0) "Last seen $diffHours hours ago" else if (diffMinutes > 0) {
                if (diffMinutes <= 1) {
                    "Last seen 1 minute ago"
                } else {
                    "Last seen $diffMinutes minutes ago"
                }
            } else "Last seen a moment ago"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun messageSentDateProper(sentDate: String): String {
        var properDate = ""
        val cal = Calendar.getInstance()
        val todayDate = Date()
        cal.time = todayDate
        val date = sentDate.split(" ").toTypedArray()
        val todayMonth = cal[Calendar.MONTH] + 1
        val todayDay = cal[Calendar.DAY_OF_MONTH]
        properDate = if (todayMonth == date[1].toInt() && todayDay == date[0].toInt()) {
            "Today" + " " + date[3] + " " + date[4]
            // 06 11 17 12:28 AM
        } else if (todayMonth == date[1].toInt() && todayDay - 1 == date[0].toInt()) {
            "Yesterday" + " " + date[3] + " " + date[4]
        } else {
            date[0] + " " + toCharacterMonth(date[1].toInt()) + " " + date[2] + " " + date[3] + " " + date[4]
        }
        return properDate
    }

    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}