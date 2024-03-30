package com.apck.proyectfx.utilities;

import java.util.HashMap;

public class Constants {

    public static  final String KEY_COLLECTIONS_USERS = "users";
    public static  final String KEY_USER_NAME = "user_name";
    public static  final String KEY_USER_MAIL = "email";
    public static  final String KEY_USER_PASSWORD = "password";
    public static  final String KEY_USER_ID = "user_id";
    public static  final String KEY_FCM_TOKEN = "fcm_token";

    public static  final String KEY_USER_ADMIN = "Administrador";
    public static  final String KEY_USER_USER = "Usuario";

    public  static  final  String KEY_PREFERENCE_NAME = "videocallpreference";
    public  static  final  String KEY_IS_SIGNEDIN = "issignedin";

    public  static  final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public  static  final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public  static  final String REMOTE_MSG_TYPE = "type";
    public  static  final String REMOTE_MSG_INVITATION = "invitation";
    public  static  final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public  static  final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public  static  final String REMOTE_MSG_DATA = "data";
    public  static  final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final  String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";

    public  static  final String REMOTE_MSG_INVITATION_ACCEPTED = "accepted";
    public  static  final String REMOTE_MSG_INVITATION_REJECTED = "rejected";
    public  static  final String REMOTE_MSG_INVITATION_CANCELLED = "rejected";
    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";

    public static final String KEY_DATABASE_EVENT_PHAT = "events";
    public static final String KEY_EVENT_DAY = "event_day";
    public static final String KEY_EVENT_DAY_NUMBER = "event_day_number";
    public static final String KEY_EVENT_MONTH = "event_month";
    public static final String KEY_EVENT_NAME = "event_name";
    public static final String KEY_EVENT_DESC = "event_desc";

    public static HashMap<String, String> getRemoteMessageHeader(){
        HashMap<String, String> headers = new HashMap<>();
        headers.put(
                Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAAPyHMNBM:APA91bGomBNXpCamLibx3372ASPj9gqbOuARyc2ZgfJetj6eShbSQ6DbTr9Ix1SXMBWc6pT_DNrV9qMtZrWeTvuan1ei3fU3G3RpZO8rxrRbsayLvDjsIf6Q49YW2iY1N5--WEdWRo-L"
        );
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }

}
