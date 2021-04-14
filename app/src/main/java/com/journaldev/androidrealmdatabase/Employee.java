package com.journaldev.androidrealmdatabase;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Employee extends RealmObject {
    public static final String PROPERTY_NAME = "text_productName";
    public static final String PROPERTY_DESC = "text_desc";
    public static final String PROPERTY_CATEGORY = "text_category";
    public static final String PROPERTY_PRICE = "text_price";
    public static final String PROPERTY_MARKETPRICE = "text_marketPrice";


    @PrimaryKey
    @Required
    public String text_productName;
    public String text_desc,text_category,text_price,text_marketPrice;


}
