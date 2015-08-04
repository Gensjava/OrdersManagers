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

            view = inflater.inflate(R.layout.layout_icon_node, null, false);
            tvValue = (TextView) view.findViewById(R.id.node_value);
            tvValue.setText(value.text);

            final PrintView iconView = (PrintView) view.findViewById(R.id.icon);

            iconView.setIconText(context.getResources().getString(value.icon));
            arrowView = (PrintView) view.findViewById(R.id.arrow_icon);
        } else {
            view = inflater.inflate(R.layout.layout_icon_node_item, null, false);

            tvValue = (TextView) view.findViewById(R.id.node_value);
            tvValue.setText(value.text);
            TextView balanceTvValue = (TextView) view.findViewById(R.id.balance_value);
            balanceTvValue.setText(value.balance);
            TextView orderTvValue = (TextView) view.findViewById(R.id.order_value);
            orderTvValue.setText("1");
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        if (isCategory) {
            arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
        }
    }

    public static class TreeItem {
        public Integer icon;
        public String text;
        public String id;
        public boolean click;
        public String balance;
        public String order;
        public boolean isCategory;
        public double price;

        //категория
        public TreeItem(Integer icon,
                        String text,
                        String id,
                        boolean click,
                        boolean isCategory) {

            this.icon = icon;
            this.text = text;
            this.id = id;
            this.click = click;
            this.isCategory = isCategory;
        }

        //товар
        public TreeItem(final String text,
                        final String id,
                        final boolean click,
                        final String balance,
                        final String order,
                        boolean isCategory,
                        double price) {

            this.text = text;
            this.id = id;
            this.click = click;
            this.balance = balance;
            this.order = order;
            this.isCategory = isCategory;
            this.price = price;
        }

        public Integer getIcon() {
            return icon;
        }

        public void setIcon(final Integer icon) {
            this.icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(final String text) {
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public boolean isClick() {
            return click;
        }

        public void setClick(final boolean click) {
            this.click = click;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(final String balance) {
            this.balance = balance;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(final String order) {
            this.order = order;
        }

        public boolean isCategory() {
            return isCategory;
        }

        public void setIsCategory(final boolean isCategory) {
            this.isCategory = isCategory;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(final double price) {
            this.price = price;
        }
    }
}

