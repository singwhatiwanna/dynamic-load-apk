/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ryg.dynamicload.sample.mainplugin;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 自定义类,实现Parcelable接口,用于DL插件的Activity之间传递Parcelable数据示例.
 * 
 * @author mrsimple
 */
public class Person implements Parcelable {

    String mName;
    int mAge;

    public Person(String name, int age) {
        mName = name;
        mAge = age;
    }

    protected Person(Parcel in) {
        mName = in.readString();
        mAge = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mAge);
    }

    public static final Parcelable.Creator<Person> CREATOR = new Creator<Person>() {

        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }

    };

    @Override
    public String toString() {
        return "Person [mName=" + mName + ", mAge=" + mAge + "]";
    }

}
