package ua.com.it_st.ordersmanagers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.models.Order;
import ua.com.it_st.ordersmanagers.models.Product;
import ua.com.it_st.ordersmanagers.models.TreeProductCategoryHolder;

/**
 * Created by Gens on 04.08.2015.
 */
public class Dialogs {

    private static TextView number, sum;

    //Создаем открываем диалог
    public static void showCustomAlertDialogEnterNumber(final String title, final Context context, final TreeProductCategoryHolder.TreeItem product) {

        //получаем Inflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View numberView;


        if (product == null) {
            return;
        }

        //каст макет
        numberView = layoutInflater.inflate(R.layout.dialog_number, null);
        //поля из макета
        final ImageView numberMinus = (ImageView) numberView.findViewById(R.id.dialog_number_minus);
        final ImageView numberPlus = (ImageView) numberView.findViewById(R.id.dialog_number_plus);
        final TextView price = (TextView) numberView.findViewById(R.id.dialog_number_price);
        number = (TextView) numberView.findViewById(R.id.dialog_number_number);
        sum = (TextView) numberView.findViewById(R.id.dialog_number_sum);

        price.setText(String.valueOf(product.getPrice()));
        sum.setText(String.valueOf(product.getPrice()));

        final double[] numberD = {1.0};
        final Animation animScale = AnimationUtils.loadAnimation(context, R.anim.scale_button);

        numberPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberD[0]++;
                number.setText(String.valueOf(numberD[0]));
                double newSum = new BigDecimal(product.getPrice() * numberD[0]).setScale(2, RoundingMode.UP).doubleValue();
                sum.setText(String.valueOf(newSum));
                v.startAnimation(animScale);
            }
        });

        numberMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberD[0] > 1) {
                    numberD[0]--;
                    number.setText(String.valueOf(numberD[0]));
                    double newSum = new BigDecimal(product.getPrice() * numberD[0]).setScale(2, RoundingMode.UP).doubleValue();
                    sum.setText(String.valueOf(newSum));
                    v.startAnimation(animScale);
                }
            }
        });


        //открываем диалог
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(numberView);
        //кнопки
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                                        int id) {

                        //Количество товара
                        double numberInDialog = Double.parseDouble(String.valueOf(number.getText()));
                        //Количество товара
                        double sumInDialog = Double.parseDouble(String.valueOf(sum.getText()));

                        //строка ТЧ заказа
                        Order.OrderLines orderLines = new Order.OrderLines(
                                ConstantsUtil.mCurrentOrder.getId(),
                                product.getId(),
                                1,
                                numberInDialog,
                                product.getPrice(),
                                sumInDialog);
                        //добавляем в табличную часть заказа
                        ConstantsUtil.setListOrderLines(orderLines);
                        //
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Отмена", null);

        final AlertDialog alert = builder.create();
        alert.show();

    }

    // Получаем текущее дату системы
    // Возвращаем дату "текущюю дату"
    private static Date getDate() {
        //текущая дата
        long curTime = System.currentTimeMillis();
        Date date = new Date(curTime);
        return date;
    }

    public static void showCustomAlertDialogEnterNumber() {
    }
}
