package com.mpoc.mpoc_project.service

import org.springframework.stereotype.Service

import java.text.DateFormat
import java.text.SimpleDateFormat

@Service
class DateFormatterID {

    static String formatToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy")

        return DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH).format(formatter.parse(date)) }
}
