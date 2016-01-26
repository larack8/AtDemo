package com.zxjdev.atdemo;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public static final String TAG = User.class.getSimpleName();

    private String userId;
    private String nickname;

    public User() {

    }

    public User(String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return nickname;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.nickname);
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.nickname = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {return new User(source);}

        public User[] newArray(int size) {return new User[size];}
    };
}
