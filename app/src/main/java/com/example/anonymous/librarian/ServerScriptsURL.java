package com.example.anonymous.librarian;

import android.content.Context;
<<<<<<< HEAD
=======
import android.content.SharedPreferences;
>>>>>>> 75315769ca91dae1157515661a175d80777a4867

/**
 * Created by ANONYMOUS on 15-Dec-17.
 */

public class ServerScriptsURL {

    public Context context;

    public ServerScriptsURL(Context context) {
        this.context = context;
    }

    public SharedPreferences sharedPreference = context.getSharedPreferences(context.getString(R.string.URL_PREFERENCE), context.MODE_PRIVATE);
    public SharedPreferences.Editor editor = sharedPreference.edit();

    String center = sharedPreference.getString(context.getString(R.string.CENTER), "");

    // private final String  = "http://www.fardeenpanjwani.com/librarian/.php";
    private final String ADD_BOOK = "http://www.fardeenpanjwani.com/librarian/add_book.php";
    private final String ADD_SUBSCRIBER = "http://www.fardeenpanjwani.com/librarian/add_subscriber.php";
    private final String ADD_TOY = "http://www.fardeenpanjwani.com/librarian/add_toy.php";
    private final String CHECK_PROFILE_PHOTO = "http://www.fardeenpanjwani.com/librarian/check_profile_photo.php";
    private final String DELETE_BOOK = "http://www.fardeenpanjwani.com/librarian/delete_book.php";
    private final String DELETE_SUBSCRIBER = "http://www.fardeenpanjwani.com/librarian/delete_subscriber.php";
    private final String DELETE_TOY = "http://www.fardeenpanjwani.com/librarian/delete_toy.php";
    private final String GET_BOOK_DETAILS = "http://www.fardeenpanjwani.com/librarian/get_book_details.php";
    private final String GET_INDIVIDUAL_ANALYSIS = "http://www.fardeenpanjwani.com/librarian/get_individual_analysis.php";
    private final String GET_INDIVIDUAL_SUBSCRIBER_DETAILS = "http://www.fardeenpanjwani.com/librarian/get_individual_subscriber_details.php";
    private final String GET_ISSUED_BOOKS = "http://www.fardeenpanjwani.com/librarian/get_issued_books.php";
    private final String GET_SINGLE_ISSUED_BOOK_DETAILS = "http://www.fardeenpanjwani.com/librarian/get_single_issued_book_details.php";
    private final String GET_SUBSCRIBERS_DETAILS = "http://www.fardeenpanjwani.com/librarian/get_subscribers_details.php";
    private final String GET_SUBSCRIBER_ANALYSIS = "http://www.fardeenpanjwani.com/librarian/get_subscriber_analysis.php";
    private final String GET_TEMP_BOOK_DETAILS = "http://www.fardeenpanjwani.com/librarian/get_temp_book_details.php";
    private final String GET_TEMP_TOY_DETAILS = "http://www.fardeenpanjwani.com/librarian/get_temp_toy_details.php";
    private final String GET_TOTAL_ANALYSIS = "http://www.fardeenpanjwani.com/librarian/get_total_analysis.php";
    private final String GET_TOY_DETAILS = "http://www.fardeenpanjwani.com/librarian/get_toy_details.php";
    private final String INSERT_TEMP_BOOK_DETAILS = "http://www.fardeenpanjwani.com/librarian/insert_temp_book_details.php";
    private final String INSERT_TEMP_TOY_DETAILS = "http://www.fardeenpanjwani.com/librarian/insert_temp_toy_details.php";
    private final String ISSUE_BOOK = "http://www.fardeenpanjwani.com/librarian/issue_book.php";
    private final String ISSUE_TOY = "http://www.fardeenpanjwani.com/librarian/issue_toy.php";
    private final String LAST_DAY_PROTOCOL = "http://www.fardeenpanjwani.com/librarian/last_day_protocol.php";
    private final String RETURN_BOOK = "http://www.fardeenpanjwani.com/librarian/return_book.php";
    private final String RETURN_TOY = "http://www.fardeenpanjwani.com/librarian/return_toy.php";
    private final String UPDATE_SUBSCRIBER_DETAILS = "http://www.fardeenpanjwani.com/librarian/update_subscriber_details.php";
    private final String UPLOAD_SUBSCRIBER_PROFILE_IMAGE_ENHANCED = "http://www.fardeenpanjwani.com/librarian/upload_subscriber_profile_image_enhanced.php";
    private final String UPLOAD_SUBSCRIBER_PROFILE_PHOTO = "http://www.fardeenpanjwani.com/librarian/upload_subscriber_profile_photo.php";
    private final String VIEW_CURRENTLY_ISSUED_TOYS = "http://www.fardeenpanjwani.com/librarian/view_currently_issued_toys.php";
    private final String CANCEL_ISSUE_BOOK_PROTOCOL = "http://www.fardeenpanjwani.com/librarian/cancel_issue_protocol/cancel_issue_book_protocol.php";
    private final String CANCEL_ISSUE_TOY_PROTOCOL = "http://www.fardeenpanjwani.com/librarian/cancel_issue_protocol/cancel_issue_toy_protocol.php";
    private final String GET_ISSUED_BOOK_TO_ID = "http://www.fardeenpanjwani.com/librarian/get_issued_book_to_id.php";
    private final String GET_LAST_UPDATED_IDS = "http://fardeenpanjwani.com/librarian/ids_handler/get_last_updated_ids.php";
    private final String UPDATE_EXISTING_IDS = "http://fardeenpanjwani.com/librarian/ids_handler/update_existing_ids.php";
    private final String GET_JOINT_ACCOUNT = "http://www.fardeenpanjwani.com/librarian/get_joint_account.php";
    private final String UPDATE_JOINT_ACCOUNT = "http://www.fardeenpanjwani.com/librarian/update_joint_account.php";

    private Context context;

<<<<<<< HEAD
    public ServerScriptsURL(Context context) {
        this.context = context;
    }

    public ServerScriptsURL() {
    }
=======
    public String ADD_BOOK() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.ADD_BOOK), "");
>>>>>>> 75315769ca91dae1157515661a175d80777a4867

        } else {

            return ADD_BOOK;

        }
    }

    public String ADD_SUBSCRIBER() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.ADD_SUBSCRIBER), "");

        } else {

            return ADD_SUBSCRIBER;

        }
    }

    public String ADD_TOY() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.ADD_TOY), "");

        } else {

            return ADD_TOY;

        }
    }

    public String CHECK_PROFILE_PHOTO() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.CHECK_PROFILE_PHOTO), "");

        } else {

            return CHECK_PROFILE_PHOTO;

        }
    }

    public String DELETE_BOOK() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.DELETE_BOOK), "");

        } else {

            return DELETE_BOOK;

        }
    }

    public String DELETE_SUBSCRIBER() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.DELETE_SUBSCRIBER), "");

        } else {

            return DELETE_SUBSCRIBER;

        }
    }

    public String DELETE_TOY() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.DELETE_TOY), "");

        } else {

            return DELETE_TOY;

        }
    }

    public String GET_BOOK_DETAILS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_BOOK_DETAILS), "");

        } else {

            return GET_BOOK_DETAILS;

        }
    }

    public String GET_INDIVIDUAL_ANALYSIS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_INDIVIDUAL_ANALYSIS), "");

        } else {

            return GET_INDIVIDUAL_ANALYSIS;

        }
    }

    public String GET_INDIVIDUAL_SUBSCRIBER_DETAILS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_INDIVIDUAL_SUBSCRIBER_DETAILS), "");

        } else {

            return GET_INDIVIDUAL_SUBSCRIBER_DETAILS;

        }
    }

    public String GET_ISSUED_BOOKS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_ISSUED_BOOKS), "");

        } else {

            return GET_ISSUED_BOOKS;

        }
    }

    public String GET_SINGLE_ISSUED_BOOK_DETAILS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_SINGLE_ISSUED_BOOK_DETAILS), "");

        } else {

            return GET_SINGLE_ISSUED_BOOK_DETAILS;

        }
    }

    public String GET_SUBSCRIBERS_DETAILS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_SUBSCRIBERS_DETAILS), "");

        } else {

            return GET_SUBSCRIBERS_DETAILS;

        }
    }

    public String GET_SUBSCRIBER_ANALYSIS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_SUBSCRIBER_ANALYSIS), "");

        } else {

            return GET_SUBSCRIBER_ANALYSIS;

        }
    }

    public String GET_TEMP_BOOK_DETAILS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_TEMP_BOOK_DETAILS), "");

        } else {

            return GET_TEMP_BOOK_DETAILS;

        }
    }

    public String GET_TEMP_TOY_DETAILS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_TEMP_TOY_DETAILS), "");

        } else {

            return GET_TEMP_TOY_DETAILS;

        }
    }

    public String GET_TOTAL_ANALYSIS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_TOTAL_ANALYSIS), "");

        } else {

            return GET_TOTAL_ANALYSIS;

        }
    }

    public String GET_TOY_DETAILS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_TOY_DETAILS), "");

        } else {

            return GET_TOY_DETAILS;

        }
    }

    public String INSERT_TEMP_BOOK_DETAILS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.INSERT_TEMP_BOOK_DETAILS), "");

        } else {

            return INSERT_TEMP_BOOK_DETAILS;

        }
    }

    public String INSERT_TEMP_TOY_DETAILS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.INSERT_TEMP_TOY_DETAILS), "");

        } else {

            return INSERT_TEMP_TOY_DETAILS;

        }
    }

    public String ISSUE_BOOK() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.ISSUE_BOOK), "");

        } else {

            return ISSUE_BOOK;

        }
    }

    public String ISSUE_TOY() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.ISSUE_TOY), "");

        } else {

            return ISSUE_TOY;

        }
    }

    public String LAST_DAY_PROTOCOL() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.LAST_DAY_PROTOCOL), "");

        } else {

            return LAST_DAY_PROTOCOL;

        }
    }

    public String RETURN_BOOK() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.RETURN_BOOK), "");

        } else {

            return RETURN_BOOK;

        }
    }

    public String RETURN_TOY() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.RETURN_TOY), "");

        } else {

            return RETURN_TOY;

        }
    }

    public String UPDATE_SUBSCRIBER_DETAILS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.UPDATE_SUBSCRIBER_DETAILS), "");

        } else {

            return UPDATE_SUBSCRIBER_DETAILS;

        }
    }

    public String UPLOAD_SUBSCRIBER_PROFILE_IMAGE_ENHANCED() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.UPLOAD_SUBSCRIBER_PROFILE_IMAGE_ENHANCED), "");

        } else {

            return UPLOAD_SUBSCRIBER_PROFILE_IMAGE_ENHANCED;

        }
    }

    public String UPLOAD_SUBSCRIBER_PROFILE_PHOTO() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.UPLOAD_SUBSCRIBER_PROFILE_PHOTO), "");

        } else {

            return UPLOAD_SUBSCRIBER_PROFILE_PHOTO;

        }
    }

    public String VIEW_CURRENTLY_ISSUED_TOYS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.VIEW_CURRENTLY_ISSUED_TOYS), "");

        } else {

            return VIEW_CURRENTLY_ISSUED_TOYS;

        }
    }

    public String CANCEL_ISSUE_BOOK_PROTOCOL() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.CANCEL_ISSUE_BOOK_PROTOCOL), "");

        } else {

            return CANCEL_ISSUE_BOOK_PROTOCOL;

        }
    }

    public String CANCEL_ISSUE_TOY_PROTOCOL() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.CANCEL_ISSUE_TOY_PROTOCOL), "");

        } else {

            return CANCEL_ISSUE_TOY_PROTOCOL;

        }
    }

    public String GET_ISSUED_BOOK_TO_ID() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_ISSUED_BOOK_TO_ID), "");

        } else {

            return GET_ISSUED_BOOK_TO_ID;

        }
    }

    public String GET_LAST_UPDATED_IDS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_LAST_UPDATED_IDS), "");

        } else {

            return GET_LAST_UPDATED_IDS;

        }
    }

    public String UPDATE_EXISTING_IDS() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.UPDATE_EXISTING_IDS), "");

        } else {

            return UPDATE_EXISTING_IDS;

        }
    }

    public String GET_JOINT_ACCOUNT() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.GET_JOINT_ACCOUNT), "");

        } else {

            return GET_JOINT_ACCOUNT;

        }
    }

    public String UPDATE_JOINT_ACCOUNT() {
        if(center.toLowerCase().contains("kompally")){

            return sharedPreference.getString(context.getString(R.string.UPDATE_JOINT_ACCOUNT), "");

        } else {

            return UPDATE_JOINT_ACCOUNT;

        }
    }
}
