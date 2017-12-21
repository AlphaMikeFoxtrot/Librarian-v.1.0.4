package com.example.anonymous.librarian.ServerProtocols;

import com.example.anonymous.librarian.SubscriberAnalysis;
import com.example.anonymous.librarian.Subscribers;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * THIS CLASS USES ASYNC TASK TO
 * RECEIVE AND POST SUBSCRIBERS
 * INFORMATION TO THE SERVER
 *
 * @author ANONYMOUS
 * @version 1.0.0
 */
public class SubscriberProtocol {

    public ArrayList<Subscribers> subscribers;
    Hashtable subscriberDetailDetail;
    ArrayList<SubscriberAnalysis> subscriberAnalysisArrayList;

    /*
    * LIST OF PROTOCOLS:
    * ------------------------
    * Get Subscribers
    * Get Subscriber Analysis
    * Get Subscriber Details
    * Update Subscriber
    * Delete Subscriber
    * Add Subscriber
    * */

    /**
     * This method gets the list of subscribers from server
     *
     * @return ArrayList : list of subscribers
     */
    public static ArrayList<Subscribers> getSubscribers(){

        return null;

    }

    /**
     * This method gets individual subscriber details from the server
     *
     * @param subscriberId : java.lang.String
     * @return Hashtable : return key value pair for details
     */
    public static Hashtable getSubscriberDetails(String subscriberId){

        return null;

    }

    /**
     * This method gets the subscriber analysis
     *
     * @param subscriberId : java.lang.String
     * @return ArrayList : returns list with the analysis
     */
    public static ArrayList<SubscriberAnalysis> getSubscriberAnalysis(String subscriberId){

        return null;

    }

    /**
     * This method deletes the subscriber from the server
     *
     * @param subscriberId : java.lang.String
     * @return java.lang.String : returns success or fail
     */
    public static String deleteSubscriber(String subscriberId){

        return null;

    }

    /**
     * This method updates the subscriber
     * with the values passed in as parameters
     *
     * @param subscriberId : java.lang.String
     * @param enrolledFor : java.lang.String
     * @param enrolledOn : java.lang.String
     * @param enrollmentType : java.lang.String
     * @param phone : java.lang.String
     * @param dob : java.lang.String
     * @return java.lang.String : returns success or fail
     */
    public static String updateSubscriber(String subscriberId, String enrolledFor, String enrolledOn, String enrollmentType, String phone, String dob){

        return null;

    }

}
