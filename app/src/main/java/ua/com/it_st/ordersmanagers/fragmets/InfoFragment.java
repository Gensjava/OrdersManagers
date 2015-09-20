package ua.com.it_st.ordersmanagers.fragmets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.it_st.ordersmanagers.R;
import ua.com.it_st.ordersmanagers.utils.InfoUtil;


public class InfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_list, container,
                false);
                  /**/

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), createsList(),
                R.layout.info_item,
                new String[]{"image", "info", "date"},
                new int[]{R.id.info_image, R.id.info_text, R.id.info_date});

        ListView lv = (ListView) rootView.findViewById(R.id.lvMain);
        lv.setAdapter(adapter);

        return rootView;
    }

    private List<Map<String, Object>> createsList() {
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        int imgInfo = R.mipmap.ic_info;
        int imgInfoError = R.mipmap.ic_info_red;
        int imgInfoOk = R.mipmap.ic_info_ok;
        int n = 0;
        int sizeList = InfoUtil.mLogLineList.size();

        for (InfoUtil.InfoItem s : InfoUtil.mLogLineList) {
            n++;
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("info", s.getTitle());
            map.put("date", s.getSubTitle());

            if (sizeList == n || n == 1) {
                map.put("image", imgInfoOk);
            } else if (s.isError()) {
                map.put("image", imgInfoError);
            } else {
                map.put("image", imgInfo);
            }

            items.add(map);
        }

        return items;
    }

}
