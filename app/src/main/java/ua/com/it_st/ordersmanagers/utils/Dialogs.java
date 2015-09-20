package ua.com.it_st.ordersmanagers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unnamed.b.atv.model.TreeNode;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.activiteies.MainActivity;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewCartFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewGoodsFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewHeaderFragment;
import ua.com.it_st.ordersmanagers.models.OrderDoc;
import ua.com.it_st.ordersmanagers.models.TreeProductCategoryHolder;

public class Dialogs {

    private static TextView number, sum;
    private static Context mContext;
    private static LayoutInflater mLayoutInflater;
    private static OrderDoc.OrderLines product;
    private static int limitAmuont;

    /* Создаем открываем диалог для ввода количества*/
    public static void showCustomAlertDialogEnterNumber(final Context activity, final String title, Object object, final String fClass) {

        mContext = activity;
        mLayoutInflater = LayoutInflater.from(activity);

        if (fClass.equals(OrderNewGoodsFragment.class.toString())) {
            product = (TreeProductCategoryHolder.TreeItem) object;
            limitAmuont = 0;
        } else if (fClass.equals(OrderNewCartFragment.class.toString())) {
            product = (OrderDoc.OrderLines) object;
            limitAmuont = 1;
        }

        final View numberView;
        /* каст макет */
        numberView = mLayoutInflater.inflate(R.layout.dialog_number, null);
        /* поля из макета */
        final ImageView numberMinus = (ImageView) numberView.findViewById(R.id.dialog_number_minus);
        final ImageView numberPlus = (ImageView) numberView.findViewById(R.id.dialog_number_plus);
        final TextView price = (TextView) numberView.findViewById(R.id.dialog_number_price);
        number = (TextView) numberView.findViewById(R.id.dialog_number_number);
        sum = (TextView) numberView.findViewById(R.id.dialog_number_sum);

        price.setText(String.valueOf(product.getPrice()));
        sum.setText(String.valueOf(product.getSum() == 0.0 ? product.getPrice() : product.getSum()));

       /*устанавливаем к-во если уже было, ставим количество товар, если = 0, тогда делаем = 1*/
        final double[] numberD = {product.getAmount() == 0.0 ? 1.0 : product.getAmount()};
        number.setText(String.valueOf(numberD[0]));

        final Animation animScale = AnimationUtils.loadAnimation(mContext, R.anim.scale_button);

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
                if (numberD[0] > limitAmuont) {
                    numberD[0]--;
                    number.setText(String.valueOf(numberD[0]));
                    double newSum = new BigDecimal(product.getPrice() * numberD[0]).setScale(2, RoundingMode.UP).doubleValue();
                    sum.setText(String.valueOf(newSum));
                    v.startAnimation(animScale);
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
                        final double numberInDialog = Double.parseDouble(String.valueOf(number.getText()));
                        //Сумма товара
                        final double sumInDialog = Double.parseDouble(String.valueOf(sum.getText()));

                        /*преобразуем тип*/
                        final MainActivity mAk = (MainActivity) mContext;

                        if (fClass.equals(OrderNewGoodsFragment.class.toString())) {
                         /*делаем поиск списка товаров для передачи параметра*/
                            final OrderNewGoodsFragment fragment = (OrderNewGoodsFragment) mAk.getSupportFragmentManager().findFragmentByTag(OrderNewGoodsFragment.class.toString());
                            if (fragment != null) {
                            /*передаем данные сумма, количество*/
                                fragment.setDialogAmount(numberInDialog, sumInDialog, (TreeProductCategoryHolder.TreeItem) product);
                                ConstantsUtil.clickModifitsirovannoiCart = true;
                            }
                        } else if (fClass.equals(OrderNewCartFragment.class.toString())) {
                           /*делаем поиск списка товаров для передачи параметра*/
                            final OrderNewCartFragment fragment = (OrderNewCartFragment) mAk.getSupportFragmentManager().findFragmentByTag(OrderNewCartFragment.class.toString());
                            if (fragment != null) {
                            /*передаем данные сумма, количество*/
                                fragment.setDialogAmount(numberInDialog, sumInDialog, product);
                                ConstantsUtil.clickModifitsirovannoiCart = true;
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

    /*диалог для ввода коментария в шапке заказа*/
    public static void showCustomAlertDialogEditComment(final Context activity, final String title) {

        mContext = activity;
        mLayoutInflater = LayoutInflater.from(activity);

        final View numberView;
        /* каст макет */
        numberView = mLayoutInflater.inflate(R.layout.order_new_select_header_coment_item, null);
        /* поля из макета */
        final EditText eComment = (EditText) numberView.findViewById(R.id.order_new_select_header_item_coment_text);
        /*получаем из  заказа комент если он есть*/
        eComment.setText(ConstantsUtil.mCurrentOrder.getNote());

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
                        /* создаем массив для передачи в шапку заказа*/
                        String[] cData = new String[2];
                        cData[0] = editText;
                        cData[1] = editText;
                         /*преобразуем тип*/
                        MainActivity mAk = (MainActivity) mContext;
                           /*делаем поиск шапки для передачи параметра*/
                        OrderNewHeaderFragment fragment = (OrderNewHeaderFragment) mAk.getSupportFragmentManager().findFragmentByTag(OrderNewHeaderFragment.class.toString());
                        if (fragment != null) {
                            /*передаем данные комента и записывем их в заказ*/
                            fragment.setSelectUpdate(cData);
                        }
                        dialog.cancel();
                    }

                }

        );
        builder.setNegativeButton(R.string.cancel, null);

        final AlertDialog alert = builder.create();
        alert.show();
    }

}
