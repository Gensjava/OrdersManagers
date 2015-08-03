package ua.com.it_st.ordersmanagers.treeElements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;

import ua.com.it_st.ordersmanagers.R;


/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    private TextView tvValue;
    private PrintView arrowView;

    public IconTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {

        View view = null;
        final LayoutInflater inflater = LayoutInflater.from(context);

        if (value.icon != null) {

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
        }

//        view.findViewById(R.id.btn_addFolder).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TreeNode newFolder = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "New Folder", "", false));
//                getTreeView().addNode(node, newFolder);
//            }
//        });
//
//        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getTreeView().removeNode(node);
//            }
//        });
        
        if (node.getLevel() == 1) {
            view.findViewById(R.id.btn_delete).setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    public static class IconTreeItem {
        public Integer icon;
        public String text;
        public String id;
        public boolean click;

        public IconTreeItem(Integer icon, String text, String id, boolean click) {
            this.icon = icon;
            this.text = text;
            this.id = id;
            this.click = click;
        }

        public IconTreeItem(final String text, final String id, final boolean click) {
            this.text = text;
            this.id = id;
            this.click = click;
        }
    }
}

