package com.example.andre.runnerartist.mapper;

import android.database.Cursor;

import java.util.List;

interface Mapper<T> {
    T map(Cursor c);
    List<T> mapList(Cursor c);
}
