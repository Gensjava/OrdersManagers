package ua.com.it_st.ordersmanagers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.fragmets.FilesLoadFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderCartFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderCatalogGoodsFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderHeaderDoc;
import ua.com.it_st.ordersmanagers.models.Orders;
import ua.com.it_st.ordersmanagers.models.TreeProductCategoryHolder;

public class Dialogs {

    public static EditText editNumber;
    public static Orders.OrdersLines product;
    public static Animation animScale;
    public static double numberD;
    public static boolean openDialog;
    private static TextView textSum;
    private static Context mContext;
    private static LayoutInflater mLayoutInflater;
    private static int limitAmuont;

    /* Создаем открываем диалог для ввода количества*/
    public static void showCustomAlertDialogEnterNumber(final Context activity, final String title, Object object, final String fClass) {

        mContext = activity;
        mLayoutInflater = LayoutInflater.from(activity);

        if (fClass.equals(OrderCatalogGoodsFragment.class.toString())) {
            product = (TreeProductCategoryHolder.TreeItem) object;
            limitAmuont = 0;
        } else if (fClass.equals(OrderCartFragment.class.toString())) {
            product = (Orders.OrdersLines) object;
            limitAmuont = 1;
        }

        final View numberView;
        /* каст макет */
        numberView = mLayoutInflater.inflate(R.layout.dialog_number, null);
        animScale = AnimationUtils.loadAnimation(mContext, R.anim.scale_button);

        final ImageView numberMinus = (ImageView) numberView.findViewById(R.id.dialog_number_minus);

        /* поля из макета */
        final ImageView numberPlus = (ImageView) numberView.findViewById(R.id.dialog_number_plus);
        final TextView price = (TextView) numberView.findViewById(R.id.dialog_number_price);
        editNumber = (EditText) numberView.findViewById(R.id.dialog_number_number);
        textSum = (TextView) numberView.findViewById(R.id.dialog_number_sum);

        price.setText(String.valueOf(product.getPrice()));
        textSum.setText(String.valueOf(product.getSum()));

       /*устанавливаем к-во */
        numberD = product.getAmount();

        if ((numberD % 1 == 0)) {
            int numberI = (int) numberD;
            editNumber.setText(String.valueOf(numberI));
        } else {
            editNumber.setText(String.valueOf(numberD == 0.0 ? "0" : numberD));
        }

        numberPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberD++;
                calculationSum(numberD, product.getPrice(), v, animScale);
            }
        });

        numberMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberD > limitAmuont) {
                    numberD--;
                    calculationSum(numberD, product.getPrice(), v, animScale);
                }
            }
        });

        /* открываем диалог */
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setView(numberView);
        /* кнопки */
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                                        int id) {

                        /* Количество товара */
                        final double amountInDialog = Double.parseDouble(String.valueOf(editNumber.getText()));
                        //Сумма товара
                        final double sumInDialog = Double.parseDouble(String.valueOf(textSum.getText()));

                        /*преобразуем тип*/
                        final MainActivity mAk = (MainActivity) mContext;

                        if (fClass.equals(OrderCatalogGoodsFragment.class.toString())) {
                         /*делаем поиск списка товаров для передачи параметра*/
                            final OrderCatalogGoodsFragment fragment = (OrderCatalogGoodsFragment) mAk.getSupportFragmentManager().findFragmentByTag(OrderCatalogGoodsFragment.class.toString());
                            if (fragment != null) {
                            /*передаем данные сумма, количество*/
                                fragment.setDialogAmount(amountInDialog, sumInDialog, (TreeProductCategoryHolder.TreeItem) product);
                            }
                        } else if (fClass.equals(OrderCartFragment.class.toString())) {
                           /*делаем поиск списка товаров для передачи параметра*/
                            final OrderCartFragment fragment = (OrderCartFragment) mAk.getSupportFragmentManager().findFragmentByTag(OrderCartFragment.class.toString());
                            if (fragment != null) {
                            /*передаем данные сумма, количество*/
                                fragment.setDialogAmount(amountInDialog, sumInDialog, product);
                            }
                        }

                        //
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(R.string.cancel, null);

        final AlertDialog alert = builder.create();
        alert.show();

    }

    public static void calculationSum(double acount, double price, View v, Animation animScale) {

        double newSum = new BigDecimal(price * acount).setScale(2, RoundingMode.UP).doubleValue();

        textSum.setText(String.valueOf(newSum));
        numberD = acount;

        if ((acount % 1 == 0)) {
            editNumber.setText(String.valueOf((int) acount));
        } else {

            editNumber.setText(String.valueOf(acount));

            if (editNumber.getText().toString().equals("0.0")) {
                editNumber.setText("0");
            }
        }

        v.startAnimation(animScale);
    }

    /*диалог для ввода коментария в шапке заказа*/
    public static void showCustomAlertDialogEditComment(final Context activity, final String title) {

        mContext = activity;
        mLayoutInflater = LayoutInflater.from(activity);
        Orders orders = (Orders) ((MainActivity) activity).getmCurrentOrder();

        final View numberView;
        /* каст макет */
        numberView = mLayoutInflater.inflate(R.layout.order_new_select_header_coment_item, null);
        /* поля из макета */
        final EditText eComment = (EditText) numberView.findViewById(R.id.order_new_select_header_item_coment_text);
        /*получаем из  заказа комент если он есть*/
        eComment.setText(orders.getNote());

        /* открываем диалог */
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setView(numberView);
        /* кнопки */
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                                        int id) {
                        /*получаем комент из View*/
                        String editText = String.valueOf(eComment.getText());
                         /*преобразуем тип*/
                        MainActivity mAk = (MainActivity) mContext;
                           /*делаем поиск шапки для передачи параметра*/
                        OrderHeaderDoc fragment = (OrderHeaderDoc) mAk.getSupportFragmentManager().findFragmentByTag(OrderHeaderDoc.class.toString());
                        if (fragment != null) {
                            /*передаем данные комента и записывем их в заказ*/
                            fragment.setSelectUpdate(editText, String.valueOf(4));
                        }
                        dialog.cancel();
                    }
                }
        );
        builder.setNegativeButton(R.string.cancel, null);

        final AlertDialog alert = builder.create();
        alert.show();
    }

    /* диалог создан для ввода вопроса да или нет
    * возвращает boolean*/
    public static void showYesNoDialog(String iMessage, String iTitle, final MainActivity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(iTitle)
                .setMessage(iMessage)
                .setIcon(R.mipmap.ic_info)
                .setCancelable(false)
                .setPositiveButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                FilesLoadFragment fragment = (FilesLoadFragment) context.getSupportFragmentManager().findFragmentByTag(FilesLoadFragment.class.toString());

                                if (fragment != null) {
                                         /*загружаем информацию с сервера*/
                                    fragment.dowloadFilesOfServer();
                                }
                            }
                        })

        ;

        builder.setNegativeButton("Нет",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

}
