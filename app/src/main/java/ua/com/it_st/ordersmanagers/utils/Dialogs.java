package ua.com.it_st.ordersmanagers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import ua.com.it_st.ordersmanagers.fragmets.OrderNewGoodsFragment;
import ua.com.it_st.ordersmanagers.fragmets.OrderNewHeaderFragment;
import ua.com.it_st.ordersmanagers.models.OrderDoc;
import ua.com.it_st.ordersmanagers.models.TreeProductCategoryHolder;

public class Dialogs {

    private static TextView number, sum;
    private static Context mContext;
    private static LayoutInflater mLayoutInflater;

    public Dialogs(final Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /* Создаем открываем диалог для ввода количества*/
    public static void showCustomAlertDialogEnterNumber(final String title, final TreeProductCategoryHolder.TreeItem product, final TreeNode node) {

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
        sum.setText(String.valueOf(product.getPrice()));

        final double[] numberD = {1.0};
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
                if (numberD[0] > 0) {
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

                        /* строка ТЧ заказа */
                        OrderDoc.OrderLines orderLines = new OrderDoc.OrderLines(
                                ConstantsUtil.mCurrentOrder.getId(),
                                product.getId(),
                                1,
                                numberInDialog,
                                product.getPrice(),
                                sumInDialog,
                                product.getText());

                        /* к-во заказа */
                        final TextView orderTvValue = (TextView) node.getViewHolder().getView().findViewById(R.id.order_new_goods_node_item_order_value);

                        /* делаем проверку товара на остатке */
                        if (product.getBalance() >= numberInDialog) {
                            if (numberInDialog > 0) {
                                orderTvValue.setVisibility(View.VISIBLE);
                                orderTvValue.setText(String.valueOf(numberInDialog));
                                /* добавляем в табличную часть заказа */
                                ConstantsUtil.setListOrderLines(orderLines);
                            } else {
                                orderTvValue.setVisibility(View.INVISIBLE);
                                /* удаляем из табличной части заказа */
                                ConstantsUtil.onListOrderLinesDelete(orderLines);
                            }
                        } else {
                            //
                            ErrorInfo.Tost(mContext.getString(R.string.not_goods_store_number), mContext);
                        }
                        /* обновляем корзину */
                        OrderNewGoodsFragment.updateCartCount();
                        //
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(R.string.cancel, null);

        final AlertDialog alert = builder.create();
        alert.show();

    }

    /*диалог для ввода коментария в шапке заказа*/
    public void showCustomAlertDialogEditComment(final String title) {

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
                });
        builder.setNegativeButton(R.string.cancel, null);

        final AlertDialog alert = builder.create();
        alert.show();
    }
}
