package com.video.aashi.school.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CreditCardFormattingTextWatcher implements TextWatcher
{
    private EditText etCard;
    boolean isDelete;

    public CreditCardFormattingTextWatcher(EditText etcard) {
        this.etCard=etcard;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(before==0)
            isDelete=false;
        else
            isDelete=true;
    }

    @Override
    public void afterTextChanged(Editable s) {
        String source = s.toString();
        int length=source.length();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(source);

        if(length>0 && length%5==0)
        {
            if(isDelete)
                stringBuilder.deleteCharAt(length-1);
            else
                stringBuilder.insert(length-1," ");
                
            etCard.setText(stringBuilder);
            etCard.setSelection(etCard.getText().length());

        }
    }
}