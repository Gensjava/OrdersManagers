package ua.com.it_st.ordersmanagers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unnamed.b.atv.model.TreeNode;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewGoodsFragment;
import ua.com.it_st.ordersmanagers.models.OrderDoc;
import ua.com.it_st.ordersmanagers.models.TreeProductCategoryHolder;

/**
 * Created by Gens on 04.08.2015.
 */
public class Dialogs {

    private static TextView number, sum;

    //Создаем открываем диалог
    public static void showCustomAlertDialogEnterNumber(final String title, final Context context, final TreeProductCategoryHolder.TreeItem product, final TreeNode node) {

        //получаем Inflater
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View numberView;

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
                if (numberD[0] > 0) {
                    numberD[0]--;
                    number.setText(String.valueOf(numberD[0]));
                    double newSum = new BigDecimal(product.getPrice() * numberD[0]).setScale(2, RoundingMode.UP).doubleValue();
                    sum.setText(String.valueOf(newSum));
                    v.startAnimation(animScale);
                }
            }
        });


        //открываем диалог
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(numberView);
        //кнопки
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                                        int id) {

                        //Количество товара
                        final double numberInDialog = Double.parseDouble(String.valueOf(number.getText()));
                        //Сумма товара
                        final double sumInDialog = Double.parseDouble(String.valueOf(sum.getText()));

                        //строка ТЧ заказа
                        OrderDoc.OrderLines orderLines = new OrderDoc.OrderLines(
                                ConstantsUtil.mCurrentOrder.getId(),
                                product.getId(),
                                1,
                                numberInDialog,
                                product.getPrice(),
                                sumInDialog,
                                product.getText());

                        //к-во заказа
                        final TextView orderTvValue = (TextView) node.getViewHolder().getView().findViewById(R.id.order_new_goods_node_item_order_value);

                        //делаем проверку товара на остатке
                        if (product.getBalance() >= numberInDialog) {
                            if (numberInDialog > 0) {
                                orderTvValue.setVisibility(View.VISIBLE);
                                orderTvValue.setText(String.valueOf(numberInDialog));
                                //добавляем в табличную часть заказа
                                ConstantsUtil.setListOrderLines(orderLines);
                            } else {
                                orderTvValue.setVisibility(View.INVISIBLE);
                                //удаляем из табличной части заказа
                                ConstantsUtil.onListOrderLinesDelete(orderLines);
                            }
                        } else {
                            //
                            final Toast toast = Toast.makeText(context,
                                    "Недостаточно остатка товара на складе!",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        //обновляем корзину
                        OrderNewGoodsFragment.updateCartCount();
                        //
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Отмена", null);

        final AlertDialog alert = builder.create();
        alert.show();

    }


}
