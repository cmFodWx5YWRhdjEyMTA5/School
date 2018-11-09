package com.video.aashi.school.fragments.payments;


import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.video.aashi.school.R;
import com.video.aashi.school.adapters.CreditCardUtils;
import com.video.aashi.school.adapters.MyEditText;

import java.util.Calendar;

import static com.video.aashi.school.adapters.CreditCardUtils.EXTRA_CARD_EXPIRY;
import static com.video.aashi.school.adapters.CreditCardUtils.EXTRA_CARD_NUMBER;
import static com.video.aashi.school.adapters.CreditCardUtils.EXTRA_VALIDATE_EXPIRY_DATE;

public class Card extends Fragment {
    PopupWindow changeSortPopUp;
    FloatingActionButton add;
    Point p;
    EditText mExpiryDate,cardnumber,cvv;
    String number = "";
    private boolean mValidateCard = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_card, container, false);
        add = (FloatingActionButton) view.findViewById(R.id.addcard);
        setHasOptionsMenu(true);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout viewGroup = (LinearLayout) view.findViewById(R.id.popup);
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = layoutInflater.inflate(R.layout.addcard, viewGroup, false);
                changeSortPopUp = new PopupWindow(getActivity());
                changeSortPopUp.setContentView(layout);
                changeSortPopUp.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                changeSortPopUp.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                changeSortPopUp.setFocusable(true);
                changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
                changeSortPopUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
                CardView close = (CardView) layout.findViewById(R.id.close);
               mExpiryDate  = (MyEditText) layout.findViewById(R.id.selectyear);
               cardnumber = (EditText)layout.findViewById(R.id.cardnumber);
               cvv =(EditText)layout.findViewById(R.id.cvv);
                if (getArguments() != null && getArguments().containsKey(EXTRA_CARD_NUMBER))
                {

                    number = getArguments().getString(EXTRA_CARD_NUMBER);

                }
                cardnumber.getText().clear();
                cardnumber.setText(number);
                cardnumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                       // cardnumber.getText().clear();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {

                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        int cursorPosition = cardnumber.getSelectionEnd();
                        int previousLength = cardnumber.getText().length();
                        String cardNumber = CreditCardUtils.handleCardNumber(s.toString());
                        int modifiedLength = cardNumber.length();
                        cardnumber.removeTextChangedListener(this);
                        cardnumber.setText(cardNumber);
                        String rawCardNumber = cardNumber.replace(CreditCardUtils.SPACE_SEPERATOR, "");
                        CreditCardUtils.CardType cardType = CreditCardUtils.selectCardType(rawCardNumber);
                        int maxLengthWithSpaces = ((cardType == CreditCardUtils.CardType.AMEX_CARD) ? CreditCardUtils. CARD_NUMBER_FORMAT_AMEX :CreditCardUtils. CARD_NUMBER_FORMAT).length();
                        cardnumber.setSelection(cardNumber.length() > maxLengthWithSpaces ? maxLengthWithSpaces : cardNumber.length());
                        cardnumber.addTextChangedListener(this);
                        if (modifiedLength <= previousLength && cursorPosition < modifiedLength) {
                            cardnumber.setSelection(cursorPosition);
                        }

                        if (rawCardNumber.length() == CreditCardUtils.selectCardLength(cardType)) {
                            cardnumber.setEnabled(false);
                        }
                    }
                });
                String expiry = "";

                Bundle args = getArguments();

                if(args != null) {


                    if(args.containsKey(EXTRA_CARD_EXPIRY)) {
                        expiry = getArguments().getString(EXTRA_CARD_EXPIRY);
                    }

                    mValidateCard = args.getBoolean(EXTRA_VALIDATE_EXPIRY_DATE, true);
                }

                if(expiry == null) {
                    expiry = "";
                }
                mExpiryDate.setText(expiry);
                mExpiryDate.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                    @Override
                    public void afterTextChanged(Editable s)
                    {
                        String text = s.toString().replace(CreditCardUtils.SLASH_SEPERATOR, "");
                        String month, year="";
                        if(text.length() >= 2) {
                            month = text.substring(0, 2);
                            if(text.length() > 2) {
                                year = text.substring(2);
                            }
                                if (mValidateCard) {
                                    int mm = Integer.parseInt(month);
                                    if (mm <= 0 || mm >= 13)
                                    {
                                            mExpiryDate.setError(getString(R.string.error_invalid_month),null);
                                            return;
                                    }
                                        if (text.length() >= 4) {
                                            int yy = Integer.parseInt(year);
                                            final Calendar calendar = Calendar.getInstance();
                                            int currentYear = calendar.get(Calendar.YEAR);
                                            int currentMonth = calendar.get(Calendar.MONTH) + 1;
                                            int millenium = (currentYear / 1000) * 1000;
                                            if (yy + millenium < currentYear) {
                                                mExpiryDate.setError(getString(R.string.error_card_expired));
                                                return;
                                            } else if (yy + millenium == currentYear && mm < currentMonth) {
                                                mExpiryDate.setError(getString(R.string.error_card_expired));
                                                return;
                                            }
                                        }
                                    }
                               }
                        else
                        {
                            month = text;
                        }
                        int previousLength = mExpiryDate.getText().length();
                        int cursorPosition = mExpiryDate.getSelectionEnd();
                        text = CreditCardUtils.handleExpiration(month,year);
                        mExpiryDate.removeTextChangedListener(this);
                        mExpiryDate.setText(text);
                        mExpiryDate.setSelection(text.length());
                        mExpiryDate.addTextChangedListener(this);
                        int modifiedLength = text.length();
                        if(modifiedLength <= previousLength && cursorPosition < modifiedLength) {
                            mExpiryDate.setSelection(cursorPosition);
                        }
                        if(text.length() == 5) {
                            cvv.setFocusable(true);
                        }
                    }
                });
                if (number == null) {
                    number = "";
                }
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeSortPopUp.dismiss();
                    }
                }
              );
            }
          }
        );

        return view;
    }
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putBoolean(EXTRA_VALIDATE_EXPIRY_DATE, mValidateCard);
        super.onSaveInstanceState(outState);
    }

    public void onActivityCreated(Bundle instate)
    {

        if(instate != null)
        {

            mValidateCard = instate.getBoolean(EXTRA_VALIDATE_EXPIRY_DATE, mValidateCard);

        }

        super.onActivityCreated(instate);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
}
