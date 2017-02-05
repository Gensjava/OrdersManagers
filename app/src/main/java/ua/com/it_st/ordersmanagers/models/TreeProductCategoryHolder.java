package ua.com.it_st.ordersmanagers.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;

import ua.com.it_st.ordersmanagers.R;

public class TreeProductCategoryHolder extends TreeNode.BaseNodeViewHolder<TreeProductCategoryHolder.TreeItem> {

    private PrintView arrowView;
    private boolean isCategory;

    public TreeProductCategoryHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, TreeItem value) {

        View view;
        final LayoutInflater inflater = LayoutInflater.from(context);

        final TextView tvValue;
        isCategory = value.isCategory;

        if (isCategory) {

            view = inflater.inflate(R.layout.order_new_goods_node, null, false);
            tvValue = (TextView) view.findViewById(R.id.order_new_goods_node_value);
            tvValue.setText(value.getName());

            final PrintView iconView = (PrintView) view.findViewById(R.id.icon);

            iconView.setIconText(context.getResources().getString(value.icon));
            arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
        } else {
            view = inflater.inflate(R.layout.order_new_goods_node_item, null, false);

            tvValue = (TextView) view.findViewById(R.id.order_new_goods_node_item_node_value);
            tvValue.setText(value.getName());

            TextView balanceTvValue = (TextView) view.findViewById(R.id.order_new_goods_node_item_balance_value);
            balanceTvValue.setText(String.valueOf(value.getBalance()));

            TextView priceTvValue = (TextView) view.findViewById(R.id.order_new_goods_node_item_price_value);
            priceTvValue.setText(String.valueOf(value.getPrice()));

            TextView orderTvValue = (TextView) view.findViewById(R.id.order_new_goods_node_item_order_value);
            orderTvValue.setText(String.valueOf(value.getAmount()));
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        if (isCategory) {
            arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
        }
    }

    public static class TreeItem extends OrderDoc.OrderLines {

        private Integer icon;
        private boolean isCategory;
        private boolean click;

        //category
        public TreeItem(Integer icon,
                        String text,
                        String id,
                        boolean click,
                        boolean isCategory) {
            super.setName(text);
            super.setGoodsId(id);
            this.icon = icon;
            this.click = click;
            this.isCategory = isCategory;
        }

        //goods
        public TreeItem(final String text,
                        final String id,
                        final boolean click,
                        final double balance,
                        final double order,
                        boolean isCategory,
                        double price) {

            super.setName(text);
            super.setGoodsId(id);
            super.setPrice(price);
            super.setAmount(order);
            super.setBalance(balance);
            this.click = click;
            this.isCategory = isCategory;

        }


        public Integer getIcon() {
            return icon;
        }

        public void setIcon(final Integer icon) {
            this.icon = icon;
        }

        public boolean isClick() {
            return click;
        }

        public void setClick(final boolean click) {
            this.click = click;
        }


        public boolean isCategory() {
            return isCategory;
        }

        public void setIsCategory(final boolean isCategory) {
            this.isCategory = isCategory;
        }

    }
}

