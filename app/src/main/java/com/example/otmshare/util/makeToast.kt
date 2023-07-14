package com.example.otmshare.util

import android.view.View
import android.widget.Toast

fun makeToast(str : String, view: View)
{
    Toast.makeText(view.context, str, Toast.LENGTH_SHORT).show()
}