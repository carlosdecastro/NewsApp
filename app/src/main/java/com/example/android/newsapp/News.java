package com.example.android.newsapp;

/**
 * An {@link News} object contains information related to a single news.
 */

public class News {

    /**
     * Title of the news
     */
    private String mTitle;

    /**
     * Section of the news
     */
    private String mSection;

    /** Author of the news */
    private String mAuthor;

    /** Date of the news */
    private String mDate;

    /** Webside URL of the news */
    private String mUrl;

    /**
     * Constructs a new {@link News} object.
     *
     * @param title is the title of the news
     * @param section is the section where the news belongs
     * @param author is the autor of the news
     * @param date is the date ofpublication
     * @param url is the website URL to find more details about the news
     */
    public News(String title, String section, String author, String date, String url) {

        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}