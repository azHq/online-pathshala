package com.example.onlinepathshala.Teacher;

import android.content.Context;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public abstract class File_Reader {

    public abstract ArrayList<String[]>  readfile(InputStream path, Context context);
}
