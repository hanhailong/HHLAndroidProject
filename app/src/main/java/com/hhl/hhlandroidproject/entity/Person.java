package com.hhl.hhlandroidproject.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hailonghan on 15/7/10.
 */
public class Person implements Parcelable{

    private String username;
    private String password;
    private int age;
    private boolean isMsgCome;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMsgCome() {
        return isMsgCome;
    }

    public void setIsMsgCome(boolean isMsgCome) {
        this.isMsgCome = isMsgCome;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeInt(this.age);
        dest.writeByte(isMsgCome ? (byte) 1 : (byte) 0);
    }

    public Person() {
    }

    protected Person(Parcel in) {
        this.username = in.readString();
        this.password = in.readString();
        this.age = in.readInt();
        this.isMsgCome = in.readByte() != 0;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
